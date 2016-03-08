package com.gt.ml;

public class AccuracyResult {

	public int dataSize;
	public int correct;
	public int incorrect;
	public double error;
	public double accuracy; 
	
	public AccuracyResult(int dataSize, int correct, int incorrect, double error) {
		super();
		this.dataSize = dataSize;
		this.correct = correct;
		this.incorrect = incorrect;
		this.error = error;
		this.accuracy = ((double)((correct) * 100))/dataSize;
	}

	@Override
	public String toString() {
		return "[dataSize=" + dataSize + ", correct=" + correct + ", incorrect=" + incorrect + ", error="
				+ error + ", accuracy=" + accuracy + "]";
	}

	public String toCommaString() {
		return new StringBuilder("").append(correct).append(",").append(dataSize).append(",")
				.append(accuracy).append(",").append(error).toString();
	}
	
	
}
