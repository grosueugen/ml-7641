package gambling;

import burlap.oomdp.auxiliary.DomainGenerator;
import burlap.oomdp.core.Attribute;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectClass;
import burlap.oomdp.core.objects.MutableObjectInstance;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.MutableState;
import burlap.oomdp.core.states.State;
import burlap.oomdp.core.Attribute.AttributeType;
import burlap.oomdp.singleagent.SADomain;
import burlap.oomdp.singleagent.explorer.TerminalExplorer;

public class GamblingDomain implements DomainGenerator {
	
	public static final String STATE_CURRENT_AMOUNT = "currentAmount";
	public static final String CLASS_AGENT = "agent";
	public static final String ACTION_BET = "bet";
	
	private int initialAmount;
	private int rounds;
	private double winProb;
	
	public GamblingDomain(int initialAmount, int rounds, double winProb) {
		this.initialAmount = initialAmount;
		this.rounds = rounds;
		this.winProb = winProb;
	}

	@Override
	public Domain generateDomain() {
		Domain domain = new SADomain();
		
		int maxAmount = (int) (Math.pow(2, rounds) * initialAmount);
		Attribute currentAmount = new Attribute(domain, STATE_CURRENT_AMOUNT, AttributeType.INT);
		currentAmount.setLims(0, maxAmount);
		
		ObjectClass agent = new ObjectClass(domain, CLASS_AGENT);
		agent.addAttribute(currentAmount);
		
		for (int i = 1; i <= maxAmount; i++) {
			new Bet(ACTION_BET, domain, winProb, i);
		}
		
		return domain;
	}
	
	public State createInitialState(Domain domain) {
		State s = new MutableState();
		ObjectInstance agent = new MutableObjectInstance(domain.getObjectClass(CLASS_AGENT), "agent0");
		agent.setValue(STATE_CURRENT_AMOUNT, initialAmount);
		s.addObject(agent);
		return s;
	}
	
	public static void main(String[] args) {
		GamblingDomain gamblingDomain = new GamblingDomain(100, 10, 0.6);
		Domain domain = gamblingDomain.generateDomain();
		State initialState = gamblingDomain.createInitialState(domain);
		TerminalExplorer terminal = new TerminalExplorer(domain, initialState);
		terminal.explore();
	}

}
