package com.gt.ml;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import shared.Trainer;

public class TimeTrainer implements Trainer {
	
	private static final long serialVersionUID = 1L;

	private final Trainer trainer;
	
	private final int seconds;
	
	private int i = 0;
	
	public TimeTrainer(Trainer trainer, int seconds) {
		this.trainer = trainer;
		this.seconds = seconds;
	}
	
	@Override
	public double train() {
		double sum = 0;		
		long endTime = System.nanoTime() + TimeUnit.SECONDS.toNanos(seconds);
		while (System.nanoTime() <= endTime) {			
			sum += trainer.train();
			i++;			
		}
		return sum / i;
	}
	
	public int getIterations() {
		return i;
	}
	
	public static void main(String[] args) {
		Trainer dummy = new Trainer() {
			@Override
			public double train() {
				try {
					Thread.currentThread().sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return 0;
			}
		};
		System.out.println(new Timestamp(System.currentTimeMillis()));
		TimeTrainer tt = new TimeTrainer(dummy, 5);
		tt.train();
		System.out.println(new Timestamp(System.currentTimeMillis()));
	}

}
