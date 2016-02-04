/*
* Copyright 2014 Basis Technology Corp.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.basistech.rosette.dm;

import com.google.common.base.Objects;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * A Relationship Mention describes arguments in a sentence and a predicate that connects them.
 */
public class RelationshipMention extends Attribute {

    private final RelationshipComponent predicate;
    private final RelationshipComponent arg1;
    private final RelationshipComponent arg2;
    private final RelationshipComponent arg3;
    private final Set<RelationshipComponent> adjuncts;
    private final Set<RelationshipComponent> locatives;
    private final Set<RelationshipComponent> temporals;
    private final String source;
    private final Double confidence;

    protected RelationshipMention(int startOffset, int endOffset,
                                  RelationshipComponent predicate,
                                  RelationshipComponent arg1,
                                  RelationshipComponent arg2,
                                  RelationshipComponent arg3,
                                  Set<RelationshipComponent> adjuncts,
                                  Set<RelationshipComponent> locatives,
                                  Set<RelationshipComponent> temporals,
                                  String source,
                                  Double confidence, Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
        this.source = source;
        this.confidence = confidence;
        this.predicate = predicate;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
        this.adjuncts = setOrNull(adjuncts);
        this.locatives = setOrNull(locatives);
        this.temporals = setOrNull(temporals);

    }

    /**
     * Returns the predicate. A predicate is usually the main verb, property or action that is expressed by the text.
     *
     * @return the predicate of the relationship
     */
    public RelationshipComponent getPredicate() {
        return predicate;
    }

    /**
     * Returns the first argument. The first argument is usually the subject, agent or main actor of the relationship.
     *
     * @return the first argument
     */
    public RelationshipComponent getArg1() {
        return arg1;
    }

    /**
     * Returns the second argument. The second argument complements the predicate and is usually the object, theme or
     * patient of the relationship.
     *
     * @return the second argument
     */
    public RelationshipComponent getArg2() {
        return arg2;
    }

    /**
     * Returns the third argument. The third argument is usually an additional object in ditransitive verbs.
     *
     * @return the third  argument
     */
    public RelationshipComponent getArg3() {
        return arg3;
    }

    /**
     * Returns a set of adjuncts. Adjuncts contain all optional parts of a relationship which are not temporal or
     * locative expressions.
     *
     * @return a set of adjuncts
     */
    public Set<RelationshipComponent> getAdjuncts() {
        return adjuncts;
    }

    /**
     * Returns a set of locative expressions. Locatives usually express the locations the action expressed by
     * the relationship took place.
     *
     * @return a set of locative expressions
     */
    public Set<RelationshipComponent> getLocatives() {
        return locatives;
    }

    /**
     * Returns a set of temporal expressions. Temporals usually express the time in which the action expressed by
     * the relationship took place.
     *
     * @return a set of termporal expressions
     */
    public Set<RelationshipComponent> getTemporals() {
        return temporals;
    }

    /**
     * Returns the relationship extraction device that produced this mention.  For example, "statistical", "rules"
     *
     * @return the relationship extraction source
     */
    public String getSource() {
        return source;
    }

    /**
     * Returns the confidence value for the mention
     *
     * @return confidence value
     */
    public Double getConfidence() {
        return confidence;
    }

    /**
     * Returns true if the predicate is synthetic (eg is not represented by the text)
     *
     * @return boolean
     */
    public boolean hasSyntheticPredicate() {
        return predicate.getExtents() == null || predicate.getExtents().isEmpty();
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
                .add("source", source)
                .add("confidence", confidence);

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

        if (confidence != null ? !confidence.equals(that.confidence) : that.confidence != null) {
            return false;
        }

        return !(source != null ? !source.equals(that.source) : that.source != null);
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
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (confidence != null ? confidence.hashCode() : 0);
        return result;
    }

    public static class Builder extends Attribute.Builder<RelationshipMention, RelationshipMention.Builder> {

        private RelationshipComponent predicate;
        private RelationshipComponent arg1;
        private RelationshipComponent arg2;
        private RelationshipComponent arg3;
        private Set<RelationshipComponent> adjuncts;
        private Set<RelationshipComponent> locatives;
        private Set<RelationshipComponent> temporals;

        private String source;
        private Double confidence;

        /**
         * Constructs a builder with the minimal required information for an relationship mentions.
         *
         * Relationship mention start and end offsets point to the region in the data that the mention was extracted from,
         * it could be a sentence, clause, or string boundaries without any linguistic meaning.
         *
         * @param startOffset the start offset in the text, in characters
         * @param endOffset the end offset in the text, in characters
         */
        public Builder(int startOffset, int endOffset) {
            super(startOffset, endOffset);
            this.adjuncts = new HashSet<>();
            this.locatives = new HashSet<>();
            this.temporals = new HashSet<>();
        }

        /**
         * Constructs a builder by copying values from an existing relationship mention.
         *
         * @param toCopy the object to create
         * @adm.ignore
         */
        public Builder(RelationshipMention toCopy) {
            super(toCopy);
            this.adjuncts = new HashSet<>();
            this.locatives = new HashSet<>();
            this.temporals = new HashSet<>();
            predicate = toCopy.predicate;
            arg1 = toCopy.arg1;
            arg2 = toCopy.arg2;
            arg3 = toCopy.arg3;
            addAllToSet(adjuncts, toCopy.adjuncts);
            addAllToSet(locatives, toCopy.locatives);
            addAllToSet(temporals, toCopy.temporals);
            this.source = toCopy.source;
            this.confidence = toCopy.confidence;
        }

        @Override
        protected Builder getThis() {
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
         * Attaches predicate
         *
         * @param predicate the predicate
         * @return this
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
        public Builder adjuncts(Set<RelationshipComponent> adjuncts) {
            this.adjuncts = new HashSet<>();
            addAllToSet(this.adjuncts, adjuncts);
            return this;
        }

        /**
         * Add an adjunct.
         *
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
        public Builder locatives(Set<RelationshipComponent> locatives) {
            this.locatives = new HashSet<>();
            addAllToSet(this.locatives, locatives);
            return this;
        }

        /**
         * Add a locative.
         *
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
        public Builder temporals(Set<RelationshipComponent> temporals) {
            this.temporals = new HashSet<>();
            addAllToSet(this.temporals, temporals);
            return this;
        }

        /**
         * Add a temporal.
         *
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
         * @param source the extractor that generated the relationshipMention.
         * @return this
         */
        public Builder source(String source) {
            this.source = source;
            return this;
        }

        /**
         * Specifies the confidence value for this mention.
         *
         * @param confidence the confidence value
         * @return this
         */
        public Builder confidence(Double confidence) {
            this.confidence = confidence;
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
                    source,
                    confidence, buildExtendedProperties());
        }
    }
}
