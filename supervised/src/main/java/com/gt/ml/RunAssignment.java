package com.gt.ml;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Enumeration;

import weka.core.*;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class RunAssignment {
	
	@SuppressWarnings("unchecked")
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
		
		ClassifierBuilder classifierBuilder = new ClassifierBuilder();
		J48 dt = classifierBuilder.runDecissionTree(trainingSet, true);
		Evaluation eval = classifierBuilder.evaluate(dt, testSet);
		System.out.println(eval.toMatrixString());
		System.out.println("accuracy: " + eval.pctCorrect());
		Attribute output = trainingSet.attribute(36);
		Enumeration<String> values = output.enumerateValues();
		int index = 0;
		while (values.hasMoreElements()) {
			String value = values.nextElement();
			System.out.println(value + " -> " + eval.precision(index) + "; " + eval.recall(index));
			index++;
		}
	}

}
