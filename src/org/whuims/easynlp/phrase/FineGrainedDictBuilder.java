package org.whuims.easynlp.phrase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public class FineGrainedDictBuilder {
	Set<String> phraseSet = new HashSet<String>();
	List<String> phrases = new ArrayList<String>();

	public void build() throws IOException {
		loadOriginalDict();
		distill();
	}

	private void distill() {

	}

	private void loadOriginalDict() throws IOException {
		List<String> lines = FileUtils.readLines(new File("resource/data/phrases/acm_keywords.txt"));
		lines.addAll(FileUtils.readLines(new File("resource/data/phrases/acl_phrase.txt")));
		for (String str : lines) {
			str = str.trim();
			if (str.contains(" ")) {
				phraseSet.add(str);
			}
		}
		Set<String> removeSet = new HashSet<String>();
		System.out.println(this.phraseSet.size());
		int count=0;
		for (String str : phraseSet) {
			if(count++%1000==0){
				System.out.println(count);
			}
			for (String t : phraseSet) {
				if (!str.equals(t)) {
					if (str.contains(t + " ") || str.contains(" " + t)) {
						removeSet.add(str);
						break;
					}
				}
			}
		}
		for (String str : removeSet) {
			this.phraseSet.remove(str);
		}
		System.out.println(this.phraseSet.size() + "\t" + removeSet.size());
		StringBuilder sb = new StringBuilder();
		for (String str : this.phraseSet) {
			sb.append(str).append("\n");
		}
		FileUtils.write(new File("resource/data/phrases/distilled_phrases.txt"), sb.toString().trim());
	}

	public static void main(String[] args) {
		FineGrainedDictBuilder builder = new FineGrainedDictBuilder();
		try {
			builder.build();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
