package com.gt.ml.graph;

import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.gt.ml.main.ClassifierTypes;
import com.gt.ml.main.time.TimeData;

public class TimeChartBuilder {
	
	private String chartTitle;
	private Map<ClassifierTypes, List<TimeData>> data;
	private String xTitle;
	private String yTitle;
	
	public TimeChartBuilder withTitle(String value) {
		this.chartTitle = value;
		return this;
	}
	
	public TimeChartBuilder withData(Map<ClassifierTypes, List<TimeData>> value) {
		this.data = value;
		return this;
	}
	
	public TimeChartBuilder withXY(String xTitle, String yTitle) {
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
		return res;
	}
	
}
