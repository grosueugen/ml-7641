package com.gt.ml;

import dist.*;
import opt.*;
import opt.example.*;
import opt.ga.*;
import shared.*;
import util.linalg.Vector;
import func.nn.backprop.*;

import java.util.*;
import java.io.*;
import java.text.*;

/**
 * Implementation of randomized hill climbing, simulated annealing, and genetic algorithm to
 * find optimal weights to a neural network that is classifying sat data.
 */
public class NNOptimization {
	
	private final static int nrAttributes = 36;
	private final static int nrLabels = 6;
	
    private static Instance[] trainInstances = initializeInstances(4435, "sat-train-new.txt");
    private static Instance[] testInstances = initializeInstances(2000, "sat-test-new.txt");

    private static int inputLayer = nrAttributes, hiddenLayer = (nrAttributes + nrLabels)/2, outputLayer = nrLabels; 
    		static int trainingIterations = 10000;
    private static BackPropagationNetworkFactory factory = new BackPropagationNetworkFactory();
    
    private static ErrorMeasure measure = new SumOfSquaresError();

    private static DataSet trainingSet = new DataSet(trainInstances);
    private static DataSet testSet = new DataSet(testInstances);

    private static BackPropagationNetwork networks[] = new BackPropagationNetwork[3];
    private static NeuralNetworkOptimizationProblem[] nnop = new NeuralNetworkOptimizationProblem[3];

    private static OptimizationAlgorithm[] oa = new OptimizationAlgorithm[3];
    private static String[] oaNames = {"RHC", "SA", "GA"};
    private static String results = "";

    private static DecimalFormat df = new DecimalFormat("0.000");

    public static void main(String[] args) {
        for(int i = 0; i < oa.length; i++) {
            networks[i] = factory.createClassificationNetwork(
                new int[] {inputLayer, hiddenLayer, outputLayer});
            nnop[i] = new NeuralNetworkOptimizationProblem(trainingSet, networks[i], measure);
        }

        oa[0] = new RandomizedHillClimbing(nnop[0]);
        oa[1] = new SimulatedAnnealing(1E11, .95, nnop[1]);
        oa[2] = new StandardGeneticAlgorithm(200, 100, 10, nnop[2]);

        //for(int i = 0; i < oa.length; i++) {
          for(int i = 0; i < 1; i++) {
            double start = System.nanoTime(), end, trainingTime, testingTime;
            train(oa[i], networks[i], oaNames[i]); //trainer.train();
            end = System.nanoTime();
            trainingTime = end - start;
            trainingTime /= Math.pow(10,9);

            Instance optimalInstance = oa[i].getOptimal();
            networks[i].setWeights(optimalInstance.getData());
            
            start = System.nanoTime();
            double trainingAccuracy = accuracy(networks[i], trainingSet);
            double testAccuracy = accuracy(networks[i], testSet);
            end = System.nanoTime();
            testingTime = end - start;
            testingTime /= Math.pow(10,9);

            results +=  "\nResults for " + oaNames[i] + ": \nTraining accuracy " + trainingAccuracy +
                        "\nTest accuracy " + testAccuracy + "\nTraining time: " + df.format(trainingTime)
                        + " seconds\nTesting time: " + df.format(testingTime) + " seconds\n";
        }

        System.out.println(results);
    }

    private static double accuracy(BackPropagationNetwork network, DataSet dataSet) {
    	int correct = 0;
    	int incorrect = 0;
    	for (int j = 0; j < dataSet.size(); j++) {
    		Instance instance = dataSet.get(j);
    		network.setInputValues(instance.getData());
    		network.run();
    		Vector predicted = network.getOutputValues();
    		Vector actual = instance.getLabel().getData();
    		boolean ok = true;
    		for (int i = 0; i < nrLabels; i++) {
    			double predictedValue = predicted.get(i);
    			double actualValue = actual.get(i);
    			if (Math.abs(predictedValue - actualValue) >= 0.5) {
    				ok = false;
    			}
    		}
    		if (ok) correct++;
    		else incorrect++;
    	}
    	System.out.println("correct: " + correct + ", incorrect: " + incorrect);
    	return ((double)(correct*100))/dataSet.size();
	}

	private static void train(OptimizationAlgorithm oa, BackPropagationNetwork network, String oaName) {
        System.out.println("\nError results for " + oaName + "\n---------------------------");

        for(int i = 0; i < trainingIterations; i++) {
            oa.train();

            double error = 0;
            for(int j = 0; j < trainInstances.length; j++) {
                network.setInputValues(trainInstances[j].getData());
                network.run();
                error += measure.value(new Instance(network.getOutputValues()), trainInstances[j]);
            }

            System.out.println(df.format(error));
        }
    }

    private static Instance[] initializeInstances(int size, String filePath) {

        double[][][] attributes = new double[size][][];

        try {
        	InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            for(int i = 0; i < attributes.length; i++) {
                Scanner scan = new Scanner(br.readLine());
                scan.useDelimiter(",");

                attributes[i] = new double[2][];
                attributes[i][0] = new double[nrAttributes]; // 36 attributes
                attributes[i][1] = new double[nrLabels];

                for(int j = 0; j < nrAttributes; j++)
                    attributes[i][0][j] = Double.parseDouble(scan.next());

                int label = Integer.valueOf(scan.next());
                for (int j = 0; j < nrLabels; j++) {
                	if (j == (label-1)) {
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
}