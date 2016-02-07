package com.gt.ml;

import java.util.List;
import java.util.Map;

import com.gt.ml.graph.TimeChartBuilder;
import com.gt.ml.graph.ChartFrame;
import com.gt.ml.main.ClassifierTypes;
import com.gt.ml.main.time.TimeData;
import com.gt.ml.main.time.TimeResult;

import weka.core.Instances;

public class RunningTimeGraph {

	public static void main(String[] args) throws Exception {
		/*if (args.length != 1) {
			System.out.println("Please provide file name");
			System.exit(0);
		}
		Instances dataSet = Utils.getInstances(args[0]);*/
		boolean wine = true;
		Instances dataSet = null;
		if (wine) {
			dataSet = Utils.getInstances("wine-white.arff");
		} else {
			dataSet = Utils.getInstances("sat.arff");
		}
		
		TimeResult res = BaseClassifier.computeRunningTime(10, 300, dataSet);
		
		Map<ClassifierTypes, List<TimeData>> trainData = res.getTrainData();
		System.out.println("trainData: " + trainData);
		TimeChartBuilder chart1 = new TimeChartBuilder().withTitle("Training time comparisons (ms)")
				.withXY("Instance Size", "Train Time (ms)").withData(trainData);
		new ChartFrame("Training time comparison (ms)", chart1.build());
		
		Map<ClassifierTypes, List<TimeData>> testData = res.getTestData();
		System.out.println("testData: " + testData);
		TimeChartBuilder chart2 = new TimeChartBuilder().withTitle("Test time comparisons (ms)")
				.withXY("Instance Size", "Test Time (ms)").withData(testData);
		new ChartFrame("Test time comparison (ms)", chart2.build());
	}
	
	
	
}
