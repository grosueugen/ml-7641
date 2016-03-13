package com.gt.ml.pb;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
/*
optimization.bat -p 1 -a all -N 100 -r 3 -i 10
 */
public class PbRunner {
	
	public static void main(String[] args) throws ParseException {		
		Options options = new Options()
				.addOption(new Option("h", "help", false, "show help"))
				.addOption(new Option("p", "problem", true, 
						"type of problem, one of <1=Even0Odd1,2=TSP,3=Knapsack,4=Alternates>"))
				.addOption(new Option("n", "n", true, "size of the pb; runs using only this n"))
				.addOption(new Option("mn", "mn", true, "multiple runs using different pb size n :<startSize,stepSize,stopSize>"))
				.addOption(new Option("r", "restarts", true, "#restarts (runs)"))
				.addOption(new Option("i", "iterations", true, "#iterations per each run"))
				.addOption(new Option("t", "time", true, "time in millis or sec; e.g. 100m=100 millis, 10=10 seconds"));
		
		CommandLine commandLine = new DefaultParser().parse(options, args);
		if (commandLine.hasOption("h")) {
			new HelpFormatter().printHelp("Run Problem", options);
			return;
		}
		System.nanoTime();
		
		int problem;
		int N;
		String mn;
		int runs;
		int iterations = -1;
		long timeMillis = -1;
		
		if (commandLine.hasOption("p")) {
			problem = Integer.valueOf(commandLine.getOptionValue("p"));
		} else {
			System.out.println("Please choose a problem; see help for information");
			return;
		}
		
		if (commandLine.hasOption("r")) {
			runs = Integer.valueOf(commandLine.getOptionValue("r"));
		} else {
			System.out.println("Please choose #runs; see help for information");
			return;
		}
		
		if (!commandLine.hasOption("i") && !commandLine.hasOption("t")) {
			System.out.println("Please choose whether using iterations or time; see help for information");
			return;
		} 
		
		boolean it = false;
		if (commandLine.hasOption("i")) {
			iterations = Integer.valueOf(commandLine.getOptionValue("i"));
			it = true;
		} else {
			String time = commandLine.getOptionValue("t");
			if (time.endsWith("m")) {
				timeMillis = Long.valueOf(time.substring(0, time.length()-1));
			} else {
				timeMillis = Long.valueOf(time) * 1000;
			}
		}
		
		if (!commandLine.hasOption("n") && !commandLine.hasOption("mn")) {
			System.out.println("Please choose whether to run using one pb size 'n' or multiple pb sizes 'mn'; see help for information");
			return;
		} 
		
		System.out.println("alg,N,optValue,#it,bestTime,bestIt");
		
		if (commandLine.hasOption("n")) {
			N = Integer.valueOf(commandLine.getOptionValue("n"));
			long now = System.currentTimeMillis();
			//System.out.println("@@@ start: single time N: " + N);
			runAlg(problem, N, runs, iterations, timeMillis, it);
			//System.out.println("@@@ end: single time N: " + N + " and took "  
			//		+ (System.currentTimeMillis() - now) + " millis");
		} else {
			mn = commandLine.getOptionValue("mn");
			String[] split = mn.split(",");
			if (split.length != 3) {
				System.out.println("mn must have 3 parameters, for example: 50,10,100 for <startSize, stepSize, stopSize>");
				return;
			}
			int startSize = Integer.valueOf(split[0]);
			int stepSize = Integer.valueOf(split[1]);
			int stopSize = Integer.valueOf(split[2]);
			for (int currentSize = startSize; currentSize <= stopSize; currentSize = currentSize + stepSize) {
				long now = System.currentTimeMillis();
				//System.out.println("@@@ start: current size: " + currentSize);
				runAlg(problem, currentSize, runs, iterations, timeMillis, it);
				//System.out.println("@@@ end: current size: " + currentSize + ", and took " 
				//		+ (System.currentTimeMillis() - now) + " millis");
			}
		}
    }

	private static void runAlg(int problem, int N, int runs, int iterations, long timeMillis, boolean it) {
		if (it) {
			switch (problem) {
			case 1:
				new Even0Odd1It(N, runs, iterations).run();
				break;
			case 2: 
				new TSPIt(N, runs, iterations).run();
				break;
			case 3: 
				int COPIES_EACH = 4;
			    double MAX_WEIGHT = 50;
			    double MAX_VOLUME = 50;
				new KnapsackIt(N, COPIES_EACH, MAX_WEIGHT, MAX_VOLUME, runs, iterations).run();
				break;
			default: throw new RuntimeException("pb not implemented, see help for information");
			}
		} else {
			switch (problem) {
			case 1:
				new Even0Odd1Time(N, runs, timeMillis).run();
				break;
			case 2: 
				new TSPTime(N, runs, timeMillis).run();
				break;
			case 3: 
				int COPIES_EACH = 4;
			    double MAX_WEIGHT = 50;
			    double MAX_VOLUME = 50;
				new KnapsackTime(N, COPIES_EACH, MAX_WEIGHT, MAX_VOLUME, runs, timeMillis).run();
				break;
			case 4: 
				new AlternatesTime(N, runs, timeMillis).run();
				break;
			default: throw new RuntimeException("pb not implemented, see help for information");
			}
		}
	}

}
