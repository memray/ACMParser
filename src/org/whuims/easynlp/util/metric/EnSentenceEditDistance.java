package org.whuims.easynlp.util.metric;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.stanford.nlp.util.StringUtils;

/**
 * 计算编辑距离
 * 
 * @author xpqiu
 * @version 1.0
 * @since 1.0
 */
public class EnSentenceEditDistance {
	List<String> wordsA = null;
	List<String> wordsB = null;
	int sizeA = 0;
	int sizeB = 0;

	static Set<String> noCostStringSet = new HashSet<String>();
	static Set<String> maxCostStringSet = new HashSet<String>();

	static {
		noCostStringSet.add("to");
	}

	public EnSentenceEditDistance(List<String> wordsA, List<String> wordsB) {
		this.wordsA = wordsA;
		this.wordsB = wordsB;
		init();
	}

	public EnSentenceEditDistance(String sentA, String sentB) {
		this.wordsA = StringUtils.split(sentA, "\\s+");
		this.wordsB = StringUtils.split(sentB, "\\s+");
		init();
	}

	private void init() {
		for (String str : this.wordsA) {
			if (!noCostStringSet.contains(str))
				sizeA++;
		}
		for (String str : this.wordsB) {
			if (!noCostStringSet.contains(str))
				sizeB++;
		}
	}

	// TODO
	public float calcNormalise() {
		float distance = calc();
		int len = sizeA> sizeB ? sizeA : sizeB;
		return (len - distance) / len;
	}

	/**
	 * 将x转换到y的编辑距离，可以自定义一些代价
	 * 
	 * @param cSeq1
	 * @param cSeq2
	 * @return 距离
	 */
	public float calc() {
		// +1 : 下标为0节点为动态规划的起点
		// cSeq1.length >= cSeq2.length > 1
		int xsLength = wordsA.size() + 1; // > ysLength
		int ysLength = wordsB.size() + 1; // > 2

		float[] lastSlice = new float[ysLength];
		float[] currentSlice = new float[ysLength];

		// first slice is just inserts
		currentSlice[0] = 0;
		for (int y = 1; y < ysLength; ++y)
			currentSlice[y] = currentSlice[y - 1] + costIns(wordsB.get(y - 1)); // y
																				// inserts
																				// down
																				// first
																				// column
																				// of
																				// lattice

		for (int x = 1; x < xsLength; ++x) {
			String strX = wordsA.get(x - 1);
			// /////////exchange between lastSlice and currentSlice////////////
			float[] lastSliceTmp = lastSlice;
			lastSlice = currentSlice;
			currentSlice = lastSliceTmp;
			currentSlice[0] = lastSlice[0] + costDel(wordsA.get(x - 1));
			for (int y = 1; y < ysLength; ++y) {
				int yMinus1 = y - 1;
				String strY = wordsB.get(yMinus1);
				// unfold this one step further to put 1 + outside all mins on
				// match
				currentSlice[y] = Math.min(
						strX.equals(strY) ? lastSlice[yMinus1] // match
								: costReplace(strX, strY) + lastSlice[yMinus1], // 替换代价
						Math.min(costDel(strX) + lastSlice[y], // 删除代价
								costIns(strY) + currentSlice[yMinus1])); // 插入代价
			}
		}
		return currentSlice[currentSlice.length - 1];
	}

	/**
	 * @param c
	 * @return 插入代价
	 */
	protected static float costIns(String str) {
		if (noCostStringSet.contains(str))
			return 0;
		if (maxCostStringSet.contains(str))
			return 5;
		return 1;
	}

	/**
	 * 删除
	 * 
	 * @param c
	 * @return 删除代价
	 */
	protected static float costDel(String str) {
		if (noCostStringSet.contains(str))
			return 0;
		if (maxCostStringSet.contains(str))
			return 5;
		return 1;
	}

	static String[][] repCostStrs = new String[][] { { "C", "G" } };

	/**
	 * x和y肯定不同的
	 * 
	 * @param x
	 * @param y
	 * @return 代价
	 */
	protected static float costReplace(String x, String y) {
		int cost = 2;
		for (String[] xy : repCostStrs) {
			if (xy[0] == x && xy[1] == y) {
				cost = 2;
				break;
			} else if (xy[0] == y && xy[1] == x) {
				cost = 2;
				break;
			}
		}
		return cost;// noCostChars.indexOf(c)!=-1?1:0;
	}

	public float sim() {
		float ld = calc();
		return 1 - ld / Math.max(this.wordsA.size(), this.wordsB.size());
	}

	public static void main(String[] args) {
		String a = "a to b c";
		String b = "a b to c";
		EnSentenceEditDistance ed = new EnSentenceEditDistance(a, b);
		System.out.println(ed.calc());
	}
}
