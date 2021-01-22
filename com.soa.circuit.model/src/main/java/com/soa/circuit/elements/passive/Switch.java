package com.soa.circuit.elements.passive;

import java.util.List;

import com.soa.circuit.common.CircuitElement;
import com.soa.circuit.common.CircuitMatrixUtil;
import com.soa.circuit.common.ElementType;
import com.soa.circuit.common.ICircuitNodeEdit;
import com.soa.circuit.exception.ModelReaderException;
import com.soa.circuit.model.AnalysisType;
import com.soa.circuit.model.CircuitModel;
import com.soa.circuit.persist.CircuitElementVertex;
import com.soa.circuit.persist.CircuitPersistanceGremlinFactory;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;

/**
 * Ref: Electronic circuit simulation with ideal switches Vanco Litovski, Milan Savic and Zeljko Mrcarica
 * In case of a close switch.
 * 		vj	vk	i	rhs
 * j			1
 * k			-1
 * z	1	1	-r	-rim
 * 
 * 
 * In case of a open switch.
 * 		vj	vk	i	rhs
 * j			1
 * k			-1
 * z	1	-1	-R	vjm-vkm
 * */


public class Switch extends CircuitElement{
	//state of the switch
	double ss = 1.0; //false == open, true == short
	
	public Switch(CircuitModel inModel) {
		super(inModel);
	}

	@Override
	public void calculateCurrent() {
		
		
	}

	@Override
	public double getCurrent() {
		if(ss == 0.0){
			return 0;
		}
		//if the switch is turned on, then calculate the current that flows 
		//through this switch.

		return 0;
	}

	@Override
	public void setCurrent(double inCurrent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getVoltage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void parseElement(String[] values) throws ModelReaderException {
		if(values.length != 5){
			throw new ModelReaderException("Correct arguments are not set for the switch ");
		}
		 ss = Double.valueOf(values[4]);
	}
	
	
	@Override
	public ElementType getType() {
		return ElementType.SWITCH;
	}

	@Override
	public double getValue() {
		return ss;
	}

	@Override
	public void setValue(double inValue) {
		ss = inValue;
	}

	@Override
	public double getConductance() {
		return 0;
	}
	
	public CircuitElementVertex save(CircuitPersistanceGremlinFactory factory){
		return null;
	}
	
	public void load(CircuitElementVertex vertex){
		
	}
	
	public DoubleMatrix2D updateVoltageMatrixB(DoubleMatrix2D B){
		DoubleMatrix2D b = new SparseDoubleMatrix2D(B.rows(),1);
		b = toCalculateVoltageAcrossSwitch(b);
		B = CircuitMatrixUtil.appendColumns(B, b);
		return B;
	}
	
	public DoubleMatrix2D updateVoltageMatrixC(DoubleMatrix2D C){
		DoubleMatrix2D c = new SparseDoubleMatrix2D(1, C.columns());
		c = toCalculateCurrentThroughSwitch(c);
		C = CircuitMatrixUtil.appendRows(C, c);
		return C;
	}
	
	public DoubleMatrix2D  updateNodeVoltage(DoubleMatrix2D e){
		if(e == null){
			e = new SparseDoubleMatrix2D(1,1);
			return e;
		}
		SparseDoubleMatrix2D ee = new SparseDoubleMatrix2D(1,1);
		ee.set(0, 0, 0);
		e = CircuitMatrixUtil.appendRows(e, ee);
		return e;
	}
	
	@Override
	public DoubleMatrix2D stampDependentMatrixD(DoubleMatrix2D D){
		
		int dr = D==null?1:D.rows();
		int dc = D==null?1:D.columns();
		if(D == null){
			D = new SparseDoubleMatrix2D(dr,dc);
			if(ss==0.0){
				D.set(0, 0, -1);
			}
		}
		SparseDoubleMatrix2D c = new SparseDoubleMatrix2D(dr,1);
		D=CircuitMatrixUtil.appendColumns(D, c);
		dc = D.columns();
		SparseDoubleMatrix2D r = new SparseDoubleMatrix2D(1,dc);
		if(ss==0.0){
			r.set(0, dc-1, -1);
		}
		D=CircuitMatrixUtil.appendRows(D, r);
		return D;
	}
	//an extra row of the B matrix
	public DoubleMatrix2D toCalculateVoltageAcrossSwitch(DoubleMatrix2D cx){
		int noNodes = getModel().getNoNodes()-1;
		if(noNodes == 0){
			return null;
		}
		//close or open
		List<ICircuitNodeEdit> nodes = getCircuitNodes();
		int nodeId = nodes.get(0).id();
		if(nodeId !=0){
			cx.set(nodeId-1, 0, 1);
		}
		nodeId = nodes.get(1).id();
		if(nodeId != 0){
			cx.set(nodeId-1, 0, -1);
		}
		
		return cx;
	}
	
	public DoubleMatrix2D toCalculateCurrentThroughSwitch(DoubleMatrix2D srcArray){
		int noNodes = getModel().getNoNodes()-1;
		if(noNodes == 0){
			return null;
		}
		List<ICircuitNodeEdit> nodes = getCircuitNodes();
		if(nodes.get(0).id() !=0){
			srcArray.set(0, nodes.get(0).id()-1, 1*ss);
		}
		if(nodes.get(1).id() !=0){
			srcArray.set(0, nodes.get(1).id()-1, -1*ss);
		}	
		return srcArray;
	}

	@Override
	public int getNoNodes() {
		return 2;
	}

	@Override
	public void update(AnalysisType inType) {
		
	}

}
