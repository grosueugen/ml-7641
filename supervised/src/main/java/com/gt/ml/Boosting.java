package com.gt.ml;

import weka.classifiers.meta.AdaBoostM1;

public class Boosting extends BaseClassifier {
	
	public Boosting() {
		classifier = new AdaBoostM1();
	}

}
