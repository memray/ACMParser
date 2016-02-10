package org.whuims.easynlp.cfgrammar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class FGDictionary {
	private String filePath = "";
	private Map<String, FGNode> stringNodeMap = new HashMap<String, FGNode>();
	private Map<FGNode, Integer> nodeTypeMap = new HashMap<FGNode, Integer>();
	// 存储某一个header开头的所有formula
	private Multimap<FGNode, FGFormula> multiMap = HashMultimap.create();
	//
	private Multimap<Integer, FGNode> typeNodeMap = HashMultimap.create();

	public FGDictionary(String filePath) {
		this.filePath = filePath;
		try {
			List<String> lines = new ArrayList<String>();
			List<String> temp = FileUtils.readLines(new File(this.filePath));
			for (String line : temp) {
				if (!line.startsWith("#"))
					lines.add(line);
			}
			loadUnTerminalNodes(lines);
			loadTerminallNodes(lines);
			loadFormulas(lines);
			FindNodeTypes();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<FGFormula> getFormulasByStartNode(FGNode node) {
		List<FGFormula> formulas = new ArrayList<FGFormula>();
		formulas.addAll(this.multiMap.get(node));
		return formulas;
	}

	/**
	 * 查找特定类型的所有节点，类型说明如下：
	 * <ul>
	 * <li>0：终结符</li>
	 * <li>1：只能改写为终结符的非终结符</li>
	 * <li>2：只能改写为非终结符的非终结符</li>
	 * <li>3：都可以的终结符</li>
	 * </ul>
	 */
	public List<FGNode> getNodeByType(int type) {
		Collection<FGNode> nodes = this.typeNodeMap.get(type);
		List<FGNode> result = new ArrayList<FGNode>(nodes.size());
		result.addAll(nodes);
		return result;
	}

	/**
	 * 0:终结符<br/>
	 * 1：只能改写为终结符的非终结符<br/>
	 * 2：只能改写为非终结符的非终结符<br/>
	 * 3：都可以的终结符<br/>
	 */
	private void FindNodeTypes() {
		for (Entry<String, FGNode> entry : this.stringNodeMap.entrySet()) {
			FGNode node = entry.getValue();
			if (node.isTerminal()) {
				this.nodeTypeMap.put(node, 0);
				this.typeNodeMap.put(0, node);
			} else {
				boolean allTerminal = true;
				boolean allUnTerminal = true;
				for (FGFormula formula : this.multiMap.get(node)) {
					if (!formula.isAllTerminal()) {
						allTerminal = false;
					}
					if (!formula.isAllUnTerminal()) {
						allUnTerminal = false;
					}
				}
				if (allTerminal) {
					this.nodeTypeMap.put(node, 1);
					this.typeNodeMap.put(1, node);
				} else if (allUnTerminal) {
					this.nodeTypeMap.put(node, 2);
					this.typeNodeMap.put(2, node);
				} else {
					this.nodeTypeMap.put(node, 3);
					this.typeNodeMap.put(3, node);
				}
			}
		}
	}

	private void loadUnTerminalNodes(List<String> lines) {
		for (String line : lines) {
			if (line.startsWith("#")) {
				continue;
			}
			if (!line.startsWith("\\s+") && !line.trim().equals("")) {
				String[] array = line.split("\\s+");
				if (array.length > 1) {
					String str = array[0].trim();
					if (str.equals("")) {
						continue;
					}
					FGNode node = FGNode.unTerminalProductOf(str);
					this.stringNodeMap.put(str, node);
				}
			}
		}
	}

	private void loadTerminallNodes(List<String> lines) throws IOException {
		// 已加载所有非终结符，接下来出现的字符串如果围在sringNodeMap中出现便都是终结符。
		for (String line : lines) {
			if (line.startsWith("#")) {
				continue;
			}
			if (!line.trim().equals("")) {
				String[] array = line.split("\\s+");
				for (int i = 0; i < array.length; i++) {
					String str = array[i].trim();
					if (str.equals("")) {
						continue;
					}
					if (this.stringNodeMap.containsKey(str)) {

					} else {
						FGNode node = FGNode.terminalProductOf(str);
						this.stringNodeMap.put(str, node);
					}
				}
			}
		}
	}

	private void loadFormulas(List<String> lines) {
		boolean interSide = false;
		String currentHeader = "";
		List<String> tails = new ArrayList<String>();
		for (int i = 0; i < lines.size(); i++) {
			if (lines.get(i).startsWith("#")) {
				continue;
			}
			String line = lines.get(i).trim();
			if (!interSide && line.endsWith("{")
					&& !this.stringNodeMap.get(line.replace("{", "").trim()).isTerminal()) {
				currentHeader = line.replace("{", "").trim();
				interSide = true;
			} else if (interSide && line.equals("}")) {
				interSide = false;
				processInsider(currentHeader, tails);
				currentHeader = "";
				tails = new ArrayList<String>();
			} else{
				if (interSide) {
					tails.add(line);
				} else {
					processLine(line);
				}
			}
		}
	}

	private void processLine(String line) {
		String[] array = line.trim().split("\\s+");
		if (array.length > 1) {
			FGNode header = this.getNode(array[0].trim());
			FGFormula formula = new FGFormula();
			formula.setStartNode(header);
			for (int i = 1; i < array.length; i++) {
				FGNode node = this.getNode(array[i].trim());
				formula.addReWriteNode(node);
			}
			this.multiMap.put(header, formula);
		}
	}

	private void processInsider(String currentHeader, List<String> tails) {
		FGFormula formula = new FGFormula();
		FGNode headNode = this.getNode(currentHeader);
		formula.setStartNode(headNode);
		for (String str : tails) {
			str = str.trim();
			if (str.equals("")) {
				formula.addReWriteNode(FGNode.newLineNodeProductOf());
				continue;
			}
			String[] array = str.split("\\s+");
			for (String s : array) {
				formula.addReWriteNode(this.getNode(s));
			}
		}
		this.multiMap.put(headNode, formula);
	}

	public FGNode getNode(String str) {
		FGNode node = this.stringNodeMap.get(str);
		if (node == null) {
			System.out.println("------->" + str);
		}
		return node;
	}

}
