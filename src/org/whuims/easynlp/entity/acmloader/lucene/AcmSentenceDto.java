package org.whuims.easynlp.entity.acmloader.lucene;

public class AcmSentenceDto {
	int id;
	int offset;
	String text;
	String postag;
	String lattice;

	public AcmSentenceDto(int id, int offset, String text, String postag, String lattice) {
		super();
		this.id = id;
		this.offset = offset;
		this.text = text;
		this.postag = postag;
		this.lattice = lattice;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getLattice() {
		return lattice;
	}

	public void setLattice(String lattice) {
		this.lattice = lattice;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(id).append("\t").append(offset).append("\t").append(text);
		sb.append("]");
		return super.toString();
	}

}
