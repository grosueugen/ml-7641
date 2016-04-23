package rlGambling;

import static rlGambling.GamblerDomain.*;

import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.RewardFunction;

public class GamblerReward implements RewardFunction {
	
	private int maxAmount;
	
	public GamblerReward(int maxAmount) {
		this.maxAmount = maxAmount;
	}
	
	@Override
	public double reward(State s, GroundedAction a, State sprime) {
		//is sprime=100 a state? according to book, it is not!
		ObjectInstance agent = sprime.getFirstObjectOfClass(CLASS_AGENT);
		int currentAmount = agent.getIntValForAttribute(STATE_CURRENT_AMOUNT);
		if (currentAmount == maxAmount) {
			return 1;
		}
		return 0;
	}

}
