package rlGambling.pi;

import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.planning.stochastic.policyiteration.PolicyIteration;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.states.State;
import burlap.oomdp.statehashing.HashableStateFactory;
import burlap.oomdp.statehashing.SimpleHashableStateFactory;
import rlGambling.GamblerDomain;
import rlGambling.GamblerReward;
import rlGambling.GamblerTerminalState;

public class RunPolicyIterationGambler {
	
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
		
		PolicyIteration planner = new PolicyIteration(domain, reward, terminalState, 1, hashingFactory, 1, 100, 100);
		
		Policy policy = planner.planFromState(initialState);
		EpisodeAnalysis episodeAnalysis = policy.evaluateBehavior(initialState, reward, terminalState);
		String output = "C:\\Users\\Eugen\\Desktop\\mdp\\gambler_pi_output_" + System.currentTimeMillis();
		System.out.println("writing to file: " + output);
		episodeAnalysis.writeToFile(output);
	}

}
