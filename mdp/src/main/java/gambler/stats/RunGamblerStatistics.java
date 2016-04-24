package gambler.stats;

import java.util.Map;
import java.util.Set;

public class RunGamblerStatistics {
	
	public static void main(String[] args) {
		if (args.length != 7 && args.length != 8) {
			System.out.println("6 params required: alg (pi,vi,ql), start max amount, start initial amount, win probability, step size, step iterations, #repeats, QL-episodes (optional)");
			System.exit(0);
		}
		String alg = args[0];		
		int startMaxAmount = Integer.parseInt(args[1]);
		int startInitialAmount = Integer.parseInt(args[2]);
		double winProb = Double.parseDouble(args[3]);
		int stepSize = Integer.parseInt(args[4]);
		int stepIterations = Integer.parseInt(args[5]);
		int iterations = Integer.parseInt(args[6]);
		
		Integer qlEpisodes = 50;
		if (args.length == 8) {
			qlEpisodes = Integer.parseInt(args[7]);
		}
		if (!alg.equalsIgnoreCase("pi") && !alg.equalsIgnoreCase("vi") && !alg.equalsIgnoreCase("ql")) {
			throw new IllegalArgumentException("alg must be one of pi, vi, ql");
		}
		
		if (alg.equalsIgnoreCase("pi")) {
			
			System.out.println("Running Gambler with Policy Iteration");
			GamblerPolicyIteration gpi = new GamblerPolicyIteration(startMaxAmount, startInitialAmount, winProb);
			gpi.setRunParameters(stepSize, stepIterations, iterations);
			Map<GamblerInput, GamblerOutput> results = gpi.execute();
			System.out.println("@@@@@@@@@@ Policy Iteration Results @@@@@@@@@@ ");
			printResults(results);
			System.out.println("End Running Gambler with Policy Iteration");
		} else if (alg.equalsIgnoreCase("vi")) {
			
			System.out.println("Running Gambler with Value Iteration");
			GamblerValueIteration gvi = new GamblerValueIteration(startMaxAmount, startInitialAmount, winProb);
			gvi.setRunParameters(stepSize, stepIterations, iterations);
			Map<GamblerInput, GamblerOutput> results = gvi.execute();
			System.out.println("@@@@@@@@@@ Value Iteration Results @@@@@@@@@@ ");
			printResults(results);
			System.out.println("End Running Gambler with Value Iteration");
		} else if (alg.equalsIgnoreCase("ql")) {
			
			System.out.println("Running Gambler with Value Iteration");
			GamblerQLearning ql = new GamblerQLearning(startMaxAmount, startInitialAmount, winProb);
			ql.setRunParameters(stepSize, stepIterations, iterations);
			ql.setMaxEpisodes(qlEpisodes);
			Map<GamblerInput, GamblerOutput> results = ql.execute();
			System.out.println("@@@@@@@@@@ QLearning Results @@@@@@@@@@ ");
			printResults(results);
			System.out.println("End Running Gambler with QLearning");
		}
	}

	private static void printResults(Map<GamblerInput, GamblerOutput> results) {
		System.out.println("##### Problem Size");
		Set<GamblerInput> inputs = results.keySet();
		for (GamblerInput input : inputs) {
			System.out.println(input.getMaxAmount());
		}
		System.out.println("##### Win Rate");
		for (GamblerInput input : results.keySet()) {
			GamblerOutput output = results.get(input);
			System.out.println(output.getWinRate());
		}
		System.out.println("##### Execution Time");
		for (GamblerInput input : results.keySet()) {
			GamblerOutput output = results.get(input);
			System.out.println(output.getExecutionTimeMs());
		}
		System.out.println("##### Number Of Iterations");
		for (GamblerInput input : results.keySet()) {
			GamblerOutput output = results.get(input);
			System.out.println(output.getIterations());
		}
	}

}
