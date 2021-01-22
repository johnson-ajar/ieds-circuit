package com.soa.circuit.model;

import java.io.IOException;
import java.text.NumberFormat;

import com.soa.circuit.exception.ModelReaderException;
import com.soa.circuit.model.reader.ModelReader;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import junit.framework.TestCase;

public class TestCCCS extends TestCase{
	public void test_cccs1(){
		ModelReader reader = new ModelReader();
		try{
			CircuitModel model = reader.readModel("sources/cccs/cccs_1.cir");
			CircuitSimulator sim = new CircuitSimulator(model);
			DoubleMatrix2D results = sim.solveLinearCircuit();
			double[] voltages = {6.75, 9.5, 10, 10};
			double[] currents = {7.25, -0.25}; 
			for(int i=0; i< voltages.length; i++){
				assertTrue(voltages[i], results.get(i, 0) );
			}
			for(int i=0; i< currents.length; i++){
				assertTrue(currents[i], results.get(voltages.length+i, 0));
			}
		}catch(IOException e){
			e.printStackTrace();
		}catch(ModelReaderException e){
			e.printStackTrace();
		}
	}
	
	public void xtest_cccs2(){
		ModelReader reader = new ModelReader();
		try{
			CircuitModel model = reader.readModel("sources/cccs/cccs_2.cir");
			CircuitSimulator sim = new CircuitSimulator(model);
			DoubleMatrix2D results = sim.solveLinearCircuit();
			double[] voltages = {54.0, 0.0};
			double[] currents = {9.0}; 
			for(int i=0; i< voltages.length; i++){
				assertTrue(voltages[i], results.get(i, 0) );
			}
			for(int i=0; i< currents.length; i++){
				assertTrue(currents[i], results.get(voltages.length+i, 0));
			}
		}catch(IOException e){
			e.printStackTrace();
		}catch(ModelReaderException e){
			e.printStackTrace();
		}
	}
	
	public void xtest_cccs3(){
		ModelReader reader = new ModelReader();
		try{
			CircuitModel model = reader.readModel("sources/cccs/cccs_3.cir");
			CircuitSimulator sim = new CircuitSimulator(model);
			DoubleMatrix2D results = sim.solveLinearCircuit();
			double[] voltages = {28.0, 64.0,0.0};
			double[] currents = {7.0}; 
			for(int i=0; i< voltages.length; i++){
				assertTrue(voltages[i], results.get(i, 0) );
			}
			for(int i=0; i< currents.length; i++){
				assertTrue(currents[i], results.get(voltages.length+i, 0));
			}
		}catch(IOException e){
			e.printStackTrace();
		}catch(ModelReaderException e){
			e.printStackTrace();
		}
	}
	
	public void xtest_cccs4(){
		ModelReader reader = new ModelReader();
		try{
			CircuitModel model = reader.readModel("sources/cccs/cccs_4.cir");
			CircuitSimulator sim = new CircuitSimulator(model);
			DoubleMatrix2D results = sim.solveLinearCircuit();
			/*[] voltages = {28.0, 64.0,0.0};
			double[] currents = {7.0}; 
			for(int i=0; i< voltages.length; i++){
				assertTrue(voltages[i], results.get(i, 0) );
			}
			for(int i=0; i< currents.length; i++){
				assertTrue(currents[i], results.get(voltages.length+i, 0));
			}*/
		}catch(IOException e){
			e.printStackTrace();
		}catch(ModelReaderException e){
			e.printStackTrace();
		}
	}
	
	
	private void assertTrue(double expected, double actual){
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(2);
		System.out.println(format.format(expected)+" "+ format.format(actual));
		assertEquals(format.format(expected), format.format(actual));
		//assertTrue(Math.round(expected - actual)/100==0);
	}
}
