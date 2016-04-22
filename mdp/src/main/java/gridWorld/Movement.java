package gridWorld;

import static gridWorld.GridWorldDomain.*;

import java.util.ArrayList;
import java.util.List;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.TransitionProbability;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.FullActionModel;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.common.SimpleAction;

public class Movement extends SimpleAction implements FullActionModel {
	
	//0: north; 1: south; 2:east; 3: west
	protected double [] directionProbs = new double[4];
	
	private int[][] map;
	
	private GridWorldDomain gwDomain;

	public Movement(String actionName, GridWorldDomain gwDomain, int direction){
		super(actionName, gwDomain.getDomain());
		this.map = gwDomain.map;
		for(int i = 0; i < 4; i++){
			if(i == direction){
				directionProbs[i] = 0.8;
			}
			else{
				directionProbs[i] = 0.2/3.;
			}
		}
	}

	@Override
	protected State performActionHelper(State s, GroundedAction groundedAction) {
		//get agent and current position
		ObjectInstance agent = s.getFirstObjectOfClass(CLASSAGENT);
		int curX = agent.getIntValForAttribute(ATTX);
		int curY = agent.getIntValForAttribute(ATTY);
		
		Position position = gwDomain.getPosition(curX, curY);
		if (position.isJump()) {
			Position jump = position.getJump();
			agent.setValue(ATTX, jump.getX());
			agent.setValue(ATTY, jump.getY());
			return s;
		}

		//sample directon with random roll
		double r = Math.random();
		double sumProb = 0.;
		int dir = 0;
		for(int i = 0; i < this.directionProbs.length; i++){
			sumProb += this.directionProbs[i];
			if(r < sumProb){
				dir = i;
				break; //found direction
			}
		}

		//get resulting position
		int [] newPos = this.moveResult(curX, curY, dir);
		//jump

		//set the new position
		agent.setValue(ATTX, newPos[0]);
		agent.setValue(ATTY, newPos[1]);

		//return the state we just modified
		return s;
	}

	@Override
	public List<TransitionProbability> getTransitions(State s, GroundedAction groundedAction) {
		//get agent and current position
		ObjectInstance agent = s.getFirstObjectOfClass(CLASSAGENT);
		int curX = agent.getIntValForAttribute(ATTX);
		int curY = agent.getIntValForAttribute(ATTY);

		List<TransitionProbability> tps = new ArrayList<TransitionProbability>(4);
		TransitionProbability noChangeTransition = null;
		for(int i = 0; i < this.directionProbs.length; i++){
			int [] newPos = this.moveResult(curX, curY, i);
			if(newPos[0] != curX || newPos[1] != curY){
				//new possible outcome
				State ns = s.copy();
				ObjectInstance nagent = ns.getFirstObjectOfClass(CLASSAGENT);
				nagent.setValue(ATTX, newPos[0]);
				nagent.setValue(ATTY, newPos[1]);

				//create transition probability object and add to our list of outcomes
				tps.add(new TransitionProbability(ns, this.directionProbs[i]));
			}
			else{
				//this direction didn't lead anywhere new
				//if there are existing possible directions
				//that wouldn't lead anywhere, aggregate with them
				if(noChangeTransition != null){
					noChangeTransition.p += this.directionProbs[i];
				}
				else{
					//otherwise create this new state and transition
					noChangeTransition = new TransitionProbability(s.copy(),
							this.directionProbs[i]);
					tps.add(noChangeTransition);
				}
			}
		}


		return tps;
	}

	protected int [] moveResult(int curX, int curY, int direction){

		//first get change in x and y from direction using 0: north; 1: south; 2:east; 3: west
		int xdelta = 0;
		int ydelta = 0;
		if(direction == 0){
			ydelta = 1;
		}
		else if(direction == 1){
			ydelta = -1;
		}
		else if(direction == 2){
			xdelta = 1;
		}
		else{
			xdelta = -1;
		}

		int nx = curX + xdelta;
		int ny = curY + ydelta;

		int width = map.length;
		int height = map[0].length;

		//make sure new position is valid (not a wall or off bounds)
		if(nx < 0 || nx >= width || ny < 0 || ny >= height ||
				map[nx][ny] == 1){
			nx = curX;
			ny = curY;
		}


		return new int[]{nx,ny};

	}
}
