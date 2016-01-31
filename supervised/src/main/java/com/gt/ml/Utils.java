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
	
}
