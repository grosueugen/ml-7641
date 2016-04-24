package gambler;

import static gambler.GamblerDomain.CLASS_AGENT;
import static gambler.GamblerDomain.STATE_CURRENT_AMOUNT;

import java.util.ArrayList;
import java.util.List;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.TransitionProbability;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.FullActionModel;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.common.SimpleAction;

public class BetAction extends SimpleAction implements FullActionModel {

	private double winProb;
	private int betAmount;
	private int maxAmount;
	
	public BetAction(String actionName, Domain domain, double winProb, int betAmount, int maxAmount) {
		super(actionName, domain);
		this.winProb = winProb;
		this.betAmount = betAmount;
		this.maxAmount = maxAmount;
	}
	
	@Override
	public boolean applicableInState(State s, GroundedAction groundedAction) {
		ObjectInstance agent = s.getFirstObjectOfClass(CLASS_AGENT);
		int currentAmount = agent.getIntValForAttribute(STATE_CURRENT_AMOUNT);
		if (currentAmount == 0) throw new IllegalStateException("current amount = 0! end game!");
		if (currentAmount <= maxAmount/2) {
			// currentAmount = 30
			return (betAmount <= currentAmount);
		} else {
			//currentAmount = 70 -> max bet allowed=30
			return (betAmount <= (maxAmount - currentAmount));
		}
	}
	
	@Override
	protected State performActionHelper(State s, GroundedAction groundedAction) {
		ObjectInstance agent = s.getFirstObjectOfClass(CLASS_AGENT);
		int currentAmount = agent.getIntValForAttribute(STATE_CURRENT_AMOUNT);
		int newAmount = playOneRound(currentAmount);
		agent.setValue(STATE_CURRENT_AMOUNT, newAmount);
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
		
		State ns1 = s.copy();
		ObjectInstance nagent1 = ns1.getFirstObjectOfClass(CLASS_AGENT);
		nagent1.setValue(STATE_CURRENT_AMOUNT, currentAmount+betAmount);
		TransitionProbability win = new TransitionProbability(ns1, winProb);
		tp.add(win);

		State ns2 = s.copy();
		ObjectInstance nagent2 = ns2.getFirstObjectOfClass(CLASS_AGENT);
		nagent2.setValue(STATE_CURRENT_AMOUNT, currentAmount-betAmount);
		TransitionProbability lose = new TransitionProbability(ns2, (1-winProb));
		tp.add(lose);
		return tp;
	}
	
	public int getBetAmount() {
		return betAmount;
	}
	
}
