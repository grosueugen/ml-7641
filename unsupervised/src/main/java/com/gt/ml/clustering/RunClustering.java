package com.gt.ml.clustering;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.EM;
import weka.clusterers.SimpleKMeans;
import weka.core.ChebyshevDistance;
import weka.core.DistanceFunction;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class RunClustering {
	
	public static void main(String[] args) throws Exception {
		runKMeansSat();
		//runKMeansWine();
		//runEMSat();
		//runEMWine();
	}

	// params: file: sat, wine, distance
	private static void runKMeansSat() throws Exception {
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
	
	private static void runEMSat() throws Exception {
		EM em = new EM();
		em.setNumClusters(6);
		em.setDebug(true);
		Instances instances = get("sat.arff");
		instances.setClassIndex(36);
		Instances instancesNoClass = removeAttributes(instances, String.valueOf(instances.numAttributes()));
		em.buildClusterer(instancesNoClass);
		
		ClusterEvaluation eval = new ClusterEvaluation();
		eval.setClusterer(em);
		eval.evaluateClusterer(instances);
		System.out.println(eval.clusterResultsToString());
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
		Instances instances = get("wine.arff");
		instances.setClassIndex(11);
		Instances instancesNoClass = removeAttributes(instances, String.valueOf(instances.numAttributes()));
		em.buildClusterer(instancesNoClass);
		
		ClusterEvaluation eval = new ClusterEvaluation();
		eval.setClusterer(em);
		eval.evaluateClusterer(instances);
		System.out.println(eval.clusterResultsToString());
	}
	
	private static Instances get(String file) throws IOException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
		Reader r = new BufferedReader(new InputStreamReader(is));
		return new Instances(r);
	}
	
	private static Instances removeAttributes(Instances dataSet, String attributesToRemove) {
		try {
			Remove filter = new Remove();
			filter.setInvertSelection(false);
			filter.setAttributeIndices(attributesToRemove);
			filter.setInputFormat(dataSet);
			return Filter.useFilter(dataSet, filter);
		} catch (Exception e) {
			throw new RuntimeException("Error removing attributes " + attributesToRemove, e);
		}
	}
	
}
