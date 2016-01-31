package com.gt.ml.graph;

import java.util.List;
import java.util.Map;

import com.gt.ml.BaseClassifier;
import com.gt.ml.ClassifierTypes;
import com.gt.ml.TimeData;
import com.gt.ml.TimeResult;
import com.gt.ml.Utils;

import weka.core.Instances;

public class RunningTimeGraph {

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println("Please provide file name");
			System.exit(0);
		}
		Instances dataSet = Utils.getInstances(args[0]);
		TimeResult res = BaseClassifier.runMultipleTimes(50, 50, dataSet);
		
		Map<ClassifierTypes, List<TimeData>> trainData = res.getTrainData();
		System.out.println("trainData: " + trainData);
		ChartBuilder chart1 = new ChartBuilder().withTitle("Training time comparisons (ms)")
				.withXY("Instance Size", "Train Time (ms)").withData(trainData);
		new ChartFrame("Training time comparison (ms)", chart1.build());
		
		Map<ClassifierTypes, List<TimeData>> testData = res.getTestData();
		System.out.println("testData: " + testData);
		ChartBuilder chart2 = new ChartBuilder().withTitle("Test time comparisons (ms)")
				.withXY("Instance Size", "Test Time (ms)").withData(testData);
		new ChartFrame("Test time comparison (ms)", chart2.build());
	}
	
	
	
}
