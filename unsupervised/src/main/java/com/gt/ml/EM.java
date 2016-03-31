package com.gt.ml;

import weka.clusterers.ClusterEvaluation;
import weka.core.Attribute;
import weka.core.EuclideanDistance;
import weka.core.Instances;

/**
 * Command line options: <inputFile> <numClusters> <outputFile>
 * Run the EM clustering on the inputFile
 * Evaluate the cluster: {@link ClusterEvaluator} and {@link ClusterEvaluation}
 * Add the cluster assignment to the data set (<numClusters> binary attributes) and save it as outputFile - optional, if <outputFile> is given
 * The new file can be used to run ANN on it
 * 
 * @author cosmin
 *
 */
public class EM {

	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			System.out.println("Usage EM <inputFile> <numClusters> [<outputFile>]");
			System.exit(-1);
		}
		String inputFile = args[0];
		int numClusters = Integer.parseInt(args[1]);
		String outputFile = args.length == 3 ? args[2] : null;

		weka.clusterers.EM em = new weka.clusterers.EM();
		Instances dataSet = MLAssignmentUtils.buildInstancesFromResource(inputFile);
		Instances dataSetNoClass = MLAssignmentUtils.removeAttributes(dataSet, String.valueOf(dataSet.numAttributes()));

		em.setNumClusters(numClusters);


		em.buildClusterer(dataSetNoClass);
		EuclideanDistance distanceFunction = new EuclideanDistance(dataSetNoClass);
		distanceFunction.setDontNormalize(true);
		ClusterEvaluator.evaluate(em, distanceFunction, dataSetNoClass);

		ClusterEvaluation evaluation = new ClusterEvaluation();
		evaluation.setClusterer(em);
		evaluation.evaluateClusterer(dataSet);
		System.out.println(evaluation.clusterResultsToString());
		

		if (outputFile != null) {
			saveWithClusterAssignment(dataSet, dataSetNoClass, em, outputFile);
		}
	}

	private static void saveWithClusterAssignment(Instances dataSet, Instances dataSetNoClass, weka.clusterers.EM em, String outputFile) throws Exception {
		System.out.println("Saving " + outputFile);
		int numClusters = em.getNumClusters();
		Attribute[] clusterAttributes = new Attribute[numClusters];
		for (int i = 0; i < numClusters; i++) {
			Attribute attr = new Attribute("Cluster" + i);
			dataSet.insertAttributeAt(attr, dataSet.numAttributes() - 1);
			clusterAttributes[i] = attr;
		}
		int[] assignments = new int[dataSet.numInstances()];
		for (int i = 0; i < dataSet.numInstances(); i++) {
			assignments[i] = em.clusterInstance(dataSetNoClass.get(i));
		}
		
		for (int i = 0; i < assignments.length; i++) {
			int cluster = assignments[i];
			for (int j = 0; j < clusterAttributes.length; j++) {
				if (cluster == j) {
					dataSet.get(i).setValue(dataSet.numAttributes() - numClusters -1 + j, 1);
				} else {
					dataSet.get(i).setValue(dataSet.numAttributes() - numClusters -1 + j, 0);
					
				}
			}
		}
		MLAssignmentUtils.write(outputFile, dataSet);

	}
}