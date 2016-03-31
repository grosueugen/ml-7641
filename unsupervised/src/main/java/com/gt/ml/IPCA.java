package com.gt.ml;

import java.util.Arrays;
import java.util.List;

import shared.DataSet;
import shared.EuclideanDistance;
import shared.Instance;
import shared.filt.InsignificantComponentAnalysis;
import util.linalg.Matrix;
import util.linalg.Vector;

public class IPCA {
	
	public static void main(String[] args) {
		if(args.length != 6) {
			System.out.println("Usage: IPCA <inputFile> <numInstances> <numAttributes> <numEigenVectorsToKeep> <dataFileOuput> <commaSeparatedListOfClassLabels>");
			System.exit(-1);
		}
		String inputFile = args[0];
		int numInstances = Integer.valueOf(args[1]);
		int numAttributes = Integer.valueOf(args[2]);
		int numEigenVectorsToKeep = Integer.valueOf(args[3]);
		String outputFile = args[4];
		List<String> labels = Arrays.asList(args[5].split(","));
		Instance[] instances = MLAssignmentUtils.initializeInstances(numInstances, inputFile, numAttributes, labels);
		DataSet original = new DataSet(instances);
		
		InsignificantComponentAnalysis ipca = new InsignificantComponentAnalysis(original, numEigenVectorsToKeep);
		DataSet transformed = run(ipca,original);
		saveAsArff(transformed, outputFile, labels);
		DataSet reconstructed = reconstruct(ipca, transformed);
		computeReconstructionError(original,reconstructed);
	}

	private static DataSet run(InsignificantComponentAnalysis ipca, DataSet original) {
        System.out.println("Eigenvalues");
        System.out.println(ipca.getEigenValues());
        System.out.println("===============================================");
        System.out.println("The projection matrix");
        System.out.println(ipca.getProjection());
        DataSet transformed = original.copy(); 
        ipca.filter(transformed);
        return transformed;
	}
	
	private static DataSet reconstruct(InsignificantComponentAnalysis ipca, DataSet transformed) {
		DataSet reconstructed = transformed.copy(); 
		Matrix reverse = ipca.getProjection().transpose();
	        for (int i = 0; i < reconstructed.size(); i++) {
	            Instance instance = reconstructed.get(i);
	            instance.setData(reverse.times(instance.getData()).plus(ipca.getMean()));
	        }
	    return reconstructed; 
	}
	
	private static void computeReconstructionError(DataSet original, DataSet reconstructed) {
		EuclideanDistance distanceFunction = new EuclideanDistance();
		double totalDataSetsDistance = distanceFunction.value(original, reconstructed);
		double averageInstanceDistane = totalDataSetsDistance/original.size();
		System.out.println("Total reconstruction error as Euclidean distance between data sets: " + totalDataSetsDistance);
		System.out.println("Average Instance reconstruction error: " + averageInstanceDistane);
		
	}


	private static void saveAsArff(DataSet dataSet, String outputFile, List<String> labels) {
		writeArffHeader(dataSet, outputFile);
		Instance[] instances = dataSet.getInstances();
		for (Instance instance : instances) {
			Vector vector = instance.getData();
			for (int i = 0; i < vector.size(); i++) {
				MLAssignmentUtils.writeToFile(outputFile, String.valueOf(vector.get(i)) + ",", true);
			}
			MLAssignmentUtils.writeToFile(outputFile, getLabel(instance.getLabel(),labels) + "\n", true);
		}
		
	}

	private static String getLabel(Instance label, List<String> labels) {
		Vector vector = label.getData();
		for (int i = 0; i < vector.size(); i++) {
			if(vector.get(i) == 1) {
				return labels.get(i);
			}
		}
		throw new RuntimeException("No label with value found");
		
	}

	private static void writeArffHeader(DataSet dataSet, String outputFile) {
		MLAssignmentUtils.writeToFile(outputFile, "@Relation IPCATransformed\n",false);
		Vector vector = dataSet.get(0).getData();
		for (int i = 0; i < vector.size(); i++) {
			MLAssignmentUtils.writeToFile(outputFile, "@attribute attr" + (i+1) + " numeric\n", true);
		}
		MLAssignmentUtils.writeToFile(outputFile, "@data\n", true);
	}
}
