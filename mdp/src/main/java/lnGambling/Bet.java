package lnGambling;

import static lnGambling.GamblingDomain.*;

import java.util.ArrayList;
import java.util.List;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.TransitionProbability;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.FullActionModel;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.common.SimpleAction;

public class Bet extends SimpleAction implements FullActionModel {
	
	private double winProb;
	private int betAmount;
	
	public Bet(String actionName, Domain domain, double winProb, int betAmount) {
		super(actionName, domain);
		this.winProb = winProb;
		this.betAmount = betAmount;
	}
	
	@Override
	public boolean applicableInState(State s, GroundedAction groundedAction) {
		ObjectInstance agent = s.getFirstObjectOfClass(CLASS_AGENT);
		int currentAmount = agent.getIntValForAttribute(STATE_CURRENT_AMOUNT);
		return (betAmount <= currentAmount);
	}

	@Override
	protected State performActionHelper(State s, GroundedAction groundedAction) {
		ObjectInstance agent = s.getFirstObjectOfClass(CLASS_AGENT);
		int currentAmount = agent.getIntValForAttribute(STATE_CURRENT_AMOUNT);
		int currentIteration = agent.getIntValForAttribute(STATE_CURRENT_ITERATION);
		int newAmount = playOneRound(currentAmount);
		agent.setValue(STATE_CURRENT_AMOUNT, newAmount);
		agent.setValue(STATE_CURRENT_ITERATION, ++currentIteration);
		return s;
	}
	
	private int playOneRound(int currentAmount) {
		double r = Math.random();
		boolean win = (r >= (1 - winProb));
		if (win) {
			currentAmount += betAmount;
		} else {
			currentAmount -= betAmount;
		}
		return currentAmount;
	}
	
	/**
	 * Probability of going to the next state according to the result of bet, is 1.
	 * //win with prob winProb => next states are currentAmount + betAmount
	 * //lose 1-winProb -> next states are currentAmount - betAmount
	 */
	@Override
	public List<TransitionProbability> getTransitions(State s, GroundedAction groundedAction) {
		List<TransitionProbability> tp = new ArrayList<>();
		ObjectInstance agent = s.getFirstObjectOfClass(CLASS_AGENT);
		int currentAmount = agent.getIntValForAttribute(STATE_CURRENT_AMOUNT);
		int currentIteration = agent.getIntValForAttribute(STATE_CURRENT_ITERATION);
		int newIteration = currentIteration + 1;
		
		State ns1 = s.copy();
		ObjectInstance nagent1 = ns1.getFirstObjectOfClass(CLASS_AGENT);
		nagent1.setValue(STATE_CURRENT_AMOUNT, currentAmount+betAmount);
		nagent1.setValue(STATE_CURRENT_ITERATION, newIteration);
		TransitionProbability win = new TransitionProbability(ns1, winProb);
		tp.add(win);

		State ns2 = s.copy();
		ObjectInstance nagent2 = ns2.getFirstObjectOfClass(CLASS_AGENT);
		nagent2.setValue(STATE_CURRENT_AMOUNT, currentAmount-betAmount);
		nagent2.setValue(STATE_CURRENT_ITERATION, newIteration);
		TransitionProbability lose = new TransitionProbability(ns2, (1-winProb));
		tp.add(lose);
		return tp;
	}

}
