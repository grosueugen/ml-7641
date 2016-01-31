package com.gt.ml;

import java.util.Enumeration;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Attribute;
import weka.core.Instances;

public class RunAssignment {
	
	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println("Please provide file name");
			System.exit(0);
		}
		Instances dataSet = Utils.getInstances(args[0]);
		int size = dataSet.numInstances();
		int trainingSize = (int) (size * 0.8);
		int testSize = size - trainingSize;
		
		Instances trainSet = new Instances(dataSet, 0, trainingSize);
		Instances testSet = new Instances(dataSet, trainingSize, testSize);
		
		long start = System.currentTimeMillis();
		
		System.out.println("------------ DECISION TREE -------------");
		DecisionTree dt = new DecisionTree();
		dt.train(trainSet);
		dt.test(testSet);
		dt.printResults();
		
		ClassifierBuilder classifierBuilder = new ClassifierBuilder();
		Classifier classifier = classifierBuilder.runDecissionTree(trainSet, true);
		Evaluation eval = classifierBuilder.evaluate(classifier, testSet);
		printStats(eval, trainSet, testSet, classifier);
		
		/*
		ClassifierBuilder classifierBuilder = new ClassifierBuilder();
		Classifier classifier = classifierBuilder.runDecissionTree(trainSet, true);
		Evaluation eval = classifierBuilder.evaluate(classifier, testSet);
		printStats(eval, trainSet, testSet, classifier);
		
		System.out.println("--------------BOOSTING WITH STUMP -------");
		
		classifierBuilder = new ClassifierBuilder();
		classifier = classifierBuilder.runBoosting(trainSet, new DecisionStump());
		eval = classifierBuilder.evaluate(classifier, testSet);
		printStats(eval, trainSet, testSet, classifier);
		
		System.out.println("--------------BOOSTING WITH J48 -------");
		
		classifierBuilder = new ClassifierBuilder();
		classifier = classifierBuilder.runBoosting(trainSet, new J48());
		eval = classifierBuilder.evaluate(classifier, testSet);
		printStats(eval, trainSet, testSet, classifier);
		
		System.out.println("-------------- NEURAL NET --------------");
		
		classifierBuilder = new ClassifierBuilder();
		classifier = classifierBuilder.runNeuralNet(trainSet);
		eval = classifierBuilder.evaluate(classifier, testSet);
		printStats(eval, trainSet, testSet, classifier);
		
		System.out.println("-------------- SVM ---------------------");
		
		classifierBuilder = new ClassifierBuilder();
		PolyKernel kernel = new PolyKernel();
		kernel.setExponent(2);
		classifier = classifierBuilder.runSvm(trainSet, kernel);
		eval = classifierBuilder.evaluate(classifier, testSet);
		printStats(eval, trainSet, testSet, classifier);*/
		
		System.out.println("Entire alg took " + (System.currentTimeMillis() - start)/1000 + " sec");
	}

	

	@SuppressWarnings("unchecked")
	private static void printStats(Evaluation eval, Instances trainingSet, Instances testSet, Classifier classifier)
			throws Exception {
		System.out.println(eval.toMatrixString());
		System.out.println("accuracy: " + eval.pctCorrect());
		Attribute output = trainingSet.attribute(36);
		Enumeration<String> values = output.enumerateValues();
		int index = 0;
		while (values.hasMoreElements()) {
			String value = values.nextElement();
			System.out.println(
					value + " -> " + eval.precision(index) + "; " + eval.recall(index) + "; " + eval.fMeasure(index));
			index++;
		}
	}

}