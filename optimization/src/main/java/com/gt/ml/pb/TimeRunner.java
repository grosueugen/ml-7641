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

public class TimeRunner {
	
	private int runs;
	private int N;
	private long time;
	
	private EvaluationFunction ef;
	
	public TimeRunner runs(int value) {
		this.runs = value;
		return this;
	}
	
	public TimeRunner n(int value) {
		this.N = value;
		return this;
	}
	
	public TimeRunner time(long value) {
		this.time = value;
		return this;
	}
	
	public TimeRunner fct(EvaluationFunction value) {
		this.ef = value;
		return this;
	}
	
	public void runAll() {
		runRHC();
		runSA();
		runGA();
		runMimic();
	}
	
	public void runRHC() {
		System.out.println("##### RHC start");
		for (int r = 0; r < runs; r++) {
			int[] ranges = new int[N];
	        Arrays.fill(ranges, 2);        
	        Distribution odd = new DiscreteUniformDistribution(ranges);
	        NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
	        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
	        RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
	        TimeTrainer fit = new TimeTrainer(rhc, time);
	        fit.train();	
	        int iterations = fit.getIterations();
	        Instance optimal = rhc.getOptimal();
	        double optimalValue = ef.value(optimal);
			System.out.println("RHC," + optimalValue + ",iterations," + iterations);
		}
		System.out.println("##### RHC end");
	}
	
	public void runSA() {
		System.out.println("##### SA start");
		for (int r = 0; r < runs; r++) {
			int[] ranges = new int[N];
	        Arrays.fill(ranges, 2);        
	        Distribution odd = new DiscreteUniformDistribution(ranges);
	        NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
	        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
	        SimulatedAnnealing sa = new SimulatedAnnealing(100, .95, hcp);	        
	        TimeTrainer fit = new TimeTrainer(sa, time);	        
	        fit.train();
	        int iterations = fit.getIterations();
	        Instance optimal = sa.getOptimal();
	        double optimalValue = ef.value(optimal);
	        System.out.println("SA," + optimalValue + ",iterations," + iterations);
		}
		System.out.println("##### SA end");
	}
	
	public void runGA() {
		System.out.println("##### GA start");
		for (int r = 0; r < runs; r++) {
			int[] ranges = new int[N];
	        Arrays.fill(ranges, 2);        
	        Distribution odd = new DiscreteUniformDistribution(ranges);
	        MutationFunction mf = new DiscreteChangeOneMutation(ranges);
	        CrossoverFunction cf = new UniformCrossOver();	        
	        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);			
	        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(20, 20, 0, gap);	        
	        TimeTrainer fit = new TimeTrainer(ga, time);
	        fit.train();
	        int iterations = fit.getIterations();
	        Instance optimal = ga.getOptimal();
	        double optimalValue = ef.value(optimal);
	        System.out.println("GA," + optimalValue + ",iterations," + iterations);
		}
		System.out.println("##### GA end");
	}
	
	public void runMimic() {
		System.out.println("##### MIMIC start");
		for (int r = 0; r < runs; r++) {
			int[] ranges = new int[N];
	        Arrays.fill(ranges, 2);        
	        Distribution odd = new DiscreteUniformDistribution(ranges);
	        Distribution df = new DiscreteDependencyTree(.1, ranges);
	        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
	        MIMIC mimic = new MIMIC(50, 10, pop);	        
	        TimeTrainer fit = new TimeTrainer(mimic, time);
	        fit.train();
	        int iterations = fit.getIterations();
	        Instance optimal = mimic.getOptimal();
	        double optimalValue = ef.value(optimal);
	        System.out.println("MIMIC," + optimalValue + ",iterations," + iterations);
		}
		System.out.println("##### MIMIC end");
	}

}
