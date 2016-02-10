package org.whuims.easynlp.entity.aclloader;

import org.whuims.easynlp.entity.commonentity.PaperUtils;
import org.whuims.easynlp.entity.commonentity.Sentence;
import org.whuims.easynlp.phrase.PhraseRegexTokenizer;

public class Test {
	public static void main(String[] args) {
		String text="These include rule-based systems [ Krupka 1998 ] , Hidden Markov Models ( HMM ) [ Bikel et al. 1997 ] and Maximum Entropy Models ( MaxEnt ) [ Borthwick 1998 ] .";
		Sentence sent=PaperUtils.sentProductOf(text);
		sent=PhraseRegexTokenizer.createInstance().tokenize(sent);
		System.out.println(text);
		System.out.println(sent.getPostaggedText());
	}

}
