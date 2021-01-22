package com.soa.circuit.model;

import java.util.List;

import com.soa.circuit.common.CircuitElement;
import com.soa.circuit.common.CircuitMatrixUtil;
import com.soa.circuit.common.CircuitNode;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;

public class CircuitAnalyzer {
	private final CircuitModel model;
	//n = no of nodes in the circuit.
	//m = no of independent voltage sources.
	private DoubleMatrix2D A = null;
	private SparseDoubleMatrix2D z = null;
	
	private DoubleFactory2D factory = DoubleFactory2D.sparse;
	
	public CircuitAnalyzer(CircuitModel inModel){
		this.model = inModel;
	}
	
	public DoubleMatrix2D buildModelMatrix(){
		List<CircuitNode> nodes = model.getNodes();
		int noNodes = nodes.size()-1;
		
		SparseDoubleMatrix2D G = new SparseDoubleMatrix2D(noNodes, noNodes); //conductance matrix nxn, interconnection b/w passive elements (i.e. resistors)
		SparseDoubleMatrix2D B = null; //nxm connection of voltage sources 
	    SparseDoubleMatrix2D C = null ; //mxn connection of voltage sources
		//B and C are closely related when only independent sources are considered.
		SparseDoubleMatrix2D D = null; //mxm dependent voltage source, is zero when only independent sources are considered.
		
		//z=[i;e;i(x)] where i is independent current source, e independent voltage source, i(x) elements whose current needs to be determined.
		SparseDoubleMatrix2D i = new SparseDoubleMatrix2D(noNodes, 1);
		SparseDoubleMatrix2D e = null;
		
		for(CircuitElement element : model.getElements()){
			G = (SparseDoubleMatrix2D)element.stampConductancesMatrix(G);
			C = (SparseDoubleMatrix2D)element.stampVoltageMatrixC(C);
			B = (SparseDoubleMatrix2D)element.stampVoltageMatrixB(B);
			D = (SparseDoubleMatrix2D)element.stampDependentMatrixD(D);
			i = (SparseDoubleMatrix2D)element.stampNodeCurrent(i);
			e = (SparseDoubleMatrix2D)element.stampNodeVoltage(e);
		}
		
		if(model.printModelState()){
			System.out.println("Conductance Matrix G: "+ G.toString());
			System.out.println("Voltage Calculation B: "+ B.toString());
			System.out.println("Curent Calculation C: "+ C.toString());
			System.out.println("Dependent Calculation D: "+D.toString());
		}
		//Building the A matrix
		A = factory.appendRows(factory.appendColumns(G, B), factory.appendColumns(C, D));
		z = (SparseDoubleMatrix2D)factory.appendRows(i, e);
		
		if(model.printModelState()){
			System.out.println("A: "+A);
			System.out.println("z: "+z);
		}
		
		return A;
	}
	
	public DoubleMatrix2D update(AnalysisType inType){
		//Updating the element voltage and current based on previous iteration.
		for(CircuitElement element : model.getElements()){
			element.update(inType);
		}
		return A;
	}
	
	public void setAnalysisType(AnalysisType inType){
		model.setAnalysisType(inType);
	}
	
	public DoubleMatrix2D getResults(){
		return model.getResults();
	}
	
	public void setTimeStep(double inTimeStep){
		model.setTimeStep(inTimeStep);
	}
	
	public void setTimeCount(int inTime){
		model.setTimeCount(inTime);
	}
	
	public boolean isNonLinear(){
		return model.isNonLinear();
	}
	
	public boolean hasACSource(){
		return model.hasACSource();
	}
	
	public boolean isConverged(){
		return model.isConverged();
	}
	
	public int noNodes(){
		return model.getNoNodes();
	}
	
	public void resetConvergence(){
		model.resetConvergence();
	}
	
	public void setResults(DoubleMatrix2D results){
		model.setResults(results);
	}
	
	public DoubleMatrix2D solveModelMatrix(){
		Algebra a = new Algebra();
		this.buildModelMatrix();
	
		DoubleMatrix2D r = a.mult(a.inverse(A) , z);
		DenseDoubleMatrix2D results = new DenseDoubleMatrix2D(r.rows(), r.columns());
		results.assign(r);
		return results;
		//model.setResults(results);
	}
}
