package gambler.stats;

import static gambler.GamblerDomain.CLASS_AGENT;
import static gambler.GamblerDomain.STATE_CURRENT_AMOUNT;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import burlap.behavior.policy.GreedyQPolicy;
import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.oomdp.core.AbstractGroundedAction;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.common.SimpleGroundedAction;
import burlap.oomdp.statehashing.HashableState;
import burlap.oomdp.statehashing.HashableStateFactory;
import burlap.oomdp.statehashing.SimpleHashableStateFactory;
import gambler.BetAction;
import gambler.GamblerDomain;
import gambler.GamblerReward;
import gambler.GamblerTerminalState;

public class GamblerQLearning {
	
	private int startMaxAmount;
	private int startInitialAmount;
	private double winProb;
	
	private int nrRepeats;
	
	private int stepSize;
	private int stepIterations;
	
	private int maxEpisodes = 50;
	
	public GamblerQLearning(int startMaxAmount, int startInitialAmount, double winProb) {
		this.startMaxAmount = startMaxAmount;
		this.startInitialAmount = startInitialAmount;
		this.winProb = winProb;
	}
	
	public void setRunParameters(int stepSize, int stepIterations, int nrRepeats) {
		this.stepSize = stepSize;
		this.stepIterations = stepIterations;
		this.nrRepeats = nrRepeats;
	}
	
	public void setMaxEpisodes(int maxEpisodes) {
		this.maxEpisodes = maxEpisodes;
	}
	
	public Map<GamblerInput, GamblerOutput> execute() {
		Map<GamblerInput, GamblerOutput> res = new LinkedHashMap<>();
		int maxAmount = startMaxAmount;
		int initialAmount = startInitialAmount;
		int divBy = maxAmount/initialAmount;
		
		for (int i = 1; i <= stepIterations; i++) {
			GamblerInput input = new GamblerInput(maxAmount, initialAmount, winProb);
			
			GamblerDomain gamblerDomain = new GamblerDomain(maxAmount, initialAmount, winProb);
			Domain domain = gamblerDomain.generateDomain();
			State initialState = gamblerDomain.createInitialState(domain);
			GamblerReward reward = new GamblerReward(maxAmount);
			GamblerTerminalState terminalState = new GamblerTerminalState(maxAmount);
			HashableStateFactory hashingFactory = new SimpleHashableStateFactory();

			QLearning agent = new QLearning(domain, 0.99, hashingFactory, 0, 1);
			agent.setRf(reward);
			agent.setTf(terminalState);
			agent.setMaximumEpisodesForPlanning(maxEpisodes);
			
			long now = System.currentTimeMillis();
			GreedyQPolicy policy = agent.planFromState(initialState);
			long executionTimeMs = (System.currentTimeMillis() - now);
			int totalQLearningIterations = agent.getIterations();
			List<GamblerStateOutput> stateOutputs = getStateOutputs(maxAmount, agent, policy);
			
			int wins = 0;
			for (int j = 1; j <= nrRepeats; j++) {
				EpisodeAnalysis episodeAnalysis = policy.evaluateBehavior(initialState, reward, terminalState);
				double discountedReturn = episodeAnalysis.getDiscountedReturn(0.99);
				if (discountedReturn != 0) wins++;
			}
			
			GamblerOutput output = new GamblerOutput(executionTimeMs, (double) wins/nrRepeats, totalQLearningIterations);
			output.addStateOutputs(stateOutputs);
			res.put(input, output);
			
			maxAmount += stepSize;
			initialAmount = maxAmount/divBy;
		}
		return res;
	}

	private List<GamblerStateOutput> getStateOutputs(int maxAmount, QLearning learner, Policy policy) {
		List<GamblerStateOutput> stateOutputs = new ArrayList<>();
		Set<HashableState> allStates = learner.getAllStates();
		for (State s : allStates) {
			ObjectInstance agent = s.getFirstObjectOfClass(CLASS_AGENT);
			int currentAmount = agent.getIntValForAttribute(STATE_CURRENT_AMOUNT);
			if (currentAmount == 0 || currentAmount == maxAmount) {
				continue;
			}
			
			AbstractGroundedAction action = policy.getAction(s);
			SimpleGroundedAction simpleAction = (SimpleGroundedAction) action;
			BetAction betAction = (BetAction) simpleAction.action;
			int betAmount = betAction.getBetAmount();
			double stateValue = learner.value(s);
			GamblerStateOutput stateOuput = new GamblerStateOutput(currentAmount, betAmount, stateValue);
			stateOutputs.add(stateOuput);
		}
		return stateOutputs;
	}

}
