package com.gt.ml.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public abstract class AbstractBest {
	
	protected final int iterations;
	
	protected final String file;
	
	protected Map<BestResult, List<BestResult>> all = new HashMap<>();
	
	public AbstractBest(String file, int nrIterations) {
		this.file = file;
		this.iterations = nrIterations;		
	}
	
	public AbstractBest(String file) {
		this(file, 1);
	}
	
	public final void compute() {
		for (int i = 0; i < iterations; i++) {
			doCompute();
		}
	}
	
	protected abstract void doCompute();
	
	protected void add(BestResult res) {
		if (all.containsKey(res)) {
			all.get(res).add(res);
		} else {
			List<BestResult> list = new ArrayList<>();
			list.add(res);
			all.put(res, list);
		}
	}
	
	public BestResult getResult() {
		Queue<BestResult> q = new PriorityQueue<>(new ErrorRateComparator());
		for (BestResult res : all.keySet()) {
			List<BestResult> list = all.get(res);
			Double error = 0D;
			for (BestResult br : list) {
				error += br.errorRate;
			}
			error = error/list.size();
			q.add(new BestResult(res.option, error));
		}
		return q.peek();
	}
	
	protected abstract void printResult();

}
