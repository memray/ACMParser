package org.whuims.easynlp.crfprocessor;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
import org.whuims.easynlp.entity.commonentity.PaperBunch;
import org.whuims.easynlp.entity.commonentity.SimplePaper;

public class ACXDataSource {
    List<String> filePathList = new ArrayList<String>();

    public void addFile(String filePath) {
        this.filePathList.add(filePath);
    }

    public PaperBunch loadPaperBunch() throws IOException {
        PaperBunch bunch = new PaperBunch("ACX", 250000);
        for (String filePath : this.filePathList) {
            PaperBunch b = SerializationUtils.deserialize(new FileInputStream(
                    filePath));
            bunch.getPapers().addAll(b.getPapers());
        }
        return bunch;
    }

    public static void main(String[] args) {
        ACXDataSource source = new ACXDataSource();
        source.addFile("h://Data/ACM/acm.ser3");
        PaperBunch bunch = null;
        try {
            bunch = source.loadPaperBunch();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int count = 0;
        for (int i = 0; i < bunch.getPapers().size(); i++) {
            SimplePaper sPaper = (SimplePaper) bunch.getPapers().get(i);
            if (sPaper.getTitleRawText().contains(" for ")) {
                System.out.println(sPaper.getTitleText());
                count++;
            }
        }
        System.out.println(count);
        System.out.println(bunch.getPapers().size());
    }
}
