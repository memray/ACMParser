package org.whuims.easynlp.parserwrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.whuims.easynlp.entity.commonentity.PaperUtils;
import org.whuims.easynlp.entity.commonentity.Sentence;
import org.whuims.easynlp.phrase.PhraseTokenizer;
import org.whuims.function.cupervised.CuperSpan;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

/**
 * a Shallow Parser based on opennlp
 * 
 */

public class ShallowParser {
	// TODO 修改，包括test中的TestChunkBuilder, 处理In this way/paper nn 处理错误的情况，注意I首字母大写

	private static ShallowParser instance = null;
	private ChunkerME chunkerME = null;
	private Set<String> stopwords = new HashSet<String>();

	// Singleton pattern
	public static ShallowParser createInstance() {
		if (ShallowParser.instance == null) {
			ShallowParser.instance = new ShallowParser();
		}
		return ShallowParser.instance;
	}

	private ShallowParser() {
		try {
			InputStream is = new FileInputStream("resource/model/en-chunker.bin");
			ChunkerModel cModel = new ChunkerModel(is);
			is.close();
			this.chunkerME = new ChunkerME(cModel);
			List<String> lines = FileUtils.readLines(new File("resource/data/sequence/english"));
			for (String line : lines) {
				this.stopwords.add(line.trim());
			}
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成Chunk和特殊字符组成的表示方式。 <br/>
	 * 
	 * @param words
	 * @param postags
	 * @return
	 */
	public String generateLattices(String[] tokens, String[] postags) {
		String[] chunks = chunk(tokens, postags);
		String result = chunk_sequence(tokens, chunks);
		return result;
	}

	public String generateUniqueStarLattices(String[] tokens, String[] postags) {
		String[] chunks = chunk(tokens, postags);
		String result1[] = chunks.clone();
		String current = "~~!@A*d@#!~!@~";
		for (int i = 0; i < tokens.length; i++) {
			if (stopwords.contains(tokens[i].toLowerCase().trim())) {
				result1[i] = "-" + tokens[i].toLowerCase().trim();
			}
		}
		StringBuilder sequence = new StringBuilder("");
		// generate shallow parsing sequence
		for (String phLable : result1) {
			if (phLable.equals("O"))
				phLable += "-Punctuation"; // The phLable of the last word is OP
			if (!current.equals(phLable.split("-")[1])) {
				current = phLable.split("-")[1];
				if (!phLable.contains("-Punctuation")) {
					sequence.append(phLable.split("-")[1]);
					sequence.append(" ");
				}
			}
		}
		return sequence.toString();
	}

	public String generateFormalChunk(String tokens[], String postags[]) {
		List<CuperSpan> spans = this.chunkToList(tokens, postags);
		StringBuilder sb = new StringBuilder();
		for (CuperSpan span : spans) {
			String text = span.getText().toLowerCase().trim();
			String type = span.getType();
			if (this.stopwords.contains(text)) {
				sb.append(text);
			} else {
				sb.append(type);
			}
			sb.append(" ");
		}
		return sb.toString().trim();
	}

	/**
	 * A shallow Parser, chunk a sentence and return an array of chunk tags
	 * 
	 * @param words
	 *            The array contains all the words
	 * @param postags
	 *            The array contains the pos tags for each word
	 * @return String[]
	 */
	public String[] chunk(String[] words, String[] postags) {
		String result[] = chunkerME.chunk(words, postags);
		return result;
	}

	public List<CuperSpan> chunkToList(String[] words, String[] postags) {
		List<CuperSpan> results = new ArrayList<CuperSpan>();
		String[] chunkResult = chunkerME.chunk(words, postags);
		Span[] spans = parseChunkResultAsSpans(words, postags, chunkResult);
		for (Span span : spans) {
			int start = span.getStart();
			int end = span.getEnd();
			String type = span.getType();
			CuperSpan cSpan = new CuperSpan();
			cSpan.setType(type);
			cSpan.setStart(start);
			cSpan.setEnd(end);
			for (int i = start; i < end; i++) {
				cSpan.getWords().add(words[i]);
				cSpan.getPostags().add(postags[i]);
			}
			// System.out.println("----\t" + span.getType() + "\t" + start +
			// "\t" + end);
			results.add(cSpan);

		}
		return results;
	}

	private Span[] parseChunkResultAsSpans(String[] aSentence, String[] aTags, String[] aPreds) {
		// initialize with the list maximum size
		List<Span> phrases = new ArrayList<Span>(aSentence.length);
		String startTag = "";
		int startIndex = 0;
		boolean foundPhrase = false;

		for (int ci = 0; ci < aPreds.length; ci++) {
			String pred = aPreds[ci];
			if (pred.startsWith("B-") || (!pred.equals("I-" + startTag) && !pred.equals("O")) || pred.equals("O")) { // start
				if (foundPhrase) { // handle the last
					phrases.add(new Span(startIndex, ci, startTag));
				}
				startIndex = ci;
				startTag = pred.length() > 2 ? pred.substring(2) : pred;
				foundPhrase = true;
			} else if (pred.equals("I-" + startTag)) { // middle
				// do nothing
			} else if (foundPhrase) {// end
				phrases.add(new Span(startIndex, ci, startTag));
				foundPhrase = false;
				startTag = "";
			}
		}
		if (foundPhrase) { // leftover
			phrases.add(new Span(startIndex, aPreds.length, startTag));
		}

		return phrases.toArray(new Span[phrases.size()]);
	}

	/**
	 * 
	 * generate the collapsed shallow parsing sequence of the sentence
	 * 
	 * @param input
	 *            The input sentence
	 * @param model
	 *            The POSModel of the chunk
	 * @param cModel
	 *            The ChunkerModel of the chunk
	 * @return HashMap<Integer,String>
	 */
	public String chunk_sequence(String[] words, String[] result) {
		String result1[] = result.clone();
		String current = "out";
		for (int i = 0; i < words.length; i++) {
			if (stopwords.contains(words[i].toLowerCase().trim())) {
				result1[i] = "-" + words[i].toLowerCase().trim();
			}
		}
		StringBuilder sequence = new StringBuilder("");

		// generate shallow parsing sequence
		for (String phLable : result1) {
			if (phLable.equals("O"))
				phLable += "-Punctuation"; // The phLable of the last word is OP
			if (!current.equals(phLable.split("-")[1])) {
				current = phLable.split("-")[1];
				if (!phLable.contains("-Punctuation")) {
					sequence.append(phLable.split("-")[1]);
					sequence.append(" ");
				}
			}
		}
		return sequence.toString();
	}

	public Set<String> getStopwords() {
		return stopwords;
	}

	public static void main(String[] args) {
		String line = "ILK2: Semantic Role Labelling for Catalan and Spanish using TiMBL";
		line = "ONTS : Optima News Translation~System";
		// line = "bootstrapping algorithm";
		line = "We propose the subtree ranking approach to parse forest reranking which is a generalization of current perceptron-based reranking methods.";
		line = "Besides, instead of using the traditional sequential state transition order, the state transition orders of GHMMs are detected based on layout structures of the corresponding web pages. ";
		line = "we propose a method based on information extraction.";
		line = "Building extraction at the sub-pixel scale from remotely sensed images based on anisotropic Markov random field";
		line = "Image matching using the medium similarity measure";
		line = "Adaptive Key-frames Extraction Based on Visual Attention Model";
		line = "Chinese Message Structures Disambiguation Based on HowNet";
		line = "QoS Routing Based on Ant Colony—Genetic Algorithm";
		line = "MODEL REFERENCE ADAPTIVE CONTROL BASED ON FUZZY NEURAL NETWORKS";
		line = "Multi-objective tracking method based on Mean Shift——connected component labeling";
		line = "A New Efficient Group Signature Scheme Based on DSA";
		line = "Algorithm of image encoding based on U-orthogonal transform";
		line = "Improvement of Plant Structure Modeling Based on L-System";
		line = "implementing target language generation based on dop technique";
		line = "Locally Adaptive Wavelet Denoising Based on Bayesian MAP Estimation";
		line = "3d surface reconstruction based on level set models";
//		line = "Improved fast codeword search algorithm based on reference vector selection";
		Sentence sent = PaperUtils.sentProductOf(line);
		sent = PhraseTokenizer.createInstance().tokenize(sent);
		String[] tokens = sent.getTokenArray();
		String[] postags = sent.getPostagArray();
		List<CuperSpan> result = ShallowParser.createInstance().chunkToList(tokens, postags);
		for (CuperSpan span : result) {
			System.out.println(span.getText() + "\t" + span.getPostags().get(0) + "\t" + span.getType());
		}
		// System.out.println(ShallowParser.createInstance().generateUniqueStarLattices(tokens,
		// postags));
		System.out.println(ShallowParser.createInstance().generateUniqueStarLattices(tokens, postags));
		System.out.println(ShallowParser.createInstance().generateFormalChunk(tokens, postags));
	}
}
