package com.gt.ml;

import static com.gt.ml.Utils.getInt;

import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.gt.ml.graph.ChartFrame;
import com.gt.ml.graph.LearnChartBuilder;
import com.gt.ml.learn.LearnData;
import com.gt.ml.learn.LearnResult;
import com.gt.ml.learn.LearningCurve;

public class LearningCurveGraph {
	
	public static void main(String[] args) throws ParseException {
		Options options = new Options()
				.addOption(new Option("h", "help", false, "show help"))
				.addOption(new Option("n", "iteration", true, "#iterations to execute"))
				.addOption(new Option("step", "step_size", true, "the incremental step size, used to increase the #instances used"));
		
		int n;
		int step;
		
		CommandLine commandLine = new DefaultParser().parse(options, args);
		if (commandLine.hasOption("h")) {
			new HelpFormatter().printHelp("Model Complexity Experiment", options);
			return;
		}
		if (commandLine.hasOption("n")) {
			Integer it = getInt(commandLine.getOptionValue("n"));
			if (it == null) {
				System.out.println("Please provide an integer for #iterations n. See help for details");
				return;
			}
			n = it;
		} else {
			System.out.println("Please provide #iterations n. See help for details");
			return;
		}
		
		if (commandLine.hasOption("step")) {
			Integer s = getInt(commandLine.getOptionValue("step"));
			if (s == null) {
				System.out.println("Please provide an integer for step. See help for details");
				return;
			}
			step = s;
		} else {
			System.out.println("Please provide step. See help for details");
			return;
		}
		
		String[] files = {"sat.arff", "wine-white.arff"};
		//String[] files = {"sat.arff"};
		for (String file : files) {
			LearnResult res = new LearningCurve(file, n, step).compute();
			
			Map<ClassifierTypes, List<LearnData>> data = res.getData();
			LearnChartBuilder chart = new LearnChartBuilder().withTitle(file + " - Learning Curve")
					.withXY("Instance Size", "Error Rate").withData(data);
			new ChartFrame("Training time comparison (ms)", chart.build());
		}
	}

}
