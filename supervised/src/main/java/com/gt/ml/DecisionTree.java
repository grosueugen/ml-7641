package com.gt.ml;

import java.util.Enumeration;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;

public class DecisionTree extends BaseClassifier {
	
	public DecisionTree() {
		J48 dt = new J48();
		dt.setUnpruned(false);
		classifier = dt;
	}
	
	@Override
	public void printMoreOptions() {
		J48 dt = (J48) (classifier);
		System.out.println(dt.measureTreeSize());
		System.out.println(dt.measureNumLeaves());
	}
	
	@Override
	public J48 getClassifier() {
		return (J48) classifier;
	}
	
}
