package com.gt.ml.pb;

import java.sql.Timestamp;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import shared.Trainer;

public class ThreadTimeTrainer implements Trainer {
	
	private static final long serialVersionUID = 1L;

	private final Trainer trainer;
	
	private final long time;
	
	private int i = 0;
	
	private final ArrayBlockingQueue<TimeResult> res = new ArrayBlockingQueue<>(1);
	
	public ThreadTimeTrainer(Trainer trainer, long timeMillis) {
		this.trainer = trainer;
		this.time = timeMillis;
	}
	
	@Override
	public double train() {
		ExecutorService executorService = Executors.newFixedThreadPool(1);
		try {
			return doTrain(executorService);
		} finally {
			executorService.shutdownNow();
			System.out.println("a");
		}
	}
	
	public double doTrain(ExecutorService executorService) {
		Future future = executorService.submit(new Runnable() {
			@Override
			public void run() {
				double sum = 0;
				int i = 0;
				try {
					res.put(new TimeResult(sum, i));
				} catch (InterruptedException e1) {
					e1.printStackTrace();
					throw new RuntimeException("Can not take/put from blocking queue!");
				}
				while (true) {
					if (Thread.currentThread().isInterrupted()) {
						return;
					}
					sum += trainer.train();
					i++;
					try {
						res.take();
						res.put(new TimeResult(sum, i));
					} catch (InterruptedException e) {
						e.printStackTrace();
						throw new RuntimeException("Can not take/put from blocking queue!");
					}
					
				}
			}
		});
		
		long endTime = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(time);
		while (System.nanoTime() <= endTime) {	
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		boolean cancel = future.cancel(true);
		if (!cancel) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			TimeResult result = res.take();
			this.i = result.i;
			return result.avg();
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException("Can not take/put from blocking queue!");
		}
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
					return 0;
				}
				return 0;
			}
		};
		System.out.println(new Timestamp(System.currentTimeMillis()));
		ThreadTimeTrainer tt = new ThreadTimeTrainer(dummy, 2000);
		tt.train();
		System.out.println(new Timestamp(System.currentTimeMillis()));
	}
	
}


