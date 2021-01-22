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

public class TestCapacitor{
	
	public static void main(String[] args){
		ModelReader reader = new ModelReader();
		try{
			CircuitModel model = reader.readModel("capacitor/capacitor3_dc.cir");
			CircuitSimulator sim = new CircuitSimulator(model);
			DoubleMatrix2D results = sim.runSimulation();
			System.out.println(results);
			/*
			double[] time = results.viewColumn(0).toArray();
			double[] ny1 = results.viewColumn(1).toArray();
			
			double[] ny2 = results.viewColumn(2).toArray();
			double[] ny3 = results.viewColumn(3).toArray();
			
			double[] nr1 = new double[ny1.length];
			for(int i=0; i<ny1.length; i++){
				nr1[i]=ny2[i]-ny3[i];
			}
			Plot2DPanel plot = new Plot2DPanel();
			plot.addLinePlot("node 1 voltage", Color.blue, time, ny1);
			
			plot.addLinePlot("node 2 voltage", Color.red, time, ny2);
			plot.addLinePlot("node 3 voltage", Color.green, time, ny3);
			plot.addLinePlot("voltage across r1", Color.MAGENTA, time, nr1);
		
			plot.addLegend(plot.NORTH);
			
			JFrame frame = new JFrame("sine waveform panel");
			frame.setContentPane(plot);
			
			frame.setVisible(true);
			frame.setSize(800, 800);
			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			*/
			//model.plotResults();
		}catch(IOException e){
			e.printStackTrace();
		}catch(ModelReaderException e){
			e.printStackTrace();
		}
	}
}
