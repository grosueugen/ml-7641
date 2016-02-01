package com.gt.ml;

import java.util.ArrayList;
import java.util.List;

import weka.core.Instances;

public class RunSatAnalysis {
	
	public static void main(String[] args) throws Exception {
		String trainFile = "sat-train.arff";
		String testFile = "sat-test.arff";
		System.out.println(trainFile + " - " + testFile);
		
		Instances trainSet = Utils.getInstances(trainFile);
		Instances testSet = Utils.getInstances(testFile);

		long start = System.currentTimeMillis();

		List<BaseClassifier> classifiers = new ArrayList<>();
		DecisionTree prunnedDt = new DecisionTree();
		classifiers.add(prunnedDt);
		DecisionTree unprunnedDt = new DecisionTree();
		unprunnedDt.getClassifier().setUnpruned(true);
		classifiers.add(unprunnedDt);

		// low confidence level makes a more drastic prunning -> in this case,
		// better results
		DecisionTree confidenceLevelDt = new DecisionTree();
		confidenceLevelDt.getClassifier().setConfidenceFactor(0.01f);

		classifiers.add(confidenceLevelDt);

		/*
		 * classifiers.add(new BoostingDT()); classifiers.add(new BoostingST());
		 * classifiers.add(new KNN()); classifiers.add(new NN());
		 * classifiers.add(new SVM());
		 */

		for (BaseClassifier c : classifiers) {
			System.out.println("--------------" + c.getClass().getSimpleName());
			c.train(trainSet);
			c.test(testSet);
			c.printResults();
		}

		System.out.println("Done in " + (System.currentTimeMillis() - start) / 1000 + " sec");
	}

}
