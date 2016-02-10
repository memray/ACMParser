package org.whuims.easynlp.cfgrammar;

public class FGNode {
	private String name;
	private String text;
	private boolean isTerminal = false;
	private static FGNode newLineNode = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isTerminal() {
		return isTerminal;
	}

	public void setTerminal(boolean isTerminal) {
		this.isTerminal = isTerminal;
	}

	public static FGNode terminalProductOf(String str) {
		FGNode node = new FGNode();
		node.setName(str);
		node.setText(str);
		node.setTerminal(true);
		return node;
	}

	public static FGNode unTerminalProductOf(String str) {
		FGNode node = new FGNode();
		node.setName(str);
		node.setText(str);
		node.setTerminal(false);
		return node;
	}

	public static FGNode newLineNodeProductOf() {
		if (newLineNode == null) {
			newLineNode = new FGNode();
			newLineNode.setName("newLine");
			newLineNode.setTerminal(true);
			newLineNode.setText("\r\n");
		}
		return newLineNode;
	}

	public static void main(String[] args) {
		System.out.println(Character.isLowerCase('!'));
	}

}
