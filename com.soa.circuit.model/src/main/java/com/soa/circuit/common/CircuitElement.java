package com.soa.circuit.common;

import java.util.ArrayList;
import java.util.List;

import com.soa.circuit.elements.source.ElementControlType;
import com.soa.circuit.exception.ModelReaderException;
import com.soa.circuit.model.AnalysisType;
import com.soa.circuit.model.CircuitModel;
import com.soa.circuit.persist.CircuitElementVertex;
import com.soa.circuit.persist.CircuitPersistanceGremlinFactory;

import cern.colt.matrix.DoubleMatrix2D;

public abstract class CircuitElement implements ICircuitElementEdit{
	public String CONNECT ="connect";
	private List<ICircuitNodeEdit> nodes;
	private CircuitModel model;
	private String name = "";
	private ElementControlType controlType = ElementControlType.UNDEFINED;
	protected CircuitElementVertex vertex;  //graph element
	private final CircuitPersistanceGremlinFactory persistence;
	//Used when initializing element object from graph.
	public CircuitElement(CircuitModel inModel){
		this.model = inModel;
		nodes = new ArrayList<ICircuitNodeEdit>();
		persistence = CircuitPersistanceGremlinFactory.getInstance();
	}
	
	protected abstract CircuitElementVertex save(CircuitPersistanceGremlinFactory factory);
	
	public abstract void load(CircuitElementVertex vertex);
	
	public DoubleMatrix2D stampConductancesMatrix(DoubleMatrix2D G){
		return G;
	}
	
	public DoubleMatrix2D stampVoltageMatrixB(DoubleMatrix2D B){
		return B;
	}
	
	public DoubleMatrix2D stampVoltageMatrixC(DoubleMatrix2D C){
		return C;
	}
	
	public DoubleMatrix2D stampDependentMatrixD(DoubleMatrix2D D){
		return D;
	}
	
	public DoubleMatrix2D stampNodeCurrent(DoubleMatrix2D i){
		return i;
	}
	
	public DoubleMatrix2D stampNodeVoltage(DoubleMatrix2D e){
		return e;
	}
	
	public abstract void update(AnalysisType inType);
	
	@Override
	public ElementControlType getControlType(){
		return controlType;
	}
	
	public void setControlType(ElementControlType inType){
		controlType = inType;
	}
	
	public CircuitElementVertex getVertex(){
		return vertex;
	}
	
	public void save(){
		vertex = save(persistence);
	}
	
	public double getTime(){
		return model.getTimeStep()*model.getTimeCount();
	}
	
	public void addNode(CircuitNode node){
		nodes.add(node);		
	}
	
	public List<ICircuitNodeEdit> getCircuitNodes(){
		return nodes;
	}
	
	public void replaceCircuitNode(CircuitNode node){
		nodes.remove(node);
	}
	
	public CircuitModel getModel(){
		return model;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean connectToNode(int id){
		for(ICircuitNodeEdit node : nodes){
			if(node.id() == id){
				return true;
			}
		}
		return false;
	}
	
	protected void createNodes(Integer... nodeIds){
		for(Integer id : nodeIds){
			CircuitNode node = null;
			if(model.containNode(id)){
				node = model.getNode(id);
			}else{
				node = new CircuitNode(Integer.valueOf(id));
				model.addNode(node);
			}
			if(node != null){
				addNode(node);
				node.addElement(this);
			}
		}
	}
	
	public abstract void calculateCurrent();
	
	
	public abstract void setCurrent(double inCurrent);
	

	public void setName(String inName){
		name = inName;
	}
	
	protected abstract void parseElement(String[] values) throws ModelReaderException;
	
	public abstract void setValue(double inValue);
	
	public abstract double getConductance();
	
	public void parse(String values){
		try{
			String[] splits = values.split(" ");
			//set Name
			setName(splits[1]);
			parseElement(splits);
		
			//add the node.
			createNodes(Integer.valueOf(splits[2]),
							Integer.valueOf(splits[3]));
			
			if(this.getType() == ElementType.DEP_VOLTAGE_SRC || this.getType() == ElementType.DEP_CURRENT_SRC){
			  createNodes(Integer.valueOf(splits[4]), Integer.valueOf(splits[5]));
			}
		}catch(ModelReaderException e){
			e.printStackTrace();
		}
		model.addCircuitElement(this);
	}
	
	@Override
	public String getLocation(){
		return "["+nodes.get(0).id()+"->"+nodes.get(1).id()+"]";
	}
	@Override
	public String toString(){
		String nodes= "(";
		for(int i=0;i<getCircuitNodes().size();i++){
			nodes+= getCircuitNodes().get(i).id()+",";
		}
		nodes = nodes.substring(0,nodes.lastIndexOf(","))+")";
		return "("+nodes+" : "+getName()+"[ V="+getVoltage()+" ,I="+getCurrent()+"]";
	}
	
	@Override
	public void connectToNodes(){
		persistence.connectToNodes(this);
	}
	
	@Override
	public void addNodes(Integer... nodeIds){
		for(Integer id : nodeIds){
			CircuitNode node = model.getNode(id);
			node.addElement(this);
			addNode(node);
		}
	}
}
