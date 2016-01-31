package com.gt.ml;

import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.trees.J48;

public class BoostingDT extends BaseClassifier {
	
	public BoostingDT() {
		AdaBoostM1 boost = new AdaBoostM1();
		boost.setClassifier(new J48());
		classifier = boost;
	}

}
