package rlGambling.pi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PolicyIterationOutput {
	
	private final long executionTimeMs;
	private final double discountReturn;
	private final int policyIterations;

	private final List<PolicyIterationStateOutput> statesOutput = new ArrayList<>();

	public PolicyIterationOutput(long executionTimeMs, double discountReturn, int policyIterations) {
		super();
		this.executionTimeMs = executionTimeMs;
		this.discountReturn = discountReturn;
		this.policyIterations = policyIterations;
	}
	
	public void addStateOutput(int amount, int bet, double stateValue) {
		statesOutput.add(new PolicyIterationStateOutput(amount, bet, stateValue));
	}
	
	public List<PolicyIterationStateOutput> getStatesOutput() {
		Collections.sort(statesOutput);
		return statesOutput;
	}
	
	public long getExecutionTimeMs() {
		return executionTimeMs;
	}
	public double getDiscountReturn() {
		return discountReturn;
	}
	public boolean isWinner() {
		return getDiscountReturn() != 0;
	}
	public int getPolicyIterations() {
		return policyIterations;
	}

	public void addStateOutputs(List<PolicyIterationStateOutput> stateOutputs) {
		this.statesOutput.addAll(stateOutputs);
		Collections.sort(statesOutput);
	}
	
}
