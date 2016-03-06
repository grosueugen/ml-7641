package com.gt.ml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileUtils {
	
	public static void main(String[] args) throws IOException {
		System.out.println("start");
		//transform("sat-train.txt", "sat-train-new.txt");
		transform("sat-test.txt", "sat-test-new.txt");
		System.out.println("end");
	}
	
	public static void transform(String inputFile, String outputFile) throws IOException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(inputFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
        String line = br.readLine();
        while (line != null) {
        	String[] strings = line.split(" ");
        	bw.newLine();
        	int i = 0;
        	for (String str : strings) {
        		bw.write(str);
        		if (i < 36) {
        			bw.write(",");
        		}
        		i++;
        	}
        	line = br.readLine();
        }
        bw.flush();
        bw.close();
	}

}
