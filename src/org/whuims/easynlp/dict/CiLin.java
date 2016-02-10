package org.whuims.easynlp.dict;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 本类用来处理《哈工大信息检索研究室同义词词林扩展版》。词表包含 77,343 条词语。
 * </p>
 * <p>
 * 表中的编码位是按照从左到右的顺序排列。第八位的标记有 3 种，分别是“=” 、“#” 、“@” ， “=”代表“相等” 、“同义”
 * 。末尾的“#”代表“不等” 、“同类” ，属于相关词语。末尾的“@”代表“自我封闭” 、“独立” ，它在词典中既没有同义词，也没有相关词。
 * </p>
 * <p>
 * <p>
 * 说明如下：之所以不采用并查集（union-find）实现同义词，原因在于，并查集不能解决这样的问题，如“人”==“士”，“士”==“读书人”，
 * 我们不希望出现 “人”==“读书人”这样的情况。同义词可能是在不同维度上实现的同义词。 文件id说明如下：
 * </p>
 * <img src="cilin.png">
 */

public class CiLin implements SynonymDict {
	private String filePath;
	/**
	 * 词到类别的Map
	 */
	private Map<String, Set<String>> classMap = new HashMap<String, Set<String>>();
	/**
	 * 类别到词表的Map
	 */
	private Map<String, List<String>> wordsMap = new HashMap<String, List<String>>();
	private Set<String> synSet = new HashSet<String>();

	public CiLin(String filePath) {
		super();
		this.filePath = filePath;
		load();
	}

	/**
	 * 找出同义的词对,建造hashset;
	 * 
	 * @return 同义词集合
	 */
	private void load() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(this.filePath), "utf-8"));
			String str = reader.readLine();
			while (str != null && str.length() != 0) {
				String[] strs = str.trim().split(" ");
				String cat = strs[0].trim();
				if (cat.endsWith("=")) {
					int wordNum = strs.length;
					List<String> words = new ArrayList<String>();
					for (int i = 1; i < wordNum; i++) {
						strs[i] = strs[i].trim();
						words.add(strs[i]);
						Set<String> tempCats = this.classMap.get(strs[i]);
						if (tempCats == null) {
							this.classMap.put(strs[i], new HashSet<String>(2));
						}
						this.classMap.get(strs[i]).add(cat);
					}
					this.wordsMap.put(cat, words);
					for (int i = 1; i < wordNum - 1; i++) {
						for (int j = i + 1; j < wordNum; j++) {
							String combine1 = strs[i] + " | " + strs[j];
							synSet.add(combine1);
							String combine2 = strs[j] + " | " + strs[i];
							synSet.add(combine2);
						}
					}
				} else {

				}
				str = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<List<String>> getSynonyms(String term) {
		List<List<String>> results = new ArrayList<List<String>>(2);
		Set<String> set = this.classMap.get(term);
		if (set == null || set.size() == 0)
			return null;
		for (String cat : set) {
			results.add(this.wordsMap.get(cat));
		}
		return results;
	}

	@Override
	public boolean isSynonym(String terma, String termb) {
		return this.synSet.contains(terma.trim() + " | " + termb);
	}

	@Override
	public List<String> getNestedSynonyms(String term) {
		List<String> results = new ArrayList<String>();
		for (List<String> temp : this.getSynonyms(term)) {
			results.addAll(temp);
		}
		return results;
	}

	public Set<String> getSynSet() {
		return synSet;
	}

	public void setSynSet(Set<String> synSet) {
		this.synSet = synSet;
	}

	public static void main(String[] argv) {
		SynonymDict dict = new CiLin("resource/dict/synonym/同义词词林扩展版.txt");
		dict.getSynonyms("人");
	}
}
