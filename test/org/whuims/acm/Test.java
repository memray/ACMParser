package org.whuims.acm;

import org.whuims.easynlp.parserwrapper.StanfordPostagger;

public class Test {
	public static void main(String[] args) {
		System.out.println(StanfordPostagger.createInstance().postag("present"));
	}

}
