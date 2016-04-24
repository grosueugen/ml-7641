package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RandomTest {

	@Test
	public void test1() {
		int res = playOneRound(0.1, 70, 0.5, 30);
		assertEquals(40, res);
		
		res = playOneRound(0.51, 70, 0.5, 30);
		assertEquals(100, res);
		
		res = playOneRound(0.50, 70, 0.5, 30);
		assertEquals(100, res);
		
		res = playOneRound(0.22, 70, 0.65, 30);
		assertEquals(40, res);
		
		res = playOneRound(0.42, 70, 0.65, 30);
		assertEquals(100, res);
	}
	private int playOneRound(double r, int currentAmount, double winProb, int betAmount) {
		boolean win = (r >= (1 - winProb));
		if (win) {
			currentAmount += betAmount;
		} else {
			currentAmount -= betAmount;
		}
		return currentAmount;
	}
	
}
