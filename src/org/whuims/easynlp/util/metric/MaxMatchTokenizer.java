package org.whuims.easynlp.util.metric;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.whuims.easynlp.entity.commonentity.Sentence;
import org.whuims.easynlp.entity.commonentity.SequenceException;
import org.whuims.easynlp.entity.commonentity.Word;
import org.whuims.easynlp.phrase.PhraseDictionary;

import edu.stanford.nlp.util.StringUtils;

/**
 * 正向最大匹配算法的实现。
 * 
 * @author Qikai
 *
 */
public class MaxMatchTokenizer {
	private static MaxMatchTokenizer instance = new MaxMatchTokenizer();
	private PhraseDictionary dict;
	private Set<String> ommitWords = new HashSet<String>();

	public static MaxMatchTokenizer createInstance() {
		return instance;
	}

	public MaxMatchTokenizer(PhraseDictionary dict) {
		super();
		this.dict = dict;
	}

	private MaxMatchTokenizer() {
		super();
		dict = new PhraseDictionary();
		try {
			Set<String> ommitSet = new HashSet<String>();
			List<String> lines = FileUtils.readLines(new File("resource/data/phrases/cnki/cnki_bi_keywords_sort.txt"));
			for (String str : lines) {
				String[] array = str.split("\t");
				if (array.length != 3)
					continue;
				str = array[1].trim().toLowerCase();
				if (ommitSet.contains(str)) {
					continue;
				}
				ommitSet.add(str);
				String[] terms = str.split("\\s+");
				if (terms.length > 1) {
					dict.addPhrase(terms, "NN", Integer.parseInt(array[2]));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Sentence longestTokenize(Sentence sent) {
		String[] tokens = sent.getTokenArray();
		List<Word> words = new ArrayList<Word>();
		List<String> tempList = new ArrayList<String>();
		for (int i = 0; i < sent.size(); i++) {
			int offset = i;
			int oldSignal = -1;
			int signal = -1;
			int lastPhrase = -1000;
			while (offset < tokens.length) {
				tempList.add(tokens[offset]);
				signal = dict.exists(tempList);
				if (this.ommitWords.contains(StringUtils.join(tempList, " ").trim())) {
					signal = 0;
				}
				// System.out.println(signal+"\t"+StringUtils.join(tempList,"_"));
				if (signal == 0) {
					if (tempList.size() == 1) {// 当前词不能同词典中任何词汇形成pefix
												// match，该词需要独立成词
						words.add(sent.getWords().get(i));
						i = offset;
					} else {
						// 这意味着起码经过了两轮添加，需要判断oldSignal
						// oldSignal不可能为0，当为0时不可能继续往后走，tempList的长度也就不可能大于1
						tempList.remove(tempList.size() - 1);// 最后一个字符添加后使得prefix
																// match失败，删除此字符。
						if (oldSignal == 1) {
							int delta = checkInnerPhrase(words, lastPhrase, i, tempList);
							for (int z = i + delta; z < offset; z++) {
								sent.getWords().get(z).setIsTerm(false);
								words.add(sent.getWords().get(z));
							}
							i = offset - 1;
						} else if (oldSignal == 2) {
							String token = StringUtils.join(tempList, " ");
							String postag = dict.getWordType(StringUtils.join(tempList, " ").trim());
							Word word = new Word(token, postag);
							if (!postag.equals("VBN")) {
								word.setIsTerm(true);
							}
							word.setIsTerm(true);
							words.add(word);
							i = offset - 1;
						}
					}
					tempList.clear();
					break;
				} else if (signal == 1) {
					// 不做任何处理，继续往前走。
					// 然后，当到了句子末尾时
					if (offset == tokens.length - 1) {
						int delta = checkInnerPhrase(words, lastPhrase, i, tempList);
						for (int j = offset - tempList.size() + 1 + delta; j < tokens.length; j++) {
							words.add(sent.getWords().get(j));
						}
						i = offset + 2;
						break;
					}
				} else {
					// 如果是2，需要继续往前走，看是否有更长的匹配，然而在句子的末尾无法继续往前走，
					// 所以需要判断当到了句末，直接将tempList的内容当作一个词汇加入句中。
					if (offset == tokens.length - 1) {
						// System.out.println("到了最后一个字段");
						String token = StringUtils.join(tempList, " ");
						String postag = dict.getWordType(StringUtils.join(tempList, " ").trim());
						words.add(new Word(token, postag));
						i = offset + 2;
						break;
					}
					lastPhrase = offset;
				}
				offset++;
				oldSignal = signal;
			}

		}
		//
		sent.setWords(words);
		return sent;
	}

	private int checkInnerPhrase(List<Word> words, int lastPhrase, int start, List<String> tempList) {
		if (lastPhrase < 0) {
			return 0;
		} else {
			List<String> list = new ArrayList<String>();
			for (int i = 0; i <= lastPhrase - start; i++) {
				list.add(tempList.get(i));
			}
			String token = StringUtils.join(list, " ");
			String postag = dict.getWordType(StringUtils.join(list, " ").trim());
			words.add(new Word(token, postag));
			return list.size();
		}
	}

	public SegTree tokenize(String line) {
		int id = 0;
		SegTree root = new SegTree();
		root.setId(id++);
		root.setText(line);
		root.setLevel(0);
		String text = root.getText();
		Sentence rootSent = productOf(text);
		rootSent = this.longestTokenize(rootSent);
		for (Word word : rootSent.getWords()) {
			SegTree subTree = new SegTree();
			subTree.setId(id++);
			subTree.setLevel(1);
			subTree.setText(word.getText());
			root.getChildren().add(subTree);
		}
		this.ommitWords.add(text);
		for (SegTree child : root.getChildren()) {
			process(child, 1, id++);
		}
		this.ommitWords.clear();
		return root;
	}

	private void process(SegTree node, int level, int id) {
		Sentence sent = productOf(node.getText());
		this.ommitWords.add(node.getText());
		sent = this.longestTokenize(sent);
		if (sent.getWords().size() == 1) {
			return;
		}
		level++;
		for (Word word : sent.getWords()) {
			SegTree subTree = new SegTree();
			subTree.setId(id);
			subTree.setLevel(level);
			subTree.setText(word.getText());
			process(subTree, level, id);
//			this.ommitWords.add(word.getText());
			node.getChildren().add(subTree);
		}
		this.ommitWords.add(node.getText());
		
	}

	private Sentence productOf(String line) {
		String[] tokens = line.trim().split("[\\s~]+");
		String[] postags = new String[tokens.length];
		Sentence sent = null;
		try {
			sent = new Sentence(tokens, postags);
		} catch (SequenceException e) {
			e.printStackTrace();
		}
		return sent;
	}

	public static void main(String[] args) {
		String line = "CRFs represent the state of the art Wall street journal corpus builder";
		line = "this have been used for a long time";
		line="support vector machine";
		line="support machine";
		line="logistical regression";
		line="inner logistical regression";
		line="logistical regression begin";
		line = "support vector machine based method";
		SegTree tree = MaxMatchTokenizer.createInstance().tokenize(line);
		System.out.println(tree.print());
	}
}
