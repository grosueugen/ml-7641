package com.gt.ml;

import weka.classifiers.meta.AdaBoostM1;

public class BoostingST extends BaseClassifier {
	
	public BoostingST() {
		AdaBoostM1 boost = new AdaBoostM1();
		classifier = boost;
	}

}
