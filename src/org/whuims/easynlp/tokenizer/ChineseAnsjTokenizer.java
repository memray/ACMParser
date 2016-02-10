package org.whuims.easynlp.tokenizer;

import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.util.MyStaticValue;

public class ChineseAnsjTokenizer {
	private static ChineseAnsjTokenizer instance = new ChineseAnsjTokenizer();

	private ChineseAnsjTokenizer() {
		MyStaticValue.userLibrary = "resource/data/phrases/cnki/cnki_cn_keywords_sort.txt";
	}

	public static ChineseAnsjTokenizer createInstance() {
		return instance;
	}

	public List<Term> tokenize(String line) {
		return NlpAnalysis.parse(line);
	}

	public String tokenizeToString(String line) {
		StringBuilder sb = new StringBuilder();
		for (Term term : this.tokenize(line)) {
			sb.append(term.getName()).append(" ");
		}
		return sb.toString().trim();
	}

	public List<String> tokenizeToList(String line) {
		List<String> result = new ArrayList<String>();
		List<Term> terms = NlpAnalysis.parse(line);
		for (Term term : terms) {
			result.add(term.getName());
		}
		return result;
	}

	public static void main(String[] args) {
		System.out.println(ChineseAnsjTokenizer.createInstance().tokenize("基于bp神经网络的支持向量机的机器翻译模型研究。"));
	}

}
