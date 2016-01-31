package com.gt.ml;

import weka.classifiers.functions.SMO;

public class SVM extends BaseClassifier {
	
	public SVM() {
		SMO svm = new SMO();
		classifier = svm;
	}

}
