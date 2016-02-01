package com.gt.ml;

import java.util.ArrayList;
import java.util.List;

import weka.classifiers.trees.J48;
import weka.core.Instances;

public class RunWineAnalysis {

	public static void main(String[] args) throws Exception {
		Instances dataSet = Utils.getInstances("wine-white.arff");
		int size = dataSet.numInstances();
		int trainingSize = (int) (size * 0.7);
		int testSize = size - trainingSize;

		Instances trainSet = new Instances(dataSet, 0, trainingSize);
		Instances testSet = new Instances(dataSet, trainingSize, testSize);

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
