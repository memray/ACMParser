package org.whuims.tools.aanLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public class AanMemLoader {
    Set<String> set = new HashSet<String>();
    public static Map<String, AANPaper> memMap = new HashMap<String, AANPaper>();
    public static List<AANPaper> papers = new ArrayList<AANPaper>();
    String filePath = "";

    public AanMemLoader(String filePath) {
        super();
        this.filePath = filePath;
        try {
            load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load() throws IOException {
        List<String> lines = FileUtils.readLines(new File(this.filePath));
        AANPaper paper = new AANPaper();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.equals("")) {
                process(paper);
            }
            if (line.startsWith("id")) {
                paper = new AANPaper();
                String temp = StringUtils.substringAfter(line, "{").trim();
                temp = StringUtils.substringBeforeLast(temp, "}");
                paper.setId(temp);
            } else if (line.startsWith("author")) {
                String temp = StringUtils.substringAfter(line, "{").trim();
                temp = StringUtils.substringBeforeLast(temp, "}");
                paper.setAuthor(temp);
            } else if (line.startsWith("title")) {
                String temp = StringUtils.substringAfter(line, "{").trim();
                temp = StringUtils.substringBeforeLast(temp, "}");
                paper.setTitle(temp);
            } else if (line.startsWith("venue")) {
                String temp = StringUtils.substringAfter(line, "{").trim();
                temp = StringUtils.substringBeforeLast(temp, "}");
                paper.setVenue(temp);
            } else if (line.startsWith("year")) {
                String temp = StringUtils.substringAfter(line, "{").trim();
                temp = StringUtils.substringBeforeLast(temp, "}");
                paper.setYear(temp);
            }
        }
        System.out.println("论文数据加载完成。");
    }

    private void process(AANPaper paper) {
        if (set.contains(paper.getId())) {
            return;
        } else {
            set.add(paper.getId());
            memMap.put(paper.getId(), paper);
            papers.add(paper);
        }
    }

    public static void main(String[] args) {
        String filePath = "H:\\Data\\ACL\\aanrelease2013\\aan\\release\\2012\\acl-metadata.txt";
        AanMemLoader loader = new AanMemLoader(filePath);
    }

}
