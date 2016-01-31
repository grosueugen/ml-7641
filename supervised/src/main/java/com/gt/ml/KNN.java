package com.gt.ml;

import weka.classifiers.lazy.IBk;
import weka.core.SelectedTag;

public class KNN extends BaseClassifier {
	
	public KNN() {
		IBk knn = new IBk();
		knn.setKNN(5);
		knn.setDistanceWeighting(new SelectedTag(IBk.WEIGHT_INVERSE, IBk.TAGS_WEIGHTING));
		classifier = knn;
	}

}
