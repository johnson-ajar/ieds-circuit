package com.soa.circuit.persist;

import com.soa.circuit.common.ElementProperties;
import com.soa.circuit.common.ElementType;
import com.soa.circuit.elements.source.ElementControlType;
import com.syncleus.ferma.AbstractVertexFrame;

//@JsonIgnoreProperties(value = {"handler"})
public class CircuitElementVertex extends AbstractVertexFrame{
	
	public CircuitElementVertex(){
		
	}
	public String getName(){
		return this.getProperty(ElementProperties.NAME.property(), String.class);
	}
	
	public ElementType getType(){
		String type = getProperty(ElementProperties.TYPE.property(), String.class);
		if(type == null){
			return ElementType.UNDEFINED;
		}
		return ElementType.valueOf(getProperty(ElementProperties.TYPE.property(), String.class));
	}
	
	public double getValue(){
		return getProperty(ElementProperties.VALUE.property(), Double.class);
	}
	
	public ElementControlType getControlType(){
		return ElementControlType.valueOf(getProperty(ElementProperties.CTL_TYPE.property()));
	}
}
