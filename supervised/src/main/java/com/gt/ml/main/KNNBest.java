package com.gt.ml.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.classifiers.lazy.IBk;
import weka.core.SelectedTag;

public class KNNBest extends AbstractBest {
	
	private static final Logger log = LoggerFactory.getLogger(KNNBest.class);
	
	private int[] ks = {1, 3, 5, 7, 10, 15, 20};
	
	private int[] weightDistaince = {1, 2, 4};
	
	public KNNBest(String file, int nrIterations) {
		super(file, nrIterations);
	}
	
	public KNNBest(String file) {
		this(file, 1);
	}

	@Override
	protected void doCompute() {
		for (int k : ks) {
			for (int wd : weightDistaince) {
				IBk knn = new IBk(k);
				knn.setDistanceWeighting(new SelectedTag(wd, IBk.TAGS_WEIGHTING));
				ClassifierContext cc = new ClassifierContext(file, knn);
				cc.run();
				Double errorRate = cc.getErrorRate();
				
				BestResult res = new BestResult("knn:k=" + k+ ",weightDistance=" + wd, errorRate);
				add(res);
			}
		}
	}
	
	public void printResult() {
		log.info("================== KNN Best: {} =====================", file);
		log.info("{}",getResult());
		log.info("================== KNN Best: {} =====================", file);
	}
	
	public static void main(String[] args) {
		KNNBest bestSat = new KNNBest("sat.arff", 1);
		bestSat.compute();
		bestSat.printResult();
		
		KNNBest bestWine = new KNNBest("wine-white.arff", 1);
		bestWine.compute();
		bestWine.printResult();
	}

}
