package org.whuims.easynlp.entity.elsloader;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.whuims.easynlp.entity.commonentity.PaperLoader;
import org.whuims.easynlp.entity.commonentity.Paragraph;
import org.whuims.easynlp.entity.commonentity.Section;
import org.whuims.easynlp.entity.commonentity.StructuredPaper;
import org.whuims.easynlp.parserwrapper.SentDetector;

public class DeepWalk {
    static int count = 0;

    public void walk(File f) {
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                walk(file);
            }
        } else {
            if (f.getName().endsWith(".xml")) {
                process(f);
                count++;
                System.out.println(count);
            }
        }

    }

    private void process(File file) {
        PaperLoader loader = new ElsPaperLoader(file.getAbsolutePath());
        try {
            StringBuilder sb = new StringBuilder();
            StructuredPaper paper = loader.load();
            sb.append(paper.getTitle());
            sb.append(paper.getJournalTitle());
            sb.append(paper.getPublishedDate());
            sb.append(paper.getTitle());
            for (Section section : paper.getSections()) {
                for (Paragraph para : section.getParagraphs()) {
                    String line = para.getDistilledText();
                    String[] array = SentDetector.createInstance().detect(line);
                    for (String str : array) {
                        sb.append(str).append("\r\n");
                    }
                }
            }
            for (String str : SentDetector.createInstance().detect(
                    paper.getAbstract().getRawText())) {
                sb.append(str).append("\r\n");
            }
//            FileUtils.write(new File("j:\\sent.dat"), sb.toString(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String filePath = "J:\\DataSet\\computer_science\\";
        DeepWalk dw = new DeepWalk();
        dw.walk(new File(filePath));
    }

}
