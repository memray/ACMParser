package org.whuims.easynlp.entity.commonentity;

import java.util.ArrayList;
import java.util.List;

public class StructuredPaper extends SimplePaper {
    private static final long serialVersionUID = 1L;
    List<Section> sections = new ArrayList<Section>();

    public void addSection(Section section) {
//        System.out.println("加入\t"+section.getText());
        this.sections.add(section);
    }

    @Override
    public String getText() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.title).append("\n");
        for (String author : authors) {
            sb.append(author).append("\t");
        }
        sb.append("\n");
        sb.append(summary).append("\n");
        for (String key : keywords) {
            sb.append(key).append("\t");
        }
        sb.append("\n");
        sb.append("\n");
        for (Section section : sections) {
            sb.append(section.getText()).append("\n");
        }
        return sb.toString();
    }

    public List<Section> getSections() {
        return this.sections;
    }

    @Override
    public String getRawText() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.title).append("\n");
        for (String author : authors) {
            sb.append(author).append("\t");
        }
        sb.append("\n");
        sb.append(summary).append("\n");
        for (String key : keywords) {
            sb.append(key).append("\t");
        }
        sb.append("\n");
        sb.append("\n");
        for (Section section : sections) {
            sb.append(section.getRawText()).append("\n");
        }
        return sb.toString();
    }
}
