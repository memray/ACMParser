package org.whuims.easynlp.crfprocessor;

import java.util.ArrayList;
import java.util.List;

import org.whuims.easynlp.entity.commonentity.Sentence;
import org.whuims.easynlp.parserwrapper.OpenNLPChunker;

public class SentProcessor {
    String tokens[];
    String postags[];
    Sentence sent = null;
    String chunkTags[];

    public SentProcessor(Sentence sent) {
        super();
        this.sent = sent;
        this.tokens = this.sent.getTokenArray();
        this.postags = this.sent.getPostagArray();
    }

    public SentProcessor(String[] tokens, String[] postags) {
        super();
        this.tokens = tokens;
        this.postags = postags;
    }

    public void process() {
        preprocess();
        // System.out.println("$$$$\t"+this.tokens.length);
        this.chunkTags = OpenNLPChunker.createInstance().chunkParse(tokens,
                postags);
        if (this.chunkTags.length < 2) {
            return;
        }
        List<Integer> list = new ArrayList<Integer>();
        String currentTag = chunkTags[0];
        if (chunkTags[0].contains("-")) {
            currentTag = chunkTags[0].split("-")[1];
        }
        boolean valid = false;
        for (int i = 0; i < chunkTags.length; i++) {
            String tag = chunkTags[i];
            String majorTag = tag;
            String[] temp = tag.split("-");
            if (temp.length == 2) {
                majorTag = temp[1];
            }
            if (!majorTag.equals(currentTag)) {
                valid = this.validePhrse(list, currentTag);
                if (valid) {
                    break;
                }
                list = new ArrayList<Integer>();
            }
            list.add(i);
            currentTag = majorTag;
        }
        if (!valid) {
            return;
        }
        list.clear();

        Seeds.sb.append("\r\n");
        for (int i = 0; i < chunkTags.length; i++) {
            String tag = chunkTags[i];
            String majorTag = tag;
            String[] temp = tag.split("-");
            if (temp.length == 2) {
                majorTag = temp[1];
            }
            if (!majorTag.equals(currentTag)) {
                processPhrase(list, currentTag);
                list = new ArrayList<Integer>();
            }
            list.add(i);
            currentTag = majorTag;
        }
    }

    private boolean validePhrse(List<Integer> list, String chunkTag) {
        StringBuilder sb = new StringBuilder();
        for (int i : list) {
            sb.append(tokens[i]).append(" ");
        }
        String text = sb.toString().toLowerCase().trim();
        if (chunkTag.equals("NP")) {
            for (String str : Seeds.getMethodSeeds()) {
                if (text.contains(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void processPhrase(List<Integer> list, String chunkTag) {
        StringBuilder sb = new StringBuilder();
        for (int i : list) {
            sb.append(tokens[i]).append(" ");
        }
        String text = sb.toString().toLowerCase().trim();
        boolean validApplication = false;
        if (chunkTag.equals("NP")) {
            for (String str : Seeds.getMethodSeeds()) {
                if (text.contains(str)) {
                    validApplication = true;
                }
            }
        }
        for (int i = 0; i < list.size(); i++) {
            int loc = list.get(i);
            Seeds.sb.append(tokens[loc] + " " + postags[loc] + " "
                    + chunkTags[loc]);
            if (chunkTag.equals("NP")) {
                if (validApplication) {
                    if (i == 0) {
                        Seeds.sb.append(" APP_BEGIN\r\n");
                    } else {
                        Seeds.sb.append(" APP_IN\r\n");
                    }
                } else {
                    Seeds.sb.append(" NONAPP\r\n");
                }
            } else {
                Seeds.sb.append(" NONAPP\r\n");
            }
        }

    }

    private void preprocess() {
        List<String> tokenList = new ArrayList<String>();
        List<String> postagList = new ArrayList<String>();
        boolean ommit = false;
        for (int i = 0; i < this.tokens.length; i++) {
            if (this.tokens[i].trim().equals("-LRB-")) {
                ommit = true;
            }
            if (!ommit) {
                tokenList.add(tokens[i]);
                postagList.add(postags[i]);
            }
            if (this.tokens[i].equals("-RRB-")) {
                ommit = false;
            }
        }
        this.tokens = new String[tokenList.size()];
        this.postags = new String[tokenList.size()];
        for (int i = 0; i < tokenList.size(); i++) {
            this.tokens[i] = tokenList.get(i);
            this.postags[i] = postagList.get(i);
        }
    }
}
