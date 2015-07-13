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
     * Predicate.
     */
    private final RelationshipComponent predicate;

    /**
     * First argument.
     */
    private final RelationshipComponent arg1;
    /**
     * Second argument.
     */
    private final RelationshipComponent arg2;
    /**
     * Third argument.
     */
    private final RelationshipComponent arg3;
    private final List<RelationshipComponent> adjuncts;
    private final List<RelationshipComponent> locatives;
    private final List<RelationshipComponent> temporals;

    /**
     * A display string representing the extractor that generated the relationshipMention
     */
    private final String relationshipSource;

    protected RelationshipMention(int startOffset, int endOffset,
                                  RelationshipComponent predicate,
                                  RelationshipComponent arg1,
                                  RelationshipComponent arg2,
                                  RelationshipComponent arg3,
                                  List<RelationshipComponent> adjuncts,
                                  List<RelationshipComponent> locatives,
                                  List<RelationshipComponent> temporals,
                                  String relationshipSource, Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
        this.relationshipSource = relationshipSource;
        this.predicate = predicate;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
        this.adjuncts = listOrNull(adjuncts);
        this.locatives = listOrNull(locatives);
        this.temporals = listOrNull(temporals);
    }

    public RelationshipComponent getPredicate() {
        return predicate;
    }

    public RelationshipComponent getArg1() {
        return arg1;
    }

    public RelationshipComponent getArg2() {
        return arg2;
    }

    public RelationshipComponent getArg3() {
        return arg3;
    }

    public List<RelationshipComponent> getAdjuncts() {
        return adjuncts;
    }

    public List<RelationshipComponent> getLocatives() {
        return locatives;
    }

    public List<RelationshipComponent> getTemporals() {
        return temporals;
    }

    public String getRelationshipSource() {
        return relationshipSource;
    }

    @Override
    protected Objects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("predicate", predicate)
                .add("arg1", arg1)
                .add("arg2", arg2)
                .add("arg3", arg3)
                .add("adjuncts", adjuncts)
                .add("locatives", locatives)
                .add("temporals", temporals)
                .add("relationshipSource", relationshipSource);

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

        if (predicate != null ? !predicate.equals(that.predicate) : that.predicate != null) {
            return false;
        }

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

        if (relationshipSource != null ? !relationshipSource.equals(that.relationshipSource) : that.relationshipSource != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (predicate != null ? predicate.hashCode() : 0);
        result = 31 * result + (arg1 != null ? arg1.hashCode() : 0);
        result = 31 * result + (arg2 != null ? arg2.hashCode() : 0);
        result = 31 * result + (arg3 != null ? arg3.hashCode() : 0);
        result = 31 * result + (adjuncts != null ? adjuncts.hashCode() : 0);
        result = 31 * result + (locatives != null ? locatives.hashCode() : 0);
        result = 31 * result + (temporals != null ? temporals.hashCode() : 0);
        result = 31 * result + (relationshipSource != null ? relationshipSource.hashCode() : 0);
        return result;
    }

    public static class Builder extends Attribute.Builder {

        private RelationshipComponent predicate;
        private RelationshipComponent arg1;
        private RelationshipComponent arg2;
        private RelationshipComponent arg3;
        private List<RelationshipComponent> adjuncts;
        private List<RelationshipComponent> locatives;
        private List<RelationshipComponent> temporals;

        private String relationshipSource;

        public Builder(int startOffset, int endOffset) {
            super(startOffset, endOffset);
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
            predicate = toCopy.predicate;
            arg1 = toCopy.arg1;
            arg2 = toCopy.arg2;
            arg3 = toCopy.arg3;
            addAllToList(adjuncts, toCopy.adjuncts);
            addAllToList(locatives, toCopy.locatives);
            addAllToList(temporals, toCopy.temporals);
            this.relationshipSource = toCopy.relationshipSource;
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
         * Attaches predicate
         *
         * @param predicate the predicate
         * @return predicate
         */
        public Builder predicate(RelationshipComponent predicate) {
            this.predicate = predicate;
            return this;
        }

        /**
         * Attaches arg1
         *
         * @param arg1 the arg1
         * @return this
         */
        public Builder arg1(RelationshipComponent arg1) {
            this.arg1 = arg1;
            return this;
        }

        /**
         * Attaches arg2
         *
         * @param arg2 the arg2
         * @return this
         */
        public Builder arg2(RelationshipComponent arg2) {
            this.arg2 = arg2;
            return this;
        }

        /**
         * Attaches arg3
         *
         * @param arg3 the arg3
         * @return this
         */
        public Builder arg3(RelationshipComponent arg3) {
            this.arg3 = arg3;
            return this;
        }

        /**
         * Attaches a list of adjuncts
         *
         * @param adjuncts the adjuncts
         * @return this
         */
        public Builder adjuncts(ListAttribute<RelationshipComponent> adjuncts) {
            this.adjuncts = Lists.newArrayList();
            addAllToList(this.adjuncts, adjuncts);
            return this;
        }

        /**
         * Add an adjunct.
         * @param adjunct the adjunct
         * @return this
         */
        public Builder addAdjunct(RelationshipComponent adjunct) {
            adjuncts.add(adjunct);
            return this;
        }

        /**
         * Attaches a list of locatives
         *
         * @param locatives the locatives
         * @return this
         */
        public Builder locatives(ListAttribute<RelationshipComponent> locatives) {
            this.locatives = Lists.newArrayList();
            addAllToList(this.locatives, locatives);
            return this;
        }

        /**
         * Add a locative.
         * @param locative the locative
         * @return this
         */
        public Builder addLocative(RelationshipComponent locative) {
            locatives.add(locative);
            return this;
        }

        /**
         * Attaches a list of temporals
         *
         * @param temporals the temporals
         * @return this
         */
        public Builder temporals(ListAttribute<RelationshipComponent> temporals) {
            this.temporals = Lists.newArrayList();
            addAllToList(this.temporals, temporals);
            return this;
        }

        /**
         * Add a temporal.
         * @param temporal the temporal
         * @return this
         */
        public Builder addTemporal(RelationshipComponent temporal) {
            temporals.add(temporal);
            return this;
        }

        /**
         * Specifies the relation id.
         *
         * @param relationshipSource the he extractor that generated the relationshipMention.
         * @return this
         */
        public Builder relationshipSource(String relationshipSource) {
            this.relationshipSource = relationshipSource;
            return this;
        }

        /**
         * Returns an immutable relation mention from the current state of the builder.
         *
         * @return the new relation mention.
         */
        public RelationshipMention build() {
            return new RelationshipMention(startOffset, endOffset,
                    predicate,
                    arg1,
                    arg2,
                    arg3,
                    adjuncts,
                    locatives,
                    temporals,
                    relationshipSource, buildExtendedProperties());
        }
    }
}
