package rlGambling;

import java.io.File;

import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.behavior.singleagent.planning.stochastic.policyiteration.PolicyIteration;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.environment.Environment;
import burlap.oomdp.singleagent.environment.SimulatedEnvironment;
import burlap.oomdp.statehashing.HashableStateFactory;
import burlap.oomdp.statehashing.SimpleHashableStateFactory;

public class RunGambler {
	private String alg;
	private int maxAmount;
	private int initialAmount;
	private double winProb;
	
	public RunGambler(String alg, int maxAmount, int initialAmount, double winProb) {
		this.alg = alg;
		this.maxAmount = maxAmount;
		this.initialAmount = initialAmount;
		this.winProb = winProb;
	}

	public static void main(String[] args) {
		if (args.length != 4) 
			throw new IllegalArgumentException("3 params are required: alg (pi,vi,ql), max amount, initial amount, win probability");
		String alg = args[0];
		int maxAmount = Integer.parseInt(args[1]);
		int initialAmount = Integer.parseInt(args[2]);
		double winProb = Double.parseDouble(args[3]);
		if (!alg.equalsIgnoreCase("pi") && !alg.equalsIgnoreCase("vi") && !alg.equalsIgnoreCase("ql")) {
			throw new IllegalArgumentException("alg must be one of pi, vi, ql");
		}
		
		RunGambler rg = new RunGambler(alg, maxAmount, initialAmount, winProb);
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
		
		PolicyIteration planner = new PolicyIteration(domain, reward, terminalState, 1, hashingFactory, 1, 100, 100);
		
		Policy policy = planner.planFromState(initialState);
		EpisodeAnalysis episodeAnalysis = policy.evaluateBehavior(initialState, reward, terminalState);
		String output = "C:\\Users\\Eugen\\Desktop\\mdp\\gambler_pi_output_" + System.currentTimeMillis();
		System.out.println("writing to file: " + output);
		episodeAnalysis.writeToFile(output);
	}

	private void runVI() {
		GamblerDomain gamblerDomain = new GamblerDomain(maxAmount, initialAmount, winProb);
		Domain domain = gamblerDomain.generateDomain();
		State initialState = gamblerDomain.createInitialState(domain);
		GamblerReward reward = new GamblerReward(maxAmount);
		GamblerTerminalState terminalState = new GamblerTerminalState(maxAmount);
		HashableStateFactory hashingFactory = new SimpleHashableStateFactory();
		
		ValueIteration planner = new ValueIteration(domain, reward, terminalState, 1, hashingFactory, 0.1, 100);
		planner.toggleReachabiltiyTerminalStatePruning(true);
		Policy policy = planner.planFromState(initialState);
		EpisodeAnalysis episodeAnalysis = policy.evaluateBehavior(initialState, reward, terminalState);
		String output = "C:\\Users\\Eugen\\Desktop\\mdp\\gambler_vi_output_" + System.currentTimeMillis();
		System.out.println("writing to file: " + output);
		episodeAnalysis.writeToFile(output);
	}

	//TODO: replace it!
	private void runQL() {
		GamblerDomain gamblerDomain = new GamblerDomain(maxAmount, initialAmount, winProb);
		Domain domain = gamblerDomain.generateDomain();
		State initialState = gamblerDomain.createInitialState(domain);
		GamblerReward reward = new GamblerReward(maxAmount);
		GamblerTerminalState terminalState = new GamblerTerminalState(maxAmount);
		HashableStateFactory hashingFactory = new SimpleHashableStateFactory();
		QLearning q = new QLearning(domain, 1, hashingFactory, 0, 1);
		
		int nrEpisodes = 50;
		
		long now = System.currentTimeMillis();
		String output = "C:\\Users\\Eugen\\Desktop\\mdp\\gambler_q_output_" + now;
		File dir = new File(output);
		boolean b = dir.mkdir();
		if (!b) throw new RuntimeException("Can not create folder " + output);
		
		Environment env = new SimulatedEnvironment(domain, reward, terminalState, initialState);
		
		for (int i = 1; i <= nrEpisodes; i++) {
			EpisodeAnalysis ea = q.runLearningEpisode(env);
			
			String file = output + "\\res_" + i;
			System.out.println("writing to file: " + file);
			ea.writeToFile(file);
			
			env.resetEnvironment();
		}
	}
	
	

}
