package com.gt.ml;

import java.util.Arrays;
import java.util.List;

import shared.DataSet;
import shared.EuclideanDistance;
import shared.Instance;
import shared.filt.PrincipalComponentAnalysis;
import util.linalg.Matrix;
import util.linalg.Vector;

/**
 * arguments: <inputFile> <numInstances> <numAttributes> <numEigenVectorsToKeep> <dataFileOuput> <commaSeparatedListOfClassLabels>
 * Read the input file (the first <numInstances> of it)
 * Run the PCA and reduce the dimensions to <numEigenVectorsToKeep> param
 * Save the reduced data set as <dataFileOutput>
 * Reconstruct the original data set and compute the reconstruction error as the average euclidean distance of the 2 data sets
 * @author cosmin
 *
 */
//pca.bat sat.arff 6000 36 6 pca.out 1,2,3,4,5,7
//pca.bat wine.abi 1000 11 0.95 pca-wine1.out 3,4,5,6,7,8,9
public class PCA {

	public static void main(String[] args) {
		if(args.length != 6) {
			System.out.println("Usage: PCA <inputFile> <numInstances> <numAttributes> <varianceToKeep> <dataFileOuput> <commaSeparatedListOfClassLabels>");
			System.exit(-1);
		}
		String inputFile = args[0];
		int numInstances = Integer.valueOf(args[1]);
		int numAttributes = Integer.valueOf(args[2]);
		double varianceToKeep = Double.valueOf(args[3]);
		String outputFile = args[4];
		List<String> labels = Arrays.asList(args[5].split(","));
		Instance[] instances = MLAssignmentUtils.initializeInstances(numInstances, inputFile, numAttributes, labels);
		DataSet original = new DataSet(instances);
		
		PrincipalComponentAnalysis pca = new PrincipalComponentAnalysis(original, varianceToKeep);
		DataSet transformed = run(pca,original);
		saveAsArff(transformed, outputFile, labels);
		DataSet reconstructed = reconstruct(pca, transformed);
		computeReconstructionError(original,reconstructed);
	}

	private static DataSet run(PrincipalComponentAnalysis pca, DataSet original) {
        System.out.println("Eigenvalues");
        //System.out.println(pca.getEigenValues());
        Matrix eigenValues = pca.getEigenValues();
		int m = eigenValues.m();
        int n = eigenValues.n();
        if (m != n) {
        	System.out.println("calculation error, m!=n eigenvalue matrix");
        	System.exit(0);
        }
        for (int i = 0; i < m; i++) {
        	System.out.println(eigenValues.get(i, i));
        }
        
        System.out.println("r dimension = " + pca.getProjection().m());
        //System.out.println("===============================================");
        //System.out.println("The projection matrix");
        //System.out.println(pca.getProjection());
        DataSet transformed = original.copy(); 
        pca.filter(transformed);
        return transformed;
	}
	
	private static DataSet reconstruct(PrincipalComponentAnalysis pca, DataSet transformed) {
		DataSet reconstructed = transformed.copy(); 
		Matrix reverse = pca.getProjection().transpose();
	        for (int i = 0; i < reconstructed.size(); i++) {
	            Instance instance = reconstructed.get(i);
	            instance.setData(reverse.times(instance.getData()).plus(pca.getMean()));
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
				MLAssignmentUtils.writeToFile(outputFile, String.valueOf(vector.get(i)) + " ", true);
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
		MLAssignmentUtils.writeToFile(outputFile, "@Relation PCATransformed\n",false);
		Vector vector = dataSet.get(0).getData();
		for (int i = 0; i < vector.size(); i++) {
			MLAssignmentUtils.writeToFile(outputFile, "@attribute attr" + (i+1) + " numeric\n", true);
		}
		MLAssignmentUtils.writeToFile(outputFile, "@data\n", true);
	}
}