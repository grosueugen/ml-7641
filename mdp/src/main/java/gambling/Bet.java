package gambling;

import static gambling.GamblingDomain.CLASS_AGENT;
import static gambling.GamblingDomain.STATE_CURRENT_AMOUNT;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.common.SimpleAction;

public class Bet extends SimpleAction {
	
	private double winProb;
	private int betAmount;
	
	public Bet(String actionName, Domain domain, double winProb, int betAmount) {
		super(actionName, domain);
		this.winProb = winProb;
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

}
