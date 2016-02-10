package org.whuims.easynlp.util.metric;

import org.whuims.easynlp.ling.stemmer.porterStemmer.PorterStemmer;

public class Test {
	public static void main(String[] args) {
		System.out.println(PorterStemmer.stemLine("interesting books"));
	}
}
