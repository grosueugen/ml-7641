package com.gt.ml.best;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gt.ml.ClassifierContext;

import weka.classifiers.trees.J48;

public class DecisionTreeBest extends AbstractBest {
	
	private static final Logger log = LoggerFactory.getLogger(DecisionTreeBest.class);

	private final float[] cfs = {0.5f, 0.25f, 0.1f, 0.01f, 0.001f};
	
	public DecisionTreeBest(String file, int nrIterations) {
		super(file, nrIterations);
	}
	
	public DecisionTreeBest(String file) {
		this(file, 1);
	}
	
	@Override
	protected void doCompute() {		
		computeNoPruning();
		computePruning();
	}

	private void computeNoPruning() {
		J48 dt = new J48();
		dt.setUnpruned(true);
		ClassifierContext cc = new ClassifierContext(file, dt);
		cc.run();
		Double errorRate = cc.getErrorRate();
		
		BestResult res = new BestResult("dt:pruning=false", errorRate);
		add(res);
	}

	private void computePruning() {
		for (float cf : cfs) {
			J48 dt = new J48();
			dt.setConfidenceFactor(cf);
			ClassifierContext cc = new ClassifierContext(file, dt);
			cc.run();
			Double errorRate = cc.getErrorRate();
			add(new BestResult("dt:pruning=true,cf=" + cf, errorRate));
		}
	}

	public void printResult() {
		log.info("================== DecisionTree Best: {} =====================", file);
		log.info("{}",getResult());
		log.info("================== DecisionTree Best: {} =====================", file);
	}
	
	public static void main(String[] args) {
		DecisionTreeBest bestSat = new DecisionTreeBest("sat.arff", 1);
		bestSat.compute();
		bestSat.printResult();
		
		DecisionTreeBest bestWine = new DecisionTreeBest("wine-white.arff", 1);
		bestWine.compute();
		bestWine.printResult();
	}
	
}
