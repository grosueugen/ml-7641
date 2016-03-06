package com.gt.ml;

import static com.gt.ml.DataSetup.hiddenLayer;
import static com.gt.ml.DataSetup.inputLayer;
import static com.gt.ml.DataSetup.nrLabels;
import static com.gt.ml.DataSetup.outputLayer;
import static com.gt.ml.DataSetup.testSet;
import static com.gt.ml.DataSetup.trainingSet;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import func.nn.backprop.BackPropagationNetwork;
import func.nn.backprop.BackPropagationNetworkFactory;
import opt.SimulatedAnnealing;
import opt.example.NeuralNetworkOptimizationProblem;
import shared.DataSet;
import shared.ErrorMeasure;
import shared.Instance;
import shared.SumOfSquaresError;
import util.linalg.Vector;

public class SAOptimization {
	
	private static Logger log = LoggerFactory.getLogger(SAOptimization.class);

	private ErrorMeasure measure;
	private BackPropagationNetworkFactory factory;
    private BackPropagationNetwork network;
    private NeuralNetworkOptimizationProblem pb;
    private SimulatedAnnealing alg;
    
    private final Map<Integer, AccuracyResult> trainingAccuracyMap = new HashMap<>();
    private final Map<Integer, AccuracyResult> testAccuracyMap = new HashMap<>();
    private final int iterations;
    
    public SAOptimization(int iterations) {
    	this.iterations = iterations;
    	init();
    }

	private void init() {
		measure = new SumOfSquaresError();
		factory = new BackPropagationNetworkFactory();
	    network = factory.createClassificationNetwork(
                new int[] {inputLayer, hiddenLayer, outputLayer});
	    pb = new NeuralNetworkOptimizationProblem(trainingSet, network, measure);
	    alg = new SimulatedAnnealing(1E11, .95, pb);
	}
	
	public void train() {
        log.info("\nError results for SA \n---------------------------");

        // set the first values
        AccuracyResult trainingAcc = accuracy(trainingSet);
        AccuracyResult testAcc = accuracy(testSet);
        trainingAccuracyMap.put(0, trainingAcc);
        testAccuracyMap.put(0, testAcc);
        
        // iterate
        for(int i = 1; i <= iterations; i++) {
        	Instance cur1 = alg.getOptimal();
            alg.train();
            Instance cur2 = alg.getOptimal();
            boolean progress = (cur1 != cur2);
            if (progress) {
	            trainingAcc = accuracy(trainingSet);
	            testAcc = accuracy(testSet);
            } else {
            	log.info("no progress, using earlier accuracy results...");
            }
            trainingAccuracyMap.put(i, trainingAcc);
            testAccuracyMap.put(i, testAcc);
            log.info("{}", trainingAcc);
            log.info("{}", testAcc);
        }
        
        // set the optimal values
        Instance optimal = alg.getOptimal();
        network.setWeights(optimal.getData());
        trainingAcc = accuracy(trainingSet);
        testAcc = accuracy(testSet);
        trainingAccuracyMap.put((iterations+1), trainingAcc);
        testAccuracyMap.put((iterations+1), testAcc);
        
    }
	
	public AccuracyResult accuracy(DataSet dataSet) {
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
	
	public Map<Integer, AccuracyResult> getTrainingAccuracy() {
		return trainingAccuracyMap;
	}
	
	public Map<Integer, AccuracyResult> getTestAccuracy() {
		return testAccuracyMap;
	}
	
	public int getIterations() {
		return iterations;
	}
	
	public static void main(String[] args) {
		int iterations;
		if (args.length == 0) {
			iterations = DataSetup.trainingIterations;
		} else {
			iterations = Integer.valueOf(args[0]);
		}
		SAOptimization sa = new SAOptimization(iterations);
		log.info("Start SA, using {} iterations", iterations);
		sa.train();
		Map<Integer, AccuracyResult> trainingAccuracy = sa.getTrainingAccuracy();
		Map<Integer, AccuracyResult> testAccuracy = sa.getTestAccuracy();
		log.info("steps with better approximation: {}", trainingAccuracy.size());
		int step = 100;
		int next = 1;
		while (next < trainingAccuracy.size()) {
			AccuracyResult trainingRes = trainingAccuracy.get(next);
			AccuracyResult testRes = testAccuracy.get(next);
			log.info("training accuracy: iteration {}: {}", next, trainingRes);
			log.info("test accuracy: iteration {}: {}", next, testRes);
			next += step;
		}
		
		log.info("final results========================");
		AccuracyResult startTrainingAccuracy = trainingAccuracy.get(0);
		AccuracyResult finalTrainingAccuracy = trainingAccuracy.get(iterations+1);
		
		AccuracyResult startTestAccuracy = testAccuracy.get(0);
		AccuracyResult finalTestAccuracy = testAccuracy.get(iterations+1);
		
		log.info("start training accuracy: {}", startTrainingAccuracy);
		log.info("final training accuracy: {}", finalTrainingAccuracy);
		
		log.info("start test accuracy: {}", startTestAccuracy);
		log.info("final test accuracy: {}", finalTestAccuracy);
		
		log.info("End SA");
	}
	
}
