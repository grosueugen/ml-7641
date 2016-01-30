package com.gt.ml;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Enumeration;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.Instances;

public class RunAssignment {
	
	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println("Please provide file name");
			System.exit(0);
		}
		String file = args[0];
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
		Reader r = new BufferedReader(new InputStreamReader(is));
		Instances dataSet = new Instances(r);
		dataSet.setClassIndex(dataSet.numAttributes() - 1);
		int size = dataSet.numInstances();
		int trainingSize = (int) (size * 0.8);
		int testSize = size - trainingSize;
		
		Instances trainingSet = new Instances(dataSet, 0, trainingSize);
		Instances testSet = new Instances(dataSet, trainingSize, testSize);
		
		System.out.println("------------ DECISION TREE -------------");
		ClassifierBuilder classifierBuilder = new ClassifierBuilder();
		Classifier classifier = classifierBuilder.runDecissionTree(trainingSet, true);
		Evaluation eval = classifierBuilder.evaluate(classifier, testSet);
		printStats(eval, trainingSet, testSet, classifier);
		
		System.out.println("--------------BOOSTING WITH STUMP -------");
		
		classifierBuilder = new ClassifierBuilder();
		classifier = classifierBuilder.runBoosting(trainingSet, new DecisionStump());
		eval = classifierBuilder.evaluate(classifier, testSet);
		printStats(eval, trainingSet, testSet, classifier);
		
		System.out.println("--------------BOOSTING WITH J48 -------");
		
		classifierBuilder = new ClassifierBuilder();
		classifier = classifierBuilder.runBoosting(trainingSet, new J48());
		eval = classifierBuilder.evaluate(classifier, testSet);
		printStats(eval, trainingSet, testSet, classifier);
		
		System.out.println("-------------- NEURAL NET --------------");
		
		classifierBuilder = new ClassifierBuilder();
		classifier = classifierBuilder.runNeuralNet(trainingSet);
		eval = classifierBuilder.evaluate(classifier, testSet);
		printStats(eval, trainingSet, testSet, classifier);
		
		System.out.println("-------------- SVM ---------------------");
		
		classifierBuilder = new ClassifierBuilder();
		PolyKernel kernel = new PolyKernel();
		kernel.setExponent(2);
		classifier = classifierBuilder.runSvm(trainingSet, kernel);
		eval = classifierBuilder.evaluate(classifier, testSet);
		printStats(eval, trainingSet, testSet, classifier);
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
