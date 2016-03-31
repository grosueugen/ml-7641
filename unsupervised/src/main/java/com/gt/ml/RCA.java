package com.gt.ml;

import java.util.Arrays;
import java.util.List;

import shared.DataSet;
import shared.EuclideanDistance;
import shared.Instance;
import shared.filt.RandomizedProjectionFilter;
import util.linalg.Vector;

//rca.bat D:\GT\files\abagail\sat.arff 6000 36 20 36 rca.out 1,2,3,4,5,7
public class RCA {

	public static void main(String[] args) {
		if(args.length != 7) {
			System.out.println("Usage: RCA <inputFile> <numInstances> <numAttributes> <componentsOut> <componentsIn> <dataFileOuput> <commaSeparatedListOfClassLabels>");
			System.exit(-1);
		}
		String inputFile = args[0];
		int numInstances = Integer.valueOf(args[1]);
		int numAttributes = Integer.valueOf(args[2]);
		int componentsOut = Integer.valueOf(args[3]);
		int componentsIn = Integer.valueOf(args[4]);
		String outputFile = args[5];
		List<String> labels = Arrays.asList(args[6].split(","));
		Instance[] instances = MLAssignmentUtils.initializeInstances(numInstances, inputFile, numAttributes, labels);
		DataSet original = new DataSet(instances);
		
		RandomizedProjectionFilter rca = new RandomizedProjectionFilter(componentsOut, componentsIn);
		DataSet transformed = run(rca, original);
		saveAsArff(transformed, outputFile, labels);
		DataSet reconstructed = reconstruct(rca, transformed);
		computeReconstructionError(original,reconstructed);
	}
	
	private static DataSet run(RandomizedProjectionFilter rca, DataSet original) {
        System.out.println("The projection matrix");
        System.out.println(rca.getProjection());
        DataSet transformed = original.copy(); 
        rca.filter(transformed);
        return transformed;
	}
	
	private static DataSet reconstruct(RandomizedProjectionFilter rca, DataSet transformed) {
		DataSet reconstructed = transformed.copy(); 
		rca.reverse(reconstructed);
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
	
	private static void writeArffHeader(DataSet dataSet, String outputFile) {
		MLAssignmentUtils.writeToFile(outputFile, "@Relation RCATransformed\n",false);
		Vector vector = dataSet.get(0).getData();
		for (int i = 0; i < vector.size(); i++) {
			MLAssignmentUtils.writeToFile(outputFile, "@attribute attr" + (i+1) + " numeric\n", true);
		}
		MLAssignmentUtils.writeToFile(outputFile, "@data\n", true);
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
	
}
