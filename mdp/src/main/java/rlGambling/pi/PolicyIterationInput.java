package rlGambling.pi;

public class PolicyIterationInput {
	
	private final int maxAmount;
	private final int initialAmount;
	private final double winProb;
	
	public PolicyIterationInput(int maxAmount, int initialAmount, double winProb) {
		super();
		this.maxAmount = maxAmount;
		this.initialAmount = initialAmount;
		this.winProb = winProb;
	}

	@Override
	public String toString() {
		return "input [maxAmount=" + maxAmount + ", initialAmount=" + initialAmount + ", winProb=" + winProb + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + initialAmount;
		result = prime * result + maxAmount;
		long temp;
		temp = Double.doubleToLongBits(winProb);
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
		if (!(obj instanceof PolicyIterationInput)) {
			return false;
		}
		PolicyIterationInput other = (PolicyIterationInput) obj;
		if (initialAmount != other.initialAmount) {
			return false;
		}
		if (maxAmount != other.maxAmount) {
			return false;
		}
		if (Double.doubleToLongBits(winProb) != Double.doubleToLongBits(other.winProb)) {
			return false;
		}
		return true;
	}
	
	public int getMaxAmount() {
		return maxAmount;
	}
	public int getInitialAmount() {
		return initialAmount;
	}
	public double getWinProb() {
		return winProb;
	}
	
}
