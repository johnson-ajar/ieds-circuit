package com.soa.circuit.elements.source;

import com.soa.circuit.common.CircuitElement;
import com.soa.circuit.common.ElementProperties;
import com.soa.circuit.exception.ModelReaderException;
import com.soa.circuit.model.CircuitModel;
import com.soa.circuit.persist.CircuitElementInitializer;
import com.soa.circuit.persist.CircuitElementVertex;
import com.soa.circuit.persist.CircuitElementVertexInitializer;
import com.soa.circuit.persist.CircuitNodeVertex;
import com.soa.circuit.persist.CircuitPersistanceGremlinFactory;

import cern.colt.matrix.DoubleMatrix2D;

public abstract class DependentSource extends CircuitElement{
	//private ElementControlType srcControlType;
	private double value=0.0;
	protected int pcNodeId;
	protected int ncNodeId;
	private final String CURRENT_IN = "_i_src_in";
	private final String CURRENT_OUT = "_i_src_out";
	private final String VOLTAGE_IN = "_v_src_in";
	private final String VOLTAGE_OUT = "_v_src_out";
	public DependentSource(CircuitModel inModel) {
		super(inModel);
	}
	
	public abstract DoubleMatrix2D getDependentSrcStamp();
	public abstract DoubleMatrix2D getControlSrcStamp();
	
	@Override
	protected void parseElement(String[] values) throws ModelReaderException {
		ElementControlType srcControlType = ElementControlType.UNDEFINED;
		if(values[7].equals("vc")){
			srcControlType = ElementControlType.VC;
		}else{
			srcControlType = ElementControlType.CC;
		}
		this.setControlType(srcControlType);
		pcNodeId = Integer.parseInt(values[4]);
		ncNodeId = Integer.parseInt(values[5]);
		
		value = Double.parseDouble(values[6]);
		
	}
	
	
	@Override
	public void calculateCurrent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getCurrent() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setCurrent(double inCurrent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getVoltage() {
		System.out.println("Calculation Voltage");
		return this.getCircuitNodes().get(1).getNodeVoltage()-this.getCircuitNodes().get(0).getNodeVoltage();
	}

	@Override
	public double getValue() {
		return value;
	}

	@Override
	public void setValue(double inValue) {
		value = inValue;
	}
	
	public void load(CircuitElementVertex vertex){
		this.vertex = vertex;
		DependentSourceInitializer initializer = new DependentSourceInitializer(this);
		initializer.initialize(vertex);
	}
	
	public CircuitElementVertex save(CircuitPersistanceGremlinFactory factory){
		return factory.save(new DependentSourceVertexInitializer(this));
	}
	
	
	private class DependentSourceInitializer extends CircuitElementInitializer<DependentSource>{

		public DependentSourceInitializer(DependentSource inElement) {
			super(inElement);
		}

		@Override
		public void initializeElement(CircuitElementVertex source) {
			pcNodeId = source.getProperty(ElementProperties.DEP_POS_NODE.property(), Integer.class);
			ncNodeId = source.getProperty(ElementProperties.DEP_NEG_NODE.property(), Integer.class);
			setControlType(vertex.getControlType());
			switch(getType()){
				case CURRENT:
					CircuitNodeVertex nodeVertex = source.traverse(e->e.inE(getName()+CURRENT_IN)).next(CircuitNodeVertex.class);
				break;
				case VOLTAGE:
				break;
			}
		}
		
	}
	
	private class DependentSourceVertexInitializer extends CircuitElementVertexInitializer<DependentSource>{
		
		public DependentSourceVertexInitializer(DependentSource inSource) {
			super(inSource);
		}
		
		@Override
		public void initializeVertex(CircuitElementVertex object) {
			// TODO Auto-generated method stub
			object.setProperty(ElementProperties.CTL_TYPE.toString(), getControlType().name());
			object.setProperty(ElementProperties.SRC_TYPE.toString(), getType().toString());
			object.setProperty(ElementProperties.DEP_POS_NODE.toString(), pcNodeId);
			object.setProperty(ElementProperties.DEP_NEG_NODE.toString(), ncNodeId);
			
				
			object.setLinkIn(getCircuitNodes().get(0).getVertex(), getName()+getType().inE());
			object.setLinkIn(getCircuitNodes().get(1).getVertex(), getName()+getType().outE());
				
			
		}
	}
	
	public int getNoNodes(){
		return 2;
	}
}



