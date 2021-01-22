package com.soa.circuit.elements.source;

import com.soa.circuit.common.CircuitElement;
import com.soa.circuit.common.ElementType;
import com.soa.circuit.exception.ModelReaderException;
import com.soa.circuit.model.AnalysisType;
import com.soa.circuit.model.CircuitModel;
import com.soa.circuit.persist.CircuitElementInitializer;
import com.soa.circuit.persist.CircuitElementVertex;
import com.soa.circuit.persist.CircuitElementVertexInitializer;
import com.soa.circuit.persist.CircuitPersistanceGremlinFactory;

import cern.colt.matrix.DoubleMatrix2D;

public class CurrentSource extends CircuitElement{
	private double current = 0.0;
	public CurrentSource(CircuitModel inModel) {
		super(inModel);
	}
	
	@Override
	public void calculateCurrent() {
		
	}

	@Override
	public void parseElement(String[] values) throws ModelReaderException{
		if(values.length!=6){
			throw new ModelReaderException("Correct arguments are not set for the current source");
		}
		
		if(values.length == 6){
			current = Double.parseDouble(values[4]);
		}

	}
	
	private void setNodeCurrent(DoubleMatrix2D i){
		int node_1 = this.getCircuitNodes().get(0).id()-1;
		int node_2 = this.getCircuitNodes().get(1).id()-1;
		if(node_1>=0){
			i.set(node_1, 0, i.get(node_1, 0)-getValue());
		}
		if(node_2>=0){
			i.set(node_2, 0, i.get(node_2, 0)+getValue());
		}
		
	}
	
	@Override
	public DoubleMatrix2D stampNodeCurrent(DoubleMatrix2D i){
		setNodeCurrent(i);
		return i;
	}
	
	@Override
	public ElementType getType() {
		return ElementType.CURRENT;
	}

	@Override
	public double getValue() {
		return current;
	}

	@Override
	public void setValue(double inValue) {
		this.current = inValue;
	}

	@Override
	public double getConductance() {
		return 0;
	}

	@Override
	public double getCurrent() {
		return getValue();
	}

	@Override
	public void setCurrent(double inCurrent) {
		setValue(inCurrent);
	}

	public CircuitElementVertex save(CircuitPersistanceGremlinFactory factory){
		return factory.save(new CurrentSourceVertexInitializer(this));
	}
	
	public void load(CircuitElementVertex vertex){
		this.vertex = vertex;
		CurrentSourceInitializer initializer = new CurrentSourceInitializer(this);
		initializer.initialize(vertex);
	}
	
	public double getVoltage() {
		System.out.println(this.getCircuitNodes().get(1).getNodeVoltage()+" "+this.getCircuitNodes().get(0).getNodeVoltage());
		return this.getCircuitNodes().get(1).getNodeVoltage()-this.getCircuitNodes().get(0).getNodeVoltage();
	}
	
	private class CurrentSourceInitializer extends CircuitElementInitializer<CurrentSource>{

		public CurrentSourceInitializer(CurrentSource target) {
			super(target);
		}

		@Override
		public void initializeElement(CircuitElementVertex source) {
			
		}
		
	}
	
	private class CurrentSourceVertexInitializer extends CircuitElementVertexInitializer<CurrentSource>{
		
		public CurrentSourceVertexInitializer(CurrentSource inSource){
			super(inSource);
		}
		
		@Override
		public void initializeVertex(CircuitElementVertex object){
			//object.setProperty(ElementProperties.SRC_TYPE.property(), getSrcType().name());
			object.setLinkIn(getCircuitNodes().get(0).getVertex(), getName()+getType().inE());
			object.setLinkOut(getCircuitNodes().get(1).getVertex(), getName()+getType().outE());
		}
	}

	@Override
	public int getNoNodes() {
		return 2;
	}

	@Override
	public void update(AnalysisType inType) {
		
	}

	
}
