package com.gt.ml.clustering;

import static com.gt.ml.clustering.Utils.get;
import static com.gt.ml.clustering.Utils.removeAttributes;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.EM;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;

public class RunClustering {
	
	public static void main(String[] args) throws Exception {
		//runKMeansSat();
		runEMSat();
		//runKMeansWine();
		//runEMWine();
	}

	// params: file: sat, wine, distance
	public static void runKMeansSat() throws Exception {
		SimpleKMeans km = new SimpleKMeans();
		km.setNumClusters(6);
		
		String res = ClusterEvaluation.evaluateClusterer(km, 
				new String[]{
						"-t", "src/main/resources/sat.arff", 
						"-c", "last",
						"-A", "weka.core.EuclideanDistance -R first-last",
						});
		System.out.println(res);
	}
	
	public static void runEMSat() throws Exception {
		EM em = new EM();
		em.setNumClusters(6);
		em.setDebug(true);
		Instances instances = get("sat.arff", 36);
		Instances instancesNoClass = removeAttributes(instances, String.valueOf(instances.numAttributes()));
		em.buildClusterer(instancesNoClass);
		
		ClusterEvaluation eval = new ClusterEvaluation();
		eval.setClusterer(em);
		eval.evaluateClusterer(instances);
		System.out.println(eval.clusterResultsToString());
	}
	
	public static void runKMeansWine() throws Exception {
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
	
	public static void runEMWine() throws Exception {
		EM em = new EM();
		em.setNumClusters(7);
		em.setDebug(true);
		Instances instances = get("wine.arff", 11);
		Instances instancesNoClass = removeAttributes(instances, String.valueOf(instances.numAttributes()));
		em.buildClusterer(instancesNoClass);
		
		ClusterEvaluation eval = new ClusterEvaluation();
		eval.setClusterer(em);
		eval.evaluateClusterer(instances);
		System.out.println(eval.clusterResultsToString());
	}
	
}
