package com.gt.ml.clustering;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;

public class RunClustering {
	
	public static void main(String[] args) throws Exception {
		//runKMeansSat();
		runKMeansWine();
	}

	// params: file: sat, wine, distance
	private static void runKMeansSat() throws Exception {
		SimpleKMeans km = new SimpleKMeans();
		km.setNumClusters(6);
		
		String res = ClusterEvaluation.evaluateClusterer(km, 
				new String[]{
						"-t", "src/main/resources/sat.arff", 
						"-c", "last",
						"-A", "weka.core.ManhattanDistance -R first-last",
						});
		System.out.println(res);
	}
	
	private static void runKMeansWine() throws Exception {
		SimpleKMeans km = new SimpleKMeans();
		km.setNumClusters(7);
		
		String res = ClusterEvaluation.evaluateClusterer(km, 
				new String[]{
						"-t", "src/main/resources/wine.arff", 
						"-c", "last",
						"-A", "weka.core.EuclideanDistance -R first-last",
						});
		System.out.println(res);
	}
	
}
