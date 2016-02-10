package org.whuims.tools.aanLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.whuims.commons.util.Pair;

public class PaperVoter {
    String paperFilePath = "";
    String citationFilePath = "";
    AanMemLoader paperMem = null;
    AanCitationMemLoader citationMem = null;

    public PaperVoter(String paperFilePath, String citationFilePath) {
        super();
        this.paperFilePath = paperFilePath;
        this.citationFilePath = citationFilePath;
        this.paperMem = new AanMemLoader(this.paperFilePath);
        this.citationMem = new AanCitationMemLoader(this.citationFilePath);
    }

    public void extract(String word) {
        Map<String, Integer> countMap = new HashMap<String, Integer>();
        for (AANPaper paper : AanMemLoader.papers) {
            if (paper.title.toLowerCase().contains(word.toLowerCase())) {
                Set<String> set = AanCitationMemLoader.getCitations(paper
                        .getId());
                if (set == null) {
                    continue;
                }
                for (String s : set) {
                    if (!countMap.containsKey(s)) {
                        countMap.put(s, 0);
                    }
                    int count = countMap.get(s);
                    count++;
                    countMap.put(s, count);
                }
            }
        }
        List<Pair> pairs = new ArrayList<Pair>();
        for (Entry<String, Integer> entry : countMap.entrySet()) {
            pairs.add(new Pair(entry.getKey(), entry.getValue()));
        }
        Collections.sort(pairs);
        StringBuilder sb = new StringBuilder();
        for (Pair pair : pairs) {
            AANPaper paper = AanMemLoader.memMap.get(pair.getKey());
            if(paper==null){
                continue;
            }
            sb.append(paper.getId()).append("\t").append(paper.getTitle())
                    .append("\t").append(paper.getYear()).append("\t")
                    .append(pair.getValue()).append("\r\n");
        }
        try {
            FileUtils.write(new File("H:\\paperVote\\" + word + ".txt"), sb
                    .toString().trim());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String paperFilePath = "H:\\Data\\ACL\\aanrelease2013\\aan\\release\\2012\\acl-metadata.txt";
        String citationFilePath = "H:\\Data\\ACL\\aanrelease2013\\aan\\release\\2012\\networks\\paper-citation-nonself-network.txt";
        PaperVoter paperVoter = new PaperVoter(paperFilePath, citationFilePath);
        paperVoter.extract("machine translation");
        paperVoter.extract("automatic summarization");
        paperVoter.extract("Chinese word segmentation");
        paperVoter.extract("sense disambiguation");
        paperVoter.extract("question answer");
    }
}
