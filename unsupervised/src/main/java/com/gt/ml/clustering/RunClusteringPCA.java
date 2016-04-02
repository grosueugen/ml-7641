package com.gt.ml.clustering;

import static com.gt.ml.clustering.Utils.*;

import weka.core.Instances;
import weka.filters.unsupervised.attribute.PrincipalComponents;

public class RunClusteringPCA {

	public static void main(String[] args) throws Exception {
		runSatKmeansPCA(0.95);
		runSatKmeansPCA(0.98);
	}

	public static void runSatKmeansPCA(double variance) throws Exception {
		Instances instances = get("sat.arff", 36);
		PrincipalComponents pca = new PrincipalComponents();
		pca.setInputFormat(instances);
		pca.setVarianceCovered(variance);
		pca.setMaximumAttributes(36);
		Instances pcaInstances = PrincipalComponents.useFilter(instances, pca);
		System.out.println(pcaInstances.numAttributes());
		
		
	}
	
}
