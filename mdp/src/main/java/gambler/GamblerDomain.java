package gambler;

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
	
	public static final String CLASS_AGENT = "agent";
	public static final String ACTION_BET = "bet";
	
	private final int maxAmount;
	private final int initialAmount;
	private final double winProb;
	
	public GamblerDomain(int maxAmount, int initialAmount, double winProb) {
		if (initialAmount < 1 || initialAmount > maxAmount) 
			throw new RuntimeException("initial amount must be > 1 and can not exceed max amount!");
		if (winProb < 0 || winProb > 1)
			throw new RuntimeException("probability must be within 0 and 1");
		this.maxAmount = maxAmount;
		this.initialAmount = initialAmount;
		this.winProb = winProb;
	}
	
	@Override
	public Domain generateDomain() {
		Domain domain = new SADomain();
		Attribute currentAmount = new Attribute(domain, STATE_CURRENT_AMOUNT, AttributeType.INT);
		currentAmount.setLims(0, maxAmount);
		
		ObjectClass agent = new ObjectClass(domain, CLASS_AGENT);
		agent.addAttribute(currentAmount);
		
		for (int i = 1; i <= maxAmount/2; i++) {
			new BetAction(ACTION_BET + "_" + i, domain, winProb, i, maxAmount);
		}
		
		return domain;
	}
	
	public State createInitialState(Domain domain) {
		State s = new MutableState();
		ObjectInstance agent = new MutableObjectInstance(domain.getObjectClass(CLASS_AGENT), "agenty0");
		agent.setValue(STATE_CURRENT_AMOUNT, initialAmount);
		s.addObject(agent);
		return s;
	}
	
	public int getMaxAmount() {
		return maxAmount;
	}
	public int getInitialAmount() {
		return initialAmount;
	}
	public double getWinProb() {
		return winProb;
	}
}
