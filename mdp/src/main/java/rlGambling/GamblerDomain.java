package rlGambling;

import burlap.oomdp.auxiliary.DomainGenerator;
import burlap.oomdp.core.Attribute;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectClass;
import burlap.oomdp.core.Attribute.AttributeType;
import burlap.oomdp.core.objects.MutableObjectInstance;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.MutableState;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.SADomain;

public class GamblerDomain implements DomainGenerator {

	public static final String STATE_CURRENT_AMOUNT = "currentAmount";
	public static final String STATE_CURRENT_ITERATION = "currentIteration";
	
	public static final String CLASS_AGENT = "agent";
	public static final String ACTION_BET = "bet";
	
	public static final int MAX_AMOUNT = 100;
	
	private int initialAmount;
	private double winProb;
	
	public GamblerDomain(int initialAmount, double winProb) {
		if (initialAmount < 1 || initialAmount > MAX_AMOUNT) 
			throw new RuntimeException("initial amount must be within $1 and $99");
		if (winProb < 0 || winProb > 1)
			throw new RuntimeException("probability must be within 0 and 1");
		this.initialAmount = initialAmount;
		this.winProb = winProb;
	}
	
	@Override
	public Domain generateDomain() {
		Domain domain = new SADomain();
		Attribute currentAmount = new Attribute(domain, STATE_CURRENT_AMOUNT, AttributeType.INT);
		currentAmount.setLims(0, MAX_AMOUNT);
		Attribute currentIteration = new Attribute(domain, STATE_CURRENT_ITERATION, AttributeType.INT);
		currentIteration.setLims(0, 100);
		
		ObjectClass agent = new ObjectClass(domain, CLASS_AGENT);
		agent.addAttribute(currentAmount);
		agent.addAttribute(currentIteration);
		
		for (int i = 1; i <= MAX_AMOUNT/2; i++) {
			new BetAction(ACTION_BET + "_" + i, domain, winProb, i);
		}
		
		return domain;
	}
	
	public State createInitialState(Domain domain) {
		State s = new MutableState();
		ObjectInstance agent = new MutableObjectInstance(domain.getObjectClass(CLASS_AGENT), "agenty0");
		agent.setValue(STATE_CURRENT_AMOUNT, initialAmount);
		agent.setValue(STATE_CURRENT_ITERATION, 0);
		s.addObject(agent);
		return s;
	}
}
