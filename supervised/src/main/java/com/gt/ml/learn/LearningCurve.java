package com.gt.ml.learn;

import static com.gt.ml.Utils.buildInstances;
import static com.gt.ml.Utils.newBestInstance;

import com.gt.ml.ClassifierTypes;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

public class LearningCurve {
	
	private final String file;
	private final int n;
	private final int step;
	
	public LearningCurve(String file, int n, int step) {
		super();
		this.file = file;
		this.n = n;
		this.step = step;
	}
	
	public LearnResult compute() {
		try {
			Instances instances = buildInstances(file);
			Instances testSet = getTestSet(instances);
			LearnResult res = new LearnResult();
			int nrTimes = 0;
			int current = step;
			
			while (current < instances.numInstances() && nrTimes < n) {
				Instances trainingSet = new Instances(instances, 0, current);			
				int size = trainingSet.numInstances();
				for (ClassifierTypes ct : ClassifierTypes.values()) {
					Classifier c = newBestInstance(ct);
					c.buildClassifier(trainingSet);
					double trainingError = getError(c, trainingSet, trainingSet);
					double testError = getError(c, trainingSet, testSet);
					res.addData(ct, size, trainingError, testError);
				}
				current += step;
				nrTimes++;
			}
			return res;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Error during computing the learning curve", ex);
		}
	}
	
	private Instances getTestSet(Instances instances) {
		int size = instances.numInstances();
		int trainingSize = (int) (size * 0.7);
		int testSize = size - trainingSize;
		return new Instances(instances, trainingSize, testSize);
	}

	private double getError(Classifier c, Instances trainingSet, Instances testSet) {
		try {
			c.buildClassifier(trainingSet);
			Evaluation eval = new Evaluation(trainingSet);
			eval.evaluateModel(c, testSet);
			return eval.pctIncorrect();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Can not build/evaulate classifier for getting error" + c, ex);
		}
	}
	
	private long test(Classifier c, Instances testSet) {
		try {
			long start = System.currentTimeMillis();
			Evaluation evaluation = new Evaluation(testSet);
			evaluation.evaluateModel(c, testSet);
			return (System.currentTimeMillis()) - start;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Can not test classifier " + c, ex);
		}
	}

}
