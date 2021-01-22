package com.soa.circuit.model;

import java.io.IOException;

import com.soa.circuit.exception.ModelReaderException;
import com.soa.circuit.model.reader.ModelReader;

import cern.colt.matrix.DoubleMatrix2D;
import junit.framework.TestCase;

public class TestVCCS extends TestCase{
		
	public void testResistor1(){
		ModelReader reader = new ModelReader();
		try{
			//CircuitModel model = reader.readModel("sources/resistor1_vccs.cir");
			CircuitModel model = reader.readModel("sources/vccs/vccs_1.cir");
			CircuitSimulator sim = new CircuitSimulator(model);
			DoubleMatrix2D results = sim.solveLinearCircuit();
			
		}catch(IOException e){
			e.printStackTrace();
		}catch(ModelReaderException e){
			e.printStackTrace();
		}
	}

}
