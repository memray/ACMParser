package org.whuims.easynlp.entity.commonentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PaperBunch implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int DEFAULT_SIZE = 1000;
    private String bunchName;
    private int initial_size;
    private List<Paper> papers;

    public PaperBunch(String bunchName, int initial_size) {
        super();
        this.bunchName = bunchName;
        this.initial_size = initial_size;
        this.papers = new ArrayList<Paper>(this.initial_size + 10);
    }

    public int size() {
        return this.papers.size();
    }

    public String getBunchName() {
        return bunchName;
    }

    public void addPaper(Paper paper) {
        papers.add(paper);
    }

    public void setBunchName(String bunchName) {
        this.bunchName = bunchName;
    }

    public List<Paper> getPapers() {
        return papers;
    }

}
