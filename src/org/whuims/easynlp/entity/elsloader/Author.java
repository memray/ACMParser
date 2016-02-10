package org.whuims.easynlp.entity.elsloader;

public class Author {
	String authorID;
	String bn;
	String fn;
	String email;
	String authorName;
	String affiliation;

	public String getProfile() {
		StringBuilder sb = new StringBuilder();
		sb.append(authorName).append("(");
		sb.append(email).append(")");
		return sb.toString();
	}

	public String getAuthorID() {
		return authorID;
	}

	public void setAuthorID(String authorID) {
		this.authorID = authorID;
	}

	public String getBn() {
		return bn;
	}

	public void setBn(String bn) {
		this.bn = bn;
	}

	public String getFn() {
		return fn;
	}

	public void setFn(String fn) {
		this.fn = fn;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

}
