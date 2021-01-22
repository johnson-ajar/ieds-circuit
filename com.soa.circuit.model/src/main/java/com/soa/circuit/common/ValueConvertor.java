package com.soa.circuit.common;

public enum ValueConvertor {
	KILO("K",1E3),
	MEGA("M", 1E6 ),
	MILLI("ml", 1E-3),
	MICRO("mi", 1E-6),
	NANO("na", 1E-9),
	PICO("pi", 1E-12 );
	
	private String id;
	private double multiplier;
	private ValueConvertor(String id, double value){
		this.id = id;
		this.multiplier = value;
	}
	
	public String toString(){
		return id;
	}
	
	public double multiplier(){
		return this.multiplier;
	}
}
