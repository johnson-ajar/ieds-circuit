package com.soa.circuit.common;

import com.soa.circuit.elements.source.ElementControlType;

public interface ICircuitElementRead {
	public String getName();
	public String getLocation();
	public ElementType getType();
	public double getVoltage();
	public double getCurrent();
	public double getValue();
	public int getNoNodes();
	public ElementControlType getControlType();
}
