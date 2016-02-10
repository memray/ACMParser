package org.whuims.easynlp.ling.wordnet;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;

public class CollocationCollector {
	public static void main(String[] args) {

		// construct the URL to the Wordnet dictionary directory
		String path = "D:/Program/WordNet/2.1/dict/";
		URL url = null;
		try {
			url = new URL("file", null, path);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		// construct the dictionary object and open it
		IDictionary dict = new Dictionary(url);
		try {
			dict.open();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// look up first sense of the word "dog "
		IIndexWord idxWord = dict.getIndexWord("many", POS.ADJECTIVE);
		IWordID wordID = idxWord.getWordIDs().get(0);
		IWord word = dict.getWord(wordID);
		System.out.println("Id = " + wordID);
		System.out.println(" Lemma = " + word.getLemma());
		System.out.println(" Gloss = " + word.getSynset().getGloss());
		for(IWord sysWord:word.getSynset().getWords()){
			System.out.println(sysWord.getLemma());
		}
		
	}
}
