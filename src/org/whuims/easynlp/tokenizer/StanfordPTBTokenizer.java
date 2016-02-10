package org.whuims.easynlp.tokenizer;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;

public class StanfordPTBTokenizer {
	public static List<HasWord> tokenize(String line) {
		List<HasWord> list = new ArrayList<HasWord>();
		PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<CoreLabel>(
				new StringReader(line), new CoreLabelTokenFactory(), "");
		for (CoreLabel label; ptbt.hasNext();) {
			label = (CoreLabel) ptbt.next();
			list.add(label);
		}
		return list;
	}

	public static String[] tokenizeToArray(String line) {
		List<HasWord> list = tokenize(line);
		String[] result = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			result[i] = list.get(i).word();
		}
		return result;
	}

	public static void main(String[] args) {
		String line = "We have developed an approach for analyzing online job advertisements in different domains ( industries ) from different regions worldwide .";
		line="Conditional Random Fields (CRFs) are the state of the art approaches taking the sequence characteristics to do better labeling. ";
		for (HasWord str : StanfordPTBTokenizer.tokenize(line)) {
			System.out.println(str.word());
		}
	}

}
