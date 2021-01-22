package com.soa.circuit.model.sample;

import com.soa.circuit.model.CircuitModel;
import com.soa.circuit.persist.CircuitPersistanceGremlinFactory;

public class OrientDBCircuitSampleFactory {
	private final CircuitPersistanceGremlinFactory persistence;
	private final CircuitModel model;
	
	public OrientDBCircuitSampleFactory(){
		persistence = CircuitPersistanceGremlinFactory.getInstance();
		persistence.connect("remote:localhost:/remoteSample", "root", "root");
		model  = new CircuitModel(persistence);
		model.load();
	}
	
	
}
