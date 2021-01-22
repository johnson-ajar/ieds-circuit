package com.soa.circuit.common;

public enum NodeProperties {
	ID("id");
	
	private String property;
	private NodeProperties(String inProperty){
		property = inProperty;
	}
	
	@Override
	public String toString(){
		return property.toString();
	}
}
