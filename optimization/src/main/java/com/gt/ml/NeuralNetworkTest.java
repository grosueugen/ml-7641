package com.gt.ml;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import opt.OptimizationAlgorithm;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.example.NeuralNetworkOptimizationProblem;
import opt.ga.StandardGeneticAlgorithm;
import shared.ConvergenceTrainer;
import shared.DataSet;
import shared.ErrorMeasure;
import shared.Instance;
import shared.SumOfSquaresError;
import func.nn.activation.LogisticSigmoid;
import func.nn.backprop.BackPropagationNetwork;
import func.nn.backprop.BackPropagationNetworkFactory;
import func.nn.backprop.StandardUpdateRule;
import func.nn.backprop.StochasticBackPropagationTrainer;

/**
 * Transformed AbaloneTest.java
 */
public class NeuralNetworkTest {
	private static String outputDir = System.getProperty("outputDir") == null ? "c:/work/transfer/"
			: System.getProperty("outputDir");
	private static String now = new SimpleDateFormat("yyyy.MM.dd.hh.mm.ss").format(new Date());
	private static String fileName = outputDir + "neuralNetworkTest." + now + ".csv";
	private static int noAttributes = 20;
	private static Instance[] trainingInstances = initializeInstances(0, 5000);
	private static Instance[] testingInstances = initializeInstances(6000, 7000);

	private static int inputLayer = noAttributes;
	private static int outputLayer = 1;
	private static int hiddenLayer = (noAttributes + outputLayer + 60) / 2;
	private static int trainingIterations = 100;
	private static BackPropagationNetworkFactory factory = new BackPropagationNetworkFactory();

	private static ErrorMeasure measure = new SumOfSquaresError();

	private static DataSet set = new DataSet(trainingInstances);

	private static BackPropagationNetwork networks[] = new BackPropagationNetwork[3];
	private static NeuralNetworkOptimizationProblem[] nnop = new NeuralNetworkOptimizationProblem[3];

	private static List<OptimizationAlgorithm> oa = new ArrayList<>();

	private static String[] oaNames = { "RHC", "SA", "GA" };
	private static String results = "";

	private static DecimalFormat df = new DecimalFormat("0.000");

	private static int algos = 3;

	private static BackPropagationNetwork buildNN() {
		int[] layers = new int[] { inputLayer, hiddenLayer, outputLayer };
		BackPropagationNetwork res = factory.createClassificationNetwork(layers, new LogisticSigmoid());
		return res;
	}

	public static void main(String[] args) {
		Locale.setDefault(Locale.US);

		//MLAssignmentUtils.writeToFile(fileName, "sep=,", false);

		for (int i = 0; i < algos; i++) {
			networks[i] = buildNN();
			nnop[i] = new NeuralNetworkOptimizationProblem(set, networks[i], measure);
		}

		oa.add(new RandomizedHillClimbing(nnop[0]));
		oa.add(new SimulatedAnnealing(1E11, .95, nnop[1]));
		oa.add(new StandardGeneticAlgorithm(200, 100, 10, nnop[2]));

		// runGradientDescent();
		for (int i = 0; i < algos; i++) {
			double start = System.nanoTime();
			double end;
			double trainingTime;
			double testingTime;

			train(oa.get(i), networks[i], oaNames[i]);
			end = System.nanoTime();
			trainingTime = (end - start) / Math.pow(10, 9);

			Instance optimalInstance = oa.get(i).getOptimal();
			networks[i].setWeights(optimalInstance.getData());

			start = System.nanoTime();
			end = System.nanoTime();
			double trainingAccuracy = accuracy(networks[i], trainingInstances);
			double testAccuracy = accuracy(networks[i], testingInstances);
			testingTime = end - start;
			testingTime /= Math.pow(10, 9);

			results += "\nResults for " + oaNames[i] + ":" + "\n Training accuracy: "
					+ df.format(trainingAccuracy * 100) + "\n Test accuracy: " + df.format(testAccuracy * 100)
					+ "\nTraining time: " + df.format(trainingTime) + " seconds" + "\nTesting time: "
					+ df.format(testingTime) + " seconds\n";
		}

		System.out.println(results);
	}

	private static void runGradientDescent() {
		BackPropagationNetwork nn = buildNN();
		ConvergenceTrainer trainer = new ConvergenceTrainer(new StochasticBackPropagationTrainer(set, nn,
				new SumOfSquaresError(), new StandardUpdateRule(0.2, 0.3)));
		trainer.train();
		System.out.println(accuracy(nn, trainingInstances));
		System.out.println(accuracy(nn, testingInstances));
		System.exit(0);
	}

	private static void train(OptimizationAlgorithm oa, BackPropagationNetwork network, String oaName) {
		System.out.println("\nError results for " + oaName + "\n---------------------------");

		for (int i = 0; i < trainingIterations; i++) {
			long start = System.currentTimeMillis();
			oa.train();
			long end = System.currentTimeMillis();
			double trainingAccuracy = accuracy(network, trainingInstances);
			double testAccuracy = accuracy(network, testingInstances);
			long iterationTime = end - start;
			print(oa, i, trainingAccuracy, testAccuracy, iterationTime);
		}
	}

	private static void print(OptimizationAlgorithm oa, int iteration, double trainingAccuracy, double testAccuracy,
			long iterationTime) {
		String fileName = outputDir + now + ".csv";
		String s = iteration + "," + oa.getClass().getSimpleName() + ","
				+ df.format(trainingAccuracy * 100).replace(",", ".") + ","
				+ df.format(testAccuracy * 100).replace(",", ".") + "," + iterationTime;
		System.out.println(s);
		//MLAssignmentUtils.writeToFile(fileName, s, true);
	}

	private static Instance[] initializeInstances(int start, int end) {
		double[][][] attributes = new double[end - start][][];
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					Thread.currentThread().getContextClassLoader().getResourceAsStream("test-function.txt")));
			for (int i = 0; i < start; i++) {
				br.readLine();
			}
			for (int i = 0; i < attributes.length; i++) {
				Scanner scan = new Scanner(br.readLine());
				scan.useDelimiter(",");

				attributes[i] = new double[2][];
				attributes[i][0] = new double[noAttributes];
				attributes[i][1] = new double[1];

				for (int j = 0; j < 20; j++) {
					attributes[i][0][j] = Double.parseDouble(scan.next());
				}
				attributes[i][1][0] = Double.parseDouble(scan.next()) == 0 ? 0.1 : 0.9;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Instance[] instances = new Instance[attributes.length];
		for (int i = 0; i < instances.length; i++) {
			instances[i] = new Instance(attributes[i][0]);
			instances[i].setLabel(new Instance(attributes[i][1][0]));
		}
		return instances;
	}

	private static double accuracy(BackPropagationNetwork network, Instance[] instances) {
		int correct = 0;
		for (int j = 0; j < instances.length; j++) {
			network.setInputValues(instances[j].getData());
			network.run();

			double predicted = Double.parseDouble(trainingInstances[j].getLabel().toString());
			double actual = Double.parseDouble(network.getOutputValues().toString());

			if (Math.abs(predicted - actual) < 0.5) {
				correct++;
			}
		}
		return (double) correct / instances.length;
	}
}