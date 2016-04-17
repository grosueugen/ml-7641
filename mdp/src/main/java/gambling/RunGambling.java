package gambling;

import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.states.State;
import burlap.oomdp.statehashing.HashableStateFactory;
import burlap.oomdp.statehashing.SimpleHashableStateFactory;

public class RunGambling {
	
	public static void main(String[] args) {
		int initialAmount = 50;
		int rounds = 5;
		double winProb = 0.6;
		int goalAmount = new ExpectedValueCalculator(initialAmount, rounds, winProb).compute();
		System.out.println("goalAmount = " + goalAmount);
		
		GamblingDomain gamblingDomain = new GamblingDomain(initialAmount, rounds, winProb);
		Domain domain = gamblingDomain.generateDomain();
		State initialState = gamblingDomain.createInitialState(domain);
		GamblingReward reward = new GamblingReward(rounds, goalAmount);
		GamblingTerminalState terminalState = new GamblingTerminalState(rounds);
		
		HashableStateFactory hashingFactory = new SimpleHashableStateFactory();
		ValueIteration planner = new ValueIteration(domain, reward, terminalState, 0.99, hashingFactory, 1, 100);
		planner.toggleReachabiltiyTerminalStatePruning(true);
		Policy policy = planner.planFromState(initialState);
		EpisodeAnalysis episodeAnalysis = policy.evaluateBehavior(initialState, reward, terminalState);
		String output = "C:\\Users\\Eugen\\Desktop\\mdp\\output_" + System.currentTimeMillis();
		episodeAnalysis.writeToFile(output);
	}

}
