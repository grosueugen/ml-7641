package rlGambling.pi;

import static rlGambling.GamblerDomain.CLASS_AGENT;
import static rlGambling.GamblerDomain.STATE_CURRENT_AMOUNT;

import java.util.ArrayList;
import java.util.List;

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
import rlGambling.BetAction;
import rlGambling.GamblerDomain;
import rlGambling.GamblerReward;
import rlGambling.GamblerTerminalState;

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
	
	public PolicyIterationResults execute() {
		PolicyIterationResults res = new PolicyIterationResults(nrRepeats);
		int maxAmount = startMaxAmount;
		int initialAmount = startInitialAmount;
		int divBy = maxAmount/initialAmount;
		
		for (int i = 1; i <= stepIterations; i++) {
			for (int j = 1; j <= nrRepeats; j++) {
				PolicyIterationInput input = new PolicyIterationInput(maxAmount, initialAmount, winProb);
				PolicyIterationOutput output = executeInternal(input);
				res.add(input, output);
			}
			maxAmount += stepSize;
			initialAmount = maxAmount/divBy;
		}
		return res;
	}

	private PolicyIterationOutput executeInternal(PolicyIterationInput input) {
		int maxAmount = input.getMaxAmount();
		int initialAmount = input.getInitialAmount();
		double winProb = input.getWinProb();
		
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
		
		EpisodeAnalysis episodeAnalysis = policy.evaluateBehavior(initialState, reward, terminalState);
		double discountedReturn = episodeAnalysis.getDiscountedReturn(0.99);
		
		List<PolicyIterationStateOutput> stateOutputs = getStateOutputs(maxAmount, planner, policy);

		PolicyIterationOutput output = new PolicyIterationOutput(executionTimeMs, discountedReturn, totalPolicyIterations);
		output.addStateOutputs(stateOutputs);
		return output;
	}

	private List<PolicyIterationStateOutput> getStateOutputs(int maxAmount, PolicyIteration planner, Policy policy) {
		List<PolicyIterationStateOutput> stateOutputs = new ArrayList<>();
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
			PolicyIterationStateOutput stateOuput = new PolicyIterationStateOutput(currentAmount, betAmount, stateValue);
			stateOutputs.add(stateOuput);
		}
		return stateOutputs;
	}
}
