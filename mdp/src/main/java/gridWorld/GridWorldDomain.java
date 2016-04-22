package gridWorld;

import burlap.oomdp.auxiliary.DomainGenerator;
import burlap.oomdp.core.Attribute;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectClass;
import burlap.oomdp.singleagent.SADomain;
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
	
	protected int[][] map2 = new int[][]{
		{0,0,0,0},
		{0,1,0,0},
		{0,0,0,0}
	};
	
	protected Position[][] map = new Position[][] {
		
		{new Position(0,0), new Position(0,1), new Position(0,2), new Position(0,3, new Position(0,0))},
		{new Position(1,0), new Position(1,1).setWall(true), new Position(1,2), new Position(1,3).setEnd(true)},
		{new Position(2,0)}
	};
	{
		Position p00 = new Position(0,0);
		
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
		// TODO Auto-generated method stub
		return null;
	}

}
