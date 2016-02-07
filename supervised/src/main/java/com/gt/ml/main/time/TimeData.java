package com.gt.ml.main.time;

public class TimeData {
	
	private final int size;
	private final long time;
	
	public TimeData(int size, long time) {
		this.size = size;
		this.time = time;
	}
	
	public int getSize() {
		return size;
	}
	
	public long getTime() {
		return time;
	}

	@Override
	public String toString() {
		return "TimeData [size=" + size + ", time=" + time + "]";
	}

	
	
}
