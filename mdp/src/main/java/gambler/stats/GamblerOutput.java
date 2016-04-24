package gambler.stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GamblerOutput {
	
	private final long executionTimeMs;
	private final int iterations;
	private final double winRate;

	private final List<GamblerStateOutput> statesOutput = new ArrayList<>();

	public GamblerOutput(long executionTimeMs, double winRate, int iterations) {
		super();
		this.executionTimeMs = executionTimeMs;
		this.winRate = winRate;
		this.iterations = iterations;
	}
	
	public void addStateOutput(int amount, int bet, double stateValue) {
		statesOutput.add(new GamblerStateOutput(amount, bet, stateValue));
	}
	
	public List<GamblerStateOutput> getStatesOutput() {
		Collections.sort(statesOutput);
		return statesOutput;
	}
	
	public long getExecutionTimeMs() {
		return executionTimeMs;
	}
	public double getWinRate() {
		return winRate;
	}
	public int getIterations() {
		return iterations;
	}

	public void addStateOutputs(List<GamblerStateOutput> stateOutputs) {
		this.statesOutput.addAll(stateOutputs);
		Collections.sort(statesOutput);
	}

	@Override
	public String toString() {
		return "output [executionTimeMs=" + executionTimeMs + ", iterations=" + iterations
				+ ", winRate=" + winRate + "]";
	}
	
	public void printStateOutputs() {
		System.out.println(statesOutput);
	}
	
}
