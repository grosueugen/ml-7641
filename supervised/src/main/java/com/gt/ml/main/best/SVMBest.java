package com.gt.ml.main.best;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gt.ml.main.ClassifierContext;

import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.functions.supportVector.RBFKernel;

public class SVMBest extends AbstractBest {
	
	private static final Logger log = LoggerFactory.getLogger(SVMBest.class);
	
	private int[] exponents = {1, 2, 3, 5, 10};
	private double[] gamma = {0.01, 0.03, 1, 2, 5};
	
	public SVMBest(String file, int nrIterations) {
		super(file, nrIterations);
	}
	
	public SVMBest(String file) {
		this(file, 1);
	}
	
	@Override
	protected void doCompute() {
		computePoly();
		computeRadial();
	}
	
	private void computePoly() {
		for (int exp : exponents) {
			SMO svm = new SMO();
			PolyKernel pk = new PolyKernel();
			pk.setExponent(exp);
			svm.setKernel(pk);
			
			ClassifierContext cc = new ClassifierContext(file, svm);
			cc.run();
			Double errorRate = cc.getErrorRate();
			add(new BestResult("svm:kernel=poly,exp=" + exp, errorRate));
		}
	}

	private void computeRadial() {
		for (double g : gamma) {
			SMO svm = new SMO();
			RBFKernel k = new RBFKernel();
			k.setGamma(g);
			svm.setKernel(k);
			
			ClassifierContext cc = new ClassifierContext(file, svm);
			cc.run();
			Double errorRate = cc.getErrorRate();
			add(new BestResult("svm:kernel=radial,gamma=" + g, errorRate));
		}
	}

	public void printResult() {
		log.info("================== SVM Best: {} =====================", file);
		log.info("{}",getResult());
		log.info("================== SVM Best: {} =====================", file);
	}

	public static void main(String[] args) {
		SVMBest bestSat = new SVMBest("sat.arff", 1);
		bestSat.compute();
		bestSat.printResult();

		SVMBest bestWine = new SVMBest("wine-white.arff", 1);
		bestWine.compute();
		bestWine.printResult();
	}

}
