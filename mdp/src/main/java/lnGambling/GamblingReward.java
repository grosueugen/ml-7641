package lnGambling;

import static lnGambling.GamblingDomain.*;

import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.RewardFunction;

/**
 * 
 * For intermediate states: 0 reward
 * For last state, if reached the goal amount: +100 otherwise -100.
 * @author Eugen
 *
 */
public class GamblingReward implements RewardFunction {
	
	private int rounds;
	private int goalAmount;
	
	public GamblingReward(int rounds, int goalAmount) {
		this.rounds = rounds;
		this.goalAmount = goalAmount;
	}
	
	@Override
	public double reward(State s, GroundedAction a, State sprime) {
		ObjectInstance agent = sprime.getFirstObjectOfClass(CLASS_AGENT);
		int currentIteration = agent.getIntValForAttribute(STATE_CURRENT_ITERATION);
		int currentAmount = agent.getIntValForAttribute(STATE_CURRENT_AMOUNT);
		if (currentIteration == rounds) {
			return Math.log(currentAmount);
		}
		return 0;
	}

}
