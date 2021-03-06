package gridWorld;

import java.awt.Color;
import java.util.List;

import burlap.behavior.policy.GreedyQPolicy;
import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.auxiliary.EpisodeSequenceVisualizer;
import burlap.behavior.singleagent.auxiliary.StateReachability;
import burlap.behavior.singleagent.auxiliary.valuefunctionvis.ValueFunctionVisualizerGUI;
import burlap.behavior.singleagent.auxiliary.valuefunctionvis.common.ArrowActionGlyph;
import burlap.behavior.singleagent.auxiliary.valuefunctionvis.common.LandmarkColorBlendInterpolation;
import burlap.behavior.singleagent.auxiliary.valuefunctionvis.common.PolicyGlyphPainter2D;
import burlap.behavior.singleagent.auxiliary.valuefunctionvis.common.StateValuePainter2D;
import burlap.behavior.singleagent.learning.LearningAgent;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.behavior.singleagent.planning.Planner;
import burlap.behavior.singleagent.planning.stochastic.policyiteration.PolicyIteration;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.behavior.valuefunction.QFunction;
import burlap.behavior.valuefunction.ValueFunction;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.SADomain;
import burlap.oomdp.singleagent.environment.Environment;
import burlap.oomdp.singleagent.environment.SimulatedEnvironment;
import burlap.oomdp.statehashing.HashableStateFactory;
import burlap.oomdp.statehashing.SimpleHashableStateFactory;
import burlap.oomdp.visualizer.Visualizer;

public class RunGridWorld {
	
	public static final String GRID_OUTPUT = "C:\\gen\\ml\\assignments4\\grid\\";
	
	private GridWorldDomain gwd;
	private Domain domain;
	private State initialState;
	private GridReward reward;
	private GridTerminalState terminalState;
	private HashableStateFactory hashingFactory;
	private Environment env;
	
	public RunGridWorld() {
		gwd = new GridWorldDomain();
		domain = gwd.generateDomain();
		initialState = gwd.getInitialState();
		reward = new GridReward(gwd);
		terminalState = new GridTerminalState(gwd);
		hashingFactory = new SimpleHashableStateFactory();
		env = new SimulatedEnvironment(domain, reward, terminalState, initialState);
	}
	
	public void visualize(String outputPath){
		Visualizer v = gwd.getVisualizer();
		new EpisodeSequenceVisualizer(v, domain, outputPath);
	}
	
	public void manualValueFunctionVis(ValueFunction valueFunction, Policy p){

		List<State> allStates = StateReachability.getReachableStates(initialState, (SADomain)domain, hashingFactory);

		//define color function
		LandmarkColorBlendInterpolation rb = new LandmarkColorBlendInterpolation();
		rb.addNextLandMark(0., Color.RED);
		rb.addNextLandMark(1., Color.cyan);

		//define a 2D painter of state values, specifying which attributes correspond to the x and y coordinates of the canvas
		StateValuePainter2D svp = new StateValuePainter2D(rb);
		svp.setXYAttByObjectClass(GridWorldDomain.CLASSAGENT, GridWorldDomain.ATTX,
				GridWorldDomain.CLASSAGENT, GridWorldDomain.ATTY);


		//create our ValueFunctionVisualizer that paints for all states
		//using the ValueFunction source and the state value painter we defined
		ValueFunctionVisualizerGUI gui = new ValueFunctionVisualizerGUI(allStates, svp, valueFunction);

		//define a policy painter that uses arrow glyphs for each of the grid world actions
		PolicyGlyphPainter2D spp = new PolicyGlyphPainter2D();
		spp.setXYAttByObjectClass(GridWorldDomain.CLASSAGENT, GridWorldDomain.ATTX,
				GridWorldDomain.CLASSAGENT, GridWorldDomain.ATTY);
		spp.setActionNameGlyphPainter(GridWorldDomain.ACTIONNORTH, new ArrowActionGlyph(0));
		spp.setActionNameGlyphPainter(GridWorldDomain.ACTIONSOUTH, new ArrowActionGlyph(1));
		spp.setActionNameGlyphPainter(GridWorldDomain.ACTIONEAST, new ArrowActionGlyph(2));
		spp.setActionNameGlyphPainter(GridWorldDomain.ACTIONWEST, new ArrowActionGlyph(3));
		spp.setRenderStyle(PolicyGlyphPainter2D.PolicyGlyphRenderStyle.DISTSCALED);


		//add our policy renderer to it
		gui.setSpp(spp);
		gui.setPolicy(p);

		//set the background color for places where states are not rendered to grey
		gui.setBgColor(Color.GRAY);

		//start it
		gui.setWidth(350);
		gui.setHeight(250);
		gui.initGUI();
	}
	
	public void simpleValueFunctionVis(ValueFunction valueFunction, Policy p) {

		List<State> allStates = StateReachability.getReachableStates(initialState, (SADomain)domain, hashingFactory);
		ValueFunctionVisualizerGUI gui = GridWorldDomain.getGridWorldValueFunctionVisualization(allStates, valueFunction, p);
		gui.initGUI();

	}
	
	public void runPolicyIteration(String outputPath) {
		PolicyIteration pi = new PolicyIteration(domain, reward, terminalState, 0.99, hashingFactory, 0.001, 1000, 100);
		Policy p = pi.planFromState(initialState);
		String path = outputPath + "pi_" + (System.currentTimeMillis()%100);
		System.out.println("pi: writing to path: " + path);
		p.evaluateBehavior(initialState, reward, terminalState).writeToFile(
				path);
		manualValueFunctionVis(pi, p);
	}
	
	public void runValueIteration(String outputPath){
		ValueIteration planner = new ValueIteration(domain, reward, terminalState, 0.99, hashingFactory, 0.001, 2);
		Policy p = planner.planFromState(initialState);
		String path = outputPath + "vi_" + (System.currentTimeMillis()%100);
		System.out.println("vi: writing to path: " + path);
		p.evaluateBehavior(initialState, reward, terminalState).writeToFile(
				path);

		//simpleValueFunctionVis((ValueFunction)planner, p);
		manualValueFunctionVis((ValueFunction)planner, p);
	}
	
	public void runQLearning(String outputPath) {
		QLearning agent = new QLearning(domain, 0.99, hashingFactory, 0, 1);
		agent.setRf(reward);
		agent.setTf(terminalState);
		agent.setMaximumEpisodesForPlanning(50);
		GreedyQPolicy p = agent.planFromState(initialState);
		String path = outputPath + "ql_" + (System.currentTimeMillis()%100);
		System.out.println("ql: writing to path: " + path);
		p.evaluateBehavior(initialState, reward, terminalState).writeToFile(
				path);
		simpleValueFunctionVis(agent, p);
		int nrEpisodes = agent.getIterations();
		System.out.println("nrEpisodes: " + nrEpisodes);
	}
	
	//todo: to run QL with different nr of iterations
	public static void main(String[] args) {
		RunGridWorld rgw = new RunGridWorld();
		if (args.length != 1) {
			System.out.println("vi/pi/ql needed as parameter");
			System.exit(0);
		}
		String outputPath = GRID_OUTPUT;
		if ("vi".equalsIgnoreCase(args[0])) {
			rgw.runValueIteration(outputPath);
			rgw.visualize(outputPath);
		} else if ("pi".equalsIgnoreCase(args[0])) {
			rgw.runPolicyIteration(outputPath);
			rgw.visualize(outputPath);
		} else if ("ql".equalsIgnoreCase(args[0])) {
			rgw.runQLearning(outputPath);
			rgw.visualize(outputPath);
		} else {
			System.out.println("vi/pi/ql needed as parameter");
			System.exit(0);
		}
	}
	
}
