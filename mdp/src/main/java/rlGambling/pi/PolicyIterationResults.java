package rlGambling.pi;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PolicyIterationResults {
	
	private final int iterationsPerPbSize;
	
	private final Map<PolicyIterationInput, List<PolicyIterationOutput>> inToOut = new LinkedHashMap<>();
	
	public PolicyIterationResults(int iterationsPerPbSize) {
		this.iterationsPerPbSize = iterationsPerPbSize;
	}
	
	public void add(PolicyIterationInput input, PolicyIterationOutput output) {
		List<PolicyIterationOutput> outputs = inToOut.get(input);
		if (outputs == null) {
			outputs = new ArrayList<>();
			inToOut.put(input, outputs);
		}
		outputs.add(output);
	}

	public Map<PolicyIterationInput, Double> getSuccessRate() {
		Map<PolicyIterationInput, Double> res = new LinkedHashMap<>();
		for (PolicyIterationInput input : inToOut.keySet()) {
			List<PolicyIterationOutput> outputs = inToOut.get(input);
			int wins = 0;
			for (PolicyIterationOutput output : outputs) {
				if (output.isWinner()) {
					wins++;
				}
			}
			res.put(input, (double) (wins/iterationsPerPbSize));
		}
		return res;
	}
	
	public Map<PolicyIterationInput, Long> getExecutionTime() {
		Map<PolicyIterationInput, Long> res = new LinkedHashMap<>();
		for (PolicyIterationInput input : inToOut.keySet()) {
			List<PolicyIterationOutput> outputs = inToOut.get(input);
			long execTime = 0;
			for (PolicyIterationOutput output : outputs) {
				execTime += output.getExecutionTimeMs();
			}
			res.put(input, (long) (execTime/iterationsPerPbSize));
		}
		return res;
	}
	
	public Map<PolicyIterationInput, Integer> getPolicyIterations() {
		Map<PolicyIterationInput, Integer> res = new LinkedHashMap<>();
		for (PolicyIterationInput input : inToOut.keySet()) {
			List<PolicyIterationOutput> outputs = inToOut.get(input);
			int totalIterations = 0;
			for (PolicyIterationOutput output : outputs) {
				totalIterations += output.getPolicyIterations();
			}
			res.put(input, (totalIterations/iterationsPerPbSize));
		}
		return res;
	}

}
