/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2014 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/

package com.basistech.rosette.dm;

import com.google.common.base.Objects;

import java.util.Map;

/**
 * A mention of a named entity in the text.
 */
public class EntityMention extends Attribute {
    private final String entityType;
    private final double confidence;
    private final int coreferenceChainId;
    private final int flags;
    private final String source;
    private final String subsource;
    private final String normalized;

    EntityMention(int startOffset, int endOffset,
                  String entityType,
                  int coreferenceChainId,
                  double confidence, int flags,
                  String source, String subsource, String normalized,
                  Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
        this.entityType = entityType;
        this.confidence = confidence;
        this.coreferenceChainId = coreferenceChainId;
        this.flags = flags;
        this.source = source;
        this.subsource = subsource;
        this.normalized = normalized;
    }

    EntityMention(int startOffset,
                  int endOffset,
                  String entityType,
                  int coreferenceChainId,
                  double confidence,
                  int flags,
                  String source,
                  String subsource,
                  String normalized) {
        super(startOffset, endOffset);
        this.entityType = entityType;
        this.confidence = confidence;
        this.coreferenceChainId = coreferenceChainId;
        this.flags = flags;
        this.source = source;
        this.subsource = subsource;
        this.normalized = normalized;
    }

    protected EntityMention() {
        // make jackson happy.
        entityType = null;
        confidence = 0.0;
        coreferenceChainId = -1;
        flags = 0;
        source = null;
        subsource = null;
        normalized = null;
    }

    /**
     * Returns the type of the entity.  For example, "PERSON", "LOCATION",
     * "ORGANIZATION".
     *
     * @return the type of entity
     */
    public String getEntityType() {
        return entityType;
    }

    /**
     * Returns the confidence of the entity extractor in identifying this mention.
     *
     * @return the confidence of the entity extractor in identifying this mention
     */
    public double getConfidence() {
        return confidence;
    }

    /**
     * Returns a chain id that links together entity mentions that refer to the
     * same entity as determined by in-document analysis. -1 if no in-document
     * coreference information is available.  Currently, the chain id is the
     * index (into the {@code EntityMention} list) of the head mention of the
     * chain.  The head mention is the (first) longest mention in the chain.
     *
     * @return the coreference chain id, or -1 if chaining has not been applied
     */
    public int getCoreferenceChainId() {
        return coreferenceChainId;
    }

    /**
     * Returns flags associated with a mention. Interpretation of the flags varies
     * by extractor and language.
     *
     * @return flags associated with the mention
     */
    public int getFlags() {
        return flags;
    }

    /**
     * Returns the entity extraction source that produced this entity.  For
     * example, "statistical", "regex", "gazetteer".
     *
     * @return the entity extraction source
     */
    public String getSource() {
        return source;
    }

    /**
     * Returns the entity extraction subsource that produced this entity.
     * This is usually filename for the regular expression file or gazetteer in which the entity appears.
     * For example, "./data/gazetteer/eng/accept/gaz-LE.bin"
     *
     * @return the entity extraction subsource
     */
    public String getSubsource() {
        return subsource;
    }

    /**
     * Returns the normalized form of the mention.  This form typically
     * normalizes spaces spaces and removes embedded newlines.  It may omit
     * prefixes in languages like Arabic.  This is not a canonical way to
     * refer to the entity (see {@link com.basistech.rosette.dm.ResolvedEntity})
     * but rather a simplified form of this particular mention text.
     *
     * @return the normalized form of the mention
     */
    public String getNormalized() {
        return normalized;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        EntityMention that = (EntityMention) o;

        if (Double.compare(that.confidence, confidence) != 0) {
            return false;
        }
        if (coreferenceChainId != that.coreferenceChainId) {
            return false;
        }
        if (flags != that.flags) {
            return false;
        }
        if (!entityType.equals(that.entityType)) {
            return false;
        }
        if (normalized != null ? !normalized.equals(that.normalized) : that.normalized != null) {
            return false;
        }
        if (source != null ? !source.equals(that.source) : that.source != null) {
            return false;
        }
        if (subsource != null ? !subsource.equals(that.subsource) : that.subsource != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        result = 31 * result + entityType.hashCode();
        temp = Double.doubleToLongBits(confidence);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + coreferenceChainId;
        result = 31 * result + flags;
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (subsource != null ? subsource.hashCode() : 0);
        result = 31 * result + (normalized != null ? normalized.hashCode() : 0);
        return result;
    }

    @Override
    protected Objects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("entityType", entityType)
                .add("confidence", confidence)
                .add("coreferenceChainId", coreferenceChainId)
                .add("flags", flags)
                .add("source", source)
                .add("subsource", subsource)
                .add("normalized", normalized);
    }

    /**
     * A builder for entity mentions.
     */
    public static class Builder extends Attribute.Builder {
        private String entityType;
        private double confidence;
        private int coreferenceChainId = -1;
        private int flags;
        private String source;
        private String subsource;
        private String normalized;

        /**
         * Constructs a builder with the minimal required information for an entity mention.
         *
         * @param startOffset the start offset in the text, in characters
         * @param endOffset the end offset in the text, in characters
         * @param entityType the type of entity (e.g. "PERSON").
         */
        public Builder(int startOffset, int endOffset, String entityType) {
            super(startOffset, endOffset);
            this.entityType = entityType;
        }

        /**
         * Constructs a builder initialized with information from an existing entity mention.
         *
         * @param toCopy the mention to copy.
         * @adm.ignore
         */
        public Builder(EntityMention toCopy) {
            super(toCopy);
            this.entityType = toCopy.entityType;
            this.confidence = toCopy.confidence;
            this.coreferenceChainId = toCopy.coreferenceChainId;
            this.flags = toCopy.flags;
            this.source = toCopy.source;
            this.subsource = toCopy.subsource;
            this.normalized = toCopy.normalized;
        }

        /**
         * Specifies the entity type.
         *
         * @param entityType the entity type
         * @return this
         */
        public Builder entityType(String entityType) {
            this.entityType = entityType;
            return this;
        }

        /**
         * Specifies the confidence.
         *
         * @param confidence the confidence
         * @return this
         */
        public Builder confidence(double confidence) {
            this.confidence = confidence;
            return this;
        }

        /**
         * Specifies the coreference chain identifier. See {@link com.basistech.rosette.dm.EntityMention#getCoreferenceChainId()}.
         *
         * @param coreferenceChainId the chain identifier, or -1 for a mention that is not linked
         * @return this
         */
        public Builder coreferenceChainId(int coreferenceChainId) {
            this.coreferenceChainId = coreferenceChainId;
            return this;
        }

        /**
         * Specifies the flags.
         *
         * @param flags    flags value
         * @return this
         */
        public Builder flags(int flags) {
            this.flags = flags;
            return this;
        }

        /**
         * Specifies the source of this mention.
         *
         * @param source the source
         * @return this
         */
        public Builder source(String source) {
            this.source = source;
            return this;
        }

        /**
         * Specifies the subsource of this mention.
         *
         * @param subsource the source
         * @return this
         */
        public Builder subsource(String subsource) {
            this.subsource = subsource;
            return this;
        }

        /**
         * Specifies the normalized form of this mention.
         *
         * @param normalized the normalized form
         * @return this
         */
        public Builder normalized(String normalized) {
            this.normalized = normalized;
            return this;
        }

        /**
         * Builds the immutable mention.
         *
         * @return the mention
         */
        public EntityMention build() {
            return new EntityMention(startOffset, endOffset, entityType, coreferenceChainId, confidence, flags, source,
                subsource, normalized, extendedProperties);
        }
    }
}
