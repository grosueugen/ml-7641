package com.gt.ml.main;

import java.util.Comparator;

public class ErrorRateComparator implements Comparator<BestResult> {
	
	@Override
	public int compare(BestResult o1, BestResult o2) {
		return o1.errorRate.compareTo(o2.errorRate);
	}

}
