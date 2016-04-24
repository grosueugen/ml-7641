package rlGambling;

import burlap.behavior.policy.GreedyQPolicy;
import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.behavior.singleagent.planning.stochastic.policyiteration.PolicyIteration;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.states.State;
import burlap.oomdp.statehashing.HashableStateFactory;
import burlap.oomdp.statehashing.SimpleHashableStateFactory;

public class RunGambler {
	
	private String alg;
	private String outputPath;
	private int maxAmount;
	private int initialAmount;
	private double winProb;
	private int qlEpisodes;
	
	public RunGambler(String alg, String outputPath, int maxAmount, int initialAmount, double winProb, Integer qlEpisodes) {
		this.alg = alg;
		this.outputPath = outputPath;
		this.maxAmount = maxAmount;
		this.initialAmount = initialAmount;
		this.winProb = winProb;
		this.qlEpisodes = qlEpisodes;
	}

	public static void main(String[] args) {
		if (args.length != 5 && args.length != 6) 
			throw new IllegalArgumentException("6 params are required: alg (pi,vi,ql), max amount, initial amount, win probability,output file, QL-episodes (optional)");
		String alg = args[0];		
		int maxAmount = Integer.parseInt(args[1]);
		int initialAmount = Integer.parseInt(args[2]);
		double winProb = Double.parseDouble(args[3]);
		String outputPath = args[4];
		Integer qlEpisodes = 50;
		if (args.length == 6) {
			qlEpisodes = Integer.parseInt(args[5]);
		}
		if (!alg.equalsIgnoreCase("pi") && !alg.equalsIgnoreCase("vi") && !alg.equalsIgnoreCase("ql")) {
			throw new IllegalArgumentException("alg must be one of pi, vi, ql");
		}
		
		RunGambler rg = new RunGambler(alg, outputPath, maxAmount, initialAmount, winProb, qlEpisodes);
		rg.run();
	}

	private void run() {
		if (alg.equalsIgnoreCase("pi")) {
			runPI();
		} else if (alg.equalsIgnoreCase("vi")) {
			runVI();
		} else if (alg.equalsIgnoreCase("ql")) {
			runQL();
		} else {
			throw new IllegalStateException("alg must be one of <pi,vi,ql>");
		}
	}

	private void runPI() {
		GamblerDomain gamblerDomain = new GamblerDomain(maxAmount, initialAmount, winProb);
		Domain domain = gamblerDomain.generateDomain();
		State initialState = gamblerDomain.createInitialState(domain);
		GamblerReward reward = new GamblerReward(maxAmount);
		GamblerTerminalState terminalState = new GamblerTerminalState(maxAmount);
		HashableStateFactory hashingFactory = new SimpleHashableStateFactory();
		
		PolicyIteration planner = new PolicyIteration(domain, reward, terminalState, 0.99, hashingFactory, 0.001, 100, 100);
		
		Policy policy = planner.planFromState(initialState);
		EpisodeAnalysis episodeAnalysis = policy.evaluateBehavior(initialState, reward, terminalState);
		System.out.println("writing to file: " + outputPath);
		episodeAnalysis.writeToFile(outputPath);
		double discountedReturn = episodeAnalysis.getDiscountedReturn(0.99);
		System.out.println("return: " + discountedReturn);
	}

	private void runVI() {
		GamblerDomain gamblerDomain = new GamblerDomain(maxAmount, initialAmount, winProb);
		Domain domain = gamblerDomain.generateDomain();
		State initialState = gamblerDomain.createInitialState(domain);
		GamblerReward reward = new GamblerReward(maxAmount);
		GamblerTerminalState terminalState = new GamblerTerminalState(maxAmount);
		HashableStateFactory hashingFactory = new SimpleHashableStateFactory();
		
		ValueIteration planner = new ValueIteration(domain, reward, terminalState, 0.99, hashingFactory, 0.001, 100);
		planner.toggleReachabiltiyTerminalStatePruning(true);
		Policy policy = planner.planFromState(initialState);
		EpisodeAnalysis episodeAnalysis = policy.evaluateBehavior(initialState, reward, terminalState);
		System.out.println("writing to file: " + outputPath);
		episodeAnalysis.writeToFile(outputPath);
		double discountedReturn = episodeAnalysis.getDiscountedReturn(0.99);
		System.out.println("return: " + discountedReturn);
	}
	
	
	private void runQL() {
		GamblerDomain gamblerDomain = new GamblerDomain(maxAmount, initialAmount, winProb);
		Domain domain = gamblerDomain.generateDomain();
		State initialState = gamblerDomain.createInitialState(domain);
		GamblerReward reward = new GamblerReward(maxAmount);
		GamblerTerminalState terminalState = new GamblerTerminalState(maxAmount);
		HashableStateFactory hashingFactory = new SimpleHashableStateFactory();

		QLearning agent = new QLearning(domain, 0.99, hashingFactory, 0, 1);
		agent.setRf(reward);
		agent.setTf(terminalState);
		agent.setMaximumEpisodesForPlanning(qlEpisodes);
		GreedyQPolicy p = agent.planFromState(initialState);
		System.out.println("ql: writing to path: " + outputPath);
		p.evaluateBehavior(initialState, reward, terminalState).writeToFile(outputPath);
		int nrEpisodes = agent.getIterations();
		System.out.println("nrEpisodes: " + nrEpisodes);
	}
	
}
