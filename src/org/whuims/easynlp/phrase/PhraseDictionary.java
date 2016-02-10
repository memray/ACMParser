package org.whuims.easynlp.phrase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * A structure store all the phrases
 */
public class PhraseDictionary {
	/**
	 * an index, store the first word of all the phrases, PhraseNode contains all the phrases start with the given word
	 */
	private Map<String, PhraseNode> map = new HashMap<String, PhraseNode>();
	/**
	 * phrase with its postag
	 */
	private Map<String, String> wordTypeMap = new HashMap<String, String>();

	/**
	 * Add a new phrase into the dictionaries, both map and wordTypeMap
	 * @param words phrase in a word array
	 * @param type postag
	 * @param freq frequency, work as a weight(seems not useful)
     * @return boolean shows update successfully or not
     */
	public boolean addPhrase(String[] words, String type, int freq) {
		if (words == null || words.length == 0) {
			return false;
		}
		// if first word of this phrase does not exists in our dict map, add a new node of it with a new PhraseNode
		if (!map.containsKey(words[0])) {
			map.put(words[0], new PhraseNode());
		}
        // add this phrase to the corresponding value
		PhraseNode node = map.get(words[0]);
		node.add(words, 0, type, freq);
		this.wordTypeMap.put(StringUtils.join(words, " ").trim().toLowerCase(),
				type);
		return true;
	}

	/**
	 * Check whether the given phrase exists in the dictionary
	 * @param words
	 * @return a signal, 0 denotes not found, 2 denotes exactly match, 1 means match partly
     */
	public int exists(String words[]) {
		PhraseNode node = this.getPhraseNode(words);
		if (node != null) {
			if (node.getTypes().size() > 0) {
				return 2;
			} else {
				return 1;
			}
		} else {
			return 0;
		}
	}

	/**
	 * Check whether the given words is included in the dictionary
	 * @param words
	 * @return
     */
	public int exists(List<String> words) {
		String[] temp = new String[words.size()];
		words.toArray(temp);
		//convert list to an array
		for (int i = 0; i < temp.length; i++) {
			temp[i] = temp[i].toLowerCase();
		}
		return exists(temp);
	}

	/**
	 * Find the phrase in dictionary
	 * @param words
	 * @return
     */
	public PhraseNode getPhraseNode(String words[]) {
		if (words == null || words.length == 0) {
			return null;
		}
		//if the map contains the first word of given phrase, then try to find whether this phrase exists in PhraseNode
		if (this.map.containsKey(words[0].toLowerCase())) {
//			System.out.println("继续查找");
//			System.out.println(words[0].toLowerCase());
			PhraseNode node = this.map.get(words[0]).find(words, 0);
			return node;
		}
		return null;
	}

	public int getSize() {
		return this.wordTypeMap.size();
	}

	public String getWordType(String word) {
		word = word.trim().toLowerCase();
		return this.wordTypeMap.get(word);
	}

	public static void main(String[] args) {
		PhraseDictionary dict = new PhraseDictionary();
		dict.addPhrase("support vector machine".split(" "), "noun", 1);
		dict.addPhrase("support vector machines".split(" "), "noun", 1);
		dict.addPhrase("support vector machine".split(" "), "noun", 1);
		dict.addPhrase("support vector".split(" "), "noun", 1);
		dict.addPhrase("support".split(" "), "noun", 1);
		dict.addPhrase("maximum entropy modeling".split(" "), "noun", 1);
		System.out.println("Does 'support vector machine learning' exist in dict?\tNo, "+dict.exists("support vector machine learning"
				.split(" ")));
		System.out.println("Does 'support vector' exist in dict?\tYes, "+dict.exists("support vector".split(" ")));
		System.out.println("Does 'maximum entropy' exist in dict?\tNo but belong to part of existing term, "+dict.exists("maximum entropy".split(" ")));
		System.out.println("Size of dict:\t"+dict.getSize());
		List<String> wordList = new ArrayList<String>();
		wordList.add("support");
		wordList.add("vector");
		wordList.add("machine");
		System.out.println(StringUtils.join(wordList, "_"));
		System.out.println(dict.exists(wordList));
	}

}
