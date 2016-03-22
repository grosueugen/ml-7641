package com.gt.ml.best;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gt.ml.ClassifierContext;

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
		log.info("########### start computeStump");
		for (int i : boostIteration) {
			log.info("########### start computeStump with boostIteration {} ", i);
			AdaBoostM1 boost = new AdaBoostM1();
			boost.setNumIterations(i);
			ClassifierContext cc = new ClassifierContext(file, boost);
			cc.run();
			Double errorRate = cc.getErrorRate();
			BestResult res = new BestResult("boost:classifier=stump,iterations=" + i, errorRate);
			add(res);
			log.info("########### end computeStump with result {} ", res);
		}
		log.info("########### end computeStump");
	}

	private void computeDecisionTreeNoPruning() {
		log.info("########### start computeDecisionTreeNoPruning");
		for (int i : boostIteration) {
			log.info("########### start computeDecisionTreeNoPruning with boostIteration {} ", i);
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
			log.info("########### end computeDecisionTreeNoPruning with result {} ", res);
		}
		log.info("########### end computeDecisionTreeNoPruning");
	}

	private void computeDecisionTreePruning() {
		log.info("########### start computeDecisionTreePruning");
		for (int i : boostIteration) {
			for (float cf : dtCfs) {
				log.info("########### start computeDecisionTreePruning with boostIteration {} and cf {} ", boostIteration, cf);
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
				log.info("########### end computeDecisionTreePruning with result {} ", res);
			}
		}
		log.info("########### end computeDecisionTreePruning");
	}

	public void printResult() {
		log.info("================== Boosting Best: {} =====================", file);
		log.info("{}",getResult());
		log.info("================== Boosting Best: {} =====================", file);
	}
	
	public static void main(String[] args) {
		BoostingBest bestSat = new BoostingBest("sat.arff", 2);
		bestSat.compute();
		bestSat.printResult();
		
		/*BoostingBest bestWine = new BoostingBest("wine-white.arff", 1);
		bestWine.compute();
		bestWine.printResult();*/
	}

}
