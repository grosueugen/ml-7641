package com.gt.ml;

import java.util.Arrays;
import java.util.Enumeration;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Attribute;
import weka.core.Instances;

public abstract class BaseClassifier {
	
	protected Classifier classifier;
	protected Evaluation evaluation;
	protected Attribute output;
	
	public long train(Instances trainSet) throws Exception {
		long start = System.currentTimeMillis();
		classifier.buildClassifier(trainSet);
		return (System.currentTimeMillis()) - start;
	}
	
	public long test(Instances testSet) throws Exception {
		long start = System.currentTimeMillis();
		evaluation = new Evaluation(testSet);
		evaluation.evaluateModel(classifier, testSet);
		output = testSet.attribute(testSet.numAttributes()-1);
		return (System.currentTimeMillis()) - start;
	}
	
	public Classifier getClassifier() {
		return classifier;
	}
	
	public Evaluation getEvaluation() {
		return evaluation;
	}
	
	@SuppressWarnings("unchecked")
	public void printResults() throws Exception {
		//System.out.println(classifier + " with options: " + Arrays.toString(classifier.getOptions()));
		System.out.println(classifier);
		System.out.println(evaluation.toMatrixString());
		System.out.println("accuracy: " + evaluation.pctCorrect());
		
		Enumeration<String> values = output.enumerateValues();
		int index = 0;
		while (values.hasMoreElements()) {
			String value = values.nextElement();
			System.out.println(
					value + " -> " + evaluation.precision(index) + "; " + evaluation.recall(index) 
						+ "; " + evaluation.fMeasure(index));
			index++;
		}
	}
	
	public static TimeResult runMultipleTimes(int n, int step, Instances instances) throws Exception {
		TimeResult result = new TimeResult();
		int nrTimes = 0;
		int current = step;
		while (current < instances.numInstances() && nrTimes < n) {
			Instances newSet = new Instances(instances, 0, current);			
			int size = newSet.numInstances();
			for (ClassifierTypes ct : ClassifierTypes.values()) {
				BaseClassifier c = newInstance(ct);
				long trainTime = c.train(newSet);
				result.addTrainData(ct, size, trainTime);
				long testTime = c.test(newSet);
				result.addTestData(ct, size, testTime);
			}
			current += step;
			nrTimes++;
		}
		return result;
	}

	public static BaseClassifier newInstance(ClassifierTypes ct) {
		switch (ct) {
		case DECISION_TREE: return new DecisionTree();
		case BOOSTING: return new Boosting();
		case KNN: return new KNN();
		case NN: return new NN();
		case SVM: return new SVM();
		default: throw new IllegalArgumentException(ct + " not implemented!");
		}
	}

}
