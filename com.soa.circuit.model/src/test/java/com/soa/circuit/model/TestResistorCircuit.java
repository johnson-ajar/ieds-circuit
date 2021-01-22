package com.soa.circuit.model;

import java.io.IOException;
import java.text.NumberFormat;

import com.soa.circuit.exception.ModelReaderException;
import com.soa.circuit.model.reader.ModelReader;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import junit.framework.TestCase;

public class TestResistorCircuit extends TestCase{
	
	public void testResistor1(){
		ModelReader reader = new ModelReader();
		try{
			CircuitModel model = reader.readModel("resistor/resistor1.cir");
			CircuitSimulator sim = new CircuitSimulator(model);
			DoubleMatrix2D results = sim.solveLinearCircuit();
			double[] voltage = {12.0, -8.0, -24.0, -2.67};
			double[] current = {3.33, 4.0};
			
			for(int i=0;i<voltage.length; i++){
				assertTrue(voltage[i], results.get(i, 0));
			}
			for(int i=0; i<current.length;i++){
				assertTrue(current[i],results.get(voltage.length+i, 0));
			}
		}catch(IOException e){
			e.printStackTrace();
		} catch (ModelReaderException e) {
			e.printStackTrace();
		}
	}
	
	public void testResistor2(){
		ModelReader reader = new ModelReader();
		try{
			CircuitModel model = reader.readModel("resistor/resistor2.cir");
			CircuitSimulator sim = new CircuitSimulator(model);
			DoubleMatrix2D results = sim.solveLinearCircuit();
			double[] voltage = {1.0, 0.538, -1.0};
			double[] current = {0.07, 0.892};
			
			for(int i=0;i<voltage.length; i++){
				assertTrue(voltage[i], results.get(i, 0));
			}
			for(int i=0; i<current.length;i++){
				assertTrue(current[i],results.get(voltage.length+i, 0));
			}
		}catch(IOException e){
			e.printStackTrace();
		} catch (ModelReaderException e) {
			e.printStackTrace();
		}
	}
	
	public void testResistor3(){
		ModelReader reader = new ModelReader();
		try{
			CircuitModel model = reader.readModel("resistor/resistor3.cir");
			CircuitSimulator sim = new CircuitSimulator(model);
			DoubleMatrix2D results = sim.solveLinearCircuit();
			
			double[] voltage = {3.125, -1.875, 8.125, 15, 13.125, 0.4375};
			double[] current = {4.4375, 3.375, 1.0};
			
			for(int i=0;i<voltage.length; i++){
				assertTrue(voltage[i], results.get(i, 0));
			}
			for(int i=0; i<current.length;i++){
				assertTrue(current[i],results.get(voltage.length+i, 0));
			}
		}catch(IOException e){
			e.printStackTrace();
		} catch (ModelReaderException e) {
			e.printStackTrace();
		}
	}
	
	public void testResistor4(){
		ModelReader reader = new ModelReader();
		try{
			CircuitModel model = reader.readModel("resistor/resistor4.cir");
			CircuitSimulator sim = new CircuitSimulator(model);
			DoubleMatrix2D results = sim.solveLinearCircuit();
			
			double[] voltage = {11.612903,
					-20.387097,
					-25,       
					 10,       
					 -5,       
					  6.322581,
					-17      };
			double[] current = {2.645161,
					  1.153226,
					  2.7 ,
					 11.842473,
					  5.353226};
			
			for(int i=0;i<voltage.length; i++){
				assertTrue(voltage[i], results.get(i, 0));
			}
			for(int i=0; i<current.length;i++){
				assertTrue(current[i],results.get(voltage.length+i, 0));
			}
		}catch(IOException e){
			e.printStackTrace();
		} catch (ModelReaderException e) {
			e.printStackTrace();
		}
	}
	
	public void testResistor5(){
		ModelReader reader = new ModelReader();
		try{
			CircuitModel model = reader.readModel("resistor/resistor5.cir");
			CircuitSimulator sim = new CircuitSimulator(model);
			DoubleMatrix2D results = sim.solveLinearCircuit();
	
			double[] voltage = {15,       
					28,       
					30,       
					25,       
					12.741935,
					43.428571,
					27.741935,
					23.428571};
			double[] current = {-3.551613,
					 2.271521,
					 0.447143,
					 0.832949,
					 0.288571,
					 1.548387};
			
			for(int i=0;i<voltage.length; i++){
				assertTrue(voltage[i], results.get(i, 0));
			}
			for(int i=0; i<current.length;i++){
				assertTrue(current[i],results.get(voltage.length+i, 0));
			}
		}catch(IOException e){
			e.printStackTrace();
		} catch (ModelReaderException e) {
			e.printStackTrace();
		}
	}
	
	public void testSwitchResistor1(){
		ModelReader reader = new ModelReader();
		try{
			CircuitModel model = reader.readModel("switch/resistor1.cir");
			CircuitSimulator sim = new CircuitSimulator(model);
			DoubleMatrix2D results = sim.solveLinearCircuit();
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
