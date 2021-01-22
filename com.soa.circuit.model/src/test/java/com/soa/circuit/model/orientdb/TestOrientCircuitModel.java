package com.soa.circuit.model.orientdb;

import java.io.IOException;
import java.text.NumberFormat;

import org.junit.Assert;
import org.junit.Test;

import com.soa.circuit.exception.ModelReaderException;
import com.soa.circuit.model.CircuitModel;
import com.soa.circuit.model.CircuitSimulator;
import com.soa.circuit.model.reader.ModelReader;
import com.soa.circuit.persist.CircuitElementVertex;
import com.soa.circuit.persist.CircuitPersistanceGremlinFactory;

import cern.colt.matrix.DoubleMatrix2D;
import junit.framework.TestCase;

public class TestOrientCircuitModel extends TestCase{
	
		@Test
		public void testResistor1(){
			ModelReader reader = new ModelReader();
			try{
				CircuitPersistanceGremlinFactory factory = CircuitPersistanceGremlinFactory.getInstance();
				factory.connect("remote:localhost:/resistor5", "admin", "admin");
				CircuitModel model = null;
				
				//if(factory.databaseExists() && !factory.containClass(CircuitElementVertex.class)){
					//model = reader.readModel("resistor/resistor3.cir");
					model = reader.readModel("resistor/resistor5.cir");
					model.save();
					
				//}else{
				//	model = new CircuitModel(factory);
				//	model.load();
				//}
				
				/*
				//model = reader.readModel("resistor/resistor1.cir");
				CircuitSimulator sim = new CircuitSimulator(model);
				
				DoubleMatrix2D results = sim.solveLinearCircuit();
				
				
				double[] voltage = {12.0, -8.0, -24.0, -2.67};
				double[] current = {-3.33, 4.0};
			
				for(int i=0;i<voltage.length; i++){
					assertTrue(voltage[i], results.get(i, 0));
				}
				for(int i=0; i<current.length;i++){
					assertTrue(current[i],results.get(voltage.length+i, 0));
				}
				*/
			}catch(IOException e){
				e.printStackTrace();
			} catch (ModelReaderException e) {
				e.printStackTrace();
			}
		}
	
		private void assertTrue(double expected, double actual){
			NumberFormat format = NumberFormat.getInstance();
			format.setMaximumFractionDigits(2);
			System.out.println(format.format(expected)+" "+ format.format(actual));
			Assert.assertEquals(format.format(expected), format.format(actual));
			//assertTrue(Math.round(expected - actual)/100==0);
		}
}
	
	
	
	