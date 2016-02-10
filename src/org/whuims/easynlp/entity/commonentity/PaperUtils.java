package org.whuims.easynlp.entity.commonentity;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.Tree;
import org.whuims.easynlp.parserwrapper.SentDetector;
import org.whuims.easynlp.parserwrapper.StanfordParser;
import org.whuims.easynlp.parserwrapper.StanfordPostagger;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PaperUtils {
	/**
	 * Return the sentence with POS-tagging information
	 * @param sentLine
	 * @return
     */
	public static Sentence sentProductOf(String sentLine) {
		String[][] taggedWords = StanfordPostagger.createInstance()
				.postagAndSplit(sentLine);
		String[] tokens = taggedWords[0];
		String[] postags = taggedWords[1];
		Sentence sent = null;
		try {
			sent = new Sentence(tokens, postags);
			sent.setRawText(sentLine);
		} catch (SequenceException e) {
			e.printStackTrace();
		}
		return sent;
	}

	private static Sentence sentProductParserOf(String sentLine) {
		Tree tree = StanfordParser.createInstance().parse(sentLine);
		List<CoreLabel> list = tree.taggedLabeledYield();
		Collections.sort(list, new Comparator<CoreLabel>() {
			@Override
			public int compare(CoreLabel o1, CoreLabel o2) {
				if (o1.index() > o2.index()) {
					return 1;
				} else if (o1.index() < o2.index()) {
					return -1;
				}
				return 0;
			}
		});
		String[] tokens = new String[list.size()];
		String[] postags = new String[list.size()];
		for (int i = 0; i < tokens.length; i++) {
			CoreLabel label = list.get(i);
			tokens[i] = label.word().trim();
			postags[i] = label.tag().trim();
		}
		Sentence sent = null;
		try {
			sent = new Sentence(tokens, postags);
			sent.setRawText(sentLine);
			sent.setTree(tree);
		} catch (SequenceException e) {
			e.printStackTrace();
		}
		return sent;
	}

	public static Paragraph paraProductOf(String rawText) {
		Paragraph para = new Paragraph();
		para.setRawText(rawText);
		String[] sents = SentDetector.createInstance().detect(rawText);
		for (String sentLine : sents) {
			para.getSentences().add(PaperUtils.sentProductOf(sentLine));
		}
		return para;
	}

	public static void main(String[] args) {
		System.out.println(PaperUtils.sentProductParserOf("In this paper, we present a general natural language processing system called CARAMEL . ")
				.getPostaggedText());
	}
}
