package org.whuims.easynlp.util.metric;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.util.StringUtils;

public class SegTree {
	private int id;
	private int level;
	private String text;
	private List<String> segs = new ArrayList<String>();
	private List<SegTree> children = new ArrayList<SegTree>();

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the segs
	 */
	public List<String> getSegs() {
		return segs;
	}

	/**
	 * @param segs
	 *            the segs to set
	 */
	public void setSegs(List<String> segs) {
		this.segs = segs;
	}

	/**
	 * @return the children
	 */
	public List<SegTree> getChildren() {
		return children;
	}

	/**
	 * @param children
	 *            the children to set
	 */
	public void setChildren(List<SegTree> children) {
		this.children = children;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String print(){
		StringBuilder sb=new StringBuilder();
		if(this.children.size()==0){
			return StringUtils.repeat("\t", this.level)+level+":"+this.text;
		}
		sb.append(StringUtils.repeat("\t", level)+level+":"+this.getText()).append("\r\n");
		for(SegTree tree:this.children){
			sb.append(tree.print()+"\r\n");
		}
		return sb.toString().replace("\r\n\r\n", "\r\n");
	}
}
