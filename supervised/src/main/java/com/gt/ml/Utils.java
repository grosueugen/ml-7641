package com.gt.ml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import weka.core.Instances;

public class Utils {
	
	public static Instances getInstances(String file) throws IOException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
		Reader r = new BufferedReader(new InputStreamReader(is));
		Instances dataSet = new Instances(r);
		dataSet.setClassIndex(dataSet.numAttributes() - 1);
		return dataSet;
	}
	
	public static Instances[] getTrainAndTest(Instances instances) {
		int size = instances.numInstances();
		int trainingSize = (int) (size * 0.7);
		int testSize = size - trainingSize;

		Instances trainSet = new Instances(instances, 0, trainingSize);
		Instances testSet = new Instances(instances, trainingSize, testSize);
		
		Instances[] res = new Instances[2];
		res[0] = trainSet;
		res[1] = testSet;
		return res;
	}
	
}
