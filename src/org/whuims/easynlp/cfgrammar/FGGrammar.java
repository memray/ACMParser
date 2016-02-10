package org.whuims.easynlp.cfgrammar;

import java.util.List;

public class FGGrammar {
	private String filePath;
	private FGDictionary dict = null;

	public FGGrammar(String filePath) {
		super();
		this.filePath = filePath;
		loadDict();
	}

	private void loadDict() {
		this.dict = new FGDictionary(this.filePath);
	}

	public String randomAbstractGenerate() {
		FGNode startNode = dict.getNode("SCI_ABSTRACT");
		return randomGenerateWithNode(startNode);
	}
	
	public String randomPaperGenerate() {
		FGNode startNode = dict.getNode("SCIPAPER_LATEX");
		return randomGenerateWithNode(startNode);
	}

	private String randomGenerateWithNode(FGNode node) {
		StringBuilder sb = new StringBuilder();
		List<FGFormula> formulas = dict.getFormulasByStartNode(node);
		FGFormula formula = formulas.get((int) (Math.random() * formulas.size()));
		for (FGNode seqNode : formula.getReWriteNodes()) {
			if(seqNode.isTerminal()){
				sb.append(seqNode.getText()).append(" ");
			}else{
				sb.append(randomGenerateWithNode(seqNode));
			}
		}
		return sb.toString();
	}

	class Pair {
		String text;
		boolean allTerminal = false;

		public Pair(String text, boolean allTerminal) {
			super();
			this.text = text;
			this.allTerminal = allTerminal;
		}

	}

	public FGDictionary getDict() {
		return dict;
	}

	public static void main(String[] args) {
		String filePath = "resource/data/scicfg/scirules_cheng.in";
		FGGrammar grammar = new FGGrammar(filePath);
		System.out.println(grammar.randomAbstractGenerate());
//		System.out.println(grammar.randomPaperGenerate());
	}

}
