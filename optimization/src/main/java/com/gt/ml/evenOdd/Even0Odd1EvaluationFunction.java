package com.gt.ml.evenOdd;

import opt.EvaluationFunction;
import shared.Instance;
import util.linalg.Vector;

//01010101
public class Even0Odd1EvaluationFunction implements EvaluationFunction {
	
	@Override
	public double value(Instance d) {
		Vector data = d.getData();
        double val = 0;
        for (int i = 0; i < data.size(); i++) {
            double nr = data.get(i);
            if (i%2 == 0 && nr == 0) {
            	val++;
            } else if (i%2 == 1 && nr == 1) {
            	val++;
            }
        }
        return val;
	}
	
	public static void main(String[] args) {
		Instance i1 = new Instance(new double[]{0,1,0,1});
		double value1 = new Even0Odd1EvaluationFunction().value(i1);
		if (value1 != 4) throw new RuntimeException(); 
		else System.out.println("correct");
		
		Instance i2 = new Instance(new double[]{1,0,1,0,1});
		double value2 = new Even0Odd1EvaluationFunction().value(i2);
		if (value2 != 0) throw new RuntimeException(); 
		else System.out.println("correct");
	}

}
