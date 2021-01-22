package com.soa.circuit.persist;

import com.soa.circuit.common.ElementProperties;
import com.syncleus.ferma.AbstractVertexFrame;

public class CircuitNodeVertex extends AbstractVertexFrame{
	public int getNodeId(){
		Integer id =  getProperty(ElementProperties.NAME.property(), Integer.class);
		return Integer.valueOf(id);
	}
}
