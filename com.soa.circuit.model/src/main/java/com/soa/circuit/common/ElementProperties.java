package com.soa.circuit.common;

public enum ElementProperties {
	NAME("name"),
	NODE("node"),
	CLASS("javaClass"),
	NODE_ID("node_id"),
	TYPE("elementType"),
	NO_NODES("noNodes"),
	SRC_TYPE("srcType"),
	DEP_POS_NODE("dep_pos"),
	DEP_NEG_NODE("dep_neg"),
	CTL_TYPE("controlType"),
	VALUE("value");
	
	private String property = "";
	private ElementProperties(String inProperty){
		property = inProperty;
	}
	
	
	public String property(){
		return property;
	}
}
