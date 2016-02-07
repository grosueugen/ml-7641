package com.gt.ml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gt.ml.main.ClassifierTypes;

public class AccuracyResult {
	
	private Map<ClassifierTypes, List<AccuracyData>> accuracyData = new HashMap<>();
	
	public void addData(ClassifierTypes ct, int trainingSize, double accuracy) {
		List<AccuracyData> data = accuracyData.get(ct);
		if (data == null) {
			data = new ArrayList<>();
			accuracyData.put(ct, data);
		}
		data.add(new AccuracyData(trainingSize, accuracy));
	}
	
	public Map<ClassifierTypes, List<AccuracyData>> getAccuracyData() {
		return accuracyData;
	}

}
