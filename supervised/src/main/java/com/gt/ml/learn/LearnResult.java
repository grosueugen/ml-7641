package com.gt.ml.learn;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.gt.ml.ClassifierTypes;

public class LearnResult {
	
	private final Map<ClassifierTypes, List<LearnData>> result = new LinkedHashMap<>();
	
	public void addData(ClassifierTypes ct, int size, double trainingError, double testError) {
		LearnData data = new LearnData(size, trainingError, testError);
		List<LearnData> list = null;
		if (result.containsKey(ct)) {
			list = result.get(ct);
		} else {
			list = new ArrayList<>();
			result.put(ct, list);
		}
		list.add(data);
	}
	
	public Map<ClassifierTypes, List<LearnData>> getData() {
		return result;
	}

}
