package com.gt.ml.learn;

public class LearnData {
	
	private final int size;
	private final double trainingError;
	private final double testError;
	
	public LearnData(int size, double trainingError, double testError) {
		this.size = size;
		this.trainingError = trainingError;
		this.testError = testError;
	}

	public int getSize() {
		return size;
	}

	public double getTrainingError() {
		return trainingError;
	}

	public double getTestError() {
		return testError;
	}

	@Override
	public String toString() {
		return "LearnData [size=" + size + ", trainingError=" + trainingError + ", testError=" + testError + "]";
	}
	
	

}
