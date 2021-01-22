package com.soa.circuit.elements.source;

public enum VoltageType {
	DC("dc"),
	AC("ac"),
	UNDEFINED("");
	
	String type = "";
	private VoltageType(String inType){
		type = inType;
	}
	
	public String getTypeName(){
		return type;
	}
	
	public static VoltageType getType(String inType){
		for(VoltageType t : values()){
			if(t.getTypeName().equalsIgnoreCase(inType)){
				return t;
			}
		}
		return UNDEFINED;
	}
	
}
