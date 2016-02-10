package org.whuims.easynlp.parserwrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.whuims.easynlp.entity.commonentity.PaperUtils;
import org.whuims.easynlp.entity.commonentity.Sentence;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.TypedDependency;

public class StanfordNNParser {
	private static StanfordNNParser instance = new StanfordNNParser();
	private DependencyParser parser = null;

	private StanfordNNParser() {
		Properties prop = new Properties();
		prop.setProperty("model",
				"edu\\stanford\\nlp\\models\\parser\\nndep\\english_SD.gz");
		parser = new DependencyParser(prop);
		parser.loadModelFile("edu\\stanford\\nlp\\models\\parser\\nndep\\english_SD.gz");
	}

	public static StanfordNNParser createInstance() {
		return instance;
	}

	public GrammaticalStructure parseGS(Sentence sent) {
		List<HasWord> words = new ArrayList<HasWord>();
		for (int i = 0; i < sent.getWords().size(); i++) {
			IndexedWord word = new IndexedWord();
			word.setWord(sent.getTokenArray()[i]);
			word.setTag(sent.getPostagArray()[i]);
			words.add(word);
		}
		GrammaticalStructure gs = parser.predict(words);
		return gs;
	}

	public GrammaticalStructure parseGS(String sentLine) {
		Sentence sent = PaperUtils.sentProductOf(sentLine);
		return parseGS(sent);
	}

	public static void main(String[] args) {
		String line = "In this paper we present a general natural language processing system called CARAMEL . ";
		GrammaticalStructure gs = StanfordNNParser.createInstance().parseGS(
				line);
		for (TypedDependency td : gs.typedDependenciesCCprocessed()) {
			System.out.println(td.gov().word() + "\t" + td.dep().word() + "\t"
					+ td.reln().getShortName()+"\t"+td.reln().getSpecific());
		}
	}
}
