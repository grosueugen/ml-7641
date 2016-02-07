package com.gt.ml.time;

import static com.gt.ml.Utils.buildInstances;
import static com.gt.ml.Utils.newBestInstance;

import com.gt.ml.ClassifierTypes;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

public class RunningTime {
	
	private final String file;
	private final int n;
	private final int step;
	
	public RunningTime(String file, int n, int step) {
		super();
		this.file = file;
		this.n = n;
		this.step = step;
	}
	
	public TimeResult compute() {
		Instances instances = buildInstances(file);
		TimeResult result = new TimeResult();
		int nrTimes = 0;
		int current = step;
		while (current < instances.numInstances() && nrTimes < n) {
			Instances newSet = new Instances(instances, 0, current);			
			int size = newSet.numInstances();
			for (ClassifierTypes ct : ClassifierTypes.values()) {
				Classifier c = newBestInstance(file, ct);
				long trainingTime = train(c, newSet);
				long testTime = test(c, newSet);
				result.addData(ct, size, trainingTime, testTime);
			}
			current += step;
			nrTimes++;
		}
		return result;
	}
	
	private long train(Classifier c, Instances trainSet) {
		try {
			long start = System.currentTimeMillis();
			c.buildClassifier(trainSet);
			return (System.currentTimeMillis()) - start;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Can not build classifier " + c, ex);
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
