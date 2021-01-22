package com.soa.circuit.model;

import java.io.IOException;
import java.text.NumberFormat;

import com.soa.circuit.exception.ModelReaderException;
import com.soa.circuit.model.reader.ModelReader;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import junit.framework.TestCase;

public class TestCCVS extends TestCase{
	
	public void xtestResistor1(){
		ModelReader reader = new ModelReader();
		try{
			CircuitModel model = reader.readModel("sources/ccvs/resistor1_ccvs.cir");
			//CircuitModel model = reader.readModel("sources/ccvs/ccvs_3.cir");
			CircuitSimulator sim = new CircuitSimulator(model);
			DoubleMatrix2D results = sim.solveLinearCircuit();
			double[] voltage = {25.0, -31.67, -20.0, -10.56, -91.67, -91.67, -91.67};
			//The current through voltage source are negative need to look into it.
			double[] current = {5.0, -4.44, 10.0, -7.08};
			
			for(int i=0; i<voltage.length; i++){
				assertTrue(voltage[i], results.get(i, 0));
			}
			
			for(int i=0; i< current.length; i++){
				assertTrue(current[i], results.get(voltage.length+i, 0));
			}
		}catch(IOException e){
			e.printStackTrace();
		}catch(ModelReaderException e){
			e.printStackTrace();
		}
	}
	
	
	public void xtestccvs_1(){
		ModelReader reader = new ModelReader();
		try{
		CircuitModel model = reader.readModel("sources/ccvs/ccvs_1.cir");
		CircuitSimulator sim = new CircuitSimulator(model);
		DoubleMatrix2D results = sim.solveLinearCircuit();
		double[] voltages = {2.57, 4.71, 4.71};
		//TODO: current through dependent voltage source is negative (not sure if it correct check.)
		double[] currents = {-0.43, 0.79};
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
	
	
	public void xtestccvs_2(){
		ModelReader reader = new ModelReader();
		try{
		CircuitModel model = reader.readModel("sources/ccvs/ccvs_2.cir");
		//CircuitModel model = reader.readModel("sources/ccvs/ccvs_3.cir");
		CircuitSimulator sim = new CircuitSimulator(model);
		DoubleMatrix2D results = sim.solveLinearCircuit();
		double[] voltages = {4.8, 2.4, -2.4, 4.8};
		//TODO: current through dependent voltage source is negative (not sure if it correct check.)
		double[] currents = {1.2, -2.4}; 
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
	
	public void xtestccvs_3(){
		ModelReader reader = new ModelReader();
		try{
		CircuitModel model = reader.readModel("sources/ccvs/ccvs_3.cir");
		CircuitSimulator sim = new CircuitSimulator(model);
		DoubleMatrix2D results = sim.solveLinearCircuit();
		double[] voltages = {24.0, 9.0, 6.0, 24.0};
		//TODO: current through dependent voltage source is negative (not sure if it correct check.)
		double[] currents = {2.25, 1.5, 1.5}; 
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
	
	public void testccvs_4(){
		ModelReader reader = new ModelReader();
		try{
		CircuitModel model = reader.readModel("sources/ccvs/ccvs_4.cir");
		CircuitSimulator sim = new CircuitSimulator(model);
		DoubleMatrix2D results = sim.solveLinearCircuit();
		
		double[] voltages = {75.0, 40.0, 20.0, 0.0};
		//TODO: current through dependent voltage source is negative (not sure if it correct check.)
		double[] currents = {7, 2, 5}; 
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
	
	private void assertTrue(double expected, double actual){
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(2);
		System.out.println(format.format(expected)+" "+ format.format(actual));
		assertEquals(format.format(expected), format.format(actual));
		//assertTrue(Math.round(expected - actual)/100==0);
	}
}
