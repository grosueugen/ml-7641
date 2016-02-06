package com.gt.ml.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.MultilayerPerceptronTanh;

public class NeuralNetBest extends AbstractBest {
	
	private static final Logger log = LoggerFactory.getLogger(NeuralNetBest.class);
	
	private Double[] learningRates = {0.1, 0.5, 0.9};
	private Double[] moments = {0D, 0.1, 0.9};
	private String[] hiddens = {"a,5", "1,5,10"};
	
	public NeuralNetBest(String file, int nrInterations) {
		super(file, nrInterations);
	}
	
	public NeuralNetBest(String file) {
		this(file, 1);
	}
	
	@Override
	protected void doCompute() {
		computeSigmoid();
		computeTanh();
	}
	
	private void computeSigmoid() {
		for (double lr : learningRates) {
			for (double m : moments) {
				for (String h : hiddens) {
					MultilayerPerceptron nn = new MultilayerPerceptron();
					nn.setLearningRate(lr);
					nn.setMomentum(m);
					nn.setHiddenLayers(h);
					
					ClassifierContext cc = new ClassifierContext(file, nn);
					cc.run();
					Double errorRate = cc.getErrorRate();
					add(new BestResult("nn:activation=sigmoid,lr=" + lr + ",m=" + m + ",hu=" + h, errorRate));
				}
			}
		}
	}

	private void computeTanh() {
		for (double lr : learningRates) {
			for (double m : moments) {
				for (String h : hiddens) {
					MultilayerPerceptronTanh nn = new MultilayerPerceptronTanh();
					nn.setLearningRate(lr);
					nn.setMomentum(m);
					nn.setHiddenLayers(h);
					
					ClassifierContext cc = new ClassifierContext(file, nn);
					cc.run();
					Double errorRate = cc.getErrorRate();
					add(new BestResult("nn:activation=tanh,lr=" + lr + ",m=" + m + ",hu=" + h, errorRate));
				}
			}
		}
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
