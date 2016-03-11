package com.gt.ml.pb;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import opt.EvaluationFunction;
import opt.example.CountOnesEvaluationFunction;
/*
optimization.bat -p 1 -a all -N 100 -r 3 -i 10
 */
public class PbRunner {
	
	public static void main(String[] args) throws ParseException {		
		Options options = new Options()
				.addOption(new Option("h", "help", false, "show help"))
				.addOption(new Option("p", "problem", true, "type of problem, one of <1=countOnes,2=Even0Odd1>"))
				.addOption(new Option("a", "alg", true, "algorithm to run, one of <all,rhc,sa,ga,mimic>"))
				.addOption(new Option("N", "N", true, "size of problem"))
				.addOption(new Option("r", "restarts", true, "#restarts (runs)"))
				.addOption(new Option("i", "iterations", true, "#iterations per each run"))
				.addOption(new Option("t", "time", true, "time in millis or sec; e.g. 100m=100 millis, 10=10seconds"));
		
		CommandLine commandLine = new DefaultParser().parse(options, args);
		if (commandLine.hasOption("h")) {
			new HelpFormatter().printHelp("Run Problem", options);
			return;
		}
		
		int problem;
		String alg;
		int N;
		int runs;
		int iterations = -1;
		long timeMillis = -1;
		
		if (commandLine.hasOption("p")) {
			problem = Integer.valueOf(commandLine.getOptionValue("p"));
		} else {
			System.out.println("Please choose a problem; see help for information");
			return;
		}
		
		if (commandLine.hasOption("a")) {
			alg = commandLine.getOptionValue("a");
		} else {
			System.out.println("Please choose an algorithm; see help for information");
			return;
		}
		
		if (commandLine.hasOption("N")) {
			N = Integer.valueOf(commandLine.getOptionValue("N"));
		} else {
			System.out.println("Please choose problem size; see help for information");
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
		
		EvaluationFunction fct = getFct(problem);
		if (it) {
			System.out.println("Running Iteration pb " + fct.getClass().getSimpleName());	
		} else {
			System.out.println("Running Time pb " + fct.getClass().getSimpleName());
		}
		
		
		if (it) {
			IterationRunner runner = new IterationRunner().n(N).runs(runs).iterations(iterations).fct(fct);
			if (alg.equals("all")) {
				runner.runAll();
			} else if (alg.equals("rhc")) {
				runner.runRHC();
			} else if (alg.equals("sa")) {
				runner.runSA();
			} else if (alg.equals("ga")) {
				runner.runGA();
			} else if (alg.equals("mimic")) {
				runner.runMimic();
			}
		} else {
			TimeRunner runner = new TimeRunner().n(N).runs(runs).time(timeMillis).fct(fct);
			if (alg.equals("all")) {
				runner.runAll();
			} else if (alg.equals("rhc")) {
				runner.runRHC();
			} else if (alg.equals("sa")) {
				runner.runSA();
			} else if (alg.equals("ga")) {
				runner.runGA();
			} else if (alg.equals("mimic")) {
				runner.runMimic();
			}
		}
    }

	private static EvaluationFunction getFct(int problem) {
		if (problem == 1) {
			return new CountOnesEvaluationFunction();
		} else if (problem == 2) {
			return new Even0Odd1EvaluationFunction();
		} 
		throw new RuntimeException("type of problem, one of <1=countOnes,2=Even0Odd1>");
	}

}
