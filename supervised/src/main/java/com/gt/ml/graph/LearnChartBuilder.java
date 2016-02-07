package com.gt.ml.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.gt.ml.main.ClassifierTypes;
import com.gt.ml.main.learn.LearnData;

public class LearnChartBuilder {
	
	private String chartTitle;
	private Map<ClassifierTypes, List<LearnData>> data;
	private String xTitle;
	private String yTitle;
	
	public LearnChartBuilder withTitle(String value) {
		this.chartTitle = value;
		return this;
	}
	
	public LearnChartBuilder withData(Map<ClassifierTypes, List<LearnData>> value) {
		this.data = value;
		return this;
	}
	
	public LearnChartBuilder withXY(String xTitle, String yTitle) {
		this.xTitle = xTitle;
		this.yTitle = yTitle;
		return this;
	}
	
	public JFreeChart build() {
		XYSeriesCollection allXY = new XYSeriesCollection();
		for (ClassifierTypes ct : data.keySet()) {
			List<LearnData> learnData = data.get(ct);
			XYSeries xyTrainingError = new XYSeries(ct.name() + " training error ");
			XYSeries xyTestError = new XYSeries(ct.name() + " test error ");
			for (LearnData ld : learnData) {
				xyTrainingError.add(ld.getSize(), ld.getTrainingError());
				xyTestError.add(ld.getSize(), ld.getTestError());
			}
			allXY.addSeries(xyTrainingError);
			allXY.addSeries(xyTestError);
		}
		JFreeChart res = ChartFactory.createXYLineChart(chartTitle, xTitle, yTitle, allXY);
		final XYPlot plot = res.getXYPlot();
		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setBaseStroke(new BasicStroke(4));
		for (int i = 0; i < plot.getSeriesCount(); i++) {
			renderer.setSeriesShapesVisible(i, false);
			renderer.setSeriesPaint(i, getColors().get(i / 2));
			if (i % 2 == 0) {
				renderer.setSeriesStroke(i, new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f,
						new float[] { 6.0f, 6.0f }, 0.0f));
			}
		}
		plot.setRenderer(renderer);
		return res;
	}
	
	private List<Color> getColors() {
		List<Color> res = new ArrayList<>();
		res.add(Color.BLUE);
		res.add(Color.RED);
		res.add(Color.GREEN);
		res.add(Color.BLACK);
		res.add(Color.MAGENTA);
		return res;
	}

}
