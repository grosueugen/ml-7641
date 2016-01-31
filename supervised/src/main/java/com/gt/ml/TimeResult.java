package com.gt.ml;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TimeResult {
	
	private Map<ClassifierTypes, List<TimeData>> trainData = new HashMap<>();
	private Map<ClassifierTypes, List<TimeData>> testData = new HashMap<>();
	
	public void addTrainData(ClassifierTypes ct, int size, long time) {
		TimeData timeData = new TimeData(size, time);
		if (trainData.containsKey(ct)) {
			trainData.get(ct).add(timeData);
		} else {
			List<TimeData> data = new LinkedList<>();
			data.add(timeData);
			trainData.put(ct, data);
		}
	}
	
	public void addTestData(ClassifierTypes ct, int size, long time) {
		TimeData timeData = new TimeData(size, time);
		if (testData.containsKey(ct)) {
			testData.get(ct).add(timeData);
		} else {
			List<TimeData> data = new LinkedList<>();
			data.add(timeData);
			testData.put(ct, data);
		}
	}
	
	public Map<ClassifierTypes, List<TimeData>> getTrainData() {
		return trainData;
	}
	
	public Map<ClassifierTypes, List<TimeData>> getTestData() {
		return testData;
	}
	
}
