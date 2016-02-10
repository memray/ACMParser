package org.whuims.easynlp.phrase;

import org.whuims.easynlp.entity.commonentity.Sentence;
import org.whuims.easynlp.entity.commonentity.Word;
import org.whuims.easynlp.exverb.TagFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * 1、处理类似于 NN + based的搭配
 * 
 * @author Qikai
 *
 */
public class PhraseRegexTokenizer {
	private static PhraseRegexTokenizer instance = new PhraseRegexTokenizer();

	private PhraseRegexTokenizer() {

	}

	public static PhraseRegexTokenizer createInstance() {
		return instance;
	}

	public Sentence tokenize(Sentence sent) {
		sent = WordAdjust(sent);
		sent = CitationAdjust(sent);
		return sent;
	}

    /**
     * Deal with citation, seems only suitable for ACL paper for now
     * @param sent
     * @return
     */
	private Sentence CitationAdjust(Sentence sent) {
		String[] tokens=sent.getTokenArray();
		List<Word> words=new ArrayList<Word>();
		for(int i=0;i<tokens.length;i++){
			if(tokens[i].equals("[")){
				StringBuilder sb=new StringBuilder();
				int j=i;
				for(j=i;j<tokens.length;j++){
					sb.append(tokens[j]).append(" ");
					if(tokens[j].trim().equals("]")){						
						break;
					}
				}
				if(isCitation(sb.toString().trim())){
					String token = sb.toString().trim();
					String postag = "NNP";
					Word word = new Word(token, postag);
					word.setIsTerm(false);
					words.add(word);
					i=j;
				}else{
				}
			}else{
				words.add(sent.getWords().get(i));
			}
		}
		sent.setWords(words);
		return sent;
	}

	private boolean isCitation(String text) {
		if(text.matches("[\\(\\[].+?\\d{4}\\s?\\w?\\s?[\\)\\]]")){
			return true;
		}
		return false;
	}

	/**
	 * Tackle the words like 'xx based' to 'xx_based'
	 * @param sent
	 * @return
     */
	private Sentence WordAdjust(Sentence sent) {
		String[] tokens = sent.getTokenArray();
		List<Word> words = new ArrayList<Word>();
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].toLowerCase().trim().equals("based")) {
				if (i + 1 < tokens.length
						&& !TagFunction.isNoun(sent.getPostagArray()[i + 1])) {
					words.add(sent.getWords().get(i));
				} else if (i > 0
						&& sent.getPostagArray()[i - 1].startsWith("NN")) {
					words.remove(words.size() - 1);
					String token = tokens[i - 1] + "_based";
					String postag = "JJ";
					Word word = new Word(token, postag);
					word.setIsTerm(true);
					words.add(word);
				} else {
					words.add(sent.getWords().get(i));
				}
			} else {
				words.add(sent.getWords().get(i));
			}
		}
		sent.setWords(words);
		return sent;
	}

}
