package com.gt.ml.pb;

import java.util.Arrays;

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

public class Even0Odd1Time {
	
	private final int N;
	private final int runs;
	private final long time;
	
	public Even0Odd1Time(int N, int runs, long millis) {
		this.N = N;
		this.runs = runs;
		this.time = millis;
	}
	
	public void run() {
		runRHC();
		runSA();
		runGA();
		runMimic();
	}
	
	private void runRHC() {
		System.out.println("##### N," + N + ",RHC start");
		double sumOptimalValue = 0;
		long bestTime = 0;
		long bestIteration = 0; 
		for (int r = 0; r < runs; r++) {
			int[] ranges = new int[N];
	        Arrays.fill(ranges, 2);        
	        EvaluationFunction ef = new Even0Odd1EvaluationFunction();
	        Distribution odd = new DiscreteUniformDistribution(ranges);
	        NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
	        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
	        RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
	        TimeTrainer fit = new TimeTrainer(rhc, time);
	        fit.train();	
	        long iterations = fit.getIterations();
	        long timeMs = fit.getBestValueInTime();
	        long it = fit.getBestValueInIteration();
	        
	        Instance optimal = rhc.getOptimal();
	        double optimalValue = ef.value(optimal);
	        sumOptimalValue += optimalValue;
	        bestTime += timeMs;
	        bestIteration += it;
	        
	        System.out.println("N," + N + ",RHC," + optimalValue + ",iterations," + iterations 
	        		+ ",bestValueInTime," + timeMs + ",bestValueInIteration," + it);
		}
		System.out.println("##### N," + N + ",RHC AVG," + (sumOptimalValue/runs) 
				+ ",bestValueInTime," + (bestTime/runs) + ",bestValueInIteration," + (bestIteration/runs));
	}

	private void runSA() {
		System.out.println("##### N," + N + ",SA start");
		double sumOptimalValue = 0;
		long bestTime = 0; 
		long bestIteration = 0;
		for (int r = 0; r < runs; r++) {
			int[] ranges = new int[N];
	        Arrays.fill(ranges, 2);     
	        EvaluationFunction ef = new Even0Odd1EvaluationFunction();  
	        Distribution odd = new DiscreteUniformDistribution(ranges);
	        NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
	        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
	        SimulatedAnnealing sa = new SimulatedAnnealing(100, .95, hcp);	        
	        TimeTrainer fit = new TimeTrainer(sa, time);
	        fit.train();	
	        long iterations = fit.getIterations();
	        long timeMs = fit.getBestValueInTime();
	        long it = fit.getBestValueInIteration();
	        
	        Instance optimal = sa.getOptimal();
	        double optimalValue = ef.value(optimal);
	        sumOptimalValue += optimalValue;
	        bestTime += timeMs;
	        bestIteration += it;
	        System.out.println("N," + N + ",SA," + optimalValue + ",iterations," + iterations 
	        		+ ",bestValueInTime," + timeMs + ",bestValueInIteration," + it);
		}
		System.out.println("##### N," + N + ",SA AVG," + (sumOptimalValue/runs) 
				+ ",bestValueInTime," + (bestTime/runs) + ",bestValueInIteration," + (bestIteration/runs));
	}

	private void runGA() {
		System.out.println("##### N," + N + ",GA start");
		double sumOptimalValue = 0;
		long bestTime = 0;
		long bestIteration = 0;
		for (int r = 0; r < runs; r++) {
			int[] ranges = new int[N];
	        Arrays.fill(ranges, 2);        
	        EvaluationFunction ef = new Even0Odd1EvaluationFunction();  
	        Distribution odd = new DiscreteUniformDistribution(ranges);
	        MutationFunction mf = new DiscreteChangeOneMutation(ranges);
	        CrossoverFunction cf = new UniformCrossOver();	        
	        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);			
	        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 100, 10, gap);	        
	        TimeTrainer fit = new TimeTrainer(ga, time);
	        fit.train();	
	        long iterations = fit.getIterations();
	        long timeMs = fit.getBestValueInTime();
	        long it = fit.getBestValueInIteration();
	        
	        Instance optimal = ga.getOptimal();
	        double optimalValue = ef.value(optimal);
	        sumOptimalValue += optimalValue;
	        bestTime += timeMs;
	        bestIteration += it;
	        
	        System.out.println("N," + N + ",GA," + optimalValue + ",iterations," + iterations 
	        		+ ",bestValueInTime," + timeMs + ",bestValueInIteration," + it);
		}
		System.out.println("##### N," + N + ",GA AVG," + (sumOptimalValue/runs) 
				+ ",bestValueInTime," + (bestTime/runs) + ",bestValueInIteration," + (bestIteration/runs));
	}

	private void runMimic() {
		System.out.println("##### N," + N + ",MIMIC start");
		double sumOptimalValue = 0;
		long bestTime = 0;
		long bestIteration = 0;
		for (int r = 0; r < runs; r++) {
			int[] ranges = new int[N];
	        Arrays.fill(ranges, 2);      
	        EvaluationFunction ef = new Even0Odd1EvaluationFunction(); 
	        Distribution odd = new DiscreteUniformDistribution(ranges);
	        Distribution df = new DiscreteDependencyTree(.1, ranges);
	        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
	        MIMIC mimic = new MIMIC(200, 100, pop);	        
	        TimeTrainer fit = new TimeTrainer(mimic, time);
	        fit.train();	
	        long iterations = fit.getIterations();
	        long timeMs = fit.getBestValueInTime();
	        long it = fit.getBestValueInIteration();
	        
	        Instance optimal = mimic.getOptimal();
	        double optimalValue = ef.value(optimal);
	        sumOptimalValue += optimalValue;
	        bestTime += timeMs;
	        bestIteration += it;
	        
	        System.out.println("N," + N + ",MIMIC," + optimalValue + ",iterations," + iterations 
	        		+ ",bestValueInTime," + timeMs + ",bestValueInIteration," + it);
		}
		System.out.println("##### N," + N + ",GA AVG," + (sumOptimalValue/runs) 
				+ ",bestValueInTime," + (bestTime/runs) + ",bestValueInIteration," + (bestIteration/runs));
	}

}
