package rlGambling;

import java.util.Map;

import rlGambling.pi.GamblerPolicyIteration;
import rlGambling.pi.PolicyIterationInput;
import rlGambling.pi.PolicyIterationResults;

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
			PolicyIterationResults results = gpi.execute();
			System.out.println("@@@@@@@@@@ Policy Iteratin Results @@@@@@@@@@ ");
			Map<PolicyIterationInput, Double> successRate = results.getSuccessRate();
			
			System.out.println("### start success rate: ");
			System.out.println(successRate);
			System.out.println("### end success rate: ");
			System.out.println("End Running Gambler with Policy Iteration");
		}
	}

}
