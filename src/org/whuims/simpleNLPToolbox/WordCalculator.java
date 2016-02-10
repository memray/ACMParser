package org.whuims.simpleNLPToolbox;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.whuims.commons.util.Pair;
import org.whuims.easynlp.tokenizer.StanfordPTBTokenizer;

public class WordCalculator {
	private String inputFilePath;
	private Map<String, Integer> termCountMap = new HashMap<String, Integer>();
	private String outputFilePath;

	public WordCalculator(String filePath, String outputFilePath) {
		super();
		this.inputFilePath = filePath;
		this.outputFilePath = outputFilePath;
	}

	public void cal() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				this.inputFilePath)));
		String line = reader.readLine();
		while (line != null) {
			processLine(line);
			line = reader.readLine();
		}
		reader.close();
	}

	private void processLine(String line) {
		String[] array = StanfordPTBTokenizer.tokenizeToArray(line.trim()
				.toLowerCase());
		for (String word : array) {
			updateWordFreq(word);
		}
	}

	private void updateWordFreq(String word) {
		if (!this.termCountMap.containsKey(word)) {
			this.termCountMap.put(word, 0);
		}
		int count = this.termCountMap.get(word);
		count++;
		this.termCountMap.put(word, count);
	}

	public void output() throws IOException {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, Integer> entry : this.termCountMap.entrySet()) {
			sb.append(entry.getKey()).append("\t").append(entry.getValue())
					.append("\n");
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				this.outputFilePath)));
		writer.write(sb.toString().trim());
		writer.close();
	}

	public void sortAndOutput() throws IOException {
		StringBuilder sb = new StringBuilder();
		List<Pair> list=new ArrayList<Pair>();
		for (Entry<String, Integer> entry : this.termCountMap.entrySet()) {
			list.add(new Pair(entry.getKey(),entry.getValue()));
		}
		Collections.sort(list);
		for(Pair pair:list){
			sb.append(pair.getKey()).append("\t").append(pair.getValue())
			.append("\n");
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				this.outputFilePath)));
		writer.write(sb.toString().trim());
		writer.close();
	}

	public static void main(String[] args) {
		args=new String[2];
		args[0]="f:\\summarySents.txt";
		args[1]="f:\\summarySents_wordcount.txt";
		if (args.length == 0) {
			System.err.println("输入两个参数，第一个为输入文件，第二个为输出文件");
			System.exit(0);
		}
		WordCalculator cal = new WordCalculator(args[0], args[1]);
		try {
			cal.cal();
			cal.sortAndOutput();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
