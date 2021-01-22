package com.soa.circuit.common;

import com.soa.circuit.model.CircuitModel;

import cern.colt.matrix.impl.SparseDoubleMatrix2D;

public abstract class NonLinearCircuitElement extends CircuitElement{
	
	private boolean isConverged = false;
	public NonLinearCircuitElement(CircuitModel inModel) {
		super(inModel);
	}
	
	public boolean isConverged(){
		return isConverged;
	}
	
	public void setConverged(boolean isConverged){
		this.isConverged = isConverged;
	}
		
}
