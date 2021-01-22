package com.soa.circuit.persist;

import com.soa.circuit.common.CircuitElement;
import com.soa.circuit.common.ElementProperties;
import com.soa.circuit.common.ICircuitNodeEdit;
import com.syncleus.ferma.DefaultClassInitializer;

public abstract class CircuitElementVertexInitializer<E extends CircuitElement> extends DefaultClassInitializer< CircuitElementVertex> {
	
	private final E element;
	public CircuitElementVertexInitializer(E inElement) {
		super(CircuitElementVertex.class);
		this.element =  inElement;
	}
	
	public abstract void initializeVertex(CircuitElementVertex object);
		
	@Override
	public void initalize(final CircuitElementVertex object){
		System.out.println("Initializing the circuit element ");
		System.out.println(element.getClass().getName()+" "+element.getName());
		
		object.setProperty(ElementProperties.CLASS.property(), object.getClass().getName());
		object.setProperty(ElementProperties.NAME.property(), element.getName());
		object.setProperty(ElementProperties.CTL_TYPE.property(), element.getControlType().name());
		object.setProperty(ElementProperties.TYPE.property(), element.getType().toString());
		object.setProperty(ElementProperties.VALUE.property(), element.getValue());
		object.setProperty(ElementProperties.NO_NODES.property(), element.getNoNodes());
		initializeVertex(object);
		
		//adding the elements to the circuit nodes.
		//TODO: Use the wire has the circuit connector.
		for(ICircuitNodeEdit node : element.getCircuitNodes()){
			object.addFramedEdge(element.getName(),node.getVertex());
		}
	}
}
