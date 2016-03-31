package com.gt.ml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import shared.Instance;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.Saver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.instance.Randomize;
import weka.filters.unsupervised.instance.RemoveWithValues;

public class MLAssignmentUtils {

	public static Instances buildInstancesFromResource(String resourceName) {
		Reader r;
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
			if(is == null) {
				//throw new RuntimeException("Non existing resource file: " + resourceName);
				is = new FileInputStream(resourceName);
			}
			r = new java.io.BufferedReader(new java.io.InputStreamReader(is));
			Instances res = new Instances(r);
			setClassIndex(res);
			return res;
		} catch (Exception e) {
			throw new RuntimeException("", e);
		}
	}

	public static Instances buildInstancesFromFileName(String fileName) {
		Reader r;
		try {
			r = new java.io.BufferedReader(new java.io.FileReader(fileName));
			Instances res = new Instances(r);
			setClassIndex(res);
			return res;
		} catch (Exception e) {
			throw new RuntimeException("", e);
		}
	}

	private static void setClassIndex(Instances res) {
		res.setClassIndex(res.numAttributes() - 1);
	}

	public static void split(String arffFileName, double trainPecentage) throws Exception {
		Instances trainingDataSet = MLAssignmentUtils.buildInstancesFromFileName(arffFileName);
		Filter filter = new Randomize();
		filter.setInputFormat(trainingDataSet);
		Instances filtered = Filter.useFilter(trainingDataSet, filter);

		ArffSaver trainSaver = new ArffSaver();
		ArffSaver testSaver = new ArffSaver();
		trainSaver.setFile(new File(arffFileName.replace(".arff", "_train.arff")));
		testSaver.setFile(new File(arffFileName.replace(".arff", "_test.arff")));
		testSaver.setRetrieval(Saver.INCREMENTAL);
		trainSaver.setRetrieval(Saver.INCREMENTAL);
		testSaver.setStructure(filtered);
		trainSaver.setStructure(filtered);

		int border = (int) (filtered.size() * trainPecentage);
		for (int i = 0; i < border; i++) {
			trainSaver.writeIncremental(filtered.get(i));
		}
		for (int i = border; i < filtered.size(); i++) {
			testSaver.writeIncremental(filtered.get(i));
		}
	}

	public static void write(String fileName, Instances dataSet) {
		try {
			ArffSaver saver = new ArffSaver();
			saver.setFile(new File(fileName));
			saver.setRetrieval(Saver.BATCH);
			saver.setInstances(dataSet);
			saver.writeBatch();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Instances shufle(Instances input) throws Exception {
		Filter randomizer = new Randomize();
		randomizer.setInputFormat(input);
		return Filter.useFilter(input, randomizer);
	}

	public static Instances loadCSV(String resourceName) throws IOException {
		CSVLoader loader = new CSVLoader();
		loader.setSource(Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName));
		Instances res = loader.getDataSet();
		return res;
	}

	
	/** The average precision over all classes */
	public static double getAveragePrecision(Evaluation eval, Instances test) {
		double res = 0;
		int classes = test.classAttribute().numValues();
		for (int i = 0; i < classes; i++) {
			res += eval.precision(i);
		}
		return res * 100 / classes;
	}

	/** The average recall over all classes */
	public static double getAverageRecall(Evaluation eval, Instances test) {
		double res = 0;
		int classes = test.classAttribute().numValues();
		for (int i = 0; i < classes; i++) {
			res += eval.recall(i);
		}
		return res * 100 / classes;
	}

	/** The average recall over all classes */
	public static double getAverageFMeasure(Evaluation eval, Instances test) {
		double res = 0;
		int classes = test.classAttribute().numValues();
		for (int i = 0; i < classes; i++) {
			res += eval.fMeasure(i);
		}
		return res * 100 / classes;
	}

	

	public static Instances removeUnwantedClasses(Instances dataSet, String nominalIndexesToKeep) throws Exception {
		RemoveWithValues filter = new RemoveWithValues();
		filter.setInputFormat(dataSet);
		filter.setAttributeIndex(String.valueOf(dataSet.numAttributes() - 1));
		filter.setInvertSelection(true);
		return Filter.useFilter(dataSet, filter);

	}

	public static Instances keepOnlyAttributes(Instances dataSet, String attributesToKeep) {
		try {
			Remove filter = new Remove();
			filter.setInvertSelection(true);
			filter.setAttributeIndices(attributesToKeep);
			filter.setInputFormat(dataSet);
			return Filter.useFilter(dataSet, filter);
		} catch (Exception e) {
			throw new RuntimeException("Error keeping attributes " + attributesToKeep, e);
		}
	}

	public static Instances removeAttributes(Instances dataSet, String attributesToRemove) {
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

	public static void writeToFile(String fileName, String s, boolean append) {
		try {
			File file = new File(fileName);
			file.createNewFile();
			FileWriter writer = new FileWriter(new File(fileName), append);
			writer.write(s);
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public static int getStartN() {
		String s = System.getProperty("startN");
		return s == null ? 10: Integer.valueOf(s);
	}

	public static int getEndN() {
		String s = System.getProperty("endN");
		return s == null ? 100 : Integer.valueOf(s);
	}

	public static int getStep() {
		String s = System.getProperty("step");
		return s == null ? 5 : Integer.valueOf(s);
	}

	public static int getTrials() {
		String s = System.getProperty("trials");
		return s == null ? 3 : Integer.valueOf(s);
	}
	
	public static String toString(double[] array) {
		StringBuilder sb = new StringBuilder();
		for (double d : array) {
			sb.append(NumberFormat.getInstance().format(d)).append(" ");
		}
		return sb.toString();
	}

	public static double average(HashMap<String, Double> map) {
		double res = 0;
		for (Double d: map.values()) {
			res += d;
		}
		return res/map.size();
	}

	public static double countIf(int[] clusterAssignments, int equalTo) {
		int res = 0;
		for (int i : clusterAssignments) {
			if(i == equalTo) {
				res ++;
			}
		}
		return res;
	}
	
	
    public static Instance[] initializeInstances(int size, String filePath, int nrAttributes, List<String> labels ) {
    	
    	int nrLabels = labels.size();
        double[][][] attributes = new double[size][][];

        try {
        	InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        	if (is == null) {
        		is = new FileInputStream(filePath);
        	}
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            for(int i = 0; i < attributes.length; i++) {
                Scanner scan = new Scanner(br.readLine());
                scan.useDelimiter(" ");

                attributes[i] = new double[2][];
                attributes[i][0] = new double[nrAttributes]; 
                attributes[i][1] = new double[nrLabels];

                for(int j = 0; j < nrAttributes; j++)
                    attributes[i][0][j] = Double.parseDouble(scan.next());

                String label = scan.next();
                for (int j = 0; j < nrLabels; j++) {
                	if (j == labels.indexOf(label)) {
                		attributes[i][1][j] = 1;
                	} else {
                		attributes[i][1][j] = 0;
                	}
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        Instance[] instances = new Instance[attributes.length];

        for(int i = 0; i < instances.length; i++) {
            instances[i] = new Instance(attributes[i][0]);
            instances[i].setLabel(new Instance(attributes[i][1]));
        }

        return instances;
    }
    
    public static Instance[] initializeRobotDataSet(int size) {
        List<String> labels = new ArrayList<String>();
        labels.add("Slight-Right-Turn");
        labels.add("Sharp-Right-Turn");
        labels.add("Move-Forward");
        labels.add("Slight-Left-Turn");
    	Instance[] instances =  initializeInstances(size, "robot-moves.txt", 24, labels);
    	return instances;
    }
    
    public static Instance[] initializeGeneratedDataSet(int size) {
        List<String> labels = new ArrayList<String>();
        labels.add("0");
        labels.add("1");
    	Instance[] instances =  initializeInstances(size, "test-function.txt", 20, labels);
    	return instances;
    }

    

}