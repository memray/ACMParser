package org.whuims.easynlp.entity.commonentity;

import edu.stanford.nlp.trees.Tree;
import org.whuims.easynlp.ling.stemmer.porterStemmer.PorterStemmer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class Sentence.
 */
public class Sentence implements ContentUnit, Serializable {
	private static final long serialVersionUID = 1L;

	/** The Constant SPACE. */
	public static final String SPACE = " ";

	private int id;
	/** The words. */
	private List<Word> words = new ArrayList<Word>();

	private String rawText;

	private Tree tree = null;

	/**
	 * word star模式的chunk表示
	 */
	private String lattices = null;

	/**
	 * Instantiates a new sentence.
	 *
	 * @param array
	 *            the array
	 * @param postags
	 *            the postags
	 * @throws SequenceException
	 *             the sequence exception
	 */
	public Sentence(String[] array, String[] postags) throws SequenceException {
		if (array.length != postags.length) {
			throw new SequenceException("words and postags are not of the same length.");
		}
		for (int i = 0; i < array.length; i++) {
			words.add(new Word(array[i], postags[i]));
		}
	}

	public String[] getTokenArray() {
		String[] tokens = new String[this.size()];
		for (int i = 0; i < tokens.length; i++) {
			Word word = this.words.get(i);
			tokens[i] = word.getText();
		}
		return tokens;
	}

	public String[] getPorterTokenArray() {
		String[] tokens = new String[this.size()];
		for (int i = 0; i < tokens.length; i++) {
			Word word = this.words.get(i);
			tokens[i] = PorterStemmer.stemWord(word.getText());
		}
		return tokens;
	}

	public String[] getPostagArray() {
		String[] postags = new String[this.size()];
		for (int i = 0; i < postags.length; i++) {
			Word word = this.words.get(i);
			postags[i] = word.getPostag();
		}
		return postags;
	}

	/**
	 * Instantiates a new sentence.
	 *
	 * @param array
	 *            the array
	 */
	public Sentence(String[] array) {
		for (String str : array) {
			words.add(new Word(str));
		}
	}

	public int size() {
		return this.words.size();
	}

	/**
	 * <p>
	 * 返回句子的文本表示。
	 * </p>
	 * <p>
	 * return the text representattion of a sentence.
	 * </p>
	 *
	 * @return the text
	 */
	public String getText() {
		StringBuilder sb = new StringBuilder();
		for (Word word : words) {
			sb.append(word.getText()).append(Sentence.SPACE);
		}
		return sb.toString().trim();
	}

	/**
	 * return the text with all phrase wrapped with tags <term></term>
	 * @return
     */
	public String getTextPhraseWrapped() {
		StringBuilder sb = new StringBuilder();
		for (Word word : words) {
			if(word.isTerm())
				sb.append("<term>"+word.getText()+"</term>").append(Sentence.SPACE);
			else
				sb.append(word.getText()).append(Sentence.SPACE);
		}
		return sb.toString().trim();
	}

    /**
     * return the text with phrase as well original words.
     * For example, "like Information Retrieval" will be parsed as "like Information Retrieval Information-Retrieval"
     * Actually this method just returns the text, if you wanna config whether return phrase only, you should set the
     *      private final boolean RETAIN_WORDS = true;
     *      in PhraseTokenizer.java to false
     * @return
     */
    public String getTextWithWordAndPhrase() {
        StringBuilder sb = new StringBuilder();
        for (Word word : words) {
            if(word.isTerm())
                sb.append(word.getText()).append(Sentence.SPACE);
            else
                sb.append(word.getText()).append(Sentence.SPACE);
        }
        return sb.toString().trim();
    }

	/**
	 * 返回句子的Pos形式，不包括文本内容。.
	 *
	 * @return the postag
	 */
	public String getPostag() {
		StringBuilder sb = new StringBuilder();
		for (Word word : words) {
			sb.append(word.getPostag()).append(Sentence.SPACE);
		}
		return sb.toString();
	}

	/**
	 * 返回带postag标记的文本内容。.
	 *
	 * @return the postagged text
	 */
	public String getPostaggedText() {
		StringBuilder sb = new StringBuilder();
		for (Word word : words) {
			sb.append(word.getText()).append("_").append(word.getPostag()).append(Sentence.SPACE);
		}
		return sb.toString();
	}

	/**
	 * return the keywords and keyphrases only included in the text
	 * @return
     */
	public ArrayList<String> getKeywords() {
		ArrayList<String> keywordList = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		for (Word word : words) {
			if(word.isTerm())
				keywordList.add(word.getText());
		}
		return keywordList;

	}

	/**
	 * 返回最原始的语句
	 * @return
     */
	public String getRawText() {
		return rawText;
	}

	public void setRawText(String rawText) {
		this.rawText = rawText;
	}

	public List<Word> getWords() {
		return words;
	}

	public void setWords(List<Word> words) {
		this.words = words;
	}

	public static void main(String[] args) {
		Sentence sent = PaperUtils.sentProductOf("Apple is good.");
		System.out.println(sent.getPostaggedText());
	}

	public Tree getTree() {
		return tree;
	}

	public void setTree(Tree tree) {
		this.tree = tree;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLattices() {
		return lattices;
	}

	public void setLattices(String lattices) {
		this.lattices = lattices;
	}


}
