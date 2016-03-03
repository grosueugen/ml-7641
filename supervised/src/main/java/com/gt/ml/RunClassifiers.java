package com.gt.ml;

import java.util.ArrayList;
import java.util.List;

import weka.core.Instances;

public class RunClassifiers {
	
	public static void main(String[] args) throws Exception {
		/*if (args.length != 1) {
			System.out.println("Please provide file name");
			System.exit(0);
		}
		Instances dataSet = Utils.getInstances(args[0]);
		int size = dataSet.numInstances();
		int trainingSize = (int) (size * 0.8);
		int testSize = size - trainingSize;
		
		Instances trainSet = new Instances(dataSet, 0, trainingSize);
		Instances testSet = new Instances(dataSet, trainingSize, testSize);*/
		
		/*Instances trainSet = Utils.getInstances(args[0]);
		Instances testSet = Utils.getInstances(args[1]);*/
		
		String trainFile = "sat-train.arff";
		String testFile = "sat-test.arff";
		System.out.println(trainFile + " - " + testFile);
		Instances trainSet = Utils.getInstances(trainFile);
		Instances testSet = Utils.getInstances(testFile);
		
		long start = System.currentTimeMillis();
		
		List<BaseClassifier> classifiers = new ArrayList<>();
		classifiers.add(new DecisionTree());
		/*classifiers.add(new BoostingDT());
		classifiers.add(new BoostingST());
		classifiers.add(new KNN());
		classifiers.add(new NN());
		classifiers.add(new SVM());*/
		
		for (BaseClassifier c : classifiers) {
			System.out.println("--------------" + c.getClass().getSimpleName());
			c.train(trainSet);
			c.test(testSet);
			c.printResults();
		}

		System.out.println("Done in " + (System.currentTimeMillis() - start)/1000 + " sec");
	}

}