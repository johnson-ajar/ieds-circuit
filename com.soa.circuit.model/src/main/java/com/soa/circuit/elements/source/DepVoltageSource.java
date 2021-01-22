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

public class DepVoltageSource extends DependentSource{
	private VoltageType voltageType;
	private double current = 0.0;
	public DepVoltageSource(CircuitModel inModel) {
		super(inModel);
	}

	@Override
	public ElementType getType() {
		return ElementType.DEP_VOLTAGE_SRC;
	}
	
	@Override
	protected void parseElement(String[] values) throws ModelReaderException {
		if(values.length != 9){
			throw new ModelReaderException("The number of inputs are wrong");
		}
		super.parseElement(values);
		voltageType = VoltageType.getType(values[8]);
	}
	
	public VoltageType getVoltageType(){
		return voltageType;
	}
	
	@Override
	public void setCurrent(double inCurrent){
		this.current = inCurrent;
	}
	
	@Override
	public double getCurrent(){
		return this.current;
	}
	
	@Override
	public double getConductance() {
		return 0.0;
	}
	
	
	@Override
	public DoubleMatrix2D stampVoltageMatrixC(DoubleMatrix2D C){
		DoubleMatrix2D c = getControlSrcStamp();
		C = CircuitMatrixUtil.appendRows(C, c);
		return C;
	}
	
	@Override
	public DoubleMatrix2D stampVoltageMatrixB(DoubleMatrix2D B){
		DoubleMatrix2D b = getDependentSrcStamp();
		B = CircuitMatrixUtil.appendColumns(B, b);
		return B;
	}
	
	@Override
	public DoubleMatrix2D stampNodeVoltage(DoubleMatrix2D e){
		int r=1;
		if(this.getControlType() == ElementControlType.CC){
			r=2;
		}
		if(e==null){
			e = new SparseDoubleMatrix2D(r,1);
			return e;
		}
		
		SparseDoubleMatrix2D ee = new SparseDoubleMatrix2D(r,1);
		e = CircuitMatrixUtil.appendRows(e, ee);
		return e;
	}
	
	@Override
	public DoubleMatrix2D stampDependentMatrixD(DoubleMatrix2D D){
		if(D == null){
			if(getControlType() == ElementControlType.VC){
				D = new SparseDoubleMatrix2D(1,1);
			}else if(getControlType() == ElementControlType.CC){
				D = new SparseDoubleMatrix2D(2,2);
				D.set(1, 0, -getValue());
			}
			return D;
		}
		//if D is not null
		if(getControlType() == ElementControlType.VC){
			SparseDoubleMatrix2D c = new SparseDoubleMatrix2D(D.rows(),1);
			D=CircuitMatrixUtil.appendColumns(D, c);
			SparseDoubleMatrix2D r = new SparseDoubleMatrix2D(1,D.columns());
			D=CircuitMatrixUtil.appendRows(D, r);
		}else if(getControlType() == ElementControlType.CC){
			SparseDoubleMatrix2D c = new SparseDoubleMatrix2D(D.rows(),2);
			D=CircuitMatrixUtil.appendColumns(D, c);
			SparseDoubleMatrix2D r = new SparseDoubleMatrix2D(2,D.columns());
			r.set(1, D.columns()/2, -this.getValue());
			D=CircuitMatrixUtil.appendRows(D, r);
		}
		return D;
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
		SparseDoubleMatrix2D srcArray = null;
		int node1 = nodes.get(0).id();
		int node2 = nodes.get(1).id();
		if(getControlType() == ElementControlType.VC){
			//(ij or i1)
			srcArray = new SparseDoubleMatrix2D(noNodes,1);
			
			if(node1 != 0){
				srcArray.set(node1-1,0, 1);
			}
					
			if(node2 != 0){
				srcArray.set(node2-1,0, -1);
			}
		}else if(getControlType() == ElementControlType.CC){
			// common mna stamp but doesn't give me right result.
			//This stamp is taken from http://users.ecs.soton.ac.uk/mz/CctSim/chap1_4.htm
			srcArray = new SparseDoubleMatrix2D(noNodes,2);
			
			if(node1!=0){
				srcArray.set(node1-1,1, 1); //k //dependent voltage source positive
			}
					
			if(node2 != 0){
				srcArray.set(node2-1,1, -1); //k'//dependent voltage source negative
			}
			
			if(pcNodeId!=0)srcArray.set(pcNodeId-1, 0, 1);  //j
			if(ncNodeId!=0)srcArray.set(ncNodeId-1, 0, -1); //j'
			
		}
		return srcArray;
	}

	@Override
	public DoubleMatrix2D getControlSrcStamp() {
		//Contribution to the C part of the matrix
				int noNodes = getModel().getNoNodes()-1;
				if(noNodes ==0){
					return null;
				}
				List<ICircuitNodeEdit> nodes = getCircuitNodes();
				if(nodes == null || nodes.size()==0){
					return null;
				}
				
				if(getType() != ElementType.DEP_VOLTAGE_SRC){
					return null;
				}
				SparseDoubleMatrix2D srcArray = null;
				int node1 = nodes.get(0).id();
				int node2 = nodes.get(1).id();
				if(getControlType() == ElementControlType.VC){
					srcArray = new SparseDoubleMatrix2D(1, noNodes);
					if(node1 != 0){
						srcArray.set(0, node1-1, 1);   //dependent voltage source positive
					}
					if(node2 != 0){
						srcArray.set(0, node2-1, -1);  //dependent voltage source negative
					}
					
					if(pcNodeId != 0){
						srcArray.set(0, pcNodeId-1, getValue()); //controlling voltage source positive 
					}
					if(ncNodeId != 0){
						srcArray.set(0, ncNodeId-1,  -getValue()); //controlling voltage source negative
					}
				}else if(getControlType() == ElementControlType.CC){
					// common stamp doesn't give me results.
					srcArray = new SparseDoubleMatrix2D(2,noNodes);
					if(pcNodeId != 0){ 
						srcArray.set(0, pcNodeId-1, 1); //j
					}
					if(ncNodeId != 0){
						srcArray.set(0, ncNodeId-1, -1); //j'
					}
					if(node1 != 0){
						srcArray.set(1, node1-1, 1); //k  //dependent voltage source positive
					}
					if(node2 != 0){
						srcArray.set(1, node2-1, -1); //k' //dependent voltage source negative
					}
				
				}
				
				return srcArray;
	}

	public double getVoltage() {
		for(CircuitElement element : getModel().getElements()){
			int node1 = element.getCircuitNodes().get(0).id();
			int node2 = element.getCircuitNodes().get(1).id();
			if( node1 == pcNodeId
					&& node2 == ncNodeId){
				if(this.getControlType() == ElementControlType.CC){
					System.out.println("Current controlled "+element.getName()+" "+element.getCurrent()+" "+getValue()+"  "+element.getCurrent()*getValue());
					return element.getCurrent()*getValue();
				}else if(this.getControlType() == ElementControlType.VC){
					return element.getVoltage()* getValue();
				}
			}
		}
		return 0.0;
	}

	@Override
	public void update(AnalysisType inType) {
		// TODO Auto-generated method stub
		
	}

}
