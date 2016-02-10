package org.whuims.easynlp.cfgrammar;

import java.util.ArrayList;
import java.util.List;

public class FGFormula {
	private FGNode startNode;
	private List<FGNode> reWriteNodes = new ArrayList<FGNode>();

	public String description() {
		StringBuilder sb = new StringBuilder();
		sb.append(startNode.getText()).append("\t->\t");
		for (FGNode node : reWriteNodes) {
			sb.append(node.getText()).append("\t");
		}
		return sb.toString().trim();
	}

	public String getWrittenString() {
		StringBuilder sb = new StringBuilder();
		for (FGNode node : reWriteNodes) {
			sb.append(node.getText()).append("\t");
		}
		return sb.toString().trim();
	}

	public boolean isAllTerminal() {
		for (FGNode node : reWriteNodes) {
			if (!node.isTerminal()) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isAllUnTerminal() {
		for (FGNode node : reWriteNodes) {
			if (node.isTerminal()) {
				return false;
			}
		}
		return true;
	}

	public void addReWriteNode(FGNode node) {
		this.reWriteNodes.add(node);
	}

	public FGNode getStartNode() {
		return startNode;
	}

	public void setStartNode(FGNode startNode) {
		this.startNode = startNode;
	}

	public List<FGNode> getReWriteNodes() {
		return reWriteNodes;
	}

	public void setReWriteNodes(List<FGNode> reWriteNodes) {
		this.reWriteNodes = reWriteNodes;
	}

}
