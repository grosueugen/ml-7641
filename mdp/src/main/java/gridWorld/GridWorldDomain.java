package gridWorld;

import java.util.List;

import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.auxiliary.valuefunctionvis.ValueFunctionVisualizerGUI;
import burlap.behavior.valuefunction.ValueFunction;
import burlap.oomdp.auxiliary.DomainGenerator;
import burlap.oomdp.core.Attribute;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectClass;
import burlap.oomdp.core.objects.MutableObjectInstance;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.MutableState;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.SADomain;
import burlap.oomdp.singleagent.environment.SimulatedEnvironment;
import burlap.oomdp.singleagent.explorer.VisualExplorer;
import burlap.oomdp.visualizer.StateRenderLayer;
import burlap.oomdp.visualizer.Visualizer;

public class GridWorldDomain implements DomainGenerator {
	
	public static final String ATTX = "x";
	public static final String ATTY = "y";

	public static final String CLASSAGENT = "agent";

	public static final String ACTIONNORTH = "north";
	public static final String ACTIONSOUTH = "south";
	public static final String ACTIONEAST = "east";
	public static final String ACTIONWEST = "west";
	
	protected final Position[][] map;
	{
		// first column
		Position p00 = new Position(0,0);
		Position p01 = new Position(0,1);
		Position p02 = new Position(0,2);
		
		// second column
		Position p10 = new Position(1,0);
		Position p11 = new Position(1,1);
		Position p12 = new Position(1,2);
				
		// third column
		Position p20 = new Position(2,0);
		Position p21 = new Position(2,1);
		Position p22 = new Position(2,2);
		
		// fourth column
		Position p30 = new Position(3,0);
		Position p31 = new Position(3,1);
		Position p32 = new Position(3,2);	
		
		//walls, jumps etc
		p11.setWall(true);
		p30.setJump(p00);
		p32.setJump(p01);
		p31.setEnd(true);
		
		map = new Position[][] {
			{p00, p01, p02},
			{p10, p11, p12},
			{p20, p21, p22},
			{p30, p31, p32}
		};
	}
	
	private SADomain domain; 
	
	@Override
	public Domain generateDomain() {
		domain = new SADomain();

		Attribute xatt = new Attribute(domain, ATTX, Attribute.AttributeType.INT);
		xatt.setLims(0, 3);

		Attribute yatt = new Attribute(domain, ATTY, Attribute.AttributeType.INT);
		yatt.setLims(0, 2);

		ObjectClass agentClass = new ObjectClass(domain, CLASSAGENT);
		agentClass.addAttribute(xatt);
		agentClass.addAttribute(yatt);

		new Movement(ACTIONNORTH, this, 0);
		new Movement(ACTIONSOUTH, this, 1);
		new Movement(ACTIONEAST, this, 2);
		new Movement(ACTIONWEST, this, 3);

		return domain;
	}
	
	public SADomain getDomain() {
		return domain;
	}
	
	public StateRenderLayer getStateRenderLayer(){
		StateRenderLayer rl = new StateRenderLayer();
		rl.addStaticPainter(new WallPainter(this));
		rl.addObjectClassPainter(CLASSAGENT, new AgentPainter(this));
		return rl;
	}

	public Visualizer getVisualizer(){
		return new Visualizer(this.getStateRenderLayer());
	}

	public Position getPosition(int x, int y) {
		return map[x][y];
	}
	
	public State getInitialState(){
		State s = new MutableState();
		ObjectInstance agent = new MutableObjectInstance(domain.getObjectClass(CLASSAGENT), "agent-grid-0");
		agent.setValue(ATTX, 0);
		agent.setValue(ATTY, 0);
		s.addObject(agent);
		return s;
	}
	
	/**
	 * Creates and returns a {@link burlap.behavior.singleagent.auxiliary.valuefunctionvis.ValueFunctionVisualizerGUI}
	 * object for a grid world. The value of states
	 * will be represented by colored cells from red (lowest value) to blue (highest value). North-south-east-west
	 * actions will be rendered with arrows using {@link burlap.behavior.singleagent.auxiliary.valuefunctionvis.common.ArrowActionGlyph}
	 * objects. The GUI will not be launched by default; call the
	 * {@link burlap.behavior.singleagent.auxiliary.valuefunctionvis.ValueFunctionVisualizerGUI#initGUI()}
	 * on the returned object to start it.
	 * @param states the states whose value should be rendered.
	 * @param valueFunction the value Function that can return the state values.
	 * @param p the policy to render
	 * @return a gridworld-based {@link burlap.behavior.singleagent.auxiliary.valuefunctionvis.ValueFunctionVisualizerGUI} object.
	 */
	public static ValueFunctionVisualizerGUI getGridWorldValueFunctionVisualization(List <State> states, ValueFunction valueFunction, Policy p){
		return ValueFunctionVisualizerGUI.createGridWorldBasedValueFunctionVisualizerGUI(states, valueFunction, p,
				CLASSAGENT, ATTX, ATTY,
				ACTIONNORTH, ACTIONSOUTH, ACTIONEAST, ACTIONWEST);
	}
	
	public static void main(String[] args) {
		GridWorldDomain gridWorld = new GridWorldDomain();
		Domain domain = gridWorld.generateDomain();
		State initialState = gridWorld.getInitialState();
		
		GridReward reward = new GridReward(gridWorld);
		GridTerminalState terminalState = new GridTerminalState(gridWorld);
		
		SimulatedEnvironment env = new SimulatedEnvironment(domain, reward, terminalState, initialState);

		//TerminalExplorer exp = new TerminalExplorer(domain, env);
		//exp.explore();

		Visualizer v = gridWorld.getVisualizer();
		VisualExplorer exp = new VisualExplorer(domain, env, v);

		exp.addKeyAction("w", ACTIONNORTH);
		exp.addKeyAction("s", ACTIONSOUTH);
		exp.addKeyAction("d", ACTIONEAST);
		exp.addKeyAction("a", ACTIONWEST);

		exp.initGUI();
	}

}
