package com.gt.ml.main;

import static com.gt.ml.main.Utils.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.gt.ml.main.best.AbstractBest;
import com.gt.ml.main.best.BoostingBest;
import com.gt.ml.main.best.DecisionTreeBest;
import com.gt.ml.main.best.KNNBest;
import com.gt.ml.main.best.NeuralNetBest;
import com.gt.ml.main.best.SVMBest;

public class ModelComplexityExperiment {
	
	private final String file;
	private final int i;
	
	public ModelComplexityExperiment(String file, int i) {
		this.file = file;
		this.i = i; 
	}

	public static void main(String[] args) throws ParseException {
		Options options = new Options()
				.addOption(new Option("h", "help", false, "show help"))
				.addOption(new Option("f", "file", true, "file path containing the data set"))
				.addOption(new Option("i", "iteration", true, "#iterations to execute, type integer, default value 1"));
		
		CommandLine commandLine = new DefaultParser().parse(options, args);
		if (commandLine.hasOption("h")) {
			new HelpFormatter().printHelp("Model Complexity Experiment", options);
			return;
		}
		String file = null;
		int i = 1;
		
		if (commandLine.hasOption("f")) {
			file = commandLine.getOptionValue("f");
		} else {
			System.out.println("Please provide data set file. See help for more details");
			return;
		}
		if (commandLine.hasOption("i")) {
			Integer it = getInt(commandLine.getOptionValue("i"));
			if (it == null) {
				System.out.println("Please provide a correct integer for i. See help for more details");
				return;
			}
		}
		
		new ModelComplexityExperiment(file, i).runAll();
		
	}
	
	private void runAll() {
		List<AbstractBest> all = new ArrayList<>();
		all.add(new DecisionTreeBest(file, i));
		all.add(new BoostingBest(file, i));
		all.add(new KNNBest(file, i));
		all.add(new NeuralNetBest(file, i));
		all.add(new SVMBest(file, i));
		
		for (AbstractBest b : all) {
			b.compute();
			b.printResult();
		}
	}

}
