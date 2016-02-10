package org.whuims.easynlp.entity.commonentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Section implements ContentUnit, Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String type;
    private String titleStr;
    private Sentence title;
    private List<Paragraph> paragraphs = new ArrayList<Paragraph>();

    public String getText() {
        StringBuilder sb = new StringBuilder();
        if (title != null) {
            sb.append("\n").append(title.getText()).append("\n\n");
        }
        for (Paragraph para : paragraphs) {
            sb.append(para.getText()).append("\n");
        }
        return sb.toString().trim();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Sentence getTitle() {
        return title;
    }

    public void setTitle(Sentence title) {
        this.title = title;
    }

    public List<Paragraph> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
    }

    @Override
    public String getRawText() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n\n").append(title.getRawText()).append("\n\n");
        for (Paragraph para : paragraphs) {
            sb.append(para.getRawText()).append("\n");
        }
        return sb.toString().trim();
    }

    public String getTitleStr() {
        return titleStr;
    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }

}
