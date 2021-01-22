package com.soa.circuit.common;

import java.util.ArrayList;
import java.util.List;

import com.soa.circuit.persist.CircuitNodeVertex;
import com.soa.circuit.persist.CircuitNodeVertexInitializer;
import com.soa.circuit.persist.CircuitPersistanceGremlinFactory;

public class CircuitNode implements ICircuitNodeEdit{
	private int nodeId;
	private List<CircuitNode> connectedNodes;
	private double nodeVoltage =0.0;
	private final CircuitPersistanceGremlinFactory persistence;
	List<CircuitElement> elements;
	
	private CircuitNodeVertex vertex;
	
	public CircuitNode(){
		this(0);
	}
	
	public CircuitNode(int id){
		this.nodeId = id;
		connectedNodes = new ArrayList<CircuitNode>();
		elements = new ArrayList<CircuitElement>();
		persistence = CircuitPersistanceGremlinFactory.getInstance();
	}
	
	public CircuitNodeVertex getVertex(){
		return vertex;
	}
	
	public int id(){
		return nodeId;
	}
	
	public List<CircuitElement> getElements(){
		return elements;
	}
	
	public void addElement(CircuitElement element){
		if(!containElement(element)){
			elements.add(element);
		}
	}
	
	public boolean containElement(CircuitElement element){
		return containElement(element.getName());
	}
	
	public boolean containElement(String inName){
		for(CircuitElement element : elements){
			if(element.getName().equalsIgnoreCase(inName)){
				return true;
			}
		}
		return false;
	}
	
	public void connectToNode(CircuitNode node){
		connectedNodes.add(node);
	}
	
	public void removeNode(CircuitNode node){
		connectedNodes.remove(node);
	}
	
	public void setNodeVoltage(double inVoltage){
		nodeVoltage = inVoltage;
	}
	
	public double getNodeVoltage(){
		return nodeVoltage;
	}
	
	public double addIndependentCurrentSrc(){
		double current = 0.0;
		for(CircuitElement element : elements){
			if(element.getType() == ElementType.CURRENT){
				List<ICircuitNodeEdit> nodes = element.getCircuitNodes();
					if(nodes.size() == 2){
						if(nodes.get(0).id()==nodeId){
							current-=element.getValue();
						}else if(nodes.get(1).id() == nodeId){
							current+=element.getValue();
						}
					}
			}
		}
		return current;
	}
	
	public List<CircuitNode> getConnectedNodes(){
		return connectedNodes;
	}
	
	public boolean isConnected(CircuitNode inNode){
		for(CircuitNode node : connectedNodes){
			if(node.id() == inNode.id()){
				return true;
			}
		}
		return false;
	}
	
	public boolean isConnected(int id){
		for(CircuitNode node : connectedNodes){
			if(node.id() == id){
				return true;
			}
		}
		return false;
	}
	
	public double calcConductance(){
		double conductance = 0.0;
		System.out.println("No of elements @ node "+ nodeId +" : "+elements.size());
		for(CircuitElement element : elements){
			System.out.println(element.getName());
			conductance+=element.getConductance();
		}
		return conductance;
	}
	
	public double adjNodeConductance(int id){
		double value = 0.0;
		for(CircuitElement element : getElements()){
			if(element.connectToNode(id)){
				value +=element.getConductance();
			}
		}
		return -value;
	}
	
	@Override
	public String toString(){
		return id()+" V= "+getNodeVoltage()+getElements();
	}
	
	public void save(){
		CircuitPersistanceGremlinFactory factory = CircuitPersistanceGremlinFactory.getInstance();
		vertex = factory.save(new CircuitNodeVertexInitializer(this));
	}

	@Override
	public void load(CircuitNodeVertex inVertex) {
		nodeId = inVertex.getNodeId();
	}
}
