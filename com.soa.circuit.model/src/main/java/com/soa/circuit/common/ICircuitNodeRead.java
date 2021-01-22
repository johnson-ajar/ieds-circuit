package com.soa.circuit.common;

import com.soa.circuit.persist.CircuitNodeVertex;

public interface ICircuitNodeRead {
	public int id();
	
	public double getNodeVoltage();
	
	public void load(CircuitNodeVertex inVertex);
	
	public CircuitNodeVertex getVertex();
}
