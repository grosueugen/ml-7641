package com.gt.ml.clustering;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.EM;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;

public class RunClustering {
	
	public static void main(String[] args) throws Exception {
		//runKMeansSat();
		//runKMeansWine();
		//runEMSat();
		//runEMWine();
		runEMWine2();
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
	
	private static void runEMSat() throws Exception {
		EM em = new EM();
		em.setNumClusters(6);
		
		String res = ClusterEvaluation.evaluateClusterer(em, 
				new String[]{
						"-t", "src/main/resources/sat-train.arff", 
						"-c", "last",
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
	
	private static void runEMWine() throws Exception {
		EM em = new EM();
		em.setNumClusters(7);
		em.setDebug(true);
		
		String res = ClusterEvaluation.evaluateClusterer(em, 
				new String[]{
						"-N", "7",
						"-t", "src/main/resources/wine.arff", 
						"-c", "last"
						});
		System.out.println(res);
	}
	
	private static void runEMWine2() throws Exception {
		EM em = new EM();
		em.setNumClusters(7);
		em.setDebug(true);
		Instances training = get("wine-train.arff");
		//training.setClassIndex(-1);
		em.buildClusterer(training);
		
		ClusterEvaluation eval = new ClusterEvaluation();
		eval.setClusterer(em);
		Instances test = get("wine-test.arff");
		test.setClassIndex(11);
		eval.evaluateClusterer(test);
		System.out.println(eval.clusterResultsToString());
	}
	
	private static Instances get(String file) throws IOException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
		Reader r = new BufferedReader(new InputStreamReader(is));
		return new Instances(r);
	}
	
}
