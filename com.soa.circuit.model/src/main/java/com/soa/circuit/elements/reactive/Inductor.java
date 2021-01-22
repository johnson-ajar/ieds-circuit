package com.soa.circuit.elements.reactive;

import com.soa.circuit.common.CircuitElement;
import com.soa.circuit.common.CircuitMatrixUtil;
import com.soa.circuit.common.ElementType;
import com.soa.circuit.common.ValueConvertor;
import com.soa.circuit.exception.ModelReaderException;
import com.soa.circuit.model.AnalysisType;
import com.soa.circuit.model.CircuitModel;
import com.soa.circuit.persist.CircuitElementVertex;
import com.soa.circuit.persist.CircuitPersistanceGremlinFactory;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;

public class Inductor extends CircuitElement{
	private double inductance = 0.0;
	private double Il = 0.0;
	private double Ilq = 0.0; //current in previous timestep
	
	public Inductor(CircuitModel inModel) {
		super(inModel);
	}

	@Override
	public ElementType getType() {
		return ElementType.INDUCTOR;
	}

	@Override
	public double getVoltage() {
		double v = getCircuitNodes().get(0).getNodeVoltage()-getCircuitNodes().get(1).getNodeVoltage();
		return v;
	}

	@Override
	public double getCurrent() {
		return Il;
	}

	@Override
	public double getValue() {
		return inductance;
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

	//calculation I(t+1)
	@Override
	public void calculateCurrent() {
		if(getConductance()>0){
			double voltdiff = getVoltage();
			Il = getConductance()*voltdiff+Ilq;
		}
	}

	@Override
	public void setCurrent(double inCurrent) {
		
	}
	
	@Override
	public DoubleMatrix2D stampNodeCurrent(DoubleMatrix2D i){
		stampCurrent(i);
		return i;
	}
	
	
	private void stampCurrent(DoubleMatrix2D z){
		int node1 = getCircuitNodes().get(0).id()-1;
		int node2 = getCircuitNodes().get(1).id()-1;
		if(node1>=0)z.set(node1, 0, z.get(node1, 0)-Ilq);
		if(node2>=0)z.set(node2, 0, z.get(node2, 0)+Ilq);
	}
	
	@Override
	public DoubleMatrix2D stampConductancesMatrix(DoubleMatrix2D G){
		stampConductance(G);
		return G;
	}
	
	private void stampConductance(DoubleMatrix2D A){
		int node1 = getCircuitNodes().get(0).id()-1;
		int node2 = getCircuitNodes().get(1).id()-1;
		double Gc = getConductance();
	
		if(node1>=0){
			A.set(node1, node1, A.get(node1, node1)+Gc);
		}
		
		if(node2>=0){
			A.set(node2, node2, A.get(node2, node2)+Gc);
		}
		
		if(node1>=0 && node2>=0){
			A.set(node1, node2, A.get(node1, node2)-Gc);
			A.set(node2, node1, A.get(node2, node1)-Gc);
		}
	}
	
	
	
	@Override
	protected void parseElement(String[] values) throws ModelReaderException {
		if(values.length != 5){
			throw new ModelReaderException("Correct arguments are not set for the capacitor");
		}
		String value = values[4];
		double multiplier = 1.0;
		if(value.endsWith(ValueConvertor.MICRO.toString())){
			 multiplier = ValueConvertor.MICRO.multiplier();
			value = value.replace(ValueConvertor.MICRO.toString(), "");
		}else if(value.endsWith(ValueConvertor.MILLI.toString())){
			multiplier = ValueConvertor.MILLI.multiplier();
			value = value.replace(ValueConvertor.MILLI.toString(), "");
		}else{
			multiplier =1.0;
		}
		inductance = Double.valueOf(value)*multiplier;
	}

	@Override
	public void setValue(double inValue) {
		inductance = inValue;
	}

	//h/2L
	@Override
	public double getConductance() {
		if(getModel().getTimeStep()==0){
			return 0.0;
		}
		return getModel().getTimeStep()/(2.0*getValue());
	}

	@Override
	public void update(AnalysisType inType) {
		if(inType == AnalysisType.DC){
			this.calculateCurrent();
		}else if(inType == AnalysisType.TRANSIENT){
			double voltdiff = getVoltage();
			Ilq = getConductance()*voltdiff+Il;
		}
		
	}

}
