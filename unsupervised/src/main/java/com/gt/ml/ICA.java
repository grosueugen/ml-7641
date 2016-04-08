package com.gt.ml;
import java.util.Arrays;
import java.util.List;

import shared.DataSet;
import shared.EuclideanDistance;
import shared.Instance;
import shared.filt.IndependentComponentAnalysis;
import util.linalg.Matrix;
import util.linalg.Vector;

/**
 * arguments: <inputFile> <numInstances> <numAttributes> <numComponents> <dataFileOuput> <commaSeparatedListOfClassLabels> 
 * Read the input file (the first <numInstances> of it) 
 * Run the ICA and reduce the dimensions to <numComponents> param 
 * Save the reduced data set as <dataFileOutput> 
 * Reconstruct the original data set and compute the reconstruction error as the average euclidean distance beween the 2 data sets
 * 
 * @author cosmin
 * 
 */

//ica.bat D:\GT\files\abagail\sat.arff 6000 36 6 ica.out 1,2,3,4,5,7
public class ICA {

	public static void main(String[] args) {
		if (args.length != 6) {
			System.out
					.println("Usage: ICA <inputFile> <numInstances> <numAttributes> <numComponents> <dataFileOuput> <commaSeparatedListOfClassLabels>");
			System.exit(-1);
		}
		String inputFile = args[0];
		int numInstances = Integer.valueOf(args[1]);
		int numAttributes = Integer.valueOf(args[2]);
		int numEigenVectorsToKeep = Integer.valueOf(args[3]);
		String outputFile = args[4];
		List<String> labels = Arrays.asList(args[5].split(","));
		Instance[] instances = MLAssignmentUtils.initializeInstances(
				numInstances, inputFile, numAttributes, labels);
		DataSet original = new DataSet(instances);

		IndependentComponentAnalysis ica = new IndependentComponentAnalysis(
				original, numEigenVectorsToKeep);

		DataSet transformed = run(ica, original);
		saveAsArff(transformed, outputFile, labels);
		DataSet reconstructed = reconstruct(ica, transformed);
		computeReconstructionError(original, reconstructed);
	}

	private static DataSet run(IndependentComponentAnalysis ica,
			DataSet original) {
		System.out.println("===============================================");
		System.out.println("The projection matrix");
		System.out.println(ica.getProjection());
		DataSet transformed = original.copy();
		ica.filter(transformed);
		return transformed;
	}

	private static DataSet reconstruct(IndependentComponentAnalysis ica,
			DataSet transformed) {
		DataSet reconstructed = transformed.copy();
		ica.reverse(reconstructed);
		return reconstructed;
		/*DataSet reconstructed = transformed.copy();
		Matrix reverse = ica.getProjection().transpose();
		for (int i = 0; i < reconstructed.size(); i++) {
			Instance instance = reconstructed.get(i);
			instance.setData(reverse.times(instance.getData()));
		}
		return reconstructed;*/
	}

	private static void computeReconstructionError(DataSet original,
			DataSet reconstructed) {
		EuclideanDistance distanceFunction = new EuclideanDistance();
		double totalDataSetsDistance = distanceFunction.value(original,
				reconstructed);
		double averageInstanceDistane = totalDataSetsDistance / original.size();
		System.out
				.println("Total reconstruction error as Euclidean distance between data sets: "
						+ totalDataSetsDistance);
		System.out.println("Average Instance reconstruction error: "
				+ averageInstanceDistane);

	}

	private static void saveAsArff(DataSet dataSet, String outputFile,
			List<String> labels) {
		writeArffHeader(dataSet, outputFile);
		Instance[] instances = dataSet.getInstances();
		for (Instance instance : instances) {
			Vector vector = instance.getData();
			for (int i = 0; i < vector.size(); i++) {
				MLAssignmentUtils.writeToFile(outputFile,
						String.valueOf(vector.get(i)) + ",", true);
			}
			MLAssignmentUtils.writeToFile(outputFile,
					getLabel(instance.getLabel(), labels) + "\n", true);
		}

	}

	private static String getLabel(Instance label, List<String> labels) {
		Vector vector = label.getData();
		for (int i = 0; i < vector.size(); i++) {
			if (vector.get(i) == 1) {
				return labels.get(i);
			}
		}
		throw new RuntimeException("No label with value found");

	}

	private static void writeArffHeader(DataSet dataSet, String outputFile) {
		MLAssignmentUtils.writeToFile(outputFile, "@Relation ICATransformed\n",
				false);
		Vector vector = dataSet.get(0).getData();
		for (int i = 0; i < vector.size(); i++) {
			MLAssignmentUtils.writeToFile(outputFile, "@attribute attr"
					+ (i + 1) + " numeric\n", true);
		}
		MLAssignmentUtils.writeToFile(outputFile, "@data\n", true);
	}
}
