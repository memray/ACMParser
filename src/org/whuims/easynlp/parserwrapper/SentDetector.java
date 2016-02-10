package org.whuims.easynlp.parserwrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import org.apache.commons.io.FileUtils;
import org.whuims.easynlp.tokenizer.StanfordPTBTokenizer;
import org.whuims.easynlp.util.Config;

public class SentDetector {
	private static SentDetector instance = new SentDetector();
	private Map<String, String> abbreviations = null;
	private SentenceDetectorME sentenceDetector = null;

	private SentDetector() {
		InputStream modelIn = null;
		abbreviations = new HashMap<String, String>();
		try {
			for (String str : FileUtils.readLines(new File("resource/model/abb.txt"))) {
				String[] array = str.split("\t");
				if (array.length == 2) {
					abbreviations.put(array[0].trim(), array[1].trim());
				}
			}
			InputStream is = new FileInputStream(new File("resource/model/en-sent.bin"));
			SentenceModel model = new SentenceModel(is);
			sentenceDetector = new SentenceDetectorME(model);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public Map<String, String> getAbbreviations() {
		return abbreviations;
	}

	public static SentDetector createInstance() {
		return instance;
	}

	public String[] detect(String input) {
		return detect(input, true);
	}

	public String[] detect(String input, boolean abbSensitive) {
		if (abbSensitive) {
			return this.detectWithABBSensitive(input);
		}
		return sentenceDetector.sentDetect(input);
	}

	public String[] detectWithABBSensitive(String input) {
		Map<String, String> tempAbbMap = new HashMap<String, String>();
		String[] array = input.split("\\s+");
		// String[] array=StanfordPTBTokenizer.tokenizeToArray(input);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			String word = array[i].trim();
			if (abbreviations.containsKey(word)) {
				sb.append(abbreviations.get(word));
				tempAbbMap.put(abbreviations.get(word), word);
			} else {
				sb.append(word);
			}
			sb.append(" ");
		}
		String[] results = sentenceDetector.sentDetect(sb.toString().trim());
		for (int i = 0; i < results.length; i++) {
			String line = results[i].trim();
			if (line.startsWith("#")) {
				continue;
			}
			String[] words = line.split("\\s+");
			StringBuilder tempSB = new StringBuilder();
			for (String word : words) {
				if (tempAbbMap.containsKey(word)) {
					tempSB.append(tempAbbMap.get(word));
				} else {
					tempSB.append(word);
				}
				tempSB.append(" ");
			}
			results[i] = tempSB.toString().trim();
		}
		return results;
	}

	public static void main(String[] args) {
		String input = "The result array now contains APT. 45 two entries. ";
		input = "Segmentation of brain tissues is very important in medical image analysis. Support Vector Machines(SVM) is considered a good candidate because of its good generalization performance,especially for dataset with small number of samples in high demensional feature space.This paper investigates the segmentation of magnetic resonance brain tissues image based on SVM.Experimental results show that the influence of kernel function and model parameters on the generalization performance of SVM is significant;SVM is suitably used as learning classifier of small sample size;To segment targets with blurry edges,intensity non-uniformity and discontinuity(such as medical images),SVM approach is a good choice.";
		input = "Majority of visual surveillance algorithms rely on effective and accurate motion detection.However , most evaluation techniques described in literature do not address the complexity and range of the issues which underpin the design of good evaluation methodology.In this paper we explore the problems associated with optimising the operating point of motion detection algorithms and objective performance evaluation . ";
		input = "Previous information extraction (IE) systems are typically organized as a pipeline architecture of separated stages which make independent local decisions. When the data grows beyond some certain size, the extracted facts become inter-dependent and thus we can take advantage of information redundancy to conduct reasoning across documents and improve the performance of IE. We describe a joint inference approach based on information network structure to conduct cross-fact reasoning with an integer linear programming framework. Without using any additional labeled data this new method obtained 13-24 user browsing cost reduction over a state-of-the-art IE system which extracts various types of facts independently.";
		input = "Mr. Vinken is chairman of Elsevier N.V., the Dutch publishing group. Rudolph Agnew, 55 years old and former chairman of Consolidated Gold Fields PLC, was named a director of this British industrial conglomerate.";
		for (String str : SentDetector.createInstance().detect(input, true)) {
			System.out.println();
			System.out.println(str);
		}
	}
}
