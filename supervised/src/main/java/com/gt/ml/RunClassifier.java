package com.gt.ml;

import static com.gt.ml.Utils.*;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.trees.J48;
import weka.core.SelectedTag;

public class RunClassifier {
	
	public static final String help = "h";
	public static final String file = "f";
	public static final String trainPercentage = "train_pct";
	public static final String crossValidation = "cv";
	public static final String kFolds = "kfolds";
	public static final String classifier = "c";
	
	public static final String dtPruning = "dt_pruning";
	public static final String dtCf = "dt_cf";
	
	public static final String boostC = "boost_c";
	public static final String boostNrIterations = "boost_nr_it";
	public static final String boostDtPruning = "boost_dt_pruning";
	public static final String boostDtCf = "boost_dt_cf";
	
	public static final String knnK = "knn_k";
	public static final String knnWeightDistance = "knn_wd";
	
	public static final String nnLearningRate = "nn_lr";
	public static final String nnMomentum = "nn_mm";
	public static final String nnHiddentUnits = "nn_hu";
	
	public static final String svmKernelFunction = "svm_kernel";
	public static final String svmPolyExp = "svm_poly_exp";
	public static final String svmRadialGamma = "svm_radial_gamma";
	
	public static void main(String[] args) throws Exception {
		ClassifierContext context = createContext(args);
		if (context == null) {
			return;
		}
		context.run();
	}

	private static ClassifierContext createContext(String[] args) throws ParseException {
		Options options = new Options()
				.addOption(new Option(help, "help", false, "show help"))
				.addOption(new Option(file, "file", true, "file path containing the data set"))
				.addOption(new Option(trainPercentage, "train-percentage", true, "training set in percentage, rest is test set; type double; default value training set: 0.7, test set 0.3"))
				.addOption(new Option(crossValidation, "cross-validation", true, "use cross-validation or not;type boolean; default value false"))
				.addOption(new Option(kFolds, "kfolds", true, "#folds used in cross-validation; type integer, default 5"))
				.addOption(new Option(classifier, "classifier", true, "classifier to use <dt, knn, boost, nn, svm> : Decision Tree(dt), Nearest Neighbours(knn), Boosting(boost), Neural Networks(nn), Support Vector Machine(svm)"))
				.addOption(new Option(dtPruning, "pruning", true, "dt: use pruning; type boolean, default value true>"))
				.addOption(new Option(dtCf, "confidence_factor", true, "dt: set confidence-factor, used for pruning; type double, default value 0.25"))
				.addOption(new Option(boostC, "boost_classifier", true, "boost: set the classifier learner; one of <stump|dt>, default stump"))
				.addOption(new Option(boostNrIterations, "boost_nr_iterations", true, "boost: set the nr of bagging iterations; type integer, default value 10>"))
				.addOption(new Option(boostDtPruning, "boost_dt_pruning", true, "boost: use pruning for dt; type boolean, default value true>"))
				.addOption(new Option(boostDtCf, "boost_dt_cf", true, "boost: use dt confidence-factor, used for pruning; type double, default value 0.25"))
				.addOption(new Option(knnK, "knn_k", true, "knn: specify the #nearest neighbours; type integer, default value 1"))
				.addOption(new Option(knnWeightDistance, "knn_weight_distance", true, "knn: specify a weight distance; type integer <1=None,2=Inverse,3=Similarity>, default value 1"))
				.addOption(new Option(nnLearningRate, "nn_learning_rate", true, "nn: backpropagation learning rate; type double, default value 0.3"))
				.addOption(new Option(nnMomentum, "nn_momentum", true, "nn: backpropagation momentum rate; type double, default value 0.2"))
				.addOption(new Option(nnHiddentUnits, "nn_hidden_units", true, "nn: comma-separated string for #hidden layers and nodes per layer. e.g. \"a,3,4\"; see weka for more details"))
				.addOption(new Option(svmKernelFunction, "svm_kernel_function", true, "svm: one of: <poly,radial>, default value poly"))
				.addOption(new Option(svmPolyExp, "svm_poly_exponent", true, "svm: the exponent value of a polynomial kernel; type double, default value 1.0"))
				.addOption(new Option(svmRadialGamma, "svm_radial_gamma", true, "svm: the gamma parameter value of the radial kernel; type double, default value 0.01"))
				;
		
		String f = null;
		Double trainP = 0.7;
		Boolean cv = false;
		Integer kfolds = 5;
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
						System.out.println("Please provide an integer for kfolds e.g. 10. See help for more details");
						return null;
					}
				}
			}
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
						System.out.println("Please provide a floating point number for dt_cf e.g. 0.25. See help for more details");
						return null;
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
						//nothing to, default value
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
								System.out.println("Please provide a floating point number for boost_dt_cf e.g. 0.25. See help for more details");
								return null;
							}
							boostDt.setConfidenceFactor(cf);
						}
						boost.setClassifier(boostDt);
					} else {
						System.out.println("boost_c can use one of <stump, dt> as values. See help for more details");
						return null;
					}
				}
				if (commandLine.hasOption(boostNrIterations)) {
					Integer nrIt = getInt(commandLine.getOptionValue(boostNrIterations));
					if (nrIt == null) {
						System.out.println("Please provide an integer for boost_nr_it. See help for more details");
						return null;
					}
					boost.setNumIterations(nrIt);
				}
				cls = boost;
				break;
			case KNN: 
				IBk ibk = new IBk();
				if (commandLine.hasOption(knnK)) {
					Integer k = getInt(commandLine.getOptionValue(knnK));
					if (k == null) {
						System.out.println("Please provide an integer for knn_n. For more details see help");
						return null;
					}
					ibk.setKNN(k);
				}
				if (commandLine.hasOption(knnWeightDistance)) {
					Integer knnwd = getInt(commandLine.getOptionValue(knnWeightDistance));
					if (knnwd == null) {
						System.out.println("Please provide one of 1,2,4 for knn_w_d. For more details see help");
						return null;
					}
					if (1 != knnwd || 2 != knnwd || 4 != knnwd) {
						System.out.println("Please provide one of 1,2,4 for knn_w_d. See help for more details");
						return null;
					}
					ibk.setDistanceWeighting(new SelectedTag(knnwd, IBk.TAGS_WEIGHTING));
				}
				cls = ibk;
				break;
			case NN:
				MultilayerPerceptron nn = new MultilayerPerceptron();
				if (commandLine.hasOption(nnLearningRate)) {
					Double nnLR = getDouble(commandLine.getOptionValue(nnLearningRate));
					if (nnLR == null) {
						System.out.println("Please provide a double for NN learning rate. See help for more details");
						return null;
					}
					nn.setLearningRate(nnLR);
				}
				if (commandLine.hasOption(nnMomentum)) {
					Double nnMR = getDouble(commandLine.getOptionValue(nnMomentum));
					if (nnMR == null) {
						System.out.println("Please provide a double for NN momentum rate. See help for more details");
						return null;
					}
					nn.setMomentum(nnMR);
				}
				if (commandLine.hasOption(nnHiddentUnits)) {
					String nnHU = commandLine.getOptionValue(nnHiddentUnits);
					nn.setHiddenLayers(nnHU);
				}
				
				cls = nn;
				break;
			case SVM:
				SMO svm = new SMO();
				if (commandLine.hasOption(svmKernelFunction)) {
					String svmkf = commandLine.getOptionValue(svmKernelFunction);
					Kernel kernel = null;
					if ("poly".equalsIgnoreCase(svmkf)) {
						PolyKernel pk = new PolyKernel();
						if (commandLine.hasOption(svmPolyExp)) {
							Double expValue = getDouble(commandLine.getOptionValue(svmPolyExp));
							if (expValue == null) {
								System.out.println("Please provide a double value for svm_poly_exp. See help for more details");
								return null;
							}
							pk.setExponent(expValue);
						}
						kernel = pk;
					} else if ("radial".equalsIgnoreCase(svmkf)) {
						RBFKernel rbfk = new RBFKernel();
						if (commandLine.hasOption(svmRadialGamma)) {
							Double gamma = getDouble(commandLine.getOptionValue(svmRadialGamma));
							if (gamma == null) {
								System.out.println("Please provide a double value for svm_radial_gamma. See help for more details");
								return null;
							}
							rbfk.setGamma(gamma);
						}
						kernel = rbfk;
					} else {
						System.out.println("Please provide one of <poly, radial> for svm_kernel_fct. See help for more details");
						return null;
					}
					svm.setKernel(kernel);
				} else {					
					if (commandLine.hasOption(svmPolyExp)) {
						PolyKernel polyKernel = new PolyKernel();
						Double expValue = getDouble(commandLine.getOptionValue(svmPolyExp));
						if (expValue == null) {
							System.out.println("Please provide a double value for svm_poly_exp. For more details, see help");
							return null;
						}
						polyKernel.setExponent(expValue);
						svm.setKernel(polyKernel);
					}
				}
				cls = svm;
				break;
			}			
		} else {
			System.out.println("Please provide a classifier. See help for more details");
			return null;
		}
		
		return new ClassifierContext(f, trainP, cls, cv, kfolds);
	}

}
