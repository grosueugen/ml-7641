package com.gt.ml.pb;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import shared.Trainer;

public class TimeTrainer implements Trainer {
	
	private static final long serialVersionUID = 1L;

	private final Trainer trainer;
	
	private final long time;
	
	private long i = 0;
	
	private long bestIteration = 0;
	
	private long bestValueInTime;
	
	public TimeTrainer(Trainer trainer, long timeMillis) {
		this.trainer = trainer;
		this.time = timeMillis;
	}
	
	@Override
	public double train() {
		double sum = 0;		
		long startTime = System.nanoTime();
		long endTime = startTime + TimeUnit.MILLISECONDS.toNanos(time);
		
		double bestResult = Double.MIN_VALUE;
		long bestTime = startTime;
		long now = System.nanoTime();
		while (now <= endTime) {			
			double result = trainer.train();
			if (result > bestResult) {
				bestResult = result;
				bestTime = now; 
				bestIteration = i;
			}
			sum += result;
			i++;
			now = System.nanoTime();
		}
		
		bestValueInTime = TimeUnit.NANOSECONDS.toMillis(bestTime - startTime);
		if (i == 0) return 0;
		return sum / i;
	}
	
	public long getIterations() {
		return i;
	}
	
	public long getBestValueInTime() {
		return bestValueInTime;
	}
	
	public long getBestValueInIteration() {
		return bestIteration;
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
		long iterations = tt.getIterations();
		System.out.println("res: " + train);
		System.out.println("it: " + iterations);
		System.out.println(new Timestamp(System.currentTimeMillis()));
	}

}
