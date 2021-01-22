package com.soa.circuit.common;

import com.soa.circuit.elements.source.ElementControlType;

public interface ICircuitElementEdit extends ICircuitElementRead{
	public void setName(String inName);
	public void setControlType(ElementControlType inType);
	public void connectToNodes();
	public void addNodes(Integer... nodeId);
}
