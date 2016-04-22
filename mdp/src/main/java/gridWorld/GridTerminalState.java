package gridWorld;

import static gridWorld.GridWorldDomain.ATTX;
import static gridWorld.GridWorldDomain.ATTY;
import static gridWorld.GridWorldDomain.CLASSAGENT;

import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;

public class GridTerminalState implements TerminalFunction {
	
	private GridWorldDomain domain;
	
	public GridTerminalState(GridWorldDomain domain) {
		this.domain = domain;
	}
	
	@Override
	public boolean isTerminal(State s) {
		return get(s).isEnd();
	}
	
	private Position get(State s) {
		ObjectInstance agent = s.getFirstObjectOfClass(CLASSAGENT);
		int ax = agent.getIntValForAttribute(ATTX);
		int ay = agent.getIntValForAttribute(ATTY);
		return domain.getPosition(ax, ay);
	}

}
