package org.whuims.easynlp.dict;

import java.util.List;

public interface SynonymDict {
	/**
	 * 返回某一个词的同义词列表
	 * 
	 * @param term
	 * @return
	 */
	public List<List<String>> getSynonyms(String term);

	/**
	 * 返回某一个词可能的所有同义词。
	 * 
	 * @param term
	 * @return
	 */
	public List<String> getNestedSynonyms(String term);

	/**
	 * 判断两个词是否是同义词
	 * 
	 * @param terma
	 * @param termb
	 * @return
	 */
	public boolean isSynonym(String terma, String termb);
}
