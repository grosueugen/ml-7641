package com.gt.ml.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.gt.ml.ClassifierTypes;
import com.gt.ml.TimeData;

public class ChartBuilder {
	
	private String chartTitle;
	private Map<ClassifierTypes, List<TimeData>> data;
	private String xTitle;
	private String yTitle;
	
	public ChartBuilder withTitle(String value) {
		this.chartTitle = value;
		return this;
	}
	
	public ChartBuilder withData(Map<ClassifierTypes, List<TimeData>> value) {
		this.data = value;
		return this;
	}
	
	public ChartBuilder withXY(String xTitle, String yTitle) {
		this.xTitle = xTitle;
		this.yTitle = yTitle;
		return this;
	}
	
	public JFreeChart build() {
		XYSeriesCollection xyCol = new XYSeriesCollection();
		for (ClassifierTypes ct : data.keySet()) {
			List<TimeData> timeData = data.get(ct);
			XYSeries xy = new XYSeries(ct.name());
			for (TimeData td : timeData) {
				xy.add(td.getSize(), td.getTime());
			}
			xyCol.addSeries(xy);
		}
		JFreeChart res = ChartFactory.createXYLineChart(chartTitle, xTitle, yTitle, xyCol);
		customize(res);
		return res;
	}
	
	private void customize(JFreeChart chart) {
		chart.setBackgroundPaint(Color.white);

		//final StandardLegend legend = (StandardLegend) chart.getLegend();
		//legend.setDisplaySeriesShapes(true);

		// get a reference to the plot for further customisation...
		final XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setBaseStroke(new BasicStroke(4));
		plot.setRenderer(renderer);

		// change the auto tick unit selection to integer units only...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// OPTIONAL CUSTOMISATION COMPLETED.
	}

}
