package com.soa.circuit.elements.source;

import com.soa.circuit.common.CircuitElement;
import com.soa.circuit.common.CircuitMatrixUtil;
import com.soa.circuit.common.ElementType;
import com.soa.circuit.exception.ModelReaderException;
import com.soa.circuit.model.AnalysisType;
import com.soa.circuit.model.CircuitModel;
import com.soa.circuit.persist.CircuitElementInitializer;
import com.soa.circuit.persist.CircuitElementVertex;
import com.soa.circuit.persist.CircuitElementVertexInitializer;
import com.soa.circuit.persist.CircuitPersistanceGremlinFactory;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;

public class VoltageSource extends CircuitElement{
	private double voltage = 0.0;
	private double current = 0.0;
	private VoltageType type = VoltageType.UNDEFINED;
	private Waveform waveform;
	
	private double frequency, phaseShift, dutyCycle;
	private double bias;
	private double freqTimeZero=bias;
	
	
	public VoltageSource(CircuitModel inModel){
		super(inModel);
	}
	
	@Override
	public void calculateCurrent() {
		
	}

	@Override
	protected void parseElement(String[] values) throws ModelReaderException{
	
		voltage = values.length>=5 ? Double.parseDouble(values[4]):5.0;
		//voltage type
		type = values.length>=6 ? VoltageType.getType(values[5]):VoltageType.DC;
		
		if(type == VoltageType.AC){
			waveform = values.length>=7 ? Waveform.valueOf(values[6]):Waveform.SINE;
			frequency = values.length>=8 ? Double.parseDouble(values[7]):40.0;
			bias = values.length>=9 ? Double.parseDouble(values[8]):0.0;
			phaseShift = values.length>=10?Double.parseDouble(values[9]):0.0;
			dutyCycle = values.length >=11?Double.parseDouble(values[10]):0.5;
		}else{
			waveform = Waveform.UNDEFINED;
		}
	}
	

	@Override
	public ElementType getType() {
		return ElementType.VOLTAGE;
	}

	@Override
	public double getValue() {
		return voltage;
	}

	@Override
	public void setValue(double inValue) {
		voltage = inValue;
	}

	@Override
	public double getConductance() {
		return 0;
	}
	
	public VoltageType getVoltageType(){
		return type;
	}
	
	public DoubleMatrix2D bVoltageNodeConnection(){
		    int noNodes = getModel().getNoNodes()-1; 
		    
			SparseDoubleMatrix2D volArray = new SparseDoubleMatrix2D(noNodes,1);
			if(noNodes==0){
				return null;
			}
			
			int node1 = getCircuitNodes().get(0).id();
			int node2 = getCircuitNodes().get(1).id();
			if(node1 != 0){
				volArray.set(node1 - 1, 0, 1);
			}
			
			if(node2 != 0){
				volArray.set(node2 -1, 0, -1);
			}
		
			return volArray;
	}
	
	public DoubleMatrix2D cVoltageNodeConnection(){
		int noNodes = getModel().getNoNodes()-1;
		SparseDoubleMatrix2D volArray = new SparseDoubleMatrix2D(1, noNodes);
		if(getCircuitNodes().size()==0){
			return null;
		}
		int node1 = getCircuitNodes().get(0).id();
		int node2 = getCircuitNodes().get(1).id();
		if(node1 != 0){
			volArray.set(0, node1-1, 1);
		}
		
		if(node2 != 0){
			volArray.set(0, node2-1, -1);
		}
		
		return volArray;
	}
	
	@Override
	public DoubleMatrix2D stampVoltageMatrixB(DoubleMatrix2D B){
		DoubleMatrix2D b = this.bVoltageNodeConnection();
		return CircuitMatrixUtil.appendColumns(B, b);
	}
	
	@Override
	public DoubleMatrix2D stampVoltageMatrixC(DoubleMatrix2D C){
		DoubleMatrix2D c = cVoltageNodeConnection();
		return CircuitMatrixUtil.appendRows(C, c);
	}
	
	@Override
	public DoubleMatrix2D stampDependentMatrixD(DoubleMatrix2D D){
		if(D == null){
			return new SparseDoubleMatrix2D(1,1);
		}
		SparseDoubleMatrix2D c = new SparseDoubleMatrix2D(D.rows(),1);
		D=CircuitMatrixUtil.appendColumns(D, c);
		SparseDoubleMatrix2D r = new SparseDoubleMatrix2D(1,D.columns());
		D=CircuitMatrixUtil.appendRows(D, r);
		return D;
	}
	
	@Override
	public DoubleMatrix2D stampNodeVoltage(DoubleMatrix2D e){
		//System.out.println("time :"+getTime()+" ac voltage : "+getVoltage());
		if(e==null){
			e = new SparseDoubleMatrix2D(1,1);
			e.set(0, 0, getVoltage());
			return e;
		}
		SparseDoubleMatrix2D ee = new SparseDoubleMatrix2D(1,1);
		ee.set(0, 0, getVoltage());
		e = CircuitMatrixUtil.appendRows(e, ee);
		return e;
	}
	 
	@Override
	public double getCurrent() {
		return current;
	}

	@Override
	public void setCurrent(double inCurrent) {
		current = -inCurrent;
	}

	@Override
	public double getVoltage() {
		double value = 0.0;
		if(getVoltageType() == VoltageType.AC){
			double w = 2*Math.PI*(getTime()- freqTimeZero)*frequency+phaseShift;
			switch(waveform){
				case SINE:
					value= bias+getValue()*Math.sin(w);
				break;
				case SQUARE:
					value = bias+((w % (2*Math.PI) > (2*Math.PI*dutyCycle))? -getValue(): getValue());
				break;
				case TRIANGLE:
					value = bias+triangleFunc(w%(2*Math.PI))*getValue();
				break;
				case SAWTOOTH:
					value = bias+(w%(2*Math.PI))*(getValue()/Math.PI)-getValue();
				break;
				case PULSE:
					value = ((w % (2*Math.PI)) < 1) ? getValue()+bias : bias;
				break;
				case UNDEFINED:
					value = getValue();
				break;
			}
		}else if(getVoltageType() == VoltageType.DC){
			value = getValue();
		}
		return value;
	}
	
	double triangleFunc(double x){
		if(x < Math.PI){
			return x*(2/Math.PI)-1;
		}
		return 1-(x-Math.PI)*(2/Math.PI);
	}
	
	public CircuitElementVertex save(CircuitPersistanceGremlinFactory factory){
		return factory.save(new VoltageSourceVertexInitializer(this));
	}
	
	public void load(CircuitElementVertex vertex){
		this.vertex = vertex;
		VoltageSourceInitializer initializer = new VoltageSourceInitializer(this);
		initializer.initialize(vertex);
	}
	
	@Override
	public int getNoNodes() {
		return 2;
	}
	
	
	private class VoltageSourceInitializer extends CircuitElementInitializer{
		public VoltageSourceInitializer(VoltageSource inElement){
			super(inElement);
		}

		@Override
		public void initializeElement(CircuitElementVertex source) {
			// TODO Auto-generated method stub
			
		}
	}
	
	private class VoltageSourceVertexInitializer extends CircuitElementVertexInitializer<VoltageSource>{

		public VoltageSourceVertexInitializer(VoltageSource inSource){
			super(inSource);
			//source = inSource;
		}
		
		@Override
		public void initializeVertex(CircuitElementVertex object){
			object.setLinkIn(getCircuitNodes().get(0).getVertex(), getName()+getType().inE());
			object.setLinkOut(getCircuitNodes().get(1).getVertex(), getName()+getType().outE());
		}
	}

	@Override
	public void update(AnalysisType inType) {
		// TODO Auto-generated method stub
		
	}
	
}
