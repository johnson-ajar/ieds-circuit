package com.soa.circuit.model;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;

public class CircuitSimulator {
	private final CircuitAnalyzer analyzer;
	//int maxTime = 4000;
	int maxTime = 25000;
	private DoubleMatrix2D results;
	public CircuitSimulator(CircuitModel inModel){
		analyzer = new CircuitAnalyzer(inModel);
		
	}
	
	//Linear circuit DC analysis-basis point.
	public DoubleMatrix2D solveLinearCircuit(){
		DoubleMatrix2D results = analyzer.solveModelMatrix();
		analyzer.setResults(results);
		analyzer.update(AnalysisType.DC);
		return results;
	}
	
	//Non-linear DC circuit analysis
	public DoubleMatrix2D solveNonLinearCircuit(){
		int iteration = 0;
		int max_iteration = 200;
		analyzer.resetConvergence();
		while(!analyzer.isConverged()){
			System.out.println("Iteration :"+iteration);
			if(iteration > max_iteration){
				break;
			}
			
			DoubleMatrix2D results =analyzer.solveModelMatrix();
			analyzer.setResults(results);
			analyzer.update(AnalysisType.DC);
			
			iteration++;
		}
		return analyzer.getResults();
	}
	
	public DoubleMatrix2D runSimulation(){
		int t=0;
		if(!analyzer.hasACSource()){
			maxTime = 100;
		}
		results = new DenseDoubleMatrix2D(maxTime, analyzer.noNodes()+1);
		double timeStep = 5e-6;
		double time = 0;//timeStep*90;
		analyzer.setTimeStep(timeStep);
		DoubleMatrix2D r = solveLinearCircuit(); //Calculating the start point.
		
		//System.out.println("Result after linear solving"+r);
		while(t<maxTime){
			System.out.println("------------------------timeStep: "+t+" time : "+time);
			analyzer.setTimeCount(t);
			
			if(analyzer.isNonLinear()){
				 r = solveNonLinearCircuit();
			}else{
				this.solveLinearCircuit();
			}
			
			r = analyzer.getResults();
			//Do transient analysis update
			analyzer.update(AnalysisType.TRANSIENT);
			updateResults(t, time, r);
			time=time+timeStep;
			t++;
		}
		return results;
	}
	
	private void updateResults(int t, double time, DoubleMatrix2D r){
		results.set(t, 0, time);
		for(int i=1; i<analyzer.noNodes(); i++){
			results.set(t, i, r.get(i-1, 0));
		}
	}
	
	
}
