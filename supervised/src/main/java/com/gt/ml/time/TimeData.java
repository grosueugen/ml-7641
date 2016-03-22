package com.gt.ml.time;

public class TimeData {
	
	private final int size;
	private final long trainingTime;
	private final long testTime;
	
	public TimeData(int size, long trainingTime, long testTime) {
		this.size = size;
		this.trainingTime = trainingTime;
		this.testTime = testTime;
	}
	
	public int getSize() {
		return size;
	}
	

	public long getTrainingTime() {
		return trainingTime;
	}
	
	public long getTestTime() {
		return testTime;
	}

	@Override
	public String toString() {
		return "TimeData [size=" + size + ", trainingTime=" + trainingTime + ", testTime=" + testTime + "]";
	}
	
}
