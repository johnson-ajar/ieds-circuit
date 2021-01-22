package com.soa.circuit.persist;

import com.soa.circuit.common.CircuitNode;
import com.soa.circuit.common.ElementProperties;
import com.syncleus.ferma.DefaultClassInitializer;

public class CircuitNodeVertexInitializer extends DefaultClassInitializer<CircuitNodeVertex>{
	
	private final CircuitNode node;
	public CircuitNodeVertexInitializer(CircuitNode inNode) {
		super(CircuitNodeVertex.class);
		node = inNode;
	}
	
	@Override
	public void initalize(CircuitNodeVertex object){
		object.setProperty(ElementProperties.CLASS.property(), object.getClass().getName());
		object.setProperty(ElementProperties.NAME.property(), node.id());
	}
}
