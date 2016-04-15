package gambling;

import java.util.Random;

public class ExpectedValueCalculator {
	
	private final int rounds;
	private final int amount;
	private final double winProb;
	
	private final int iterations = 1000000;
	
	public ExpectedValueCalculator(int amount, int rounds, double winProb) {
		this.amount = amount;
		this.rounds = rounds;
		this.winProb = winProb;
	}
	
	public int compute() {
		long expectation = 0;
		for (int i = 0; i < iterations; i++) {
			int exp = computeExpectation();
			expectation += exp;
		}
		return (int) (expectation/iterations);
	}
	
	private int computeExpectation() {
		int currentAmount = amount;
		for (int i = 0; i < rounds; i++) {
			Random r = new Random();
			double sample = r.nextDouble();
			boolean win = (sample >= (1 - winProb));			
			double bet = (2 * winProb - 1) * currentAmount;
			
			// System.out.println("bet: " + bet);
			if (win) {
				currentAmount += bet;
			} else {
				currentAmount -= bet;
			}
			//System.out.println("sample: " + sample + ", win?" + win + ", bet: " + bet + "amount: " + currentAmount);
		}
		//System.out.println("currentAmount: " + currentAmount);
		return currentAmount;
	}

	public static void main(String[] args) {
		int res = new ExpectedValueCalculator(100, 20, 0.6).compute();
		System.out.println(res);
	}

}
