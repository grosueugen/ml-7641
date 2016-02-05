package com.gt.ml.main;

import com.gt.ml.ClassifierTypes;

public class TestMain {
	
	public static void main(String[] args) {
		ClassifierTypes valueOf = ClassifierTypes.toEnum("dxt");
		System.out.println(valueOf);
	}

}
