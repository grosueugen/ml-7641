package com.gt.ml;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class ClassifierBuilder {
	
	public J48 runDecissionTree(Instances trainingSet, boolean pruning) throws Exception {
		J48 dt = new J48();
		dt.setUnpruned(!pruning);
		dt.buildClassifier(trainingSet);
		return dt;
	}
	
	public AdaBoostM1 runBoosting(Instances trainingSet, Classifier classifier) throws Exception {
		AdaBoostM1 boost = new AdaBoostM1();
		boost.setClassifier(classifier);
		boost.buildClassifier(trainingSet);
		return boost;
	}
	
	public Classifier runNeuralNet(Instances trainingSet) throws Exception {
		MultilayerPerceptron nn = new MultilayerPerceptron();
		nn.buildClassifier(trainingSet);
		return nn;
	}
	
	public Classifier runSvm(Instances trainingSet, Kernel kernel) throws Exception {
		SMO svm = new SMO();
		svm.setKernel(kernel);
		svm.buildClassifier(trainingSet);
		return svm;
	}
	
	public Evaluation evaluate(Classifier classifier, Instances testSet) throws Exception {
		Evaluation eval = new Evaluation(testSet);
		eval.evaluateModel(classifier, testSet);
		return eval;
	}

}
