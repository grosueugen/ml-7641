package com.gt.ml;

import static com.gt.ml.DataSetup.hiddenLayer;
import static com.gt.ml.DataSetup.inputLayer;
import static com.gt.ml.DataSetup.nrLabels;
import static com.gt.ml.DataSetup.outputLayer;
import static com.gt.ml.DataSetup.testSet;
import static com.gt.ml.DataSetup.trainingIterations;
import static com.gt.ml.DataSetup.trainingSet;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import func.nn.backprop.BackPropagationNetwork;
import func.nn.backprop.BackPropagationNetworkFactory;
import opt.RandomizedHillClimbing;
import opt.example.NeuralNetworkOptimizationProblem;
import shared.DataSet;
import shared.ErrorMeasure;
import shared.Instance;
import shared.SumOfSquaresError;
import util.linalg.Vector;

public class RHCOptimization {
	
	private static Logger log = LoggerFactory.getLogger(RHCOptimization.class);

	private ErrorMeasure measure;
	private BackPropagationNetworkFactory factory;
    private BackPropagationNetwork network;
    private NeuralNetworkOptimizationProblem pb;
    private RandomizedHillClimbing alg;
    
    private final Map<Integer, AccuracyResult> accuracyMap = new HashMap<>();
    
    public RHCOptimization() {
    	init();
    }

	private void init() {
		measure = new SumOfSquaresError();
		factory = new BackPropagationNetworkFactory();
	    network = factory.createClassificationNetwork(
                new int[] {inputLayer, hiddenLayer, outputLayer});
	    pb = new NeuralNetworkOptimizationProblem(trainingSet, network, measure);
	    alg = new RandomizedHillClimbing(pb);
	}
	
	public void train() {
        log.info("\nError results for RHC \n---------------------------");

        for(int i = 0; i < trainingIterations; i++) {
            alg.train();
            AccuracyResult res = accuracy(testSet);
            accuracyMap.put(i, res);
            log.info("{}", res);
        }
    }
	
	private AccuracyResult accuracy(DataSet dataSet) {
    	int correct = 0;
    	int incorrect = 0;
    	double error = 0;
    	for (int j = 0; j < dataSet.size(); j++) {
    		Instance instance = dataSet.get(j);
    		network.setInputValues(instance.getData());
    		network.run();
    		Vector predicted = network.getOutputValues();
    		error += measure.value(new Instance(predicted), instance);
    		Vector actual = instance.getLabel().getData();
    		boolean ok = true;
    		for (int i = 0; i < nrLabels; i++) {
    			double predictedValue = predicted.get(i);
    			double actualValue = actual.get(i);
    			if (Math.abs(predictedValue - actualValue) >= 0.5) {
    				ok = false;
    			}
    		}
    		if (ok) correct++;
    		else incorrect++;
    	}
    	//log.info("correct: {}, incorrect: {}, error: {}", correct, incorrect, error);
    	return new AccuracyResult(dataSet.size(), correct, incorrect, error);
	}
	
	public Map<Integer, AccuracyResult> getAccuracyMap() {
		return accuracyMap;
	}
	
	public static void main(String[] args) {
		log.info("Start RHC");
		RHCOptimization rhc = new RHCOptimization();
		rhc.train();
		Map<Integer, AccuracyResult> accuracyMap = rhc.getAccuracyMap();
		log.info("{}", accuracyMap);
		log.info("End RHC");
	}
	
}
