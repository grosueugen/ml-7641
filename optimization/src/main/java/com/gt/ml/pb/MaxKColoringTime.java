package com.gt.ml.pb;

import java.util.Random;

import dist.DiscreteDependencyTree;
import dist.DiscretePermutationDistribution;
import dist.Distribution;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.SwapNeighbor;
import opt.ga.CrossoverFunction;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MaxKColorFitnessFunction;
import opt.ga.MutationFunction;
import opt.ga.SingleCrossOver;
import opt.ga.StandardGeneticAlgorithm;
import opt.ga.SwapMutation;
import opt.ga.Vertex;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.Instance;

public class MaxKColoringTime {

	private final int N; // number of vertices
	private final int L = 8; // L adjacent nodes per vertex
	private final int K = 16; // K possible colors

	private final int runs;
	private final long time;

	public MaxKColoringTime(int N, int runs, long millis) {
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
		// System.out.println("##### N," + N + ",RHC start");
		double sumOptimalValue = 0;
		long sumIterations = 0;
		long bestTime = 0;
		long bestIteration = 0;
		int conflict = 0;
		int nonConflict = 0;
		for (int r = 0; r < runs; r++) {
			Random random = new Random(N * L);
			// create the random velocity
			Vertex[] vertices = new Vertex[N];
			for (int i = 0; i < N; i++) {
				Vertex vertex = new Vertex();
				vertices[i] = vertex;
				vertex.setAdjMatrixSize(L);
				for (int j = 0; j < L; j++) {
					vertex.getAadjacencyColorMatrix().add(random.nextInt(N * L));
				}
			}
			MaxKColorFitnessFunction ef = new MaxKColorFitnessFunction(vertices);
			Distribution odd = new DiscretePermutationDistribution(K);
			NeighborFunction nf = new SwapNeighbor();
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
			sumIterations += iterations;
			bestTime += timeMs;
			bestIteration += it;
			if (ef.isConflict()) {
				conflict++;
			} else {
				nonConflict++;
			}

			// System.out.println("N," + N + ",RHC," + optimalValue +
			// ",iterations," + iterations
			// + ",bestValueInTime," + timeMs + ",bestValueInIteration," + it);
		}
		// System.out.println("##### N," + N + ",RHC AVG," +
		// (sumOptimalValue/runs)
		// + ",bestValueInTime," + (bestTime/runs) + ",bestValueInIteration," +
		// (bestIteration/runs));
		System.out.println("RHC," + N + "," + (sumOptimalValue / runs) + "," + (sumIterations / runs) + ","
				+ (bestTime / runs) + "," + (bestIteration / runs) 
				+ "," + conflict + "," + nonConflict);
	}

	private void runSA() {
		// System.out.println("##### N," + N + ",RHC start");
		double sumOptimalValue = 0;
		long sumIterations = 0;
		long bestTime = 0;
		long bestIteration = 0;
		int conflict = 0;
		int nonConflict = 0;
		for (int r = 0; r < runs; r++) {
			Random random = new Random(N * L);
			// create the random velocity
			Vertex[] vertices = new Vertex[N];
			for (int i = 0; i < N; i++) {
				Vertex vertex = new Vertex();
				vertices[i] = vertex;
				vertex.setAdjMatrixSize(L);
				for (int j = 0; j < L; j++) {
					vertex.getAadjacencyColorMatrix().add(random.nextInt(N * L));
				}
			}
			MaxKColorFitnessFunction ef = new MaxKColorFitnessFunction(vertices);
	        Distribution odd = new DiscretePermutationDistribution(K);
	        NeighborFunction nf = new SwapNeighbor();
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
			sumIterations += iterations;
			bestTime += timeMs;
			bestIteration += it;
			if (ef.isConflict()) {
				conflict++;
			} else {
				nonConflict++;
			}

			// System.out.println("N," + N + ",RHC," + optimalValue +
			// ",iterations," + iterations
			// + ",bestValueInTime," + timeMs + ",bestValueInIteration," + it);
		}
		// System.out.println("##### N," + N + ",RHC AVG," +
		// (sumOptimalValue/runs)
		// + ",bestValueInTime," + (bestTime/runs) + ",bestValueInIteration," +
		// (bestIteration/runs));
		System.out.println("SA," + N + "," + (sumOptimalValue / runs) + "," + (sumIterations / runs) + ","
				+ (bestTime / runs) + "," + (bestIteration / runs) 
				+ "," + conflict + "," + nonConflict);
	}

	private void runGA() {
		// System.out.println("##### N," + N + ",RHC start");
		double sumOptimalValue = 0;
		long sumIterations = 0;
		long bestTime = 0;
		long bestIteration = 0;
		int conflict = 0;
		int nonConflict = 0;
		for (int r = 0; r < runs; r++) {
			Random random = new Random(N * L);
			// create the random velocity
			Vertex[] vertices = new Vertex[N];
			for (int i = 0; i < N; i++) {
				Vertex vertex = new Vertex();
				vertices[i] = vertex;
				vertex.setAdjMatrixSize(L);
				for (int j = 0; j < L; j++) {
					vertex.getAadjacencyColorMatrix().add(random.nextInt(N * L));
				}
			}
			MaxKColorFitnessFunction ef = new MaxKColorFitnessFunction(vertices);
	        Distribution odd = new DiscretePermutationDistribution(K);
	        MutationFunction mf = new SwapMutation();
	        CrossoverFunction cf = new SingleCrossOver();
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
			sumIterations += iterations;
			bestTime += timeMs;
			bestIteration += it;
			if (ef.isConflict()) {
				conflict++;
			} else {
				nonConflict++;
			}

			// System.out.println("N," + N + ",RHC," + optimalValue +
			// ",iterations," + iterations
			// + ",bestValueInTime," + timeMs + ",bestValueInIteration," + it);
		}
		// System.out.println("##### N," + N + ",RHC AVG," +
		// (sumOptimalValue/runs)
		// + ",bestValueInTime," + (bestTime/runs) + ",bestValueInIteration," +
		// (bestIteration/runs));
		System.out.println("GA," + N + "," + (sumOptimalValue / runs) + "," + (sumIterations / runs) + ","
				+ (bestTime / runs) + "," + (bestIteration / runs) 
				+ "," + conflict + "," + nonConflict);
	}

	private void runMimic() {
		// System.out.println("##### N," + N + ",RHC start");
		double sumOptimalValue = 0;
		long sumIterations = 0;
		long bestTime = 0;
		long bestIteration = 0;
		int conflict = 0;
		int nonConflict = 0;
		for (int r = 0; r < runs; r++) {
			Random random = new Random(N * L);
			// create the random velocity
			Vertex[] vertices = new Vertex[N];
			for (int i = 0; i < N; i++) {
				Vertex vertex = new Vertex();
				vertices[i] = vertex;
				vertex.setAdjMatrixSize(L);
				for (int j = 0; j < L; j++) {
					vertex.getAadjacencyColorMatrix().add(random.nextInt(N * L));
				}
			}
			MaxKColorFitnessFunction ef = new MaxKColorFitnessFunction(vertices);
	        Distribution odd = new DiscretePermutationDistribution(K);
	        
	        Distribution df = new DiscreteDependencyTree(.1); 
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
			sumIterations += iterations;
			bestTime += timeMs;
			bestIteration += it;
			if (ef.isConflict()) {
				conflict++;
			} else {
				nonConflict++;
			}

			// System.out.println("N," + N + ",RHC," + optimalValue +
			// ",iterations," + iterations
			// + ",bestValueInTime," + timeMs + ",bestValueInIteration," + it);
		}
		// System.out.println("##### N," + N + ",RHC AVG," +
		// (sumOptimalValue/runs)
		// + ",bestValueInTime," + (bestTime/runs) + ",bestValueInIteration," +
		// (bestIteration/runs));
		System.out.println("MIMIC," + N + "," + (sumOptimalValue / runs) + "," + (sumIterations / runs) + ","
				+ (bestTime / runs) + "," + (bestIteration / runs) 
				+ "," + conflict + "," + nonConflict);
	}

}
