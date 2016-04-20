package rlGambling;

import static rlGambling.GamblerDomain.*;
import static rlGambling.GamblerDomain.STATE_CURRENT_AMOUNT;

import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;

public class GamblerTerminalState implements TerminalFunction {
	
	@Override
	public boolean isTerminal(State s) {
		ObjectInstance agent = s.getFirstObjectOfClass(CLASS_AGENT);
		int currentAmount = agent.getIntValForAttribute(STATE_CURRENT_AMOUNT);
		return (currentAmount == 0 || currentAmount == MAX_AMOUNT);
	}

}
