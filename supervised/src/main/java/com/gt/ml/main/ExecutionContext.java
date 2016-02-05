package com.gt.ml.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Duration;
import java.util.Date;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

public class ExecutionContext {
	
	private static final Logger log = LoggerFactory.getLogger(ExecutionContext.class);
	
	private final String file;
	private final double trainingPercentage;
	private final Classifier classifier;
	private final boolean crossValidation;
	private final int kFolds;
	
	private Instances dataSet;
	private Instances trainingSet;
	private Instances testSet;
	
	public ExecutionContext(String file, double trainingPercentage, Classifier classifier, boolean crossValidation,
			int kFolds) {
		this.file = file;
		this.trainingPercentage = trainingPercentage;
		this.classifier = classifier;
		this.crossValidation = crossValidation;
		this.kFolds = kFolds;
		init();
	}

	private void init() {
		try {
			buildDataSet();		
			int size = dataSet.numInstances();
			int trainingSize = (int) (size * trainingPercentage);
			int testSize = size - trainingSize;
			
			trainingSet = new Instances(dataSet, 0, trainingSize);
			testSet = new Instances(dataSet, trainingSize, testSize);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void compute() {
		try {
			if (crossValidation) {
				doCrossValidation();
			} else {
				doTraining();
			}
			doTesting();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private void doTraining() throws Exception {
		long start = System.currentTimeMillis();
		classifier.buildClassifier(trainingSet);
		long time = (System.currentTimeMillis() - start);
		log.info("classifier {} training took  {}", classifier.getClass().getSimpleName(),  Duration.ofMillis(time));
	}
	
	public Evaluation doTesting() throws Exception {
		long start = System.currentTimeMillis();
		Evaluation eval = new Evaluation(testSet);
		eval.evaluateModel(classifier, testSet);
		long time = (System.currentTimeMillis() - start);
		log.info("classifier {} testing took  {}", classifier.getClass().getSimpleName(),  Duration.ofMillis(time));
		double pctIncorrect = eval.pctIncorrect();
		log.info("test matrix: {}", eval.toMatrixString());
		log.info("test error: {}", pctIncorrect);
		return eval;
	}
	
	private Evaluation doCrossValidation() throws Exception {
		long start = System.currentTimeMillis();
		log.info("started cross validation {}", new Date());
		Evaluation eval = new Evaluation(trainingSet);
		eval.crossValidateModel(classifier, trainingSet, kFolds, new Random());
		long time = (System.currentTimeMillis() - start);
		log.info("classifier {} cross validation took  {}", classifier.getClass().getSimpleName(),  Duration.ofMillis(time));
		double pctIncorrect = eval.pctIncorrect();
		log.info("cross validation matrix: {}", eval.toMatrixString());
		log.info("cross validation error: {}", pctIncorrect);
		return eval;
	}

	private void buildDataSet() throws IOException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
		Reader r = new BufferedReader(new InputStreamReader(is));
		dataSet = new Instances(r);
		dataSet.setClassIndex(dataSet.numAttributes() - 1);
		dataSet.randomize(new Random());
	}
	
}
