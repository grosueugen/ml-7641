package com.gt.ml.graph;

import java.awt.Dimension;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

@SuppressWarnings("serial")
public class ChartFrame extends ApplicationFrame {
	
	public ChartFrame(String title, JFreeChart chart) {
		super(title);
		initUI(chart);
	}

	private void initUI(JFreeChart chart) {
		ChartPanel panel = new ChartPanel(chart);
		panel.setSize(new Dimension(800, 400));
		setContentPane(panel);
		pack();
		RefineryUtilities.centerFrameOnScreen(this);
		setVisible(true);		
	}
	
}
