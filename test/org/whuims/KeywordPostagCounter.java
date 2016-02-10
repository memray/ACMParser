package org.whuims;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.whuims.easynlp.parserwrapper.StanfordPostagger;

public class KeywordPostagCounter {
	public static void main(String args[]) {
		Map<Character, Integer> map = new TreeMap<Character, Integer>();
		List<String> lines = null;
		try {
			lines = FileUtils.readLines(new File("resource/data/phrases/acm_keywords.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (String str : lines) {
			String postagStr = StanfordPostagger.createInstance().postag(str);
			for (String temp : postagStr.split("\\s+")) {
				String[] array = temp.split("_");
				if (array.length != 2) {
					continue;
				}
				String postag = temp.split("_")[1];
				if (!map.containsKey(postag.charAt(0))) {
					map.put(postag.charAt(0), 0);
				}
				int value = map.get(postag.charAt(0));
				value++;
				map.put(postag.charAt(0), value);
			}
		}
		for (Entry<Character, Integer> entry : map.entrySet()) {
			System.out.println(entry.getKey() + "\t" + entry.getValue());
		}
	}

}
