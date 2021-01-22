package com.soa.circuit.elements.diode;

import com.soa.circuit.common.ElementType;
import com.soa.circuit.common.NonLinearCircuitElement;
import com.soa.circuit.exception.ModelReaderException;
import com.soa.circuit.model.AnalysisType;
import com.soa.circuit.model.CircuitModel;
import com.soa.circuit.persist.CircuitElementVertex;
import com.soa.circuit.persist.CircuitPersistanceGremlinFactory;

import cern.colt.matrix.DoubleMatrix2D;


/* SPICE DIODE PARAMETERS.
 * check the below link to remove the diode model.
 * https://uk.mathworks.com/help/physmod/elec/ref/spicediode.html
 * https://www.allaboutcircuits.com/textbook/semiconductors/chpt-3/spice-models/
 * 
 * Parameter:Description:Unit:DefaultValue
 * KF : flicker noise coefficient : 0
 * AF : flicker noise exponent : 1
 * TNOM : parameter measurement temperature : 27
 * BV : Reverse breakdown knee voltage : V : Infinite
 * CJO : Zero-bias p-n capacitance : F: 0
 * EG : Bandgap voltage : eV : 1.11
 * FC : Forward-bias depletion capacitance coefficient : no unit : 0.5
 * IBLV : Low level reverse breakdown knee current : A : 0
 * IBV : Reverse breakdown knee current : A : 1e-10
 * IKF : High-injection knee current : A : Infinite 
 * IS : Saturation current : A : 1e-14 
 * ISR : Recombination current parameter : A : 0 
 * M : p-n grading coefficient : no unit : 0.5 
 * N : Emission coefficient : no unit : 1.0 
 * NR : Emission coefficient for ISR : no unit : 2.0 
 * RS : Parasitic resistance : Ohm : 0 
 * TT : Transit time : sec : 0 
 * VJ : p-n junction potential : V : 1.0 
 * XTI : IS saturation current temperature exponent : no unit : 3.0 

 * */

public class Diode extends NonLinearCircuitElement{
	private final double k = 1.38e-23; //boltzmann constant jk-1
	private final double q = 1.6e-19; //charge
	private final double T = 300; //temperatur in kelvin.
	private final double N = 1.0; // emission co-efficient
	private final double Vt = (N*k*T)/q; //25.85mv
	private final double Is = 1e-14; //saturation current
	
	
	private final double Vcrit = Vt*Math.log((N*Vt)/(Is*Math.sqrt(2)));
	
	private double Vd =0.0; //voltage across diode which varies and starts with an initial value.
							//the initial value is provided by the user.
	private double lVd=0.0; //previous iteration diode voltage.
	private double gd = 0.0;
	private double Ie = 0.0;
	
	private double Id = 0.0; //diode current;
		
	public Diode(CircuitModel inModel) {
		super(inModel);
	}

	@Override
	public ElementType getType() {
		return ElementType.DIODE;
	}

	@Override
	public double getVoltage() {
		return Vd;
	}

	@Override
	public double getCurrent() {
		return Id;
	}

	@Override
	public double getValue() {
		return 0.0;
	}

	@Override
	public int getNoNodes() {
		return 2;
	}

	@Override
	protected CircuitElementVertex save(CircuitPersistanceGremlinFactory factory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void load(CircuitElementVertex vertex) {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void setCurrent(double inCurrent) {
		
	}

	@Override
	protected void parseElement(String[] values) throws ModelReaderException {
		//TODO: This parameter has to be extended further. 
		if(values.length != 5){
			throw new ModelReaderException("correct arguments are not assigned to diode");
		}
		Vd = Double.valueOf(values[4]);
		lVd = Vd;
	}

	@Override
	public void setValue(double inValue) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public DoubleMatrix2D stampConductancesMatrix(DoubleMatrix2D G){
		setConductance(G);
		return G;
	}
	
	@Override
	public DoubleMatrix2D stampNodeCurrent(DoubleMatrix2D i){
		setNodeCurrent(i);
		return i;
	}

	//calculation gd=dId/dVd = (Is/N*Vt)*exp(Vd/N*Vt)
	//use the new limited diode voltage to calculate conductance gd. 
	@Override
	public double getConductance() {
		double eval = Vd<0.0 ?1.0:Math.exp(Vd/Vt); //to enable convergence.
		gd= (Is/Vt)*eval;
		return gd;
	}
	
	//use the new limited diode voltage to calculate conductance gd.
	@Override
	public void calculateCurrent() {
		//Id(k) = Is*(exp(Vd/Vt)-1)
		double eval = Vd <0 ? 1.0 : Math.exp(Vd/Vt);
		Id = Is*(eval-1);
		Ie = Id - gd*Vd;
	}
	
	
	
	//This limit stepping algorithm is illustrated in 
	// Circuit Simulation Methods and Algorithms (Jan Ogrodzki Pg 188)
	private double limitVoltage(double nVd, double oVd){
		//Vcrit, Is, N, Vt are constants.
		if(nVd > Vcrit && Math.abs(nVd-oVd)>(2*Vt)){
			if(oVd>0){
				double arg = 1+(nVd-oVd)/Vt;
				if(arg>0){
					//incrementing the voltage
					nVd = oVd +Vt*Math.log(arg);
					//calculate voltage from the last good value of current Id.
					double tVd = Vt*Math.log(1+Id/Is);
					//double tVd = Math.log(1e-6/Is)*Vt;
					nVd = Math.max(tVd, nVd);
				}else{
					nVd = Vcrit;
				}
			}else{
				nVd = Vt*Math.log(nVd/Vt);
			}
		}
		return nVd;
	}
	

	@Override
	public void update(AnalysisType inType) {
		if(inType==AnalysisType.TRANSIENT){
			return;
		}
		double cVd = getDiodeVoltage();
		double dV = Math.abs(cVd-lVd);
		boolean isConverged = dV>0.001?false:true; //0.01 is the convergence limit.
		this.setConverged(isConverged);
		if(isConverged){
			return;
		}
		Vd = limitVoltage(cVd, lVd);
		
		System.out.println("cVd: "+cVd+", lVd: "+lVd+" dV: "+dV+" Vd: "+Vd);
		lVd = Vd;
	}
	
	
	private double getDiodeVoltage(){
		double vn1 = getCircuitNodes().get(0).getNodeVoltage(); //node1>=0 ? getModel().getResults().get(node1, 0):0; 
		double vn2 = getCircuitNodes().get(1).getNodeVoltage();//node2>=0 ? getModel().getResults().get(node2, 0):0;
		return vn1-vn2;
	}
	
	private void setConductance(DoubleMatrix2D G){
		if(Vd <0){
			return;
		}
		int node1 = this.getCircuitNodes().get(0).id()-1;
		int node2 = this.getCircuitNodes().get(1).id()-1;
		double tgd = getConductance();
		if(node1>=0){
			G.set(node1, node1, G.get(node1,node1) +tgd);
		}
		if(node2>=0){
			G.set(node2, node2, G.get(node2, node2)+tgd);
		}
		if(node1 >=0 && node2 >=0){
			G.set(node1, node2, G.get(node1, node2)-tgd);
			G.set(node2, node1, G.get(node2,  node1)-tgd);
		}
		
	}
	
	private void setNodeCurrent(DoubleMatrix2D z){
		if(Vd < 0){
			return;
		}
		int node1 = this.getCircuitNodes().get(0).id()-1;
		int node2 = this.getCircuitNodes().get(1).id()-1;
		this.calculateCurrent();
		if(node1>=0){
			z.set(node1, 0, z.get(node1, 0)-Ie);
		}
		if(node2>=0){
			z.set(node2, 0, z.get(node2,0)+Ie);
		}
	}
}
