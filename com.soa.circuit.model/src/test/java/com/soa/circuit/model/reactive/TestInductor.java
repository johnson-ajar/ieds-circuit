package com.soa.circuit.model.reactive;

import java.awt.Color;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import java.io.IOException;
import org.math.plot.Plot2DPanel;

import com.soa.circuit.exception.ModelReaderException;
import com.soa.circuit.model.CircuitModel;
import com.soa.circuit.model.CircuitSimulator;
import com.soa.circuit.model.reader.ModelReader;

import cern.colt.matrix.DoubleMatrix2D;

public class TestInductor{
	public static void main(String[] args){
		ModelReader reader = new ModelReader();
		try{
			CircuitModel model = reader.readModel("inductor/inductor3.cir");
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
