package com.soa.circuit.persist;

import com.soa.circuit.common.CircuitElement;
import com.soa.circuit.common.CircuitNode;
import com.soa.circuit.elements.passive.Resistor;
import com.soa.circuit.elements.source.CurrentSource;
import com.soa.circuit.elements.source.DepCurrentSource;
import com.soa.circuit.elements.source.DepVoltageSource;
import com.soa.circuit.elements.source.VoltageSource;
import com.soa.circuit.model.CircuitModel;

public class ElementFactory {

	private static ElementFactory instance;
	
	private ElementFactory(){
		
	}
	
	public static ElementFactory getInstance(){
		if(instance == null){
			instance = new ElementFactory();
		}
		return instance;
	}
	
	
	public void  loadElement(CircuitModel model, CircuitElementVertex vertex){
		if(vertex == null){
			return;
		}
		CircuitElement element = null;
			switch(vertex.getType()){
				case RESISTOR:
					element = new Resistor(model);	
				break;
				case VOLTAGE:
					element = new VoltageSource(model);
				break;
				case CURRENT:
					element = new CurrentSource(model);
				break;
				case DEP_VOLTAGE_SRC:
					element = new DepVoltageSource(model);
				break;
				case DEP_CURRENT_SRC:
					element = new DepCurrentSource(model);
				break;
			}
			if(element == null){
				return;
			}
			element.load(vertex);
			model.addCircuitElement(element);
	}
	
	public void loadNode(CircuitModel model, CircuitNodeVertex nodeVertex){
		int index = nodeVertex.getNodeId();
		CircuitNode node = new CircuitNode(index);
		node.load(nodeVertex);
		model.addNode(node);
	}
	
	public void connectElementToNode(CircuitModel model, CircuitElementVertex element){
			CircuitNodeVertex inNode = element.traverse(e->e.inE(element.getType().inE())).next(CircuitNodeVertex.class);
			CircuitNodeVertex outNode = element.traverse(e->e.outE(element.getType().outE())).next(CircuitNodeVertex.class);
			model.getNode(inNode.getNodeId());
	}
}
