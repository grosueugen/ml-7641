package gambler;

import static gambler.GamblerDomain.*;

import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;

public class GamblerTerminalState implements TerminalFunction {
	
	private int maxAmount;
	
	public GamblerTerminalState(int maxAmount) {
		this.maxAmount = maxAmount;
	}
	
	@Override
	public boolean isTerminal(State s) {
		ObjectInstance agent = s.getFirstObjectOfClass(CLASS_AGENT);
		int currentAmount = agent.getIntValForAttribute(STATE_CURRENT_AMOUNT);
		return (currentAmount == 0 || currentAmount == maxAmount);
	}

}
