package gambler.stats;

import static gambler.GamblerDomain.CLASS_AGENT;
import static gambler.GamblerDomain.STATE_CURRENT_AMOUNT;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.planning.stochastic.policyiteration.PolicyIteration;
import burlap.oomdp.core.AbstractGroundedAction;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.common.SimpleGroundedAction;
import burlap.oomdp.statehashing.HashableStateFactory;
import burlap.oomdp.statehashing.SimpleHashableStateFactory;
import gambler.BetAction;
import gambler.GamblerDomain;
import gambler.GamblerReward;
import gambler.GamblerTerminalState;

public class GamblerPolicyIteration {
	
	private int startMaxAmount;
	private int startInitialAmount;
	private double winProb;
	
	private int nrRepeats;
	
	private int stepSize;
	private int stepIterations;
	
	public GamblerPolicyIteration(int startMaxAmount, int startInitialAmount, double winProb) {
		this.startMaxAmount = startMaxAmount;
		this.startInitialAmount = startInitialAmount;
		this.winProb = winProb;
	}
	
	public void setRunParameters(int stepSize, int stepIterations, int nrRepeats) {
		this.stepSize = stepSize;
		this.stepIterations = stepIterations;
		this.nrRepeats = nrRepeats;
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
			
			PolicyIteration planner = new PolicyIteration(domain, reward, terminalState, 0.99, hashingFactory, 0, 100, 100);
			long now = System.currentTimeMillis();
			Policy policy = planner.planFromState(initialState);
			long executionTimeMs = (System.currentTimeMillis() - now);
			int totalPolicyIterations = planner.getTotalPolicyIterations();
			List<GamblerStateOutput> stateOutputs = getStateOutputs(maxAmount, planner, policy);
			
			int wins = 0;
			for (int j = 1; j <= nrRepeats; j++) {
				EpisodeAnalysis episodeAnalysis = policy.evaluateBehavior(initialState, reward, terminalState);
				double discountedReturn = episodeAnalysis.getDiscountedReturn(0.99);
				if (discountedReturn != 0) wins++;
			}
			
			GamblerOutput output = new GamblerOutput(executionTimeMs, (double) wins/nrRepeats, totalPolicyIterations);
			output.addStateOutputs(stateOutputs);
			res.put(input, output);
			
			maxAmount += stepSize;
			initialAmount = maxAmount/divBy;
		}
		return res;
	}

	private List<GamblerStateOutput> getStateOutputs(int maxAmount, PolicyIteration planner, Policy policy) {
		List<GamblerStateOutput> stateOutputs = new ArrayList<>();
		List<State> allStates = planner.getAllStates();
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
			double stateValue = planner.value(s);
			GamblerStateOutput stateOuput = new GamblerStateOutput(currentAmount, betAmount, stateValue);
			stateOutputs.add(stateOuput);
		}
		return stateOutputs;
	}
}
