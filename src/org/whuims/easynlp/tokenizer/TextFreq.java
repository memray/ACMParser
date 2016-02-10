package org.whuims.easynlp.tokenizer;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class TextFreq {
    private String filePath;

    public TextFreq(String filePath) {
        super();
        this.filePath = filePath;
    }

    public void cal() throws IOException {
        File f = new File(this.filePath);
        StringBuilder sb = new StringBuilder();
        loadText(f, sb);
        String[] array = StanfordPTBTokenizer.tokenizeToArray(sb.toString());
        sb = null;
        calFreq(array);
    }

    private void calFreq(String[] array) {

    }

    private void loadText(File f, StringBuilder sb) throws IOException {
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                loadText(file, sb);
            }
        } else {
            sb.append(FileUtils.readFileToString(f));
        }
    }

}
