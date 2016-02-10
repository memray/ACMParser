package org.whuims.easynlp.parserwrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.whuims.easynlp.entity.commonentity.PaperUtils;
import org.whuims.easynlp.phrase.PhraseTokenizer;
import org.whuims.easynlp.tokenizer.StanfordPTBTokenizer;
import org.whuims.easynlp.util.Config;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

// TODO: Auto-generated Javadoc
/**
 * The Class StanfordParser.
 */
public class StanfordParser {

	/** The instance. */
	private static StanfordParser instance = new StanfordParser();

	/** The lp. */
	private LexicalizedParser lp = null;

	/**
	 * Instantiates a new stanford parser.
	 */
	private StanfordParser() {
		init();
	}

	/**
	 * Inits the.
	 */
	private void init() {
		lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
	}

	/**
	 * Gets the single instance of StanfordParser.
	 *
	 * @return single instance of StanfordParser
	 */
	public static StanfordParser createInstance() {
		return instance;
	}

	/**
	 * 输入字符串
	 *
	 * @param sent
	 *            the sent
	 * @return the tree
	 */
	public Tree parse(String[] words) {
		List<CoreLabel> rawWords = Sentence.toCoreLabelList(words);
		return parse(rawWords);
	}

	public Tree parse(String sentLine) {
		return this.parse(StanfordPTBTokenizer.tokenizeToArray(sentLine));
	}

	public GrammaticalStructure parseGS(String[] tokens, String postags[]) {
		String str = StringUtils.join(tokens, " ");
		GrammaticalStructure resultGS = null;
		int hash = str.hashCode();
		File serFile = new File("f:\\experiment\\parseLineSER\\" + hash + ".ser");
		if (serFile.exists()) {
			try {
				resultGS = SerializationUtils.deserialize(new FileInputStream(serFile));
				return resultGS;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		List<TaggedWord> sentence = new ArrayList<TaggedWord>();
		for (int i = 0; i < tokens.length; i++) {
			sentence.add(new TaggedWord(tokens[i], postags[i]));
		}
		resultGS = this.pack(this.lp.parse(sentence));
		try {
			SerializationUtils.serialize(resultGS, new FileOutputStream(serFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return resultGS;
	}

	public Tree parse(String[] tokens, String postags[]) {
		String line = StringUtils.join(tokens, "\t");
		// int hash = line.hashCode();
		String hash = tokens.length + "_" + line.hashCode();
		File serFile = new File(Config.getProp("PennTree_Pass_DIR") + "parseTreeSER/" + hash + ".ser");
		if (serFile.exists()) {
			try {
				Tree tree = SerializationUtils.deserialize(new FileInputStream(serFile));
				return tree;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		List<TaggedWord> sentence = new ArrayList<TaggedWord>();
		for (int i = 0; i < tokens.length; i++) {
			TaggedWord taggedWord = new TaggedWord(tokens[i], postags[i]);
			sentence.add(taggedWord);
		}
		Tree tree = this.lp.parse(sentence);
		try {
			SerializationUtils.serialize(tree, new FileOutputStream(serFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return tree;
	}

	/**
	 * Parses the.
	 *
	 * @param labels
	 *            the labels
	 * @return the tree
	 */
	public Tree parse(List<CoreLabel> labels) {
		Tree parsedTree = lp.apply(labels);
		// System.out.println("完成parse");
		return parsedTree;
	}

	/**
	 * Pack.
	 *
	 * @param parsedTree
	 *            the parsed tree
	 * @return the grammatical structure
	 */
	public GrammaticalStructure pack(Tree parsedTree) {
		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		GrammaticalStructure gs = gsf.newGrammaticalStructure(parsedTree);
		return gs;
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		String line = "This paper presents a hybrid approach for named entity (NE) tagging which combines MaxEnt, HMM and rules.";
		line = "Discriminative learning methods , such as Maximum Entropy Markov Models, Projection Based Markov Models, Conditional Random Fields, Sequence AdaBoost, Sequence Perceptron, Hidden Markov Support Vector Machines and Maximum-Margin Markov Networks, overcome the limitations of HMMs .";
		line = "These comprehensive , readable tutorials and survey papers give guided tours through the literature and explain topics to those who seek to learn the basics of areas outside their specialties . ";
		line = "We used the Wall-Street-Journal of the years 88-89.";
		line = "Conditional Random Fields (CRFs) are the state of the art approaches taking the sequence characteristics to do better labeling. ";
		line = "Recent work on ontology-based Information Extraction (IE) has tried to make use of knowledge from the target ontology in order to improve semantic annotation results.";
		line = "we propose a joint paradigm integrating three factors -- segmentation, relation, and segmentation-relation joint factors, to solve all relevant subtasks simultaneously.";
		org.whuims.easynlp.entity.commonentity.Sentence sent = PaperUtils.sentProductOf(line);
		sent = PhraseTokenizer.createInstance().tokenize(sent);
		Tree parsedTree = StanfordParser.createInstance().parse(sent.getTokenArray(), sent.getPostagArray());
		parsedTree.pennPrint();
		GrammaticalStructure gs = StanfordParser.createInstance().pack(parsedTree);
		for (TypedDependency td : gs.allTypedDependencies()) {
			// System.out.println(td.gov().word() + "_" + td.gov().tag() + "\t"
			// + td.dep().word() + "_" + td.dep().tag()
			// + "\t" + td.reln().getShortName());
		}
	}

}
