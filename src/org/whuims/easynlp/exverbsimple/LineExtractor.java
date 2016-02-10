package org.whuims.easynlp.exverbsimple;

import org.whuims.easynlp.entity.commonentity.PaperUtils;
import org.whuims.easynlp.entity.commonentity.Sentence;
import org.whuims.easynlp.parserwrapper.StanfordParser;

import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.Tree;

public class LineExtractor {
    private String line;
    private String[] tokens;
    private String[] postags;
    Tree tree = null;
    GrammaticalStructure gs = null;

    public LineExtractor(String line) {
        super();
        this.line = line;
        preprocess();
    }

    public void extract() {
        GrammaPacker packer = new GrammaPacker(this.gs,this.tokens,this.postags);
        packer.pack();

    }

    private void preprocess() {
        if (tokens == null || postags == null
                || tokens.length != postags.length) {
            Sentence sent = PaperUtils.sentProductOf(this.line);
            this.tokens = sent.getTokenArray();
            this.postags = sent.getPostagArray();
        }
        this.tree = StanfordParser.createInstance().parse(this.tokens,
                this.postags);
        this.gs = StanfordParser.createInstance().pack(tree);
    }
}
