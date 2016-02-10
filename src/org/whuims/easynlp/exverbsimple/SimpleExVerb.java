package org.whuims.easynlp.exverbsimple;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.whuims.easynlp.parserwrapper.SentDetector;

public class SimpleExVerb {
    String paraString;
    String[] sents = null;

    public SimpleExVerb(String paraString) {
        super();
        this.paraString = paraString;
        prePocess();
    }

    public void extract() {
        parseEachLine();

    }

    private void parseEachLine() {
        for (String sent : sents) {
            LineExtractor extractor = new LineExtractor(sent);
            extractor.extract();
            System.out.println();
            break;
        }
    }

    private void prePocess() {
        this.sents = SentDetector.createInstance().detect(paraString);
    }

    public static void main(String[] args) {
        String paraString = null;
        try {
            paraString = FileUtils.readFileToString(new File(
                    "resource/data/simpleexverb/1.txt"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        SimpleExVerb se = new SimpleExVerb(paraString);
        se.extract();
    }

}
