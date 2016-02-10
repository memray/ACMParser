package org.whuims.easynlp.entity.commonentity;

import java.io.StringReader;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;

public class Test {
    public static void main(String[] args) {
        String line = "1. book(which book)";
        PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<CoreLabel>(
                new StringReader(line), new CoreLabelTokenFactory(), "");
        for (CoreLabel label; ptbt.hasNext();) {
            label = (CoreLabel) ptbt.next();
            System.out.println(label);
        }
    }
}
