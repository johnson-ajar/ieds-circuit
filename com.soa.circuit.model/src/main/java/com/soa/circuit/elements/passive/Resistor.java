package com.soa.circuit.elements.passive;

import com.soa.circuit.common.CircuitElement;
import com.soa.circuit.common.ElementType;
import com.soa.circuit.common.ICircuitNodeEdit;
import com.soa.circuit.exception.ModelReaderException;
import com.soa.circuit.model.AnalysisType;
import com.soa.circuit.model.CircuitModel;
import com.soa.circuit.persist.CircuitElementInitializer;
import com.soa.circuit.persist.CircuitElementVertex;
import com.soa.circuit.persist.CircuitElementVertexInitializer;
import com.soa.circuit.persist.CircuitPersistanceGremlinFactory;

import cern.colt.matrix.DoubleMatrix2D;

public class Resistor extends CircuitElement{
	private double resistance = 0.0;
	private double current = 0.0;
	
	public Resistor(){
		this(null);
	}
	public Resistor(CircuitModel inModel){
		super(inModel);
	}
	
	
	
	@Override
	public void calculateCurrent() {
		if(this.getCircuitNodes().size()!=getNoNodes()){
			System.out.println("No of resistence node is not equal to 2");
		}else if(this.getCircuitNodes().size() ==2){
			ICircuitNodeEdit node1 = this.getCircuitNodes().get(0);
			ICircuitNodeEdit node2 = this.getCircuitNodes().get(1);
			double current = Math.abs(node2.getNodeVoltage() - node1.getNodeVoltage())/resistance;
			setCurrent(current);
		}
	}

	@Override
	protected void parseElement(String[] values)throws ModelReaderException {
		if(values.length != 5){
			throw new ModelReaderException("Correct arguments are not set for the resistor");
		}
		resistance = Double.valueOf(values[4]);
	}

	@Override
	public ElementType getType() {
		return ElementType.RESISTOR;
	}

	@Override
	public double getValue() {
		return resistance;
	}

	@Override
	public void setValue(double inValue) {
		resistance = inValue;
	}

	@Override
	public double getConductance() {
		return 1.0/resistance;
	}

	@Override
	public double getCurrent() {
		return getVoltage()/getValue();
	}

	@Override
	public void setCurrent(double inCurrent) {
		current = inCurrent;
	}

	@Override
	public double getVoltage() {
		return this.getCircuitNodes().get(0).getNodeVoltage() -this.getCircuitNodes().get(1).getNodeVoltage();
	}
	
	public CircuitElementVertex save(CircuitPersistanceGremlinFactory factory){
		return factory.save(new ResistorVertexInitializer(this));
	}
	
	public void load(CircuitElementVertex vertex){
		this.vertex = vertex;
		ResistorInitializer initializer = new ResistorInitializer(this);
		initializer.initialize(vertex);
	}
	
	@Override
	public int getNoNodes() {
		return 2;
	}
	
	//Used when saving the circuit element
	private class ResistorVertexInitializer extends CircuitElementVertexInitializer<Resistor>{
		
		public ResistorVertexInitializer(Resistor inElement) {
			super(inElement);
		}

		@Override
		public void initializeVertex(CircuitElementVertex object) {
			object.setLinkIn(getCircuitNodes().get(0).getVertex(), getName()+getType().inE());
			object.setLinkOut(getCircuitNodes().get(1).getVertex(), getName()+getType().outE());
		}
		
		
	}
	
	//Used when loading the circuit element.
	private class ResistorInitializer extends CircuitElementInitializer<Resistor>{

		public ResistorInitializer(Resistor target) {
			super(target);
		}

		@Override
		public void initializeElement(CircuitElementVertex source) {
			
		}
		
	}
	
	private void setConductance(DoubleMatrix2D G){
		ICircuitNodeEdit node1 = this.getCircuitNodes().get(0);
		ICircuitNodeEdit node2 = this.getCircuitNodes().get(1);
		
		int node1_id = node1.id()-1;
		int node2_id = node2.id()-1;
		if(node1_id >=0){
			G.set(node1_id, node1_id, G.get(node1_id, node1_id)+getConductance());
		}
		if(node2_id>=0){
			G.set(node2_id, node2_id, G.get(node2_id, node2_id)+getConductance());
		}
		if(node1_id >=0 && node2_id>=0){
			G.set(node1_id, node2_id, G.get(node1_id,node2_id)-getConductance());
			G.set(node2_id, node1_id, G.get(node2_id, node1_id)-getConductance());
		}
	}
	
	@Override
	public DoubleMatrix2D stampConductancesMatrix(DoubleMatrix2D G) {
		setConductance(G);	
		return G;
	}
	
	
	@Override
	public void update(AnalysisType inType){
		
	}
	
}
