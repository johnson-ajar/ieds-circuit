package com.soa.circuit.common;

public enum ElementType {
	
	SWITCH(true, "_switch_in","_switch_out"),
	DIODE(false,"_diode_in", "_diode_out"),
	RESISTOR(true, "_resistor_in","_resistor_out"),
	VOLTAGE(true, "_v_src_in", "_v_src_out"),
	CURRENT(true, "_i_src_in", "_i_src_out"),
	DEP_VOLTAGE_SRC(true, "_v_src_in","_v_src_out"),
	DEP_CURRENT_SRC(true, "_i_src_in","_i_src_out"),
	CAPACITOR(true, "_cap_src_in", "_cap_src_out"),
	INDUCTOR(true, "_ind_src_in", "_ind_src_out"),
	UNDEFINED(false);
	
	String[] edgeLabels;
	private boolean isLinear;
	private ElementType(boolean isLinear, String... inLabels){
		edgeLabels = inLabels;
		this.isLinear = isLinear;
	}
	
	public String inE(){
		return edgeLabels[0];
	}
	
	public String outE(){
		return edgeLabels[1];
	}
	
	public boolean isLinear(){
		return isLinear;
	}
}
