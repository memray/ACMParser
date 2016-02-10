package org.whuims.easynlp.entity.commonentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 仅有元数据的文档。包括doi、
 * @author Qikai
 *
 */
public class SimplePaper implements Paper, ContentUnit, Serializable {
    private static final long serialVersionUID = 1L;
    protected int id;
    protected String doi = "";
    protected String journalTitle = "";
    protected String volume = "";
    protected String dateStr = "";
    protected List<String> authors = new ArrayList<String>(3);
    protected Sentence title;
    private String titleRawText;
    protected Section summary;
    private String abstractRawText;
    protected List<String> keywords = new ArrayList<String>(5);

    @Override
    public String getDoi() {
        return doi;
    }

    @Override
    public String getJournalTitle() {
        return this.journalTitle;
    }

    @Override
    public String getVolume() {
        return this.volume;
    }

    @Override
    public String getPublishedDate() {
        return this.dateStr;
    }

    public Sentence getTitle() {
        return this.title;
    }

    public Section getAbstract() {
        return this.summary;
    }

    @Override
    public List<String> getKeywords() {
        return this.keywords;
    }

    @Override
    public List<String> getAuthors() {
        return this.authors;
    }

    @Override
    public String getText() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getTitle().getText());
        sb.append(this.getAbstract().getText());
        return sb.toString();
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public void setJournalTitle(String journalTitle) {
        this.journalTitle = journalTitle;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public void setTitle(Sentence title) {
        this.title = title;
    }

    public void setAbstract(Section summary) {
        this.summary = summary;
    }

    @Override
    public String getRawText() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.titleRawText).append("\n").append(this.abstractRawText);
        return sb.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitleRawText() {
        return titleRawText;
    }

    public void setTitleRawText(String titleRawText) {
        this.titleRawText = titleRawText;
    }

    public String getAbstractRawText() {
        return abstractRawText;
    }

    public void setAbstractRawText(String abstractRawText) {
        this.abstractRawText = abstractRawText;
    }

    @Override
    public String getTitleText() {
        return this.title.getText();
    }

    @Override
    public String getAbstractText() {
        return this.summary.getText();
    }

}
