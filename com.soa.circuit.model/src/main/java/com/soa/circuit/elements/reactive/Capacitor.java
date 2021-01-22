package com.soa.circuit.elements.reactive;

import com.soa.circuit.common.CircuitElement;
import com.soa.circuit.common.ElementType;
import com.soa.circuit.common.ValueConvertor;
import com.soa.circuit.exception.ModelReaderException;
import com.soa.circuit.model.AnalysisType;
import com.soa.circuit.model.CircuitModel;
import com.soa.circuit.persist.CircuitElementVertex;
import com.soa.circuit.persist.CircuitPersistanceGremlinFactory;

import cern.colt.matrix.DoubleMatrix2D;

public class Capacitor extends CircuitElement{
	private double capacitance = 0.0;
	private double Ic = 0.0;
	private double Icq = 0.0;
	
	public Capacitor(CircuitModel inModel) {
		super(inModel);
	}

	@Override
	public ElementType getType() {
		return ElementType.CAPACITOR;
	}

	@Override
	public double getVoltage() {
		return getCircuitNodes().get(0).getNodeVoltage()-getCircuitNodes().get(1).getNodeVoltage();
	}

	//Get the current at kth iteration
	@Override
	public double getCurrent() {
		return Ic;
	}

	@Override
	public double getValue() {
		return capacitance;
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
	public DoubleMatrix2D stampConductancesMatrix(DoubleMatrix2D G){
		stampConductance(G);
		return G;
	}
	
	@Override
	public DoubleMatrix2D stampNodeCurrent(DoubleMatrix2D i){
		stampCurrent(i);
		return i;
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
	
	private void stampCurrent(DoubleMatrix2D z){
		int node1 = getCircuitNodes().get(0).id()-1;
		int node2 = getCircuitNodes().get(1).id()-1;
		
		double voltdiff = getVoltage();
		//Icq = -getConductance()*voltdiff-Ic;
		if(node1>=0)z.set(node1, 0, z.get(node1, 0)-Icq);
		if(node2>=0)z.set(node2, 0, z.get(node2, 0)+Icq);
	}
	
	//Calculate the current through capacitor at k+1 step.
	@Override
	public void calculateCurrent() {
		double voltdiff = getVoltage();
		if(getConductance()>0)
		Ic = getConductance()*voltdiff+Icq;
	}

	@Override
	public void setCurrent(double inCurrent) {
		
	}
	
	@Override
	protected void parseElement(String[] values) throws ModelReaderException {
		if(values.length != 5){
			throw new ModelReaderException("Correct arguments are not set for the capacitor");
		}
		double multiplier =1.0;
		String value = values[4];
		if(value.endsWith(ValueConvertor.MICRO.toString())){
			multiplier = ValueConvertor.MICRO.multiplier();
			value = value.replace(ValueConvertor.MICRO.toString(), "");
		}else if(value.endsWith(ValueConvertor.PICO.toString())){
			multiplier = ValueConvertor.PICO.multiplier();
			value = value.replace(ValueConvertor.PICO.toString(), "");
		}else if(value.endsWith(ValueConvertor.NANO.toString())){
			multiplier = ValueConvertor.NANO.multiplier();
			value = value.replace(ValueConvertor.NANO.toString(), "");
		}
		capacitance = Double.valueOf(value)*multiplier;
	}

	@Override
	public void setValue(double inValue) {
		capacitance = inValue;
	}
	
	//2C/h
	@Override
	public double getConductance() {
		if(getModel().getTimeStep()==0){
			return 0.0;
		}
		return (2.0*getValue())/getModel().getTimeStep();
	}
	
	
	@Override
	public void update(AnalysisType inType) {
		//TODO: update the capcitor stamp for time k+1.
		// For transient analysis, according to Trapezoidal integration technique
		// calculate Icq at h
		// update the conductance matrix
		if(inType == AnalysisType.DC){
			this.calculateCurrent();
		}else if(inType == AnalysisType.TRANSIENT){
			double voltdiff = getVoltage();
			Icq = -getConductance()*voltdiff-Ic;
		}
		
	}
	
	
}
