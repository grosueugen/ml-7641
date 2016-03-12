package com.gt.ml.pb;

import java.util.Arrays;
import java.util.Random;

import dist.DiscreteDependencyTree;
import dist.DiscreteUniformDistribution;
import dist.Distribution;
import opt.DiscreteChangeOneNeighbor;
import opt.EvaluationFunction;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.example.KnapsackEvaluationFunction;
import opt.ga.CrossoverFunction;
import opt.ga.DiscreteChangeOneMutation;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.ga.UniformCrossOver;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.Instance;

public class KnapsackTime {
	
	private final int NUM_ITEMS;
	private final int COPIES_EACH;
	private final double MAX_WEIGHT;
	private final double MAX_VOLUME;
	private final double KNAPSACK_VOLUME;
	
	private final int runs;
	private final long time;
	
	public KnapsackTime(int NUM_ITEMS, int COPIES_EACH, double MAX_WEIGHT, double MAX_VOLUME, 
			int runs, long time) {
		this.NUM_ITEMS = NUM_ITEMS;
		this.COPIES_EACH = COPIES_EACH;
		this.MAX_WEIGHT = MAX_WEIGHT;
		this.MAX_VOLUME = MAX_VOLUME;
		KNAPSACK_VOLUME = MAX_VOLUME * NUM_ITEMS * COPIES_EACH * .4;
		this.runs = runs;
		this.time = time;
	}
	
	public void run() {
		runRHC();
		runSA();
		runGA();
		runMimic();
	}

	private void runRHC() {
		System.out.println("##### RHC start");
		double sumOptimalValue = 0;
		for (int r = 0; r < runs; r++) {
			Random random = new Random();
			int[] copies = new int[NUM_ITEMS];
	        Arrays.fill(copies, COPIES_EACH);
	        double[] weights = new double[NUM_ITEMS];
	        double[] volumes = new double[NUM_ITEMS];
	        for (int i = 0; i < NUM_ITEMS; i++) {
	            weights[i] = random.nextDouble() * MAX_WEIGHT;
	            volumes[i] = random.nextDouble() * MAX_VOLUME;
	        }
	         int[] ranges = new int[NUM_ITEMS];
	        Arrays.fill(ranges, COPIES_EACH + 1);
	        EvaluationFunction ef = new KnapsackEvaluationFunction(weights, volumes, KNAPSACK_VOLUME, copies);
	        Distribution odd = new DiscreteUniformDistribution(ranges);
	        NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
	        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
	        RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp); 
	        TimeTrainer fit = new TimeTrainer(rhc, time);
	        fit.train();	
	        int iterations = fit.getIterations();
	        Instance optimal = rhc.getOptimal();
	        double optimalValue = ef.value(optimal);
	        sumOptimalValue += optimalValue;
	        System.out.println("RHC," + optimalValue + ",iterations," + iterations);
		}
		System.out.println("RHC AVG," + (sumOptimalValue/runs));
	}

	private void runSA() {
		System.out.println("##### SA start");
		double sumOptimalValue = 0;
		for (int r = 0; r < runs; r++) {
			Random random = new Random();
			int[] copies = new int[NUM_ITEMS];
	        Arrays.fill(copies, COPIES_EACH);
	        double[] weights = new double[NUM_ITEMS];
	        double[] volumes = new double[NUM_ITEMS];
	        for (int i = 0; i < NUM_ITEMS; i++) {
	            weights[i] = random.nextDouble() * MAX_WEIGHT;
	            volumes[i] = random.nextDouble() * MAX_VOLUME;
	        }
	         int[] ranges = new int[NUM_ITEMS];
	        Arrays.fill(ranges, COPIES_EACH + 1);
	        EvaluationFunction ef = new KnapsackEvaluationFunction(weights, volumes, KNAPSACK_VOLUME, copies);
	        Distribution odd = new DiscreteUniformDistribution(ranges);
	        NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
	        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
	        
	        SimulatedAnnealing sa = new SimulatedAnnealing(100, .95, hcp);
	        TimeTrainer fit = new TimeTrainer(sa, time);
	        fit.train();	
	        int iterations = fit.getIterations();
	        Instance optimal = sa.getOptimal();
	        double optimalValue = ef.value(optimal);
	        sumOptimalValue += optimalValue;
	        System.out.println("SA," + optimalValue + ",iterations," + iterations);
		}
		System.out.println("SA AVG," + (sumOptimalValue/runs));
	}

	private void runGA() {
		System.out.println("##### GA start");
		double sumOptimalValue = 0;
		for (int r = 0; r < runs; r++) {
			Random random = new Random();
			int[] copies = new int[NUM_ITEMS];
	        Arrays.fill(copies, COPIES_EACH);
	        double[] weights = new double[NUM_ITEMS];
	        double[] volumes = new double[NUM_ITEMS];
	        for (int i = 0; i < NUM_ITEMS; i++) {
	            weights[i] = random.nextDouble() * MAX_WEIGHT;
	            volumes[i] = random.nextDouble() * MAX_VOLUME;
	        }
	         int[] ranges = new int[NUM_ITEMS];
	        Arrays.fill(ranges, COPIES_EACH + 1);
	        EvaluationFunction ef = new KnapsackEvaluationFunction(weights, volumes, KNAPSACK_VOLUME, copies);
	        Distribution odd = new DiscreteUniformDistribution(ranges);
	        MutationFunction mf = new DiscreteChangeOneMutation(ranges);
	        CrossoverFunction cf = new UniformCrossOver();
	        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
	        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 150, 25, gap);
	        TimeTrainer fit = new TimeTrainer(ga, time);
	        fit.train();	
	        int iterations = fit.getIterations();
	        Instance optimal = ga.getOptimal();
	        double optimalValue = ef.value(optimal);
	        sumOptimalValue += optimalValue;
	        System.out.println("GA," + optimalValue + ",iterations," + iterations);
		}
		System.out.println("GA AVG," + (sumOptimalValue/runs));
	}

	private void runMimic() {
		System.out.println("##### MIMIC start");
		double sumOptimalValue = 0;
		for (int r = 0; r < runs; r++) {
			Random random = new Random();
			int[] copies = new int[NUM_ITEMS];
	        Arrays.fill(copies, COPIES_EACH);
	        double[] weights = new double[NUM_ITEMS];
	        double[] volumes = new double[NUM_ITEMS];
	        for (int i = 0; i < NUM_ITEMS; i++) {
	            weights[i] = random.nextDouble() * MAX_WEIGHT;
	            volumes[i] = random.nextDouble() * MAX_VOLUME;
	        }
	         int[] ranges = new int[NUM_ITEMS];
	        Arrays.fill(ranges, COPIES_EACH + 1);
	        EvaluationFunction ef = new KnapsackEvaluationFunction(weights, volumes, KNAPSACK_VOLUME, copies);
	        Distribution odd = new DiscreteUniformDistribution(ranges);
	        Distribution df = new DiscreteDependencyTree(.1, ranges); 
	        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
	        MIMIC mimic = new MIMIC(200, 100, pop);
	        TimeTrainer fit = new TimeTrainer(mimic, time);
	        fit.train();	
	        int iterations = fit.getIterations();
	        Instance optimal = mimic.getOptimal();
	        double optimalValue = ef.value(optimal);
	        sumOptimalValue += optimalValue;
	        System.out.println("MIMIC," + optimalValue + ",iterations," + iterations);
		}
		System.out.println("MIMIC AVG," + (sumOptimalValue/runs));
	}

}
