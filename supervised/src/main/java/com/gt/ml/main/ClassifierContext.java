package com.gt.ml.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

public class ClassifierContext {
	
	private static final Logger log = LoggerFactory.getLogger(ClassifierContext.class);
	
	private final String file;
	private final double trainingPercentage;
	private final Classifier classifier;
	private final boolean crossValidation;
	private final int kFolds;
	
	private Instances dataSet;
	private Instances trainingSet;
	private Instances testSet;
	
	private Double errorRate;
	private Long crossValidationTime;
	private Long trainingTime;
	private Long testTime;
	
	public ClassifierContext(String file, double trainingPercentage, Classifier classifier, boolean crossValidation,
			int kFolds) {
		this.file = file;
		this.trainingPercentage = trainingPercentage;
		this.classifier = classifier;
		this.crossValidation = crossValidation;
		this.kFolds = kFolds;
		init();
	}
	
	public ClassifierContext(String file, Classifier classifier) {
		this(file, 0.7, classifier, false, 5);
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

	public void run() {
		try {
			if (crossValidation) {
				doCrossValidation();
			} else {
				doTraining();
				doTesting();
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private void doTraining() throws Exception {
		long start = System.currentTimeMillis();
		classifier.buildClassifier(trainingSet);
		long time = (System.currentTimeMillis() - start);
		trainingTime = time;
		log.info("classifier {} training took  {} millis <=> {} seconds", classifier.getClass().getSimpleName(), 
				time, time / 1000);
	}
	
	public Evaluation doTesting() throws Exception {
		long start = System.currentTimeMillis();
		Evaluation eval = new Evaluation(testSet);
		eval.evaluateModel(classifier, testSet);
		long time = (System.currentTimeMillis() - start);
		log.info("classifier {} testing took {} millis <=> {} seconds", classifier.getClass().getSimpleName(),  
				time, time/1000);
		double pctIncorrect = eval.pctIncorrect();
		errorRate = pctIncorrect;
		testTime = time;
		log.info("test matrix: {}", eval.toMatrixString());
		log.info("test error: {}", pctIncorrect);
		//log.info("{}", classifier);
		log.info("Evaluation Summary {}", eval.toSummaryString());
		return eval;
	}
	
	private Evaluation doCrossValidation() throws Exception {
		long start = System.currentTimeMillis();
		log.info("started cross validation using {} folds at {}", kFolds, new Date());
		Evaluation eval = new Evaluation(dataSet);
		eval.crossValidateModel(classifier, dataSet, kFolds, new Random());
		long time = (System.currentTimeMillis() - start);
		log.info("classifier {} cross validation took {} millis <=> {} seconds", classifier.getClass().getSimpleName(),
				time, time/1000);
		crossValidationTime = time;
		double pctIncorrect = eval.pctIncorrect();
		errorRate = pctIncorrect; 
		log.info("cross validation matrix: {}", eval.toMatrixString());
		log.info("cross validation error: {}", pctIncorrect);
		log.info("Evaluation Summary {}", eval.toSummaryString());
		return eval;
	}

	private void buildDataSet() throws IOException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
		Reader r = new BufferedReader(new InputStreamReader(is));
		dataSet = new Instances(r);
		dataSet.setClassIndex(dataSet.numAttributes() - 1);
		dataSet.randomize(new Random());
	}
	
	public Instances getDataSet() {
		return dataSet;
	}
	
	public Instances getTrainingSet() {
		return trainingSet;
	}
	
	public Instances getTestSet() {
		return testSet;
	}
	
	public Double getErrorRate() {
		return errorRate;
	}
	
	public Long getTrainingTime() {
		return trainingTime;
	}
	
	public Long getTestTime() {
		return testTime;
	}
	
	public Long getCrossValidationTime() {
		return crossValidationTime;
	}
	
	public Classifier getClassifier() {
		return classifier;
	}
	
}
