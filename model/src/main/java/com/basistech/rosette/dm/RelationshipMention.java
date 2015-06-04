/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2015 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/

package com.basistech.rosette.dm;

import com.google.common.base.Objects;

import java.util.List;
import java.util.Map;

public class RelationshipMention extends BaseAttribute {

    /**
     * A display string representing the relation
     */
    private final String relPhrase;

    /**
     * An unbounded list of relation arguments
     */
    private final List<RelationshipArgument> relArgs;

    /**
     * Specifies whether the relation appears in the sentence (synthetic=false)
     * or whether it is based on a syntactic configuration like apposition or
     * possessives.
     */
    private final boolean synthetic;

    /**
     * placeholder for an identifier from an external knowledge-base the predicate resolves to.
     */
    private final String relId;

    protected RelationshipMention(String relPhrase, List<RelationshipArgument> relArgs, boolean synthetic, String relId, Map<String, Object> extendedProperties) {
        super(extendedProperties);
        this.relPhrase = relPhrase;
        this.relArgs = relArgs;
        this.synthetic = synthetic;
        this.relId = relId;
    }

    public String getRelPhrase() {
        return relPhrase;
    }

    public List<RelationshipArgument> getRelArgs() {
        return relArgs;
    }

    public String getRelId() {
        return relId;
    }

    public boolean getSynthetic() {
        return synthetic;
    }

    @Override
    protected Objects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("relPhrase", relPhrase)
                .add("relArgs", relArgs)
                .add("synthetic", synthetic)
                .add("relId", relId);

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

        RelationshipMention that = (RelationshipMention) o;

        if (synthetic != that.synthetic) {
            return false;
        }

        if (!relArgs.equals(that.relArgs)) {
            return false;
        }

        if (relId != null ? !relId.equals(that.relId) : that.relId != null) {
            return false;
        }

        if (!relPhrase.equals(that.relPhrase)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + relPhrase.hashCode();
        result = 31 * result + relArgs.hashCode();
        result = 31 * result + (synthetic ? 1 : 0);
        result = 31 * result + (relId != null ? relId.hashCode() : 0);
        return result;
    }

    public static class Builder extends BaseAttribute.Builder {

        private String relPhrase;
        private List<RelationshipArgument> relArgs;
        private boolean synthetic;
        private String relId;


        public Builder(String relPhrase, List<RelationshipArgument> relArgs) {
            super();
            this.relPhrase = relPhrase;
            this.relArgs = relArgs;
            this.synthetic = false;
        }

        /**
         * Constructs a builder by copying values from an existing resolved entity.
         *
         * @param toCopy the object to create
         * @adm.ignore
         */
        public Builder(RelationshipMention toCopy) {
            super(toCopy);
            this.relPhrase = toCopy.relPhrase;
            this.relArgs = toCopy.relArgs;
            this.synthetic = toCopy.synthetic;
            this.relId = toCopy.relId;
        }

        /**
         * Specifies a phrase representing the relation
         *
         * @param relPhrase the relation phrase
         * @return
         */
        public Builder relPhrase(String relPhrase) {
            this.relPhrase = relPhrase;
            return this;
        }

        /**
         * Adds an extended value key-value pair.
         *
         * @param key the key
         * @param value the value
         */
        public Builder extendedProperty(String key, Object value) {
            this.extendedProperties.put(key, value);
            return this;
        }

        /**
         * Specifies the relation arguments.
         *
         * @param relArgs the relation arguments.
         * @return this
         */
        public Builder relArgs(List<RelationshipArgument> relArgs) {
            this.relArgs = relArgs;
            return this;
        }

        /**
         * Specifies whether the relation is synthetic or textual.
         *
         * @param synthetic flag indicating whether the relation is synthetic or textual.
         * @return this
         */
        public Builder synthetic(boolean synthetic) {
            this.synthetic = synthetic;
            return this;
        }

        /**
         * Specifies the relation id.
         *
         * @param relId the relation id.
         * @return this
         */
        public Builder relId(String relId) {
            this.relId = relId;
            return this;
        }

        /**
         * Returns an immutable relation mention from the current state of the builder.
         *
         * @return the new relation mention.
         */
        public RelationshipMention build() {
            return new RelationshipMention(relPhrase, relArgs, synthetic, relId, extendedProperties);
        }
    }
}
