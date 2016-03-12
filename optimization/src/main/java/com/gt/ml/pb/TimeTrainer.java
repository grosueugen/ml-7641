package com.gt.ml.pb;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import shared.Trainer;

public class TimeTrainer implements Trainer {
	
	private static final long serialVersionUID = 1L;

	private final Trainer trainer;
	
	private final long time;
	
	private int i = 0;
	
	public TimeTrainer(Trainer trainer, long timeMillis) {
		this.trainer = trainer;
		this.time = timeMillis;
	}
	
	@Override
	public double train() {
		double sum = 0;		
		long endTime = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(time);
		while (System.nanoTime() <= endTime) {			
			double result = trainer.train();
			// mimic can take a long time for 1 iteration, I do not want it to cheat when time contraint
			if (System.nanoTime() <= endTime) {
				sum += result;
				i++;			
			}
		}
		if (i == 0) return 0;
		return sum / i;
	}
	
	public int getIterations() {
		return i;
	}
	
	public static void main(String[] args) {
		Trainer dummy = new Trainer() {
			private static final long serialVersionUID = 1L;

			@Override
			public double train() {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return 2;
			}
		};
		System.out.println(new Timestamp(System.currentTimeMillis()));
		TimeTrainer tt = new TimeTrainer(dummy, 450);
		double train = tt.train();
		int iterations = tt.getIterations();
		System.out.println("res: " + train);
		System.out.println("it: " + iterations);
		System.out.println(new Timestamp(System.currentTimeMillis()));
	}

}
