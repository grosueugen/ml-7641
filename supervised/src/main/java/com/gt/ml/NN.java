package com.gt.ml;

import weka.classifiers.functions.MultilayerPerceptron;

public class NN extends BaseClassifier {
	
	public NN() {
		classifier = new MultilayerPerceptron();
	}

}
