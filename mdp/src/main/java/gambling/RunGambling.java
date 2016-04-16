package gambling;

import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.planning.Planner;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.states.State;
import burlap.oomdp.statehashing.HashableStateFactory;
import burlap.oomdp.statehashing.SimpleHashableStateFactory;

public class RunGambling {
	
	public static void main(String[] args) {
		int initialAmount = 10;
		int rounds = 10;
		double winProb = 0.6;
		int goalAmount = new ExpectedValueCalculator(initialAmount, rounds, winProb).compute();
		
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
		String output = "D:\\projects\\ml-7641\\mdp\\src\\main\\resources\\output_" + System.currentTimeMillis();
		episodeAnalysis.writeToFile(output);
	}

}
