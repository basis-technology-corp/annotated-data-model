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
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 *
 * An Relationship Mention
 * Relationship mention describes a relationship between arguments in a sentence and a predicate that connects them.
 *
 * Relationship mention start and end offsets point to the region in the data that the mention was extracted from,
 * it could be a sentence, clause, or string boundaries without a linguistic meaning.
 *
 */
public class RelationshipMention extends Attribute {

    /**
     * A display string representing the predicate
     */
    private final String predPhrase;

    /**
     * list of start and end offsets, representing the evidences in the data for a predicate
     */
    private final List<Evidence> evidences;

    /**
     * First argument.
     */
    private final RelationshipArgument arg1;
    /**
     * Second argument.
     */
    private final RelationshipArgument arg2;
    /**
     * Third argument.
     */
    private final RelationshipArgument arg3;
    private final List<RelationshipArgument> adjuncts;
    private final List<RelationshipArgument> locatives;
    private final List<RelationshipArgument> temporals;

    /**
     * placeholder for an identifier from an external knowledge-base the predicate resolves to.
     */
    private final String relId;

    protected RelationshipMention(int startOffset, int endOffset, String predPhrase, List<Evidence> evidences,
                                  RelationshipArgument arg1,
                                  RelationshipArgument arg2,
                                  RelationshipArgument arg3,
                                  List<RelationshipArgument> adjuncts,
                                  List<RelationshipArgument> locatives,
                                  List<RelationshipArgument> temporals,
                                  String relId, Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
        this.predPhrase = predPhrase;
        this.relId = relId;
        this.evidences = listOrNull(evidences);
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
        this.adjuncts = listOrNull(adjuncts);
        this.locatives = listOrNull(locatives);
        this.temporals = listOrNull(temporals);
    }

    public String getPredPhrase() {
        return predPhrase;
    }

    public List<Evidence> getEvidences() {
        return evidences;
    }

    public RelationshipArgument getArg1() {
        return arg1;
    }

    public RelationshipArgument getArg2() {
        return arg2;
    }

    public RelationshipArgument getArg3() {
        return arg3;
    }

    public List<RelationshipArgument> getAdjuncts() {
        return adjuncts;
    }

    public List<RelationshipArgument> getLocatives() {
        return locatives;
    }

    public List<RelationshipArgument> getTemporals() {
        return temporals;
    }

    public String getRelId() {
        return relId;
    }

    @Override
    protected Objects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("predPhrase", predPhrase)
                .add("evidences", evidences)
                .add("arg1", arg1)
                .add("arg2", arg2)
                .add("arg3", arg3)
                .add("adjuncts", adjuncts)
                .add("locatives", locatives)
                .add("temporals", temporals)
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

        if (arg1 != null ? !arg1.equals(that.arg1) : that.arg1 != null) {
            return false;
        }

        if (arg2 != null ? !arg2.equals(that.arg2) : that.arg2 != null) {
            return false;
        }

        if (arg3 != null ? !arg3.equals(that.arg3) : that.arg3 != null) {
            return false;
        }

        if (adjuncts != null ? !adjuncts.equals(that.adjuncts) : that.adjuncts != null) {
            return false;
        }

        if (locatives != null ? !locatives.equals(that.locatives) : that.locatives != null) {
            return false;
        }

        if (temporals != null ? !temporals.equals(that.temporals) : that.temporals != null) {
            return false;
        }

        if (relId != null ? !relId.equals(that.relId) : that.relId != null) {
            return false;
        }

        if (predPhrase != null ? !predPhrase.equals(that.predPhrase) : that.predPhrase != null) {
            return false;
        }

        if (evidences != null ? !evidences.equals(that.getEvidences()) : that.evidences != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (predPhrase != null ? predPhrase.hashCode() : 0);
        result = 31 * result + (evidences != null ? evidences.hashCode() : 0);
        result = 31 * result + (arg1 != null ? arg1.hashCode() : 0);
        result = 31 * result + (arg2 != null ? arg2.hashCode() : 0);
        result = 31 * result + (arg3 != null ? arg3.hashCode() : 0);
        result = 31 * result + (adjuncts != null ? adjuncts.hashCode() : 0);
        result = 31 * result + (locatives != null ? locatives.hashCode() : 0);
        result = 31 * result + (temporals != null ? temporals.hashCode() : 0);
        result = 31 * result + (relId != null ? relId.hashCode() : 0);
        return result;
    }

    public static class Builder extends Attribute.Builder {

        private String predPhrase;
        private RelationshipArgument arg1;
        private RelationshipArgument arg2;
        private RelationshipArgument arg3;
        private List<RelationshipArgument> adjuncts;
        private List<RelationshipArgument> locatives;
        private List<RelationshipArgument> temporals;

        private String relId;
        private List<Evidence> evidences;

        public Builder(int startOffset, int endOffset, String predPhrase) {
            super(startOffset, endOffset);
            this.predPhrase = predPhrase;
            this.evidences = Lists.newArrayList();
            this.adjuncts = Lists.newArrayList();
            this.locatives = Lists.newArrayList();
            this.temporals = Lists.newArrayList();
        }

        /**
         * Constructs a builder by copying values from an existing resolved entity.
         *
         * @param toCopy the object to create
         * @adm.ignore
         */
        public Builder(RelationshipMention toCopy) {
            super(toCopy);
            this.predPhrase = toCopy.predPhrase;
            arg1 = toCopy.arg1;
            arg2 = toCopy.arg2;
            arg3 = toCopy.arg3;
            addAllToList(adjuncts, toCopy.adjuncts);
            addAllToList(locatives, toCopy.locatives);
            addAllToList(temporals, toCopy.temporals);
            this.relId = toCopy.relId;
            this.evidences = toCopy.evidences;
        }

        /**
         * Specifies a phrase representing the predicate
         *
         * @param predPhrase the predicate phrase
         * @return
         */
        public Builder predPhrase(String predPhrase) {
            this.predPhrase = predPhrase;
            return this;
        }

        /**
         * Specifies a phrase evidences
         *
         * @param evidences the relation phrase evidences
         * @return
         */
        public Builder evidences(List<Evidence> evidences) {
            this.evidences = evidences;
            return this;
        }

        /**
         * Adds an extended value key-value pair.
         *
         * @param key the key
         * @param value the value
         */
        public Builder extendedProperty(String key, Object value) {
            super.extendedProperty(key, value);
            return this;
        }

        /**
         * Attaches arg1
         *
         * @param arg1 the arg1
         * @return this
         */
        public Builder arg1(RelationshipArgument arg1) {
            this.arg1 = arg1;
            return this;
        }

        /**
         * Attaches arg2
         *
         * @param arg2 the arg2
         * @return this
         */
        public Builder arg2(RelationshipArgument arg2) {
            this.arg2 = arg2;
            return this;
        }

        /**
         * Attaches arg3
         *
         * @param arg3 the arg3
         * @return this
         */
        public Builder arg3(RelationshipArgument arg3) {
            this.arg3 = arg3;
            return this;
        }

        /**
         * Attaches a list of adjuncts
         *
         * @param adjuncts the adjuncts
         * @return this
         */
        public Builder adjuncts(ListAttribute<RelationshipArgument> adjuncts) {
            this.adjuncts = Lists.newArrayList();
            addAllToList(this.adjuncts, adjuncts);
            return this;
        }

        /**
         * Add an adjunct.
         * @param adjunct the adjunct
         * @return this
         */
        public Builder addAdjunct(RelationshipArgument adjunct) {
            adjuncts.add(adjunct);
            return this;
        }

        /**
         * Attaches a list of locatives
         *
         * @param locatives the locatives
         * @return this
         */
        public Builder locatives(ListAttribute<RelationshipArgument> locatives) {
            this.locatives = Lists.newArrayList();
            addAllToList(this.locatives, locatives);
            return this;
        }

        /**
         * Add a locative.
         * @param locative the locative
         * @return this
         */
        public Builder addLocative(RelationshipArgument locative) {
            locatives.add(locative);
            return this;
        }

        /**
         * Attaches a list of temporals
         *
         * @param temporals the temporals
         * @return this
         */
        public Builder temporals(ListAttribute<RelationshipArgument> temporals) {
            this.temporals = Lists.newArrayList();
            addAllToList(this.temporals, temporals);
            return this;
        }

        /**
         * Add a temporal.
         * @param temporal the temporal
         * @return this
         */
        public Builder addTemporal(RelationshipArgument temporal) {
            temporals.add(temporal);
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
            return new RelationshipMention(startOffset, endOffset, predPhrase, evidences,
                    arg1,
                    arg2,
                    arg3,
                    adjuncts,
                    locatives,
                    temporals,
                    relId,
                    buildExtendedProperties());
        }
    }
}
