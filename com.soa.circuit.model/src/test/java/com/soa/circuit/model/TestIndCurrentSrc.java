package com.soa.circuit.model;

import java.io.IOException;
import java.text.NumberFormat;

import com.soa.circuit.exception.ModelReaderException;
import com.soa.circuit.model.reader.ModelReader;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import junit.framework.TestCase;

public class TestIndCurrentSrc extends TestCase{

		public void testCurrentSrc1(){
			ModelReader reader = new ModelReader();
			try{
				CircuitModel model = reader.readModel("sources/ind_i_v_src/resistor1_icv.cir");
				CircuitSimulator sim = new CircuitSimulator(model);
				DoubleMatrix2D results = sim.solveLinearCircuit();
				
				double[] voltage  =  {12.0, -38.0, -24.0, -12.667, -98.0};
				double[] current = {3.33, 6.5};
				for(int i=0; i<voltage.length; i++){
					assertTrue(voltage[i], results.get(i, 0));
				}
				for(int i=0; i<current.length;i++){
					assertTrue(current[i], results.get(voltage.length+i, 0));
				}
			}catch(IOException e){
				e.printStackTrace();
			}catch(ModelReaderException e){
				e.printStackTrace();
			}
		}
		
		public void testCurrentSrc2(){
			ModelReader reader = new ModelReader();
			try{
				CircuitModel model = reader.readModel("sources/ind_i_v_src/resistor2_icv.cir");
				CircuitSimulator sim = new CircuitSimulator(model);
				DoubleMatrix2D results = sim.solveLinearCircuit();
				
				double[] voltage = {1.0, 0.538, -1.0};
				double[] current = {5.069, -14.107};
				
				for(int i=0; i< voltage.length; i++){
					assertTrue(voltage[i], results.get(i,0));
				}
				for(int i=0;i<current.length; i++){
					assertTrue(current[i], results.get(voltage.length+i, 0));
				}
			}catch(IOException e){
				e.printStackTrace();
			}catch(ModelReaderException e){
				e.printStackTrace();
			}
		}
		
		public void testCurrentSrc3(){
			ModelReader reader = new ModelReader();
			try{
				CircuitModel model = reader.readModel("sources/ind_i_v_src/resistor3_icv.cir");
				CircuitSimulator sim = new CircuitSimulator(model);
				DoubleMatrix2D results = sim.solveLinearCircuit();
				
				double[] voltage = {3.125, -1.875, 8.125, 15.0, 13.125};
				double[] current = {-9.5625, 14.4375, 3.375, 1};
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
		
		public void testCurrentSrc4(){
			ModelReader reader = new ModelReader();
			try{
				CircuitModel model = reader.readModel("sources/ind_i_v_src/resistor4_icv.cir");
				CircuitSimulator sim = new CircuitSimulator(model);
				DoubleMatrix2D results = sim.solveLinearCircuit();
				
				double[] voltages = {-1.290, -33.290, -25.0, 10.0,-5.0, 3.742, -17.0};
				double[] currents = {7.4838, 7.9274, -12.3, 16.036, 2.1274};
				for(int i=0;i<voltages.length; i++){
					assertTrue(voltages[i], results.get(i, 0));
				}
				for(int i=0; i<currents.length;i++){
					assertTrue(currents[i], results.get(voltages.length+i, 0));
				}
			}catch(IOException e){
				e.printStackTrace();
			}catch(ModelReaderException e){
				e.printStackTrace();
			}
		}
		
		public void testCurrentSrc5(){
			ModelReader reader = new ModelReader();
			try{
				CircuitModel model = reader.readModel("sources/ind_i_v_src/resistor5_icv.cir");
				CircuitSimulator sim = new CircuitSimulator(model);
				DoubleMatrix2D results = sim.solveLinearCircuit();
				
				double[] voltages = {15.0, 28.0, 30.0, 25.0, 12.74, 43.428, 27.741, 23.428};
				double[] currents = {-3.551, 2.271, 0.4471, 0.832, -9.711, -8.451};
				for(int i=0;i<voltages.length; i++){
					assertTrue(voltages[i], results.get(i, 0));
				}
				for(int i=0; i<currents.length; i++){
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
