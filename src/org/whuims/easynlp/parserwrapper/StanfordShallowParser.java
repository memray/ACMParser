package org.whuims.easynlp.parserwrapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.whuims.easynlp.entity.commonentity.PaperUtils;
import org.whuims.easynlp.entity.commonentity.Sentence;
import org.whuims.easynlp.phrase.PhraseTokenizer;
import org.whuims.easynlp.util.Config;
import org.whuims.function.cupervised.CuperSpan;

import edu.stanford.nlp.trees.Tree;
import opennlp.tools.util.Span;

public class StanfordShallowParser {

	/** The instance. */
	private static StanfordShallowParser instance = new StanfordShallowParser();
	private Map<String, String> phraseTagMap = new HashMap<String, String>();

	private StanfordShallowParser() {
		super();
		init();
	}

	private void init() {
		try {
			for (String str : FileUtils.readLines(new File(Config.getProp("Penn_Phrase_Tag_File")))) {
				String[] array = str.split(" - ");
				phraseTagMap.put(array[0].trim(), array[1].trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the single instance of StanfordParser.
	 *
	 * @return single instance of StanfordParser
	 */
	public static StanfordShallowParser createInstance() {
		return instance;
	}

	public List<Span> chunk(String[] tokens, String postags[]) {
		// System.out.println(StringUtils.join(tokens," "));
		Tree tree = StanfordParser.createInstance().parse(tokens, postags);
		// tree.pennPrint();
		List<Span> result = new ArrayList<Span>();
		List<Tree> leaves = tree.getLeaves();
		Tree lastAncestor = null;
		int start = 0;
		for (int i = 0; i <= leaves.size(); i++) {
			if (i < leaves.size()) {
				Tree leaf = leaves.get(i);
				Tree phraseAncestor = null;
				for (int j = 1; j < 100; j++) {
					Tree temp = leaf.ancestor(j, tree);
					if (this.phraseTagMap.containsKey(temp.label().value())) {
						phraseAncestor = temp;
						break;
					}
				}
				if (lastAncestor != null && phraseAncestor != lastAncestor) {
					String type = lastAncestor.label().value();
					result.add(new Span(start, i, type));
					start = i;
				}
				lastAncestor = phraseAncestor;
			} else {
				result.add(new Span(start, i, lastAncestor.label().value()));
			}
		}
		return result;
	}

	public List<CuperSpan> chunkToList(String[] tokens, String[] postags) {
		List<CuperSpan> results = new ArrayList<CuperSpan>();
		List<Span> spans = this.chunk(tokens, postags);
		for (Span span : spans) {
			int start = span.getStart();
			int end = span.getEnd();
			String type = span.getType();
			CuperSpan cSpan = new CuperSpan();
			cSpan.setType(type);
			cSpan.setStart(start);
			cSpan.setEnd(end);
			for (int i = start; i < end; i++) {
				cSpan.getWords().add(tokens[i]);
				cSpan.getPostags().add(postags[i]);
			}
			results.add(cSpan);

		}
		return results;
	}

	public static void main(String[] args) {
		String testLine = "Without using any additional labeled data this new method obtained 13% 24% user browsing cost reduction over a state-of-the-art IE system which extracts various types of facts independently.";
		testLine = "This paper presents a Two-Phase Sampling (2PS) technique that detects templates and extracts query-related information from the sampled documents of a database.";
		testLine = "Conditional Random Fields (CRFs) are the state of the art approaches taking the sequence characteristics to do better labeling. ";
		// testLine="This paper presents a new two-phase pattern (2PP) discovery
		// technique for information extraction.";
		testLine = "Recent work on ontology-based Information Extraction (IE) has tried to make use of knowledge from the target ontology in order to improve semantic annotation results.";
		testLine = "we propose a joint paradigm integrating three factors -- segmentation, relation, and segmentation-relation joint factors, to solve all relevant subtasks simultaneously.  ";
		testLine = "Our use of linear~logic as a ` glue ' for assembling meanings also allows for a coherent treatment of modification as well as of the LFG requirements of completeness and coherence .";
		testLine="In this paper, we present a LTR model for information retrieval";
		Sentence sent = PaperUtils.sentProductOf(testLine);
		sent = PhraseTokenizer.createInstance().tokenize(sent);
		List<CuperSpan> list = StanfordShallowParser.createInstance().chunkToList(sent.getTokenArray(),
				sent.getPostagArray());
		for (CuperSpan span : list) {
			System.out.println("--\t" + span.getType() + "\t" + span.getText() + "\t" + span.getPostags());
		}
	}
}
