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
		TimeResult res = BaseClassifier.runMultipleTimes(1, 500, dataSet);
		
		Map<ClassifierTypes, List<TimeData>> trainData = res.getTrainData();
		System.out.println("trainData: " + trainData);
		ChartBuilder chart1 = new ChartBuilder().withTitle("Training time comparisons")
				.withXY("Instance Size", "Train Time").withData(trainData);
		new ChartFrame("Training time comparison", chart1.build());
		
		Map<ClassifierTypes, List<TimeData>> testData = res.getTestData();
		System.out.println("testData: " + testData);
		ChartBuilder chart2 = new ChartBuilder().withTitle("Test time comparisons")
				.withXY("Instance Size", "Train Time").withData(testData);
		new ChartFrame("Test time comparison", chart2.build());
	}
	
	
	
}
