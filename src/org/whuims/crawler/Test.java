package org.whuims.crawler;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public class Test {
	public static void main(String[] args) {
		Set<String> dict = new HashSet<String>();
		List<String> alines = null;
		try {
			alines = FileUtils.readLines(new File(
					"resource/wordsEn.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (String line : alines) {
			dict.add(line.trim());
		}
		String line = "Our best systems achieve F1-scores of 0.76 on events and 0.72 on flu- ents.";
		StringBuilder sb = new StringBuilder();
		line = line.trim().replace("<p>", "").replace("</p>", "");
		String[] array = line.split("\\s+");
		for (int i = 0; i < array.length; i++) {
			if (array[i].endsWith("-") && i < array.length - 1) {
				String word = array[i].substring(0, array[i].length() - 1)
						+ array[i + 1];
				if (dict.contains(word)) {
					sb.append(word).append(" ");
					i++;
				}else{
					sb.append(array[i].trim());
				}
			} else {
				sb.append(array[i]).append(" ");
			}
		}
		System.out.println(sb.toString());
	}

}
