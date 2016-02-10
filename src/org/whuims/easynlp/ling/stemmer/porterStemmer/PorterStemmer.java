package org.whuims.easynlp.ling.stemmer.porterStemmer;

import java.util.HashMap;
import java.util.Map;

import org.whuims.easynlp.ling.stemmer.Stemmer;

public class PorterStemmer extends Stemmer {
	private static Map<String, String> stemCache = new HashMap<String, String>();

	public static String stemWord(String str) {
		str = str.trim();
		if (stemCache.containsKey(str)) {
			return stemCache.get(str);
		} else {
			char[] array = str.toCharArray();
			PorterStemmerImpl stemmer = new PorterStemmerImpl();
			for (char ch : array) {
				stemmer.add(ch);
			}
			stemmer.stem();
			String result = stemmer.toString();
			stemCache.put(str, result);
			return result;
		}
	}

	public static String stemLine(String line) {
		StringBuilder sb = new StringBuilder();
		String array[] = line.split("\\s+");
		for (String str : array) {
			sb.append(stemWord(str)).append(" ");
		}
		return sb.toString().trim();
	}

	public static void main(String[] args) {
		System.out.println(PorterStemmer.stemLine("interesting books "));
	}

	@Override
	protected String stemTerm(String term) {
		term = term.trim();
		char[] array = term.toCharArray();
		PorterStemmerImpl stemmer = new PorterStemmerImpl();
		for (char ch : array) {
			stemmer.add(ch);
		}
		stemmer.stem();
		String result = stemmer.toString();
		return result;
	}

}
