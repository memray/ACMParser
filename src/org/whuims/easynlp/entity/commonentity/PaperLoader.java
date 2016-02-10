package org.whuims.easynlp.entity.commonentity;

import org.dom4j.DocumentException;

public interface PaperLoader {

    public abstract Paper loadPaper() throws DocumentException;

    public abstract StructuredPaper load() throws DocumentException;

}