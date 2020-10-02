/*
* Copyright 2018 Basis Technology Corp.
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

import com.google.common.base.MoreObjects;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A Relationship Mention describes arguments in a sentence and a predicate that connects them.
 * It also includes event-related information (such as the location and time the action represented by the mention
 * took place), an indication of the relationship's salience and modality, as well as metadata such the name of the
 * algorithm that yielded the extraction and the confidence thereof.
 * For example, the sentence "Leila Lopes, Miss Angola 2011, was crowned Miss Universe 2011 in Brazil on September 12th, 2011."
 * can yield a following RelationshipMention (among others):
 * <blockquote><pre>
 *     arg1: [Leila Lopes(Q232501)]
 *     predicate: [be crowned]
 *     arg2: [Miss Universe 2011(Q932457)]
 *     temporals:    [on September 12th]
 *     locatives:   [In Brazil(Q155)]
 *     context:    [assertion]
 *     source:    [rules:2]
 *     confidence:    [0.679]
 * </pre></blockquote>
 */
@EqualsAndHashCode(callSuper = true)
public class RelationshipMention extends Attribute implements Serializable {
    private static final long serialVersionUID = 250L;

    private final RelationshipComponent predicate;
    private final RelationshipComponent arg1;
    private final RelationshipComponent arg2;
    private final RelationshipComponent arg3;
    private final Set<RelationshipComponent> adjuncts;
    private final Set<RelationshipComponent> locatives;
    private final Set<RelationshipComponent> temporals;
    private final String source;
    private final Double confidence;
    private final Set<String> modality;
    private final Double salience;

    protected RelationshipMention(int startOffset, int endOffset,
                                  RelationshipComponent predicate,
                                  RelationshipComponent arg1,
                                  RelationshipComponent arg2,
                                  RelationshipComponent arg3,
                                  Set<RelationshipComponent> adjuncts,
                                  Set<RelationshipComponent> locatives,
                                  Set<RelationshipComponent> temporals,
                                  String source,
                                  Double confidence,
                                  Set<String> modality,
                                  Double salience,
                                  Map<String, Object> extendedProperties) {
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
        this.modality = setOrNull(modality);
        this.salience = salience;
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
     * Returns a set of locative expressions. Locatives usually express the locations where the action expressed by
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
     * @return a set of temporal expressions
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
     * Returns true if the predicate is synthetic, i.e. does not occurr in the text.
     * For example, the sentence "meet Bill Flax, my brother in law" may yield a synthetic "is" predicate:
     * <blockquote><pre>
     *  [Bill Flax]    [is]    [my brother in law]
     * </pre></blockquote>
     * @return boolean
     */
    public boolean hasSyntheticPredicate() {
        return predicate.getExtents() == null || predicate.getExtents().isEmpty();
    }

    /**
     * Returns the 'modality' values for this relationship.
     * 'Modality' is a catch-all term for a variety of contextual
     * modifications of a relationship mention, such as negation or expression of an opinion. The possible
     * values are defined by the particular extraction system.
     * The Rosette relationship extractor currently emits "assertion", "negation", "question", "opinion" and "uncertainty"
     * @return the modality, or {@code null} if none.
     */
    public Set<String> getModality() {
        return modality;
    }

    /**
     * Returns the salience of this relationship mention, or {@code null} if no
     * salience was calculated. Note that salience values may be quantized.
     * @return the salience.
     */
    public Double getSalience() {
        return salience;
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("predicate", predicate)
                .add("arg1", arg1)
                .add("arg2", arg2)
                .add("arg3", arg3)
                .add("adjuncts", adjuncts)
                .add("locatives", locatives)
                .add("temporals", temporals)
                .add("source", source)
                .add("confidence", confidence)
                .add("salience", salience)
                .add("modality", modality);
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
        private Set<String> modality;
        private Double salience;

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
            this.modality = toCopy.modality;
            this.salience = toCopy.salience;
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
         * Specifies the modalities for this relationship mention.
         * @see RelationshipMention#getModality()
         * @param modality the modality.
         * @return this
         */
        public Builder modality(Set<String> modality) {
            this.modality = new HashSet<>();
            this.modality.addAll(modality);
            return this;
        }

        /**
         * Specifies the salience value for this mention.
         * @param salience the salience value.
         * @return this
         */
        public Builder salience(Double salience) {
            this.salience = salience;
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
                    confidence,
                    modality,
                    salience,
                    buildExtendedProperties());
        }
    }
}
