package org.whuims.easynlp.exverbsimple;

import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeGraphNode;
import edu.stanford.nlp.trees.TypedDependency;

public class GrammaPacker {
    private GrammaticalStructure gs = null;
    private String[] tokens;
    private String[] postags;

    public GrammaPacker(GrammaticalStructure gs, String[] tokens,
            String[] postags) {
        this.gs = gs;
        this.tokens = tokens;
        this.postags = postags;
    }

    public void pack() {
        // 新建语法关系表示。
        for (TypedDependency td : gs.allTypedDependencies()) {
            System.out.println(td.gov().index() + "\t" + td.dep().index()
                    + "\t" + td.gov().value() + "\t" + td.dep().value() + "\t"
                    + td.reln().getLongName());
        }
        System.out.println("----------------------------------");

    }

}
