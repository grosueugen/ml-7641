package com.gt.ml.pb;

import opt.EvaluationFunction;
import shared.Instance;
import util.linalg.Vector;

public class AlternatesEvaluationFunction implements EvaluationFunction {
	
	@Override
	public double value(Instance d) {
		Vector data = d.getData();
        double val = 1;
        double prevNr = data.get(0);
        for (int i = 1; i < data.size(); i++) {
            double nr = data.get(i);
            if (nr != prevNr) {
            	val++;
            }
            prevNr = nr;
        }
        return val;
	}
	
	public static void main(String[] args) {
		Instance i = new Instance(new double[]{0,1,0,1});
		double value = new AlternatesEvaluationFunction().value(i);
		if (value != 4) throw new RuntimeException(); 
		else System.out.println("correct");
		
		i = new Instance(new double[]{1,0,1,0});
		value = new AlternatesEvaluationFunction().value(i);
		if (value != 4) throw new RuntimeException(); 
		else System.out.println("correct");
		
		i = new Instance(new double[]{1,1,1,1});
		value = new AlternatesEvaluationFunction().value(i);
		if (value != 1) throw new RuntimeException(); 
		else System.out.println("correct");
		
		i = new Instance(new double[]{0,0,0,0});
		value = new AlternatesEvaluationFunction().value(i);
		if (value != 1) throw new RuntimeException(); 
		else System.out.println("correct");
		
		i = new Instance(new double[]{0,0,1,0});
		value = new AlternatesEvaluationFunction().value(i);
		if (value != 3) throw new RuntimeException(); 
		else System.out.println("correct");
		
		
	}

}
