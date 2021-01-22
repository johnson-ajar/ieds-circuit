package com.soa.circuit.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.soa.circuit.common.CircuitElement;
import com.soa.circuit.common.CircuitNode;
import com.soa.circuit.common.ElementType;
import com.soa.circuit.common.NonLinearCircuitElement;
import com.soa.circuit.elements.source.VoltageSource;
import com.soa.circuit.elements.source.VoltageType;
import com.soa.circuit.persist.CircuitPersistanceGremlinFactory;

import cern.colt.matrix.DoubleMatrix2D;

public class CircuitModel {
	
	private List<CircuitElement> elements;
	private List<CircuitNode> nodes;
	private Map<ElementType, Integer> elementCount;
	private CircuitPersistanceGremlinFactory factory;
	
	private boolean containNonLinearElements = false;
	
	private boolean hasACSource = false;
	
	private DoubleMatrix2D results;
	
	private CircuitResult circuitResult;
	
	private int time =0;
	
	//h in the inductor and capacitor model
	//t(k+1)=t(k)+h
	private double timeStep = 0;
	
	private AnalysisType analysisType;
	
	public CircuitModel(){
		elements = new ArrayList<CircuitElement>();
		nodes = new ArrayList<CircuitNode>();
		nodes.add(new CircuitNode(0));
		elementCount = new HashMap<ElementType,Integer>();
		init();
	}
	
	public CircuitModel(CircuitPersistanceGremlinFactory inFactory){
		this();
		factory = inFactory;
	}
	
	public boolean printModelState(){
		return true;
	}
	
	private void init(){
		for(ElementType type : ElementType.values()){
			elementCount.put(type, 0);
		}
	}
	
	public void setResults(DoubleMatrix2D r){
		this.results = r;
		int n=0;
		for(CircuitNode node : this.getNodes()){
			if(node.id()!=0){
				node.setNodeVoltage(results.get(n, 0));
				n=n+1;
			}
		}
		
		for(CircuitElement element : this.getElements()){
			if(element.getType() == ElementType.VOLTAGE 
					||element.getType() == ElementType.DEP_VOLTAGE_SRC){
				element.setCurrent(results.get(n, 0));
				n=n+1;
			}
		}
		if(circuitResult == null){
			circuitResult = new CircuitResult(this);
		}
		
	}
	
	public void plotResults(){
		circuitResult.plotResults();
	}
	
	public DoubleMatrix2D getResults(){
		return this.results;
	}
	
	public void setTimeCount(int inTime){
		if(this.time != inTime){
			circuitResult.addResult();
		}
		this.time = inTime;
	}
	
	public int getTimeCount(){
		return this.time;
	}
	
	public void setTimeStep(double tStep){
		this.timeStep =tStep;
	}
	
	public double getTimeStep(){
		return this.timeStep;
	}
	
	public void setAnalysisType(AnalysisType inType){
		this.analysisType = inType;
	}
	
	public AnalysisType getAnalysisType(){
		return this.analysisType;
	}
	
	public void addNode(CircuitNode node){
		if(!containNode(node)){
			nodes.add(node);
		}
	}
	
	public int getNoNodes(){
		return nodes.size();
	}
	
	public void addCircuitElement(CircuitElement element){
		elements.add(element);
		if(!this.containNonLinearElements){
			this.containNonLinearElements = !element.getType().isLinear();
		}
		if(!this.hasACSource){
			if(ElementType.VOLTAGE == element.getType()){
				VoltageSource vSrc = (VoltageSource) element;
				hasACSource = (vSrc.getVoltageType() == VoltageType.AC);
			} 
		}
		int count = elementCount.get(element.getType())+1;
		elementCount.put(element.getType(), count);
	}
	
	public boolean isNonLinear(){
		return containNonLinearElements;
	}
	
	public boolean hasACSource(){
		return hasACSource;
	}
	
	public CircuitPersistanceGremlinFactory getGraphFactory(){
		return factory;
	}
	
	public boolean containNode(CircuitNode inNode){
		for(CircuitNode node : nodes){
			if(node.id() == inNode.id()){
				return true;
			}
		}
		return false;
	}
	
	public boolean containNode(int id){
		for(CircuitNode node : nodes){
			if(node.id() == id){
				System.out.println("contain node "+id);
				return true;
			}
		}
		return false;
	}
	
	public CircuitNode getNode(int id){
		for(CircuitNode node : nodes){
			if(node.id() == id){
				return node;
			}
		}
		return null;
	}
	
	public List<CircuitNode> getNodes(){
		return nodes;
	}
	
	public List<CircuitElement> getElements(){
		return elements;
	}
	
	public int noElement(ElementType inType){
		return elementCount.get(inType);
	}
	
	public boolean hasDepVoltageSrc(){
		for(CircuitElement element : elements){
			if(element.getType() == ElementType.DEP_CURRENT_SRC || element.getType() == ElementType.DEP_VOLTAGE_SRC){
				return true;
			}
		}
		return false;
	}
	
	public void setNodeVoltage(int id, double inVoltage){
		getNode(id).setNodeVoltage(inVoltage);
	}
	
	public void updateModel(DoubleMatrix2D results){
		for(int i=0;i<nodes.size()-1;i++){
			setNodeVoltage(i+1,results.get(i, 0));
		}
		System.out.println(nodes.size()+" "+ elements.size());
		int vCount = 0;
		for(int i=0;i<elements.size();i++){
			if(elements.get(i).getType() == ElementType.VOLTAGE){
				elements.get(i).setCurrent(results.get(nodes.size()-1+vCount++, 0));
			}else if(elements.get(i).getType() == ElementType.DEP_VOLTAGE_SRC){
				elements.get(i).setCurrent(results.get(nodes.size()-1+vCount++,0));
			}else{
				elements.get(i).calculateCurrent();
			}
		}
	}
	
	@Override
	public String toString(){
		String value = "";
		for(int i=0; i< nodes.size(); i++){
			value+=nodes.get(i)+"\n";
		}
		for(int i=0;i<elements.size();i++){
			value+=elements.get(i)+"\n";
		}
		return value;
	}
	
	public void save(){
		//The order is important
		//circuit node vertices
		System.out.println("Saving circuit Model");
		for(CircuitNode node : this.getNodes()){
			node.save();
		}
		
		//circuit element vertices
		for(CircuitElement element : this.getElements()){
			element.save();
		}
		
		
		//adding edges between nodes and vertices
	}
	
	public void load(){
		factory.load(this);
	}
	
	public boolean isConverged(){
		for(CircuitElement element : this.getElements()){
			if(!element.getType().isLinear()){
				if(!((NonLinearCircuitElement)element).isConverged()){
					return false;
				}
			}
		}
		return true;
	}
	
	public void resetConvergence(){
		for(CircuitElement element : this.getElements()){
			if(!element.getType().isLinear()){
				((NonLinearCircuitElement)element).setConverged(false);
			}
		}
	}
}
