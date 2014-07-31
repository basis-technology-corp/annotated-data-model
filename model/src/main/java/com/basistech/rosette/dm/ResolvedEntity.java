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
 * A named entity resolved to an entity 'in the world'.
 * Generally, resolved entities correspond to {@link com.basistech.rosette.dm.EntityMention}s,
 * but the data model allows for the alternative.
 */
public class ResolvedEntity extends Attribute {
    private final String entityId;
    //I prefer 'chainId' over 'coreferenceChainId' but picked the latter to make it consistent with EntityMention
    private final int coreferenceChainId;
    private final double confidence;

    ResolvedEntity(int startOffset, int endOffset, String entityId,
                   int coreferenceChainId, double confidence,
                   Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
        this.entityId = entityId;
        this.coreferenceChainId = coreferenceChainId;
        this.confidence = confidence;
    }

    ResolvedEntity(int startOffset, int endOffset, String entityId,
                   int coreferenceChainId, double confidence) {
        super(startOffset, endOffset);
        this.entityId = entityId;
        this.coreferenceChainId = coreferenceChainId;
        this.confidence = confidence;
    }

    protected ResolvedEntity() {
        // make jackson happy.
        entityId = null;
        confidence = 0.0;
        coreferenceChainId = -1;
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
     * Returns the in-document coreference chain ID for this entity, or -1 if there is none.
     *
     * @return the in-document coreference chain ID for this entity, or -1 if there is none
     */
    public int getCoreferenceChainId() {
        return coreferenceChainId;
    }

    /**
     * Returns the confidence for this resolved entity.
     *
     * @return the confidence for this resolved entity
     */
    public double getConfidence() {
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

        if (!entityId.equals(that.entityId)) {
            return false;
        }
        if (Double.compare(that.confidence, confidence) != 0) {
            return false;
        }
        if (coreferenceChainId != that.coreferenceChainId) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        result = 31 * result + entityId.hashCode();
        temp = Double.doubleToLongBits(confidence);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + coreferenceChainId;
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
        private double confidence; // NAN?
        private int coreferenceChainId = -1;

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
            return new ResolvedEntity(startOffset, endOffset, entityId, coreferenceChainId, confidence, extendedProperties);
        }
    }
}
