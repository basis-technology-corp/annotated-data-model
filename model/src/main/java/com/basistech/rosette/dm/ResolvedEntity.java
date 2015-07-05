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
 * A reference to a "real world" entity.  For example, an {@code EntityMention} of
 * "George" could refer to many different Georges.  A {@code ResolvedEntity}
 * associates the mention with an id from some external knowledge base, e.g.
 * <a href="http://www.wikidata.org/wiki/Q23">Q23</a> from Wikidata.
 */
public class ResolvedEntity extends Attribute {
    private final String entityId;
    //I prefer 'chainId' over 'coreferenceChainId' but picked the latter to make it consistent with EntityMention
    private final Integer coreferenceChainId;
    private final Double confidence;

    protected ResolvedEntity(int startOffset, int endOffset, String entityId,
                             Integer coreferenceChainId, Double confidence,
                             Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
        this.entityId = entityId;
        this.coreferenceChainId = coreferenceChainId;
        this.confidence = confidence;
    }

    /**
     * Returns the unique identifier of this entity.
     *
     * @return the unique identifier of this entity
     */
    public String getEntityId() {
        return entityId;
    }

    /**
     * Returns the in-document coreference chain ID for this entity, or null if there is none.
     *
     * @return the in-document coreference chain ID for this entity, or null if there is none
     */
    public Integer getCoreferenceChainId() {
        return coreferenceChainId;
    }

    /**
     * Returns the confidence for this resolved entity, or null if there is none.
     *
     * @return the confidence for this resolved entity, or null if there is none
     */
    public Double getConfidence() {
        return confidence;
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

        ResolvedEntity that = (ResolvedEntity) o;

        if (confidence != null ? !confidence.equals(that.confidence) : that.confidence != null) {
            return false;
        }
        if (coreferenceChainId != null ? !coreferenceChainId.equals(that.coreferenceChainId) : that.coreferenceChainId != null) {
            return false;
        }
        if (!entityId.equals(that.entityId)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + entityId.hashCode();
        result = 31 * result + (coreferenceChainId != null ? coreferenceChainId.hashCode() : 0);
        result = 31 * result + (confidence != null ? confidence.hashCode() : 0);
        return result;
    }

    @Override
    protected Objects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("entityId", entityId)
                .add("confidence", confidence)
                .add("coreferenceChainId", coreferenceChainId);
    }

    /**
     * A builder for resolved entities.
     */
    public static class Builder extends Attribute.Builder {
        private String entityId;
        private Double confidence; // NAN?
        private Integer coreferenceChainId;

        /**
         * Constructs a builder from the required values.
         *
         * @param startOffset the start offset in characters
         * @param endOffset the end offset in character
         * @param entityId the entity ID
         */
        public Builder(int startOffset, int endOffset, String entityId) {
            super(startOffset, endOffset);
            this.entityId = entityId;
        }

        /**
         * Constructs a builder by copying values from an existing resolved entity.
         *
         * @param toCopy the object to create
         * @adm.ignore
         */
        public Builder(ResolvedEntity toCopy) {
            super(toCopy);
            this.entityId = toCopy.entityId;
            this.confidence = toCopy.confidence;
            this.coreferenceChainId = toCopy.coreferenceChainId;
        }

        /**
         * Specifies the entity unique ID.
         *
         * @param entityId the ID
         */
        public Builder entityId(String entityId) {
            this.entityId = entityId;
            return this;
        }

        /**
         * Specifies the confidence value.
         *
         * @param confidence the confidence value
         * @return this
         */
        public Builder confidence(double confidence) {
            this.confidence = confidence;
            return this;
        }

        /**
         * Specifies the coreference chain id.
         *
         * @param coreferenceChainId the chain id
         * @return this
         */
        public Builder coreferenceChainId(int coreferenceChainId) {
            this.coreferenceChainId = coreferenceChainId;
            return this;
        }

        /**
         * Returns an immutable resolved entity from the current state of the builder.
         *
         * @return the new resolved entity
         */
        public ResolvedEntity build() {
            return new ResolvedEntity(startOffset, endOffset, entityId, coreferenceChainId, confidence,
                    buildExtendedProperties());
        }
    }
}
