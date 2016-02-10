package org.whuims.easynlp.entity.commonentity;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class Word is a abstract representation of word.
 */
public class Word implements Serializable, ContentUnit {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The text of given word. */
	private String text;

	/** The postag of given word. */
	private String postag;

	private boolean isTerm = false;

	/**
	 * Instantiates a new word.
	 *
	 * @param text
	 *            the text
	 * @param postag
	 *            the postag
	 */
	public Word(String text, String postag) {
		super();
		this.text = text;
		this.postag = postag;
	}

	/**
	 * Instantiates a new word.
	 *
	 * @param text
	 *            the text
	 */
	public Word(String text) {
		super();
		this.text = text;
	}

	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText() {
//		if (this.text.equals("-LRB-")) {
//			return "(";
//		} else if (this.text.equals("-RRB-")) {
//			return ")";
//		}else if(this.text.equals("-LSB-")){
//			return "[";
//		}else if(this.text.equals("-RSB-")){
//			return "]";
//		}else if(this.text.equals("-LCB-")){
//			return "{";
//		}else if(this.text.equals("-RCB-")){
//			return "}";
//		}
		return text;
	}

	/**
	 * Sets the text.
	 *
	 * @param text
	 *            the new text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Gets the postag.
	 *
	 * @return the postag
	 */
	public String getPostag() {
		return postag;
	}

	/**
	 * Sets the postag.
	 *
	 * @param postag
	 *            the new postag
	 */
	public void setPostag(String postag) {
		this.postag = postag;
	}

	@Override
	public String getRawText() {
		return this.text;
	}

    /**
     * Return it's a phrase or not
     * @return
     */
	public boolean isTerm() {
		return isTerm;
	}

	public void setIsTerm(boolean isTerm) {
		this.isTerm = isTerm;
	}

}
