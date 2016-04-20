package rlGambling;

import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.planning.stochastic.policyiteration.PolicyIteration;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.states.State;
import burlap.oomdp.statehashing.HashableStateFactory;
import burlap.oomdp.statehashing.SimpleHashableStateFactory;

public class RunPolicyIterationGambler {
	
	public static void main(String[] args) {
		GamblerDomain gamblerDomain = new GamblerDomain(51, 0.5);
		Domain domain = gamblerDomain.generateDomain();
		State initialState = gamblerDomain.createInitialState(domain);
		GamblerReward reward = new GamblerReward();
		GamblerTerminalState terminalState = new GamblerTerminalState();
		HashableStateFactory hashingFactory = new SimpleHashableStateFactory();
		
		PolicyIteration planner = new PolicyIteration(domain, reward, terminalState, 1, hashingFactory, 1, 100, 100);
		
		Policy policy = planner.planFromState(initialState);
		EpisodeAnalysis episodeAnalysis = policy.evaluateBehavior(initialState, reward, terminalState);
		String output = "C:\\Users\\Eugen\\Desktop\\mdp\\gambler_pi_output_" + System.currentTimeMillis();
		System.out.println("writing to file: " + output);
		episodeAnalysis.writeToFile(output);
	}

}
