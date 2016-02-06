package com.gt.ml.main;

public class BestResult {

	public final String option;
	public final Double errorRate;

	public BestResult(String option, Double errorRate) {
		this.option = option;
		this.errorRate = errorRate;
	}
	
	public BestResult(BestResult br) {
		this.option = br.option;
		this.errorRate = br.errorRate;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((option == null) ? 0 : option.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BestResult other = (BestResult) obj;
		if (option == null) {
			if (other.option != null)
				return false;
		} else if (!option.equals(other.option))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BestResult [option=" + option + ", errorRate=" + errorRate + "]";
	}

	
	
}
