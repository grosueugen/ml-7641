package com.gt.ml.count;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gt.ml.TimeTrainer;

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
import opt.example.CountOnesEvaluationFunction;
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

public class CountOnesTime {
	
	private static final Logger log = LoggerFactory.getLogger(CountOnesTime.class);
	
	public static void main(String[] args) {		
		if (args.length != 4) {
			System.out.println("4 params neeed: alg(all|rhc|sa|ga), N, runs, time (for millis end with m, otherwise seconds default)");
			System.exit(0);
		}
		
		String alg = args[0];
		int N = Integer.valueOf(args[1]);
		int runs = Integer.valueOf(args[2]);
		long millis;
		String time = args[3];
		if (time.endsWith("m")) {
			millis = Long.valueOf(time.substring(0, time.length()-1));
		} else {
			millis = Long.valueOf(time) * 1000;
		}
		log.info("starting with alg: {}, N: {}, runs: {}, time in millis: {}", alg, N, runs, millis);
		
		if (alg.equals("all")) {
			runRHC(N, runs, millis);
			runSA(N, runs, millis);
			runGA(N, runs, millis);
			runMimic(N, runs, millis);
		} else if (alg.equals("rhc")) {
			runRHC(N, runs, millis);
		} else if (alg.equals("sa")) {
			runSA(N, runs, millis);
		} else if (alg.equals("ga")) {
			runGA(N, runs, millis);
		} else if (alg.equals("mimic")) {
			runMimic(N, runs, millis);
		} else {
			System.out.println("Incorrect alg, should be one of all, rhc, sa, ga, mimic");
			System.exit(0);
		}
		
		log.info("ended with alg: {}, N: {}, runs: {}, time in millis: {}", alg, N, runs, millis);
    }

	private static void runRHC(int N, int runs, long millis) {
		System.out.println("##### RHC start");
		for (int r = 0; r < runs; r++) {
			int[] ranges = new int[N];
	        Arrays.fill(ranges, 2);        
			EvaluationFunction ef = new CountOnesEvaluationFunction();
	        Distribution odd = new DiscreteUniformDistribution(ranges);
	        NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
	        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
	        RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
	        TimeTrainer fit = new TimeTrainer(rhc, millis);
	        fit.train();	
	        int iterations = fit.getIterations();
	        Instance optimal = rhc.getOptimal();
	        double optimalValue = ef.value(optimal);
			System.out.println("RHC," + optimalValue + ",iterations," + iterations);
		}
		System.out.println("##### RHC end");
	}
	
	private static void runSA(int N, int runs, long millis) {
		System.out.println("##### SA start");
		for (int r = 0; r < runs; r++) {
			int[] ranges = new int[N];
	        Arrays.fill(ranges, 2);        
			EvaluationFunction ef = new CountOnesEvaluationFunction();
	        Distribution odd = new DiscreteUniformDistribution(ranges);
	        NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
	        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
	        SimulatedAnnealing sa = new SimulatedAnnealing(100, .95, hcp);	        
	        TimeTrainer fit = new TimeTrainer(sa, millis);	        
	        fit.train();
	        int iterations = fit.getIterations();
	        Instance optimal = sa.getOptimal();
	        double optimalValue = ef.value(optimal);
	        System.out.println("SA," + optimalValue + ",iterations," + iterations);
		}
		System.out.println("##### SA end");
	}
	
	private static void runGA(int N, int runs, long millis) {
		System.out.println("##### GA start");
		for (int r = 0; r < runs; r++) {
			int[] ranges = new int[N];
	        Arrays.fill(ranges, 2);        
	        EvaluationFunction ef = new CountOnesEvaluationFunction();
	        Distribution odd = new DiscreteUniformDistribution(ranges);
	        MutationFunction mf = new DiscreteChangeOneMutation(ranges);
	        CrossoverFunction cf = new UniformCrossOver();	        
	        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);			
	        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(20, 20, 0, gap);	        
	        TimeTrainer fit = new TimeTrainer(ga, millis);
	        fit.train();
	        int iterations = fit.getIterations();
	        Instance optimal = ga.getOptimal();
	        double optimalValue = ef.value(optimal);
	        System.out.println("GA," + optimalValue + ",iterations," + iterations);
		}
		System.out.println("##### GA end");
	}
	
	private static void runMimic(int N, int runs, long millis) {
		System.out.println("##### MIMIC start");
		for (int r = 0; r < runs; r++) {
			int[] ranges = new int[N];
	        Arrays.fill(ranges, 2);        
	        EvaluationFunction ef = new CountOnesEvaluationFunction();
	        Distribution odd = new DiscreteUniformDistribution(ranges);
	        Distribution df = new DiscreteDependencyTree(.1, ranges);
	        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
	        MIMIC mimic = new MIMIC(50, 10, pop);	        
	        TimeTrainer fit = new TimeTrainer(mimic, millis);
	        fit.train();
	        int iterations = fit.getIterations();
	        Instance optimal = mimic.getOptimal();
	        double optimalValue = ef.value(optimal);
	        System.out.println("MIMIC," + optimalValue + ",iterations," + iterations);
		}
		System.out.println("##### MIMIC end");
	}

}
