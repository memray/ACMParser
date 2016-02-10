package org.whuims.acm.hypernym.snow;

import java.util.List;

import org.whuims.easynlp.parserwrapper.StanfordParser;
import org.whuims.easynlp.tokenizer.StanfordPTBTokenizer;

import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

public class SentenceProcessor {
    String sent;

    public SentenceProcessor(String sent) {
        super();
        this.sent = sent;
    }

    public void process() {
        Tree tree = StanfordParser.createInstance().parse(
                StanfordPTBTokenizer.tokenizeToArray(sent));
        tree.pennPrint();
        TreebankLanguagePack tlp = new PennTreebankLanguagePack();
        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
        GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
        List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
        System.out.println(tdl);
    }

    public static void main(String[] args) {
        String sent = "There are various ways to call the code, but here's a simple example to get started with using either PTBTokenizer directly or calling DocumentPreprocessor.";
        SentenceProcessor processor = new SentenceProcessor(sent);
        processor.process();
    }

}
