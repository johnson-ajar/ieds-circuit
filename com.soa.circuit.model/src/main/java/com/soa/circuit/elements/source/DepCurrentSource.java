package com.soa.circuit.elements.source;

import java.util.List;

import com.soa.circuit.common.CircuitElement;
import com.soa.circuit.common.CircuitMatrixUtil;
import com.soa.circuit.common.ElementType;
import com.soa.circuit.common.ICircuitNodeEdit;
import com.soa.circuit.exception.ModelReaderException;
import com.soa.circuit.model.AnalysisType;
import com.soa.circuit.model.CircuitModel;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;

public class DepCurrentSource extends DependentSource{
	
	public DepCurrentSource(CircuitModel inModel) {
		super(inModel);
	}

	@Override
	public ElementType getType() {
		return ElementType.DEP_CURRENT_SRC;
	}
	
	@Override
	protected void parseElement(String[] values) throws ModelReaderException {
		if(values.length != 8){
			throw new ModelReaderException("The number of inputs are wrong");
		}
		super.parseElement(values);
	}

	@Override
	public double getConductance() {
		return 0;
	}
	
	public void setConductanceMatrix(DoubleMatrix2D G){
		int node1 = this.getCircuitNodes().get(0).id()-1;
		int node2 = this.getCircuitNodes().get(1).id()-1;
		if(node1 >= 0 && pcNodeId-1 >= 0){
			G.set(node1, pcNodeId-1, G.get(node1, pcNodeId-1)+getValue());
		}
		if(node1 >= 0 && ncNodeId-1 >= 0){
			G.set(node1, ncNodeId-1, G.get(node1, ncNodeId)-getValue());
		}
		if(node2 >= 0 && ncNodeId-1 >= 0){
			G.set(node2, ncNodeId-1, G.get(node2, ncNodeId)+getValue());
		}
		if(node2 >=0 && pcNodeId-1 >=0 ){
			G.set(node2, pcNodeId-1, G.get(node2, pcNodeId)-getValue());
		}
	}
	
	@Override 
	public DoubleMatrix2D stampConductancesMatrix(DoubleMatrix2D G){
		if(getControlType() == ElementControlType.CC){
			return G;
		}
		setConductanceMatrix(G);
		return G;
	}	
	
	@Override
	public DoubleMatrix2D stampVoltageMatrixC(DoubleMatrix2D C){
		if(getControlType() == ElementControlType.VC){
			return C;
		}
		DoubleMatrix2D c = getControlSrcStamp();
		C = CircuitMatrixUtil.appendRows(C, c);
		
		return C;
	}
	
	@Override
	
	public DoubleMatrix2D stampVoltageMatrixB(DoubleMatrix2D B){
		if(getControlType() == ElementControlType.VC){
			return B;
		}
		DoubleMatrix2D b = getDependentSrcStamp();
		B = CircuitMatrixUtil.appendColumns(B, b);
		return B;
	}
	
	@Override
	public DoubleMatrix2D stampDependentMatrixD(DoubleMatrix2D D){
		
		if(getControlType() == ElementControlType.VC){
			return D;
		}
		if(D==null){
			if(getControlType() == ElementControlType.CC){
				D = new SparseDoubleMatrix2D(1,1);
				return D;
			}
		}
		
		if(getControlType() == ElementControlType.CC){
			SparseDoubleMatrix2D c = new SparseDoubleMatrix2D(D.rows(),1);
			D=CircuitMatrixUtil.appendColumns(D, c);
			SparseDoubleMatrix2D r = new SparseDoubleMatrix2D(1,D.columns());
			D=CircuitMatrixUtil.appendRows(D, r);
		}
		return D;
	}
	
	@Override
	public DoubleMatrix2D stampNodeVoltage(DoubleMatrix2D e){
		if(getControlType() == ElementControlType.VC){
			return e;
		}
		
		if(e==null){
			e = new SparseDoubleMatrix2D(1,1);
			return e;
		}
		SparseDoubleMatrix2D ee = new SparseDoubleMatrix2D(1,1);
		e = CircuitMatrixUtil.appendRows(e, ee);
		
		return e;
	}
	
	@Override
	public DoubleMatrix2D getDependentSrcStamp() {
		//Contribution to the B part of the matrix.
		int noNodes = getModel().getNoNodes()-1;
		if(noNodes ==0){
			return null;
		}
		List<ICircuitNodeEdit> nodes = getCircuitNodes();
		if(nodes == null || nodes.size()==0){
			return null;
		}
		SparseDoubleMatrix2D srcArray = new SparseDoubleMatrix2D(noNodes,1);
		int node1 = nodes.get(0).id();
	
		if(this.getControlType() == ElementControlType.CC){
			if(pcNodeId !=0) srcArray.set(pcNodeId-1, 0, 1);
			if(ncNodeId !=0) srcArray.set(ncNodeId-1, 0, -1);
		}
		
		if(node1 != 0){
			srcArray.set(node1-1,0,  getValue());
		}
		int node2 = nodes.get(1).id();
		if(node2!=0){
			srcArray.set(node2-1, 0, -getValue());
		}
		return srcArray;
	}

	//C part of the matrix get the controlling nodes 1(+ node) and -1(- node)
	@Override
	public DoubleMatrix2D getControlSrcStamp() {
		int noNodes = getModel().getNoNodes()-1;
		if(noNodes == 0){
			return null;
		}
		if(getCircuitNodes().size() == 0){
			return null;
		}
		SparseDoubleMatrix2D volArray = new SparseDoubleMatrix2D(1, noNodes);
		
		if(pcNodeId!=0){
			volArray.set(0, pcNodeId-1,1);
		}
		if(ncNodeId!=0){
			volArray.set(0, ncNodeId-1,-1);
		}
		
		return volArray;
	}
	
	@Override
	public double getCurrent(){
		for(CircuitElement element : getModel().getElements()){
			int node1 = element.getCircuitNodes().get(0).id();
			int node2 = element.getCircuitNodes().get(1).id();
			if( node1 == pcNodeId &&
					node2 == ncNodeId){
				if(element.getType() == ElementType.CURRENT){
					return element.getCurrent()*getValue();
				}else if(element.getType() == ElementType.VOLTAGE){
					return element.getVoltage()*getValue();
				}
			}
		}
		return 0.0;
	}

	@Override
	public void update(AnalysisType inType) {
		
	}

}
