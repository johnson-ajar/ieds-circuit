package com.soa.circuit.model.reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.soa.circuit.elements.diode.Diode;
import com.soa.circuit.elements.passive.Resistor;
import com.soa.circuit.elements.passive.Switch;
import com.soa.circuit.elements.reactive.Capacitor;
import com.soa.circuit.elements.reactive.Inductor;
import com.soa.circuit.elements.reactive.Capacitor;
import com.soa.circuit.elements.source.CurrentSource;
import com.soa.circuit.elements.source.DepCurrentSource;
import com.soa.circuit.elements.source.DepVoltageSource;
import com.soa.circuit.elements.source.DependentSource;
import com.soa.circuit.elements.source.VoltageSource;
import com.soa.circuit.exception.ModelReaderException;
import com.soa.circuit.model.CircuitModel;
import com.soa.circuit.model.example.FileResource;
import com.soa.circuit.persist.CircuitPersistanceGremlinFactory;

public class ModelReader {
	
	private final CircuitModel model;
	public ModelReader(){
		CircuitPersistanceGremlinFactory factory = CircuitPersistanceGremlinFactory.getInstance();
		model = new CircuitModel(factory);
	}
	
	public CircuitModel readModel(String fileName) throws FileNotFoundException, ModelReaderException{
		System.out.println("Reading model from "+fileName);
		FileResource rFile = FileResource.getInstance();
		BufferedReader reader = rFile.getBufferedReader("com.soa.circuit.model", "examples/", fileName);
		if(reader == null){
			System.out.println("reader is null");
		}
		String line = null;
		try{
			while((line = reader.readLine())!=null){
				System.out.println(line);
				if(line.startsWith("#")){
					continue;
				}else if(line.startsWith("r")){
					Resistor r = new Resistor(model);
					r.parse(line);
				}else if(line.startsWith("s")){
					Switch s = new Switch(model);
					s.parse(line);
				}else if(line.startsWith("v")){
					VoltageSource vs = new VoltageSource(model);
					vs.parse(line);
				}else if(line.startsWith("i")){
					CurrentSource cs = new CurrentSource(model);
					cs.parse(line);
				}else if(line.startsWith("dvs")){
					DependentSource vc = new DepVoltageSource(model);
					vc.parse(line);
				}else if(line.startsWith("dcs")){
					DependentSource cc = new DepCurrentSource(model);
					cc.parse(line);
				}else if(line.startsWith("d")){
					Diode d = new Diode(model);
					d.parse(line);
				}else if(line.startsWith("c")){
					Capacitor c = new Capacitor(model);
					c.parse(line);
				}else if(line.startsWith("l")){
					Inductor l = new Inductor(model);
					l.parse(line);
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return model;
	}
		
}
