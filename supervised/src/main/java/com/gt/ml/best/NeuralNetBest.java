package com.gt.ml.best;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gt.ml.ClassifierContext;

import weka.classifiers.functions.MultilayerPerceptron;

public class NeuralNetBest extends AbstractBest {
	
	private static final Logger log = LoggerFactory.getLogger(NeuralNetBest.class);
	
	private Double[] learningRates = {0.1, 0.5, 0.9};
	private Double[] moments = {0D, 0.1, 0.9};
	private String[] hiddens = {"", "a,5", "a,5,10"};
	
	public NeuralNetBest(String file, int nrInterations) {
		super(file, nrInterations);
	}
	
	public NeuralNetBest(String file) {
		this(file, 1);
	}
	
	@Override
	protected void doCompute() {
		computeSigmoid();
	}
	
	private void computeSigmoid() {
		log.info("########### start compute ");
		for (double lr : learningRates) {
			for (double m : moments) {
				for (String h : hiddens) {
					log.info("########### start compute with learning rate {} moment {} hidden unit {} ", lr, m, h);
					MultilayerPerceptron nn = new MultilayerPerceptron();
					nn.setLearningRate(lr);
					nn.setMomentum(m);
					if (!h.isEmpty()) {
						nn.setHiddenLayers(h);
					}
					
					ClassifierContext cc = new ClassifierContext(file, nn);
					cc.run();
					Double errorRate = cc.getErrorRate();
					BestResult res = new BestResult("nn:activation=sigmoid,lr=" + lr + ",m=" + m + ",hu=" + h, errorRate);
					add(res);
					log.info("########### end compute with result {} ", res);
				}
			}
		}
		log.info("########### end compute ");
	}

	public void printResult() {
		log.info("================== Neural Net Best: {} =====================", file);
		log.info("{}",getResult());
		log.info("================== Neural Net Best: {} =====================", file);
	}
	
	public static void main(String[] args) {
		NeuralNetBest bestSat = new NeuralNetBest("sat.arff", 1);
		bestSat.compute();
		bestSat.printResult();
		
		/*NeuralNetBest bestWine = new NeuralNetBest("wine-white.arff", 1);
		bestWine.compute();
		bestWine.printResult();*/
	}

}
