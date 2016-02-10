package org.whuims.easynlp.exverb;

import java.util.HashMap;
import java.util.Map;

public class TagFunction {
	private static Map<String, Integer> tagMap = new HashMap<String, Integer>();
	static {
		tagMap.put("NN", 1);
		tagMap.put("NNP", 1);
		tagMap.put("NNPS", 1);
		tagMap.put("NNS", 1);

		tagMap.put("VB", 2);// 动词符号为2
		tagMap.put("VBD", 2);
		tagMap.put("VBG", 2);
		tagMap.put("VBP", 2);
		tagMap.put("VBZ", 2);
		tagMap.put("VBN", 2);//做OpenIE时去掉此行。

		tagMap.put("JJ", 3);// 形容词为3
		tagMap.put("JJR", 3);
		tagMap.put("JJS", 3);

		tagMap.put("MD", 4);// 副词符号为4
		tagMap.put("RB", 4);
		tagMap.put("RBR", 4);
		tagMap.put("RBS", 4);
		tagMap.put("RP", 4);

		tagMap.put("", 5);
		tagMap.put("$", 8);
		tagMap.put("''", 8);
		tagMap.put("(", 8);
		tagMap.put(")", 8);
		tagMap.put(",", 8);
		tagMap.put("--", 8);
		tagMap.put(".", 8);
		tagMap.put(":", 8);
		tagMap.put("``", 8);
		tagMap.put("POS", 9);
		tagMap.put("CC", 9);
	}

	public static boolean isVerb(String type) {
		return TagFunction.tagMap.containsKey(type.trim()) && TagFunction.tagMap.get(type.trim())==2;
	}

	public static int getSuperType(String type) {
		if (tagMap.containsKey(type)) {
			return tagMap.get(type);
		}
		return 0;
	}

	public static boolean isVerbAndExtra(String type) {
		type = type.trim();
		return tagMap.containsKey(type)
				&& (tagMap.get(type) == 2 || tagMap.get(type) == 4 || type
						.equals("CC"));
	}

	public static boolean isAdv(String type) {
		return tagMap.containsKey(type) && tagMap.get(type) == 4;
	}

	public static boolean isNoun(String type) {
		return tagMap.containsKey(type) && tagMap.get(type) == 1;
	}

	public static boolean isNounOrExtra(String type) {
		return isNoun(type);
	}

	public static boolean isAdj(String type) {
		return tagMap.containsKey(type) && tagMap.get(type) == 3;
	}

	public static boolean isNounOrAdj(String type) {
		return isNoun(type) || isAdj(type);
	}

	public static boolean isNounOrAdjOrDet(String type) {
		return isNounOrAdj(type) || isDet(type);
	}

	private static boolean isDet(String type) {
		return type.equals("DT") || type.equals("CD");
	}

	/**
	 * Collapsed Tree中忽略的词性
	 * 
	 * @param postag
	 * @return
	 */
	public static boolean isOmitInTreeParser(String postag) {
		return tagMap.containsKey(postag) && tagMap.get(postag) >= 8;
	}

}
