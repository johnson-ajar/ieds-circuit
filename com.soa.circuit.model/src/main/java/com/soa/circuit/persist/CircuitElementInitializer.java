package com.soa.circuit.persist;

import com.soa.circuit.common.CircuitElement;
import com.soa.circuit.common.ElementProperties;

public abstract class CircuitElementInitializer<T extends CircuitElement> {
	protected T element;
	public CircuitElementInitializer(T target){
		element = target;
	}
	
	public abstract void initializeElement(CircuitElementVertex source);
	
	public void initialize(CircuitElementVertex source){
		element.setName(source.getProperty(ElementProperties.NAME.property(), String.class));
		element.setValue(source.getProperty(ElementProperties.VALUE.property(), Double.class));
		initializeElement(source);
	}
}
