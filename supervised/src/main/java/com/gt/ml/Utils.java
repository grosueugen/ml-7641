package com.gt.ml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class Utils {
	
	private static final Logger log = LoggerFactory.getLogger(Utils.class);
	
	public static Integer getInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}
	
	public static Float getFloat(String str) {
		try {
			return Float.parseFloat(str);
		} catch (Exception ex) {			
			System.out.println(ex.getMessage());
			return null;
		}
	}
	
	public static Double getDouble(String str) {
		try {
			return Double.parseDouble(str);
		} catch (Exception ex) {			
			System.out.println(ex.getMessage());
			return null;
		}
	}
	
	public static Instances buildInstances(String file) {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
			Reader r = new BufferedReader(new InputStreamReader(is));
			Instances dataSet = new Instances(r);
			dataSet.setClassIndex(dataSet.numAttributes() - 1);
			dataSet.randomize(new Random());
			return dataSet;
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("Can not build instances", ex);
		}
	}
	
	public static Classifier newBestInstance(String file, ClassifierTypes ct) {
		if ("sat.arff".equals(file)) {
			switch (ct) {
			case DECISION_TREE:
				J48 dt = new J48();
				return dt;
			case BOOSTING:
				AdaBoostM1 boost = new AdaBoostM1();
				return boost;
			case KNN:
				IBk knn = new IBk();
				return knn;
			case NN:
				MultilayerPerceptron nn = new MultilayerPerceptron();
				return nn;
			case SVM:
				SMO svm = new SMO();
				return svm;
			default:
				throw new IllegalArgumentException("Classifier type " + ct + " not supported");
			}

		} else if ("wine-white.arff".equals(file)) {
			switch (ct) {
			case DECISION_TREE:
				J48 dt = new J48();
				return dt;
			case BOOSTING:
				AdaBoostM1 boost = new AdaBoostM1();
				return boost;
			case KNN:
				IBk knn = new IBk();
				return knn;
			case NN:
				MultilayerPerceptron nn = new MultilayerPerceptron();
				return nn;
			case SVM:
				SMO svm = new SMO();
				return svm;
			default:
				throw new IllegalArgumentException("Classifier type " + ct + " not supported");
			}
		} else {
			log.warn("Using a file that was not tested for model complexity {}, using default parameters!", file);
			switch (ct) {
			case DECISION_TREE:
				J48 dt = new J48();
				return dt;
			case BOOSTING:
				AdaBoostM1 boost = new AdaBoostM1();
				return boost;
			case KNN:
				IBk knn = new IBk();
				return knn;
			case NN:
				MultilayerPerceptron nn = new MultilayerPerceptron();
				return nn;
			case SVM:
				SMO svm = new SMO();
				return svm;
			default:
				throw new IllegalArgumentException("Classifier type " + ct + " not supported");
			}
		}
	}

}
