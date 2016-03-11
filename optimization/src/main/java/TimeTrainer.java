

import java.time.Instant;

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
		long endTime = Instant.now().plusSeconds(seconds).toEpochMilli();
		long curTime = System.nanoTime();
		while (curTime <= endTime) {			
			sum += trainer.train();
			i++;			
		}
		return sum / i;
	}
	
	public int getIterations() {
		return i;
	}

}
