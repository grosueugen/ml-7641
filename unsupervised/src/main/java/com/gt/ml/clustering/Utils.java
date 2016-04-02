package com.gt.ml.clustering;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class Utils {
	
	public static Instances get(String file) throws IOException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
		Reader r = new BufferedReader(new InputStreamReader(is));
		return new Instances(r);
	}
	
	public static Instances get(String file, int classIndex) throws IOException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
		Reader r = new BufferedReader(new InputStreamReader(is));
		Instances instances = new Instances(r);
		instances.setClassIndex(classIndex);
		return instances;
	}
	
	public static Instances removeAttributes(Instances dataSet, String attributesToRemove) {
		try {
			Remove filter = new Remove();
			filter.setInvertSelection(false);
			filter.setAttributeIndices(attributesToRemove);
			filter.setInputFormat(dataSet);
			return Filter.useFilter(dataSet, filter);
		} catch (Exception e) {
			throw new RuntimeException("Error removing attributes " + attributesToRemove, e);
		}
	}

}
