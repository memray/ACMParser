package org.whuims;

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

public class NNDepTest {
	public static void main(String[] args) {
		String line = "Mining scientific data for patterns and relationships has been a common practice for decades , and the very simple use of self-mutating genetic algorithms is nothing new , either . ";
		Properties prop = new Properties();
		prop.setProperty("model",
				"edu\\stanford\\nlp\\models\\parser\\nndep\\english_SD.gz");
		DependencyParser parser = new DependencyParser(prop);
		parser.loadModelFile("edu\\stanford\\nlp\\models\\parser\\nndep\\english_SD.gz");
		Sentence sent = PaperUtils
				.sentProductOf("Electronically available multi-modal data is unprecedented in terms of its volume, variety, velocity and size.");
		List<HasWord> words = new ArrayList<HasWord>();
		for (int i = 0; i < sent.getWords().size(); i++) {
			IndexedWord word = new IndexedWord();
			word.setWord(sent.getTokenArray()[i]);
			word.setTag(sent.getPostagArray()[i]);
			words.add(word);
		}
		GrammaticalStructure gs = parser.predict(words);
		for (TypedDependency dep : gs.typedDependenciesCCprocessed()) {
			System.out.println(dep.gov().word() + "\t"
					+ dep.reln().getShortName() + "\t" + dep.dep().word());
		}

	}
}
