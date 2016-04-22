package gridWorld;

import static gridWorld.GridWorldDomain.*;

import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.RewardFunction;

public class GridReward implements RewardFunction {
	
	private GridWorldDomain domain;
	
	private GridReward(GridWorldDomain domain) {
		this.domain = domain;
	}
	
	//if move forward: -1
	//if same position (bump into wall/board): -5
	//if jump position: return -10
	//if final state: return 100
	@Override
	public double reward(State s, GroundedAction a, State sprime) {
		Position newPosition = get(sprime);
		Position oldPosition = get(s);
		if (newPosition.equals(oldPosition)) return -5;//worse because it did no progress towards goal
		if (oldPosition.isJump()) return -10;
		if (newPosition.isEnd()) return 100;
		return -1;
	}
	
	private Position get(State s) {
		ObjectInstance agent = s.getFirstObjectOfClass(CLASSAGENT);
		int ax = agent.getIntValForAttribute(ATTX);
		int ay = agent.getIntValForAttribute(ATTY);
		return domain.getPosition(ax, ay);
	}

}
