package rlGambling;

import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.states.State;
import burlap.oomdp.statehashing.HashableStateFactory;
import burlap.oomdp.statehashing.SimpleHashableStateFactory;

public class RunValueIterationGambler {
	
	public static void main(String[] args) {
		if (args.length != 3) 
			throw new IllegalArgumentException("3 params are required: max amount, initial amount, win probability");
		int maxAmount = Integer.parseInt(args[0]);
		int initialAmount = Integer.parseInt(args[1]);
		double winProb = Double.parseDouble(args[2]);
		
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

}
