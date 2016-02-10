package org.whuims.easynlp.util.metric;

public class TextSimilarityWithSynonym {
	private SegTree segTreeA;
	private SegTree segTreeB;

	public TextSimilarityWithSynonym(String textA, String textB) {
		this.segTreeA = MaxMatchTokenizer.createInstance().tokenize(textA);
		this.segTreeB = MaxMatchTokenizer.createInstance().tokenize(textB);
	}

	public double cal() {
		return 0d;
	}

}
