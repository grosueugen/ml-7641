package rlGambling;

import java.io.File;

import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.environment.Environment;
import burlap.oomdp.singleagent.environment.SimulatedEnvironment;
import burlap.oomdp.statehashing.HashableStateFactory;
import burlap.oomdp.statehashing.SimpleHashableStateFactory;

public class RunQLearningGambler {
	
	public static void main(String[] args) throws Exception {
		if (args.length != 3) 
			throw new IllegalArgumentException("3 params are required: max amount, initial amount, win probability");
		int maxAmount = Integer.parseInt(args[0]);
		int initialAmount = Integer.parseInt(args[1]);
		double winProb = Double.parseDouble(args[2]);
		
		GamblerDomain gamblerDomain = new GamblerDomain(maxAmount, initialAmount, winProb);
		Domain domain = gamblerDomain.generateDomain();
		State initialState = gamblerDomain.createInitialState(domain);
		GamblerReward reward = new GamblerReward(maxAmount);
		GamblerTerminalState terminalState = new GamblerTerminalState(maxAmount);
		HashableStateFactory hashingFactory = new SimpleHashableStateFactory();
		QLearning q = new QLearning(domain, 1, hashingFactory, 0, 1);
		
		int nrEpisodes = 50;
		
		long now = System.currentTimeMillis();
		String output = "C:\\Users\\Eugen\\Desktop\\mdp\\gambler_q_output_" + now;
		File dir = new File(output);
		boolean b = dir.mkdir();
		if (!b) throw new RuntimeException("Can not create folder " + output);
		
		Environment env = new SimulatedEnvironment(domain, reward, terminalState, initialState);
		
		for (int i = 1; i <= nrEpisodes; i++) {
			EpisodeAnalysis ea = q.runLearningEpisode(env);
			
			String file = output + "\\res_" + i;
			System.out.println("writing to file: " + file);
			ea.writeToFile(file);
			
			env.resetEnvironment();
		}
	}

}
