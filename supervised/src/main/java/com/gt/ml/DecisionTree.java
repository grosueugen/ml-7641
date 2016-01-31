package com.gt.ml;

import weka.classifiers.trees.J48;

public class DecisionTree extends BaseClassifier {
	
	public DecisionTree() {
		J48 dt = new J48();
		dt.setUnpruned(false);
		classifier = dt;
	}
	
}
