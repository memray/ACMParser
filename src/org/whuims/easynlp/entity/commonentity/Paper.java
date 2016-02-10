package org.whuims.easynlp.entity.commonentity;

import java.util.List;

public interface Paper {
    public String getDoi();

    public String getJournalTitle();

    public String getVolume();

    public String getPublishedDate();

    public String getTitleText();

    public String getAbstractText();

    public List<String> getKeywords();

    public List<String> getAuthors();

    public String getText();

}
