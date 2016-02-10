package org.whuims.easynlp.entity.commonentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class Paragraph.
 */
public class Paragraph implements ContentUnit,Serializable {
    private static final long serialVersionUID = 6000294368728304438L;

    /**
     * The Enum Paragraph_Type.
     */
    public enum Paragraph_Type {

        /** The SV text. */
        SVText,
        /** The SV formular. */
        SVFormular,
        /** The SV figure. */
        SVFigure,
        /** The SV table. */
        SVTable
    };

    /** The id. */
    String id;

    /** The type. */
    Paragraph_Type type;

    /** The class. */
    String theClass;

    /** The sentences. */
    List<Sentence> sentences = new ArrayList<Sentence>();

    private String rawText;

    /* (non-Javadoc)
     * @see org.whuims.easynlp.entity.ContentUnit#getText()
     */
    @Override
    public String getText() {
        StringBuilder sb = new StringBuilder();
        for (Sentence s : sentences) {
            sb.append(s.getText()).append(Sentence.SPACE);
        }
        return sb.toString().trim();
    }

    public String getRawText() {
        if (rawText != null) {
            return rawText;
        }
        StringBuilder sb = new StringBuilder();
        for (Sentence s : sentences) {
            sb.append(s.getRawText()).append(Sentence.SPACE);
        }
        return sb.toString().trim();
    }

    /**
     * Gets the distilled text.
     *
     * @return the distilled text
     */
    public String getDistilledText() {
        String rawText = getRawText();
        rawText = rawText.replaceAll("<a href=\"#.+?\">", "");
        rawText = rawText.replace("</a>", "");
        rawText = rawText.replaceAll("\\{math_begin\\}.+?\\{math_end\\}",
                "FORMULAR");
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public Paragraph_Type getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type the new type
     */
    public void setType(Paragraph_Type type) {
        this.type = type;
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public void setSentences(List<Sentence> sentences) {
        this.sentences = sentences;
    }

    /**
     * Gets the the class.
     *
     * @return the the class
     */
    public String getTheClass() {
        return theClass;
    }

    /**
     * Sets the the class.
     *
     * @param theClass the new the class
     */
    public void setTheClass(String theClass) {
        this.theClass = theClass;
    }

}
