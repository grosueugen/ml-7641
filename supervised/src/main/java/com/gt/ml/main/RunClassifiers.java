package com.gt.ml.main;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.gt.ml.ClassifierTypes;

import weka.classifiers.Classifier;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.trees.J48;

public class RunClassifiers {
	
	public static final String help = "h";
	public static final String file = "f";
	public static final String trainPercentage = "trainp";
	public static final String crossValidation = "cv";
	public static final String kFolds = "kfolds";
	public static final String classifier = "c";
	public static final String dtPruning = "dt_pruning";
	public static final String dtCf = "dt_cf";
	public static final String boostC = "boost_c";
	public static final String boostDtPruning = "boost_dt_pruning";
	public static final String boostDtCf = "boost_dt_cf";
	
	public static void main(String[] args) throws Exception {
		ExecutionContext context = createContext(args);
		if (context == null) {
			return;
		}
		context.compute();
	}

	private static ExecutionContext createContext(String[] args) throws ParseException {
		Options options = new Options()
				.addOption(new Option(help, "help", false, "show help"))
				.addOption(new Option(file, "file", true, "file path containing the data set"))
				.addOption(new Option(trainPercentage, "train-percentage", true, "training set in percentage, rest is test set; default value training set 70%, test set 30%"))
				.addOption(new Option(crossValidation, "cross-validation", true, "use cross-validation or not"))
				.addOption(new Option(kFolds, "kfolds", true, "#folds used in cross-validation"))
				.addOption(new Option(classifier, "classifier", true, "classifier to use <dt, knn, boost, nn, svm> : Decision Tree(dt), Nearest Neighbours(knn), Boosting(boost), Neural Networks(nn), Support Vector Machine(svm)"))
				.addOption(new Option(dtPruning, "pruning", true, "dt: use pruning <false|true(default)>"))
				.addOption(new Option(dtCf, "confidence-factor", true, "dt: set confidence-factor, used for pruning, default 0.25"))
				.addOption(new Option(boostC, "boost-classifier", true, "boost: set the classifier <stump|dt(default)>"))
				.addOption(new Option(boostDtPruning, "boost-dt-pruning", true, "boost: use pruning for dt <false|true(default)>"))
				.addOption(new Option(boostDtCf, "boost-dt-cf", true, "boost: use dt confidence-factor, used for pruning, default 0.25"))
				;
		
		String f = null;
		Double trainP = 0.7;
		Boolean cv = false;
		Integer kfolds = 1;
		ClassifierTypes classifierType = null;
		Classifier cls = null;
		
		CommandLine commandLine = new DefaultParser().parse(options, args);
		if (commandLine.hasOption(help)) {
			new HelpFormatter().printHelp("Run Classifiers", options);
			return null;
		}
		
		if (commandLine.hasOption(file)) {
			f = commandLine.getOptionValue(file);
		} else {
			System.out.println("Please provide data set file. See help for more details");
			return null;
		}
		
		if (commandLine.hasOption(trainPercentage)) {
			String trainp = commandLine.getOptionValue(trainPercentage);
			trainP = getDouble(trainp);
			if (trainP == null || trainP >= 1 || trainP <= 0) {
				System.out.println("Please provide an double between (0,1) range for training set pct. See help for more details");
				return null;
			}
		} 
		
		if (commandLine.hasOption(crossValidation)) {
			cv = Boolean.valueOf(commandLine.getOptionValue(crossValidation));
			if (cv) {
				if (commandLine.hasOption(kFolds)) {
					kfolds = getInt(commandLine.getOptionValue(kFolds));
					if (kfolds == null) {
						System.out.println("Please provide an integer for kfolds e.g. 10");
						return null;
					}
				}
			}
		} else {
			cv = false;
		}
		
		if (commandLine.hasOption(classifier)) {
			String c = commandLine.getOptionValue(classifier);
			classifierType = ClassifierTypes.toEnum(c);
			switch (classifierType) {
			case DECISION_TREE:
				J48 dt = new J48();
				if (commandLine.hasOption(dtPruning)) {
					Boolean pruning = Boolean.valueOf(commandLine.getOptionValue(dtPruning));
					dt.setUnpruned(!pruning);
				}
				if (commandLine.hasOption(dtCf)) {
					String cfStr = commandLine.getOptionValue(dtCf);
					Float cf = getFloat(cfStr);
					if (cf == null) {
						System.out.println("Please provide a floating point number for dt_cf e.g. 0.25");
					}
					dt.setConfidenceFactor(cf);
				}
				cls = dt;
				break;
			case BOOSTING:
				AdaBoostM1 boost = new AdaBoostM1();
				if (commandLine.hasOption(boostC)) {
					String boostCls = commandLine.getOptionValue(boostC);
					if ("stump".equalsIgnoreCase(boostCls)) {
						//nothing to set/default value
					} else if ("dt".equalsIgnoreCase(boostCls)) {
						J48 boostDt = new J48();
						if (commandLine.hasOption(boostDtPruning)) {
							Boolean boostPruning = Boolean.valueOf(commandLine.getOptionValue(boostDtPruning));
							boostDt.setUnpruned(!boostPruning);
						}
						if (commandLine.hasOption(boostDtCf)) {
							String cfStr = commandLine.getOptionValue(boostDtCf);
							Float cf = getFloat(cfStr);
							if (cf == null) {
								System.out.println("Please provide a floating point number for boost_dt_cf e.g. 0.25");
							}
							boostDt.setConfidenceFactor(cf);
						}
						boost.setClassifier(boostDt);
					} else {
						System.out.println("boost_c can use stump or dt as values. For more details, see help");
					}					
				}
				cls = boost;
				break;
			case KNN: 
				break;
			case NN:
				break;
			case SVM:
				break;
			}
		} else {
			System.out.println("Please provide classifier. See help for more details");
			return null;
		}
		
		return new ExecutionContext(f, trainP, cls, cv, kfolds);
	}

	private static Integer getInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}
	
	private static Float getFloat(String str) {
		try {
			return Float.parseFloat(str);
		} catch (Exception ex) {			
			System.out.println(ex.getMessage());
			return null;
		}
	}
	
	private static Double getDouble(String str) {
		try {
			return Double.parseDouble(str);
		} catch (Exception ex) {			
			System.out.println(ex.getMessage());
			return null;
		}
	}
	
}
