package org.whuims.easynlp.crfprocessor;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.whuims.easynlp.entity.commonentity.PaperBunch;
import org.whuims.easynlp.entity.commonentity.Paragraph;
import org.whuims.easynlp.entity.commonentity.Section;
import org.whuims.easynlp.entity.commonentity.Sentence;
import org.whuims.easynlp.entity.commonentity.SimplePaper;

public class ACXProcessor {
    PaperBunch bunch = null;

    public ACXProcessor() {
        ACXDataSource source = new ACXDataSource();
        source.addFile("h://Data/ACM/acl.ser3");
        try {
            bunch = source.loadPaperBunch();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void process() {
        for (int i = 0; i < bunch.getPapers().size(); i++) {
            SimplePaper sPaper = (SimplePaper) bunch.getPapers().get(i);
            Section abs = sPaper.getAbstract();
            if (abs == null)
                continue;
            if (abs.getParagraphs() == null)
                continue;
            for (Paragraph para : abs.getParagraphs()) {
                List<Sentence> sents = para.getSentences();
                for (Sentence sent : sents) {
                    processSent(sent);
                }
            }
        }
        try {
            FileUtils.writeStringToFile(new File("h:\\crf_process\\method_train.dat"),
                    Seeds.sb.toString().trim());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processSent(Sentence sent) {
        String[] tokens = sent.getTokenArray();
        if (tokens.length < 2) {
            return;
        }
        SentProcessor sentProcessor = new SentProcessor(sent);
        sentProcessor.process();
    }

    public static void main(String[] args) {
        ACXProcessor processor = new ACXProcessor();
        processor.process();
    }

}
