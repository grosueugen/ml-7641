package com.gt.ml.main.best;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gt.ml.main.ClassifierContext;

import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.trees.J48;

public class BoostingBest extends AbstractBest {
	
	private static final Logger log = LoggerFactory.getLogger(BoostingBest.class);
	
	private int[] boostIteration = {5, 10, 15};
	private final float[] dtCfs = {0.5f, 0.25f, 0.1f, 0.01f, 0.001f};
	
	public BoostingBest(String file, int nrIterations) {
		super(file, nrIterations);
	}
	
	public BoostingBest(String file) {
		this(file, 1);
	}
	
	@Override
	protected void doCompute() {
		computeStump();
		computeDecisionTreeNoPruning();
		computeDecisionTreePruning();
	}
	
	private void computeStump() {
		for (int i : boostIteration) {
			AdaBoostM1 boost = new AdaBoostM1();
			boost.setNumIterations(i);
			ClassifierContext cc = new ClassifierContext(file, boost);
			cc.run();
			Double errorRate = cc.getErrorRate();
			BestResult res = new BestResult("boost:classifier=stump,iterations=" + i, errorRate);
			add(res);
		}
	}

	private void computeDecisionTreeNoPruning() {
		for (int i : boostIteration) {
			AdaBoostM1 boost = new AdaBoostM1();
			J48 dt = new J48();
			dt.setUnpruned(true);
			boost.setClassifier(dt);
			boost.setNumIterations(i);
			ClassifierContext cc = new ClassifierContext(file, boost);
			cc.run();
			Double errorRate = cc.getErrorRate();
			BestResult res = new BestResult("boost:classifier=dt,iterations=" + i, errorRate);
			add(res);
		}
	}

	private void computeDecisionTreePruning() {
		for (int i : boostIteration) {
			for (float cf : dtCfs) {
				AdaBoostM1 boost = new AdaBoostM1();
				J48 dt = new J48();
				dt.setConfidenceFactor(cf);
				boost.setClassifier(dt);
				boost.setNumIterations(i);
				ClassifierContext cc = new ClassifierContext(file, boost);
				cc.run();
				Double errorRate = cc.getErrorRate();
				BestResult res = new BestResult("boost:classifier=dt,iterations=" + i + ",cf=" + cf, errorRate);
				add(res);
			}
		}
	}

	public void printResult() {
		log.info("================== Boosting Best: {} =====================", file);
		log.info("{}",getResult());
		log.info("================== Boosting Best: {} =====================", file);
	}
	
	public static void main(String[] args) {
		BoostingBest bestSat = new BoostingBest("sat.arff", 1);
		bestSat.compute();
		bestSat.printResult();
		
		BoostingBest bestWine = new BoostingBest("wine-white.arff", 1);
		bestWine.compute();
		bestWine.printResult();
	}

}
