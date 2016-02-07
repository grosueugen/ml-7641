package com.gt.ml.main;

import static com.gt.ml.main.Utils.*;

import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.gt.ml.graph.TimeChartBuilder;
import com.gt.ml.graph.ChartFrame;
import com.gt.ml.main.time.RunningTime;
import com.gt.ml.main.time.TimeData;
import com.gt.ml.main.time.TimeResult;

public class RunningTimeGraph {

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
		for (String file : files) {
			TimeResult res = new RunningTime(file, n, step).compute();
			
			Map<ClassifierTypes, List<TimeData>> trainData = res.getTrainData();
			TimeChartBuilder chart1 = new TimeChartBuilder().withTitle(file + " - Training time comparisons (ms)")
					.withXY("Instance Size", "Training Time (ms)").withData(trainData);
			new ChartFrame("Training time comparison (ms)", chart1.build());
			
			Map<ClassifierTypes, List<TimeData>> testData = res.getTestData();
			TimeChartBuilder chart2 = new TimeChartBuilder().withTitle(file + " - Test time comparisons (ms)")
					.withXY("Instance Size", "Test Time (ms)").withData(testData);
			new ChartFrame("Test time comparison (ms)", chart2.build());
		}
	}
	
}
