package org.whuims.easynlp.util.metric;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.whuims.easynlp.ling.stemmer.porterStemmer.PorterStemmer;
import org.whuims.easynlp.util.datastructure.UnionFindTree;

/**
 * 所有的文本在处理之前都被porter处理了，并且进行了lowercase处理。
 * 
 * @author Qikai
 *
 */
public class EnSentenceEditDistanceWithSemantic extends EnSentenceEditDistance {
	public static final int wordLen = 9;
	private static UnionFindTree<String> ufTree = new UnionFindTree<String>();

	static {
		try {
			List<String> lines = FileUtils.readLines(new File("resource/data/phrases/cnki/cnki_bi_keywords_sort.txt"));
			for (String line : lines) {
				String[] array = line.split("\t");
				if (array.length != 3)
					continue;
				String cnWord = array[0].toLowerCase().trim();
				String enWord = array[1].toLowerCase().trim();
				enWord = PorterStemmer.stemLine(enWord);
				int count = Integer.parseInt(array[2]);
				if (count > 0)
					ufTree.union(cnWord, enWord);
			}
			for (String str : FileUtils.readLines(new File("resource/data/editdistance/noCostWords.txt"))) {
				EnSentenceEditDistance.noCostStringSet.add(PorterStemmer.stemWord(str.trim()).toLowerCase());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public EnSentenceEditDistanceWithSemantic(List<String> wordsA, List<String> wordsB) {
		super(wordsA, wordsB);
	}

	public EnSentenceEditDistanceWithSemantic(String sentA, String sentB) {
		super(PorterStemmer.stemLine(sentA.toLowerCase()), PorterStemmer.stemLine(sentB.toLowerCase()));
	}

	/**
	 * 将x转换到y的编辑距离，可以自定义一些代价
	 */
	public float calc() {
		float d[][]; // 矩阵
		int n = super.wordsA.size();
		int m = super.wordsB.size();
		int i; // 遍历str1的
		int j; // 遍历str2的
		if (n == 0) {
			return m;
		}
		if (m == 0) {
			return n;
		}
		d = new float[n + 1][m + 1];
		for (i = 0; i <= n; i++) { // 初始化第一列
			d[i][0] = i;
		}
		for (j = 0; j <= m; j++) { // 初始化第一行
			d[0][j] = j;
		}
		for (i = 1; i <= n; i++) { // 遍历str1，n=str1.length()
			String strX = super.wordsA.get(i - 1);
			// 去匹配str2
			for (j = 1; j <= m; j++) {
				// 根据同义计算未来代价
				for (int ii = 1; ii <= wordLen; ii++) {
					if (ii + i - 1 > super.wordsA.size())
						break;
					for (int jj = 1; jj <= wordLen; jj++) {
						if (jj + j - 1 > super.wordsB.size())
							break;
						String tempA = union(i - 1, ii + i - 1, super.wordsA);
						String tempB = union(j - 1, jj + j - 1, super.wordsB);
						// System.out.println(tempA + "\t|\t" + tempB + "\t" +
						// ufTree.isSameGroup(tempA, tempB));
						if (ufTree.isSameGroup(tempA, tempB)) {
							if (d[i + ii - 1][j + jj - 1] > 0)
								d[i + ii - 1][j + jj - 1] = Math.min(d[i + ii - 1][j + jj - 1], d[i - 1][j - 1] + 0.1f);
							else
								d[i + ii - 1][j + jj - 1] = d[i - 1][j - 1] + 0.1f;
						}

					}
				}
				String strY = super.wordsB.get(j - 1);
				float temp = (strX.equals(strY) ? d[i - 1][j - 1] // match
						: costReplace(strX, strY) + d[i - 1][j - 1]);
				if (d[i][j] > 0) {
					temp = Math.min(temp, d[i][j]);
				}
				d[i][j] = Math.min(temp, // 替换代价
						Math.min(costDel(strX) + d[i - 1][j], // 删除代价
								costIns(strY) + d[i][j - 1])); // 插入代价
			}
		}
		return d[n][m];
	}

	private String union(int start, int end, List<String> list) {
		StringBuilder sb = new StringBuilder();
		for (int i = start; i < end; i++) {
			sb.append(list.get(i)).append(" ");
		}
		return sb.toString().trim();
	}

	public static void main(String[] args) {
		String str1 = "Loongson processor";
		String str2 = "Godson CPU";
		EnSentenceEditDistanceWithSemantic ed = new EnSentenceEditDistanceWithSemantic(str1, str2);
		System.out.println("etd=" + ed.calc());
		System.out.println("sim=" + ed.sim());
	}

}
