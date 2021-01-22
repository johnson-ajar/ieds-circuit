package com.soa.circuit.model;

import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SpringLayout;

import org.math.plot.Plot2DPanel;

import com.soa.circuit.common.CircuitElement;
import com.soa.circuit.common.CircuitNode;

import cern.colt.list.DoubleArrayList;
import cern.colt.matrix.DoubleFactory1D;

public class CircuitResult {
	private final CircuitModel model;
	private DoubleArrayList time;
	private DoubleFactory1D dd = DoubleFactory1D.sparse;
	private Map<Integer, DoubleArrayList> nodeVoltages;
	private Map<String, DoubleArrayList> elementVoltages;
	private Map<String, DoubleArrayList> elementCurrents;
	public CircuitResult(CircuitModel inModel){
		this.model = inModel;
	
		time = new DoubleArrayList();
		nodeVoltages = new HashMap<Integer, DoubleArrayList>();
		for(CircuitNode node : model.getNodes()){
			nodeVoltages.put(node.id(), new DoubleArrayList());
		}
		elementVoltages = new HashMap<String, DoubleArrayList>();
		elementCurrents = new HashMap<String, DoubleArrayList>();
		for(CircuitElement element : model.getElements()){
			elementVoltages.put(element.getName(), new DoubleArrayList());
			elementCurrents.put(element.getName(), new DoubleArrayList());
		}
		
		
	}
	
	
	public void addResult(){
		time.add(model.getTimeCount()*model.getTimeStep());
		for(CircuitNode node : model.getNodes()){
			nodeVoltages.get(node.id()).add(node.getNodeVoltage());
		}
		
		for(CircuitElement element : model.getElements()){
			elementVoltages.get(element.getName()).add(element.getVoltage());
			elementCurrents.get(element.getName()).add(element.getCurrent());
		}
			
	}
	
	
	public void plotResults(){
		CircuitResultFrame rFrame = new CircuitResultFrame();
		rFrame.plotResults();
	}
	
	
	private class CircuitResultFrame extends JFrame{
		private JTabbedPane tabPane = new JTabbedPane();
		JPanel nodeVoltagePane;
		JPanel elementVoltagePane;
		JPanel elementCurrentPane;
		JPanel nodeVoltageCheckboxPane;
		SpringLayout nodeVoltageCheckboxLayout;
		public CircuitResultFrame(){
			nodeVoltagePane = new JPanel();
			nodeVoltagePane.setLayout(new GridLayout());
			nodeVoltageCheckboxPane = new JPanel();
			nodeVoltageCheckboxLayout = new SpringLayout();
			nodeVoltageCheckboxPane.setLayout(nodeVoltageCheckboxLayout);
			
			elementVoltagePane = new JPanel();
			elementVoltagePane.setLayout(new GridLayout());
			elementCurrentPane = new JPanel();
			elementCurrentPane.setLayout(new GridLayout());
			setContentPane(tabPane);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setBounds(100, 100, 917, 788);
		}
		
		public void plotResults(){
			tabPane.addTab("Node Voltage", nodeVoltagePane);
			tabPane.addTab("ElementVoltage", elementVoltagePane);
			tabPane.addTab("Element Current", elementCurrentPane );
			
			Plot2DPanel nodeVoltagePlot =new Plot2DPanel();
			int count = 0;
			for(Integer node : nodeVoltages.keySet()){				
				nodeVoltagePlot.addLinePlot("NODE "+node, time.elements(), nodeVoltages.get(node).elements());
				JCheckBox box = new JCheckBox();
				box.setText("NODE "+node);	
				nodeVoltageCheckboxPane.add(box);
				count++;
			}
			
			Plot2DPanel elementVoltagePlot = new Plot2DPanel();
			Plot2DPanel elementCurrentPlot = new Plot2DPanel();
			
			System.out.println(time.size()+" "+time.elements().length+" "+time.partFromTo(0, time.size()-1).elements().length);
			for(String name : elementVoltages.keySet()){
				double[] t = time.partFromTo(0, time.size()-1).elements();
				double[] v = elementVoltages.get(name).partFromTo(0, time.size()-1).elements();
				double[] c = elementCurrents.get(name).partFromTo(0, time.size()-1).elements();
				elementVoltagePlot.addLinePlot(name, t, v);
				elementCurrentPlot.addLinePlot(name, t, c);
			}
			nodeVoltagePlot.addLegend("SOUTH");
			elementVoltagePlot.addLegend("SOUTH");
			elementCurrentPlot.addLegend("SOUTH");
			nodeVoltagePane.add(nodeVoltagePlot);
			nodeVoltagePane.add(nodeVoltageCheckboxPane);
			elementVoltagePane.add(elementVoltagePlot);
			elementCurrentPane.add(elementCurrentPlot);
			setVisible(true);
		}
	}
}
