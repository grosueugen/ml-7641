package rlGambling;

import java.io.File;
import java.io.IOException;

import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.learning.LearningAgent;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.environment.Environment;
import burlap.oomdp.singleagent.environment.SimulatedEnvironment;
import burlap.oomdp.statehashing.HashableStateFactory;
import burlap.oomdp.statehashing.SimpleHashableStateFactory;

public class RunQLearningGambler {
	
	public static void main(String[] args) throws Exception {
		GamblerDomain gamblerDomain = new GamblerDomain(70, 0.4);
		Domain domain = gamblerDomain.generateDomain();
		State initialState = gamblerDomain.createInitialState(domain);
		GamblerReward reward = new GamblerReward();
		GamblerTerminalState terminalState = new GamblerTerminalState();
		HashableStateFactory hashingFactory = new SimpleHashableStateFactory();
		LearningAgent q = new QLearning(domain, 1, hashingFactory, 0, 1);
		
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
