package com.gt.ml;

import static com.gt.ml.Utils.getInt;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gt.ml.graph.LearnChartBuilder;
import com.gt.ml.learn.LearnData;
import com.gt.ml.learn.LearnResult;
import com.gt.ml.learn.LearningCurve;

public class LearningCurveGraph {
	
	private static final Logger log = LoggerFactory.getLogger(LearningCurveGraph.class);
	
	public static void main(String[] args) throws ParseException, IOException {
		Options options = new Options()
				.addOption(new Option("h", "help", false, "show help"))
				.addOption(new Option("f", "file", true, "file with data sets "))
				.addOption(new Option("of", "output_file", true, "location of the output jpeg graph file e.g. learningCurve.jpeg"))
				.addOption(new Option("width", "width", true, "width of the image, type integer, default 800"))
				.addOption(new Option("height", "height", true, "height of the image, default 600"))
				.addOption(new Option("n", "iteration", true, "#iterations to execute"))
				.addOption(new Option("step", "step_size", true, "the incremental step size, used to increase the #instances used"));
		
		String file;
		String outputFile;
		int n;
		int step;
		int width = 800;
		int height = 600;
		
		CommandLine commandLine = new DefaultParser().parse(options, args);
		if (commandLine.hasOption("h")) {
			new HelpFormatter().printHelp("Model Complexity Experiment", options);
			return;
		}
		if (commandLine.hasOption("f")) {
			file = commandLine.getOptionValue("f");
		} else {
			System.out.println("Please provide a data set file. See help for details");
			return;
		}
		
		if (commandLine.hasOption("of")) {
			outputFile = commandLine.getOptionValue("of");
		} else {
			System.out.println("Please provide an output file to store jpeg graph. See help for details");
			return;
		}
		if (commandLine.hasOption("width")) {
			Integer w = getInt(commandLine.getOptionValue("width"));
			if (w == null) {
				System.out.println("Please provide an integer for width. See help for details");
				return;
			}
			width = w;
		} 
		if (commandLine.hasOption("height")) {
			Integer h = getInt(commandLine.getOptionValue("height"));
			if (h == null) {
				System.out.println("Please provide an integer for height. See help for details");
				return;
			}
			height = h;
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
		
		log.info(
				"$$$$$$$$$ start computing LearningCurve for data set {}, #n {}, #step {}, output file {}, width {}, height {} ",
				file, n, step, outputFile, width, height);
		
		// String[] files = {"sat.arff", "wine-white.arff"};
		LearnResult res = new LearningCurve(file, n, step).compute();
		Map<ClassifierTypes, List<LearnData>> data = res.getData();
		LearnChartBuilder chart = new LearnChartBuilder().withTitle(file + " - Learning Curve")
				.withXY("Instance Size", "Error Rate").withData(data);
		JFreeChart freeChart = chart.build();
		ChartUtilities.saveChartAsJPEG(new File(outputFile), freeChart, width, height);
		
		String output2 = outputFile + "600_400";
		ChartUtilities.saveChartAsJPEG(new File(output2), freeChart, 600, 400);
		
		String output3 = outputFile + "800_600";
		ChartUtilities.saveChartAsJPEG(new File(output3), freeChart, 800, 600);
		
		String output4 = outputFile + "800_800";
		ChartUtilities.saveChartAsJPEG(new File(output4), freeChart, 800, 800);
		
		String output5 = outputFile + "1000_800";
		ChartUtilities.saveChartAsJPEG(new File(output5), freeChart, 1000, 800);
		
		log.info(
				"$$$$$$$$$ end computing LearningCurve for data set {}, #n {}, #step {}, output file {}, width {}, height {} ",
				file, n, step, outputFile, width, height);
		
		//ChartFrame chartFrame = new ChartFrame("Training time comparison (ms)", chart);
	}

}
