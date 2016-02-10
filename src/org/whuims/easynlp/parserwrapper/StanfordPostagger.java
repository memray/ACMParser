package org.whuims.easynlp.parserwrapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.whuims.easynlp.exverb.TagFunction;
import org.whuims.easynlp.tokenizer.StanfordPTBTokenizer;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * 本文件提供Stanford Postagger的单例封装。
 * 
 * @author Qikai
 *
 */
public class StanfordPostagger {
	private static StanfordPostagger instance = new StanfordPostagger();
	private MaxentTagger tagger = null;

	private StanfordPostagger() {
		tagger = new MaxentTagger(
				"edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");

	}

	public static StanfordPostagger createInstance() {
		return instance;
	}

	public String directPostag(String sentLine) {
		return this.tagger.tagString(sentLine);
	}

	public String postag(String sentLine) {
		String[][] result = postagAndSplit(sentLine);
		String[] tokens = result[0];
		String[] postags = result[1];
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tokens.length; i++) {
			sb.append(tokens[i]).append("_").append(postags[i]).append(" ");
		}
		return sb.toString().trim();
	}

	public String[][] postagAndSplit(String sentLine) {
		List<HasWord> sent = StanfordPTBTokenizer.tokenize(sentLine);
		List<TaggedWord> taggedWords = tagger.tagSentence(sent);
		String[] tokens = new String[taggedWords.size()];
		String[] postags = new String[taggedWords.size()];
		for (int j = 0; j < taggedWords.size(); j++) {
			tokens[j] = taggedWords.get(j).word();
			String tag = taggedWords.get(j).tag().trim();
			postags[j] = tag;
		}
		postags = recheck(postags, tokens);
		return new String[][] { tokens, postags };
	}

	private String[] recheck(String[] postags, String[] tokens) {
		if (postags.length <= 1) {
			return postags;
		}
		for (int i = 0; i < postags.length; i++) {
			if (postags[i].equals("VBN")) {
				if (i == postags.length - 1 && TagFunction.isAdj(postags[i - 1])) {
					postags[i] = "JJ";
				} else if (i == 0) {
				} else if (TagFunction.isAdj(postags[i - 1]) && TagFunction.isNounOrAdjOrDet(postags[i + 1])) {
					postags[i] = "JJ";
				}
			} else if (postags[i].equals("CD")
					&& (tokens[i].contains("-") || tokens[i].contains("_") || tokens[i].contains("~"))) {
				postags[i] = "NN";// 用于解决后续句子的错误We used the Wall Street Journal
									// of the years 88-89 .
			}
		}
		return postags;
	}

	public static void main(String[] args) {
		String line = "These comprehensive , readable tutorials and survey papers give guided tours through the literature and explain topics to those who seek to learn the basics of areas outside their specialties . ";
		line = "In this paper, we present a general natural language processing system called CARAMEL . ";
		line = "Microsoft’s cheapest listed online storage is about 2.4 cents a gigabyte";
		line = "stanford based parser";
		line = "We used the Wall Street Journal of the years 88-89 .";
		line = "Besides, instead of using the traditional sequential state transition order, the state transition orders of GHMMs are detected based on layout structures of the corresponding web pages. ";
		line = "we propose a method through information extraction.";
		line = "We have developed an approach for analyzing online job advertisements in different domains ( industries ) from different regions worldwide .";
		line = "Semi-supervised learning addressed the problem of utilizing unlabeled data along with supervised labeled data, to build better classifiers.";
		String output = StanfordPostagger.createInstance().postag(line);
		output = StanfordPostagger.createInstance().directPostag(line);
		System.out.println(output);
		// BufferedReader reader;
		// try {
		// reader = new BufferedReader(new FileReader("j:\\sent.dat"));
		// line = reader.readLine();
		// while (line != null) {
		// line = line.trim();
		// if (line.startsWith("###") || line.equals("")) {
		// line = reader.readLine();
		// continue;
		// }
		// String output = StanfordPostagger.createInstance().postag(line);
		// if (output.contains("VBN")) {
		// System.out.println(output);
		// }
		// line = reader.readLine();
		// }
		// reader.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}
}
