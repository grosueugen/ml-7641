package com.gt.ml.pb;

public class TimeResult {
	
	final double sum;
	final int i;
	
	public TimeResult(double sum, int i) {
		this.sum = sum;
		this.i = i;
	}
	
	double avg() {
		if (i == 0) return 0;
		return (sum/i);
	}

}
