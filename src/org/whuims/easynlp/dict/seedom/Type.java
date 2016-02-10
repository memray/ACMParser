package org.whuims.easynlp.dict.seedom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class Type.
 */
public class Type implements Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The domain. */
    Domain domain;
    
    /** The type desc. */
    String typeDesc;
    
    /** The words. */
    List<String> words = new ArrayList<String>();

    /**
     * Gets the domain.
     *
     * @return the domain
     */
    public Domain getDomain() {
        return domain;
    }

    /**
     * Sets the domain.
     *
     * @param domain the new domain
     */
    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    /**
     * Gets the type desc.
     *
     * @return the type desc
     */
    public String getTypeDesc() {
        return typeDesc;
    }

    /**
     * Sets the type desc.
     *
     * @param typeDesc the new type desc
     */
    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    /**
     * Gets the words.
     *
     * @return the words
     */
    public List<String> getWords() {
        return words;
    }

    /**
     * Sets the words.
     *
     * @param words the new words
     */
    public void setWords(List<String> words) {
        this.words = words;
    }

}
