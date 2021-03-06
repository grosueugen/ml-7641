package gambler.stats;

/**
 * @author Eugen Grosu
 *
 */
public class GamblerStateOutput implements Comparable<GamblerStateOutput> {
	
	private final int amount;
	private final int bet;
	private final double stateValue;
	
	public GamblerStateOutput(int amount, int bet, double stateValue) {
		super();
		this.amount = amount;
		this.bet = bet;
		this.stateValue = stateValue;
	}

	@Override
	public String toString() {
		return "output [amount=" + amount + ", bet=" + bet + ", stateValue=" + stateValue + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + amount;
		result = prime * result + bet;
		long temp;
		temp = Double.doubleToLongBits(stateValue);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof GamblerStateOutput)) {
			return false;
		}
		GamblerStateOutput other = (GamblerStateOutput) obj;
		if (amount != other.amount) {
			return false;
		}
		if (bet != other.bet) {
			return false;
		}
		if (Double.doubleToLongBits(stateValue) != Double.doubleToLongBits(other.stateValue)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int compareTo(GamblerStateOutput o) {
		return (amount - o.amount);
	}

}
