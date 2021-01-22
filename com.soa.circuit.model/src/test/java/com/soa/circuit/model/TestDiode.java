package com.soa.circuit.model;
import java.io.IOException;

import com.soa.circuit.exception.ModelReaderException;
import com.soa.circuit.model.reader.ModelReader;

import cern.colt.matrix.DoubleMatrix2D;
import junit.framework.TestCase;

public class TestDiode{
	
	public void xtestDiode1(){
		ModelReader reader = new ModelReader();
		try{
			//Require more than 50 iteration to converge.
			//CircuitModel model = reader.readModel("diode/diode1.cir");
			CircuitModel model = reader.readModel("capacitor/capacitor3_dc.cir");
			//CircuitModel model = reader.readModel("inductor/inductor3.cir");
			CircuitSimulator sim = new CircuitSimulator(model);
			
			DoubleMatrix2D lresults = sim.solveLinearCircuit();
			System.out.println("results :"+lresults);
			DoubleMatrix2D results = sim.solveNonLinearCircuit();
			System.out.println("non-linear result "+results);
		}catch(IOException e){
			e.printStackTrace();
		}catch(ModelReaderException e){
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args){
		ModelReader reader = new ModelReader();
		try{
			CircuitModel model = reader.readModel("diode/diode4.cir");
			CircuitSimulator sim = new CircuitSimulator(model);
			DoubleMatrix2D results = sim.runSimulation();
			model.plotResults();
		}catch(IOException e){
			e.printStackTrace();
		}catch(ModelReaderException e){
			e.printStackTrace();
		}
	}
}
