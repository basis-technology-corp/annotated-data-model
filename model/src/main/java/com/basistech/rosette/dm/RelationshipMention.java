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
 * A Relationship Mention describes arguments in a sentence and a predicate that connects them.
 */
public class RelationshipMention extends Attribute {

    private final RelationshipComponent predicate;
    private final List<RelationshipComponent> arguments;
    private final List<RelationshipComponent> adjuncts;
    private final List<RelationshipComponent> locatives;
    private final List<RelationshipComponent> temporals;
    private final String source;

    protected RelationshipMention(int startOffset, int endOffset,
                                  RelationshipComponent predicate,
                                  List<RelationshipComponent> arguments,
                                  List<RelationshipComponent> adjuncts,
                                  List<RelationshipComponent> locatives,
                                  List<RelationshipComponent> temporals,
                                  String source, Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
        this.source = source;
        this.predicate = predicate;
        this.arguments = listOrNull(arguments);
        this.adjuncts = listOrNull(adjuncts);
        this.locatives = listOrNull(locatives);
        this.temporals = listOrNull(temporals);
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
        if (arguments != null && arguments.size() > 0) {
            return arguments.get(0);
        }
        return null;
    }

    /**
     * Returns the second argument. The second argument complements the predicate and is usually the object, theme or
     * patient of the relationship.
     *
     * @return the second argument
     */
    public RelationshipComponent getArg2() {
        if (arguments != null && arguments.size() > 1) {
            return arguments.get(1);
        }
        return null;
    }

    /**
     * Returns the third argument. The third argument is usually an additional object in ditransitive verbs.
     *
     * @return the third  argument
     */
    public RelationshipComponent getArg3() {
        if (arguments != null && arguments.size() > 2) {
            return arguments.get(2);
        }
        return null;
    }

    /**
     * Returns a list of arguments.
     *
     * @return a list of arguments
     */
    public List<RelationshipComponent> getArguments() {
        return arguments;
    }

    /**
     * Returns a list of adjuncts. Adjuncts contain all optional parts of a relationship which are not temporal or
     * locative expressions.
     *
     * @return a list of adjuncts
     */
    public List<RelationshipComponent> getAdjuncts() {
        return adjuncts;
    }

    /**
     * Returns a list of locative expressions. Locatives usually express the locations the action expressed by
     * the relationship took place.
     *
     * @return a list of locative expressions
     */
    public List<RelationshipComponent> getLocatives() {
        return locatives;
    }

    /**
     * Returns a list of temporal expressions. Temporals usually express the time in which the action expressed by
     * the relationship took place.
     *
     * @return a list of termporal expressions
     */
    public List<RelationshipComponent> getTemporals() {
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

    @Override
    protected Objects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("predicate", predicate)
                .add("arguments", getArguments())
                .add("adjuncts", adjuncts)
                .add("locatives", locatives)
                .add("temporals", temporals)
                .add("source", source);

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

        if (arguments != null ? !arguments.equals(that.arguments) : that.arguments != null) {
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

        if (source != null ? !source.equals(that.source) : that.source != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (predicate != null ? predicate.hashCode() : 0);
        result = 31 * result + (arguments != null ? arguments.hashCode() : 0);
        result = 31 * result + (adjuncts != null ? adjuncts.hashCode() : 0);
        result = 31 * result + (locatives != null ? locatives.hashCode() : 0);
        result = 31 * result + (temporals != null ? temporals.hashCode() : 0);
        result = 31 * result + (source != null ? source.hashCode() : 0);
        return result;
    }

    public static class Builder extends Attribute.Builder {

        private RelationshipComponent predicate;
        private List<RelationshipComponent> arguments;
        private List<RelationshipComponent> adjuncts;
        private List<RelationshipComponent> locatives;
        private List<RelationshipComponent> temporals;

        private String source;

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
            this.arguments = Lists.newArrayList();
            this.adjuncts = Lists.newArrayList();
            this.locatives = Lists.newArrayList();
            this.temporals = Lists.newArrayList();
        }

        /**
         * Constructs a builder by copying values from an existing relationship mention.
         *
         * @param toCopy the object to create
         * @adm.ignore
         */
        public Builder(RelationshipMention toCopy) {
            super(toCopy);
            predicate = toCopy.predicate;
            addAllToList(arguments, toCopy.arguments);
            addAllToList(adjuncts, toCopy.adjuncts);
            addAllToList(locatives, toCopy.locatives);
            addAllToList(temporals, toCopy.temporals);
            this.source = toCopy.source;
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
            this.arguments.add(0, arg1);
            return this;
        }

        /**
         * Attaches arg2
         *
         * @param arg2 the arg2
         * @return this
         */
        public Builder arg2(RelationshipComponent arg2) {
            this.arguments.add(1, arg2);
            return this;
        }

        /**
         * Attaches arg3
         *
         * @param arg3 the arg3
         * @return this
         */
        public Builder arg3(RelationshipComponent arg3) {
            this.arguments.add(2, arg3);
            return this;
        }

        /**
         * Attaches a list of arguments
         *
         * @param arguments the arguments
         * @return this
         */
        public Builder arguments(List<RelationshipComponent> arguments) {
            addAllToList(this.arguments, arguments);
            return this;
        }

        /**
         * Attaches a list of adjuncts
         *
         * @param adjuncts the adjuncts
         * @return this
         */
        public Builder adjuncts(List<RelationshipComponent> adjuncts) {
            this.adjuncts = Lists.newArrayList();
            addAllToList(this.adjuncts, adjuncts);
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
        public Builder locatives(List<RelationshipComponent> locatives) {
            this.locatives = Lists.newArrayList();
            addAllToList(this.locatives, locatives);
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
        public Builder temporals(List<RelationshipComponent> temporals) {
            this.temporals = Lists.newArrayList();
            addAllToList(this.temporals, temporals);
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
         * @param source the he extractor that generated the relationshipMention.
         * @return this
         */
        public Builder source(String source) {
            this.source = source;
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
                    arguments,
                    adjuncts,
                    locatives,
                    temporals,
                    source, buildExtendedProperties());
        }
    }
}
