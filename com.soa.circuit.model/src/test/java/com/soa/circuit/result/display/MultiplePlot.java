package com.soa.circuit.result.display;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import org.math.plot.Plot2DPanel;

public class MultiplePlot extends JFrame {

private JPanel contentPane;
private JPanel contentPane1;
private JTabbedPane tabPane; 

/**
 * Launch the application.
 */
public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
        public void run() {
            try {
                MultiplePlot frame = new MultiplePlot();
                frame.dz();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });
}

/**
 * Create the frame.
 */
public  void dz() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 917, 788);
    tabPane = new JTabbedPane();
    contentPane = new JPanel();
    contentPane1 = new JPanel();
    tabPane.addTab("voltageTab", contentPane);
    tabPane.addTab("currentTab", contentPane1);
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(tabPane);
   // SpringLayout sl_contentPane = new SpringLayout();
    //contentPane.setLayout(sl_contentPane);
    contentPane.setLayout(new GridLayout());
    contentPane1.setLayout(new GridLayout());
    // define your data
    double[] x = { 0, 1, 2, 3, 4, 5 };
    double[] y = { 45, 89, 6, 32, 63, 12 };

    // create your PlotPanel (you can use it as a JPanel)
    Plot2DPanel plot = new Plot2DPanel();

    // define the legend position
    plot.addLegend("SOUTH");

    // add a line plot to the PlotPanel
    plot.addLinePlot("my plot", x, y);
    
    contentPane.add(plot);

    Plot2DPanel plot1 = new Plot2DPanel();
    plot1.addLegend("second plot");
    plot1.addLinePlot("second plot", x,y);


  //  sl_contentPane.putConstraint(SpringLayout.EAST, plot, 600, SpringLayout.WEST, contentPane);
   // sl_contentPane.putConstraint(SpringLayout.NORTH, plot, 15, SpringLayout.NORTH, contentPane);
   // sl_contentPane.putConstraint(SpringLayout.WEST, plot, 15, SpringLayout.WEST, contentPane);
   // sl_contentPane.putConstraint(SpringLayout.SOUTH, plot, 600, SpringLayout.NORTH, contentPane);

/*  panel.setLayout(new GridBagLayout());
    panel.add(plot);
    plot.validate();
    plot.repaint();
    plot.setBounds(50,50,100,100);*/

    contentPane.add(plot);
    contentPane1.add(plot1);
    JPanel panel = new JPanel();
    panel.setBackground(Color.BLACK);
    
    //sl_contentPane.putConstraint(SpringLayout.NORTH, plot1, 20, SpringLayout.NORTH, contentPane);
    //sl_contentPane.putConstraint(SpringLayout.WEST, plot1, 16, SpringLayout.EAST, plot);
    //sl_contentPane.putConstraint(SpringLayout.SOUTH, plot1, 408, SpringLayout.NORTH, contentPane);
    //sl_contentPane.putConstraint(SpringLayout.EAST, plot1, 251, SpringLayout.EAST, plot);
   


}
}