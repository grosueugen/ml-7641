package com.gt.ml.time;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.gt.ml.ClassifierTypes;

public class TimeResult {
	
	private Map<ClassifierTypes, List<TimeData>> data = new HashMap<>();
	
	public void addData(ClassifierTypes ct, int size, long trainingTime, long testTime) {
		TimeData timeData = new TimeData(size, trainingTime, testTime);
		if (data.containsKey(ct)) {
			data.get(ct).add(timeData);
		} else {
			List<TimeData> list = new LinkedList<>();
			list.add(timeData);
			data.put(ct, list);
		}
	}
	
	public Map<ClassifierTypes, List<TimeData>> getData() {
		return data;
	}
	
}
