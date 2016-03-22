package com.gt.ml;

public enum ClassifierTypes {
	
	DECISION_TREE("dt"),
	BOOSTING("boost"),
	KNN("knn"),
	NN("nn"),
	SVM("svm"),
	;
	
	private String shortName;
	
	private ClassifierTypes(String shortName) {
		this.shortName = shortName;
	}
	
	public String getShortName() {
		return shortName;
	}
	
	public static ClassifierTypes toEnum(String str) {
		if ("dt".equalsIgnoreCase(str)) {
			return DECISION_TREE;
		} 
		if ("boost".equalsIgnoreCase(str)) {
			return BOOSTING;
		}
		if ("knn".equalsIgnoreCase(str)) {
			return KNN;
		}
		if ("nn".equalsIgnoreCase(str)) {
			return NN;
		}
		if ("svm".equalsIgnoreCase(str)) {
			return SVM;
		}
		throw new IllegalArgumentException("Please provide a correct value (dt,boost,knn,nn,svm) for classifier type. "
				+ str + " is not supported!");
	}

}
