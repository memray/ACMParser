package org.whuims.easynlp.entity.aclloader.lucene;

public class SentenceDto {
	private String filePath;
	private int offset;
	private String text;
	private String oriText;
	private String postag;
	private String oriPostag;

	public SentenceDto(String filePath, String text, String oriText,
			String postag, String oriPostag, int offset) {
		super();
		this.filePath = filePath;
		this.text = text;
		this.oriText = oriText;
		this.postag = postag;
		this.setOriPostag(oriPostag);
		this.offset = offset;
	}

	public SentenceDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getPostag() {
		return postag;
	}

	public void setPostag(String postag) {
		this.postag = postag;
	}

	public String getOriText() {
		return oriText;
	}

	public void setOriText(String oriText) {
		this.oriText = oriText;
	}

	public String getOriPostag() {
		return oriPostag;
	}

	public void setOriPostag(String oriPostag) {
		this.oriPostag = oriPostag;
	}

}
