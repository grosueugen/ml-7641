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
		GamblerDomain gamblerDomain = new GamblerDomain(10, 0.1);
		Domain domain = gamblerDomain.generateDomain();
		State initialState = gamblerDomain.createInitialState(domain);
		GamblerReward reward = new GamblerReward();
		GamblerTerminalState terminalState = new GamblerTerminalState();
		HashableStateFactory hashingFactory = new SimpleHashableStateFactory();
		
		ValueIteration planner = new ValueIteration(domain, reward, terminalState, 0.99, hashingFactory, 0.1, 100);
		planner.toggleReachabiltiyTerminalStatePruning(true);
		Policy policy = planner.planFromState(initialState);
		EpisodeAnalysis episodeAnalysis = policy.evaluateBehavior(initialState, reward, terminalState);
		String output = "C:\\Users\\Eugen\\Desktop\\mdp\\gambler_vi_output_" + System.currentTimeMillis();
		System.out.println("writing to file: " + output);
		episodeAnalysis.writeToFile(output);
	}

}
