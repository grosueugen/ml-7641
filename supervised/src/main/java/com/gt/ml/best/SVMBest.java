package com.gt.ml.best;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gt.ml.ClassifierContext;

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
		log.info("########### start computePoly ");
		for (int exp : exponents) {
			log.info("########### start computePoly with exponent {} ", exp);
			SMO svm = new SMO();
			PolyKernel pk = new PolyKernel();
			pk.setExponent(exp);
			svm.setKernel(pk);
			
			ClassifierContext cc = new ClassifierContext(file, svm);
			cc.run();
			Double errorRate = cc.getErrorRate();
			BestResult res = new BestResult("svm:kernel=poly,exp=" + exp, errorRate);
			add(res);
			log.info("########### end computePoly with result ", res);
		}
		log.info("########### end computePoly ");
	}

	private void computeRadial() {
		log.info("########### start computeRadial ");
		for (double g : gamma) {
			log.info("########### start computeRadial with gamma {} ", gamma);
			SMO svm = new SMO();
			RBFKernel k = new RBFKernel();
			k.setGamma(g);
			svm.setKernel(k);
			
			ClassifierContext cc = new ClassifierContext(file, svm);
			cc.run();
			Double errorRate = cc.getErrorRate();
			BestResult res = new BestResult("svm:kernel=radial,gamma=" + g, errorRate);
			add(res);
			log.info("########### end computeRadial with result ", res);
		}
		log.info("########### end computeRadial ");
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
