package com.soa.circuit.model.transistor;

import com.soa.circuit.common.CircuitNode;
import com.soa.circuit.elements.transistor.Transistor;
import com.soa.circuit.model.CircuitModel;

import junit.framework.TestCase;

public class TestTransistorModel extends TestCase{
	private CircuitModel model;
	private Transistor transistor;
	public void setUp(){
		model = new CircuitModel();
		CircuitNode nodeE = new CircuitNode(1);
		CircuitNode nodeB = new CircuitNode(2);
		CircuitNode nodeC = new CircuitNode(3);
		model.addNode(nodeE);
		model.addNode(nodeB);
		model.addNode(nodeC);
		
		transistor = new Transistor(model);
		transistor.addNode(nodeE);
		transistor.addNode(nodeB);
		transistor.addNode(nodeC);
	}
	
	public void testModel(){
		
	}
}
