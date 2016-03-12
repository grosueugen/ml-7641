package com.gt.ml.pb;

import java.util.Arrays;
import java.util.Random;

import dist.DiscreteDependencyTree;
import dist.DiscretePermutationDistribution;
import dist.DiscreteUniformDistribution;
import dist.Distribution;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.SwapNeighbor;
import opt.example.TravelingSalesmanCrossOver;
import opt.example.TravelingSalesmanEvaluationFunction;
import opt.example.TravelingSalesmanRouteEvaluationFunction;
import opt.example.TravelingSalesmanSortEvaluationFunction;
import opt.ga.CrossoverFunction;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.ga.SwapMutation;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.Instance;

public class TSPTime {
	
	private final int N;
	private final int runs;
	private final long time;
	
	public TSPTime(int N, int runs, long time) {
		this.N = N;
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
	        double[][] points = new double[N][2];
	        for (int i = 0; i < points.length; i++) {
	            points[i][0] = random.nextDouble();
	            points[i][1] = random.nextDouble();   
	        }
	        TravelingSalesmanEvaluationFunction ef = new TravelingSalesmanRouteEvaluationFunction(points);
	        Distribution odd = new DiscretePermutationDistribution(N);
	        NeighborFunction nf = new SwapNeighbor();
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
	        double[][] points = new double[N][2];
	        for (int i = 0; i < points.length; i++) {
	            points[i][0] = random.nextDouble();
	            points[i][1] = random.nextDouble();   
	        }
	        TravelingSalesmanEvaluationFunction ef = new TravelingSalesmanRouteEvaluationFunction(points);
	        Distribution odd = new DiscretePermutationDistribution(N);
	        NeighborFunction nf = new SwapNeighbor();
	        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
	        SimulatedAnnealing sa = new SimulatedAnnealing(1E12, .95, hcp);
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
	        double[][] points = new double[N][2];
	        for (int i = 0; i < points.length; i++) {
	            points[i][0] = random.nextDouble();
	            points[i][1] = random.nextDouble();   
	        }
	        TravelingSalesmanEvaluationFunction ef = new TravelingSalesmanRouteEvaluationFunction(points);
	        Distribution odd = new DiscretePermutationDistribution(N);
	        MutationFunction mf = new SwapMutation();
	        CrossoverFunction cf = new TravelingSalesmanCrossOver(ef);
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
	        double[][] points = new double[N][2];
	        for (int i = 0; i < points.length; i++) {
	            points[i][0] = random.nextDouble();
	            points[i][1] = random.nextDouble();   
	        }
	        TravelingSalesmanEvaluationFunction ef = new TravelingSalesmanSortEvaluationFunction(points);
	        int[] ranges = new int[N];
	        Arrays.fill(ranges, N);
	        Distribution odd = new  DiscreteUniformDistribution(ranges);
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
