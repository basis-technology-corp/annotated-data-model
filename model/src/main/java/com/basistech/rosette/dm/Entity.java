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

public class Entity extends Attribute {
    private final String entityId;
    private final int coreferenceChainId;
    private final double confidence;
    private final boolean error;

    Entity(int startOffset, int endOffset, String entityId,
           int coreferenceChainId, double confidence, boolean error,
           Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
        this.entityId = entityId;
        this.coreferenceChainId = coreferenceChainId;
        this.confidence = confidence;
        this.error = error;
    }

    Entity(int startOffset, int endOffset, String entityId,
           int coreferenceChainId, double confidence,
           boolean error) {
        super(startOffset, endOffset);
        this.entityId = entityId;
        this.coreferenceChainId = coreferenceChainId;
        this.confidence = confidence;
        this.error = error;
    }

    protected Entity() {
        // make jackson happy.
        entityId = null;
        confidence = 0.0;
        coreferenceChainId = -1;
        error = true;
    }

    public String getEntityId() {
        return entityId;
    }

    public int getCoreferenceChainId() {
        return coreferenceChainId;
    }

    public double getConfidence() {
        return confidence;
    }

    public boolean isError() {
        return error;
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

        Entity that = (Entity) o;

        if (!entityId.equals(that.entityId)) {
            return false;
        }
        if (Double.compare(that.confidence, confidence) != 0) {
            return false;
        }
        if (coreferenceChainId != that.coreferenceChainId) {
            return false;
        }
        if (error != that.error) {
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
        result = 31 * result + (error ? 1 : 0);
        return result;
    }

    @Override
    protected Objects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("entityId", entityId)
                .add("confidence", confidence)
                .add("coreferenceChainId", coreferenceChainId)
                .add("error", error);
    }

    /**
     * A builder for entity.
     */
    public static class Builder extends Attribute.Builder {
        private String entityId;
        private double confidence;
        private int coreferenceChainId = -1;
        private boolean error;

        public Builder(int startOffset, int endOffset, String entityId) {
            super(startOffset, endOffset);
            this.entityId = entityId;
        }

        public Builder(Entity toCopy) {
            super(toCopy);
            this.entityId = toCopy.entityId;
            this.confidence = toCopy.confidence;
            this.coreferenceChainId = toCopy.coreferenceChainId;
            this.error = toCopy.error;
        }

        public void entityId(String entityId) {
            this.entityId = entityId;
        }

        public void confidence(double confidence) {
            this.confidence = confidence;
        }

        public void coreferenceChainId(int coreferenceChainId) {
            this.coreferenceChainId = coreferenceChainId;
        }

        public void error(boolean error) {
            this.error = error;
        }

        public Entity build() {
            return new Entity(startOffset, endOffset, entityId, coreferenceChainId, confidence, error, extendedProperties);
        }
    }

}
