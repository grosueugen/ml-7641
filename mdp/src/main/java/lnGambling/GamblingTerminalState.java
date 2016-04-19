package lnGambling;

import static lnGambling.GamblingDomain.*;

import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;

public class GamblingTerminalState implements TerminalFunction {
	
	private int rounds;
	
	public GamblingTerminalState(int rounds) {
		this.rounds = rounds;
	}
	
	@Override
	public boolean isTerminal(State s) {
		ObjectInstance agent = s.getFirstObjectOfClass(CLASS_AGENT);
		int currentIteration = agent.getIntValForAttribute(STATE_CURRENT_ITERATION);
		int currentAmount = agent.getIntValForAttribute(STATE_CURRENT_AMOUNT);
		return (rounds == currentIteration || currentAmount <= 0);
	}

}
