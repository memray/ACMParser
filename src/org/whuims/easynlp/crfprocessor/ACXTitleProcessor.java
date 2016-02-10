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

public class ACXTitleProcessor {
    PaperBunch bunch = null;

    public ACXTitleProcessor() {
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
            if (sPaper.getTitleText().contains(" for "))
                processSent(sPaper.getTitle());
        }

    }

    private void processSent(Sentence sent) {
        String[] tokens = sent.getTokenArray();
        if (tokens.length < 2) {
            return;
        }
        TitleSentProcessor sentProcessor = new TitleSentProcessor(sent);
        sentProcessor.process();
    }

    public static void main(String[] args) {
        ACXTitleProcessor processor = new ACXTitleProcessor();
        processor.process();
    }

}
