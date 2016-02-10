package org.whuims.easynlp.dict.seedom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class Domain.
 */
public class Domain implements Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The domain id. */
    String domainID;
    
    /** The header. */
    String header;
    
    /** The desc. */
    String desc;
    
    /** The related domain. */
    String relatedDomain;
    
    /** The louw code. */
    String louwCode;

    /** The types. */
    List<Type> types = new ArrayList<Type>();

    /**
     * Instantiates a new domain.
     *
     * @param domainID the domain id
     * @param header the header
     * @param desc the desc
     * @param relatedDomain the related domain
     * @param louwCode the louw code
     */
    public Domain(String domainID, String header, String desc,
            String relatedDomain, String louwCode) {
        super();
        this.domainID = domainID;
        this.header = header;
        this.desc = desc;
        this.relatedDomain = relatedDomain;
        this.louwCode = louwCode;
    }

    /**
     * Gets the header.
     *
     * @return the header
     */
    public String getHeader() {
        return header;
    }

    /**
     * Sets the header.
     *
     * @param header the new header
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * Gets the domain id.
     *
     * @return the domain id
     */
    public String getDomainID() {
        return domainID;
    }

    /**
     * Sets the domain id.
     *
     * @param domainID the new domain id
     */
    public void setDomainID(String domainID) {
        this.domainID = domainID;
    }

    /**
     * Gets the desc.
     *
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Sets the desc.
     *
     * @param desc the new desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * Gets the related domain.
     *
     * @return the related domain
     */
    public String getRelatedDomain() {
        return relatedDomain;
    }

    /**
     * Sets the related domain.
     *
     * @param relatedDomain the new related domain
     */
    public void setRelatedDomain(String relatedDomain) {
        this.relatedDomain = relatedDomain;
    }

    /**
     * Gets the louw code.
     *
     * @return the louw code
     */
    public String getLouwCode() {
        return louwCode;
    }

    /**
     * Sets the louw code.
     *
     * @param louwCode the new louw code
     */
    public void setLouwCode(String louwCode) {
        this.louwCode = louwCode;
    }

    /**
     * Gets the types.
     *
     * @return the types
     */
    public List<Type> getTypes() {
        return types;
    }

    /**
     * Sets the types.
     *
     * @param types the new types
     */
    public void setTypes(List<Type> types) {
        this.types = types;
    }

}
