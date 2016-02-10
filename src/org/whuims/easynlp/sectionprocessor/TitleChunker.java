package org.whuims.easynlp.sectionprocessor;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class TitleChunker {
    String filePath;

    public TitleChunker(String filePath) {
        super();
        this.filePath = filePath;
    }

    public void process() throws IOException {
        List<String> lines = FileUtils.readLines(new File(this.filePath));
        for (String line : lines) {
            line = line.trim();
            System.out.println(line);
        }
    }

    public static void main(String[] args) {
        String filePath = "h:/aclwhole/acltitle.txt";
        TitleChunker tc = new TitleChunker(filePath);
        try {
            tc.process();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
