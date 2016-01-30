package com.gt.ml;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class ClassifierBuilder {
	
	public J48 runDecissionTree(Instances trainingSet, boolean pruning) throws Exception {
		J48 dt = new J48();
		dt.setUnpruned(!pruning);
		dt.buildClassifier(trainingSet);
		return dt;
	}
	
	public Evaluation evaluate(Classifier classifier, Instances testSet) throws Exception {
		Evaluation eval = new Evaluation(testSet);
		eval.evaluateModel(classifier, testSet);
		return eval;
	}

}
