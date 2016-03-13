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
						"type of problem, one of <1=Even0Odd1,2=TSP,3=Knapsack>"))
				.addOption(new Option("n", "n", true, "size of the pb"))
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
		String params;
		int runs;
		int iterations = -1;
		long timeMillis = -1;
		
		if (commandLine.hasOption("p")) {
			problem = Integer.valueOf(commandLine.getOptionValue("p"));
		} else {
			System.out.println("Please choose a problem; see help for information");
			return;
		}
		
		if (commandLine.hasOption("n")) {
			params = commandLine.getOptionValue("n");
		} else {
			System.out.println("Please choose parameters for the problem; see help for information");
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
		
		if (it) {
			switch (problem) {
			case 1:
				int N = Integer.valueOf(params);
				new Even0Odd1It(N, runs, iterations).run();
				break;
			case 2: 
				N = Integer.valueOf(params);
				new TSPIt(N, runs, iterations).run();
				break;
			case 3: 
				int NUM_ITEMS = Integer.valueOf(params);
				int COPIES_EACH = 4;
			    double MAX_WEIGHT = 50;
			    double MAX_VOLUME = 50;
				new KnapsackIt(NUM_ITEMS, COPIES_EACH, MAX_WEIGHT, MAX_VOLUME, runs, iterations).run();
				break;
			}
		} else {
			switch (problem) {
			case 1:
				int N = Integer.valueOf(params);
				new Even0Odd1Time(N, runs, timeMillis).run();
				break;
			case 2: 
				N = Integer.valueOf(params);
				new TSPTime(N, runs, timeMillis).run();
				break;
			case 3: 
				int NUM_ITEMS = Integer.valueOf(params);
				int COPIES_EACH = 4;
			    double MAX_WEIGHT = 50;
			    double MAX_VOLUME = 50;
				new KnapsackTime(NUM_ITEMS, COPIES_EACH, MAX_WEIGHT, MAX_VOLUME, runs, timeMillis).run();
				break;
			}
			
		}
			
    }

}
