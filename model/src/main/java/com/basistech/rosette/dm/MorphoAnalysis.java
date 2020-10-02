/*
* Copyright 2019 Basis Technology Corp.
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
import com.google.common.collect.Lists;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * A MorphoAnalysis contains all the results of analyzing a word, or something like a word.
 * This data model assumes that, presented with a token, an analyzer will produce one or more analyses,
 * where each analysis is a coordinated collection of attributes. The simplest example is a part-of-speech tag
 * and a lemma, but some languages yield more data.
 * <br>
 * This one class contains the basic attributes that are more or less common to the languages supported by
 * Rosette Base Linguistics. There are subclasses for some specific languages.
 * <br>
 * In some languages, words are decompounded into pieces that can, themselves, be analyzed.
 */
@EqualsAndHashCode(callSuper = true)
public class MorphoAnalysis extends BaseAttribute implements Serializable {
    private static final long serialVersionUID = 250L;
    // every language we support has part-of-speech and lemma
    private final String partOfSpeech;
    private final String lemma;
    // several language have compounds; we store a full token so that we can have analyses of the components.
    private final List<Token> components;
    private final String raw; // raw representation of the analysis; interpretation depends on the language and implementation.
    private final TagSet tagSet;

    /**
     * Creates an analysis.
     *
     * @param partOfSpeech part-of-speech
     * @param lemma the lemma
     * @param components compound components
     * @param raw raw analysis
     * @param tagSet the tag set
     * @param extendedProperties extended properties
     */
    protected MorphoAnalysis(String partOfSpeech,
                             String lemma,
                             List<Token> components,
                             String raw,
                             TagSet tagSet,
                             Map<String, Object> extendedProperties) {
        super(extendedProperties);
        this.partOfSpeech = partOfSpeech;
        this.lemma = lemma;
        this.components = listOrNull(components);
        this.raw = raw;
        this.tagSet = tagSet;
    }

    /**
     * Creates an analysis.
     *
     * @param partOfSpeech part-of-speech
     * @param lemma the lemma
     * @param components compound components
     * @param raw raw analysis
     * @param extendedProperties extended properties
     */
    protected MorphoAnalysis(String partOfSpeech,
                             String lemma,
                             List<Token> components,
                             String raw,
                             Map<String, Object> extendedProperties) {
        this(partOfSpeech, lemma, components, raw, null, extendedProperties);
    }

    /**
     * Returns the part-of-speech.
     *
     * @return the part-of-speech
     */
    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    /**
     * Returns the lemma.
     *
     * @return the lemma
     */
    public String getLemma() {
        return lemma;
    }

    /**
     * Returns the compound components.
     *
     * @return the compound components
     */
    public List<Token> getComponents() {
        return components;
    }

    /**
     * Returns the raw analysis.
     * The raw analysis is a language and analyzer-specific string, typically
     * used to convey debug-level information.  Applications should usually not
     * depend on the raw analysis.
     *
     * @return the raw analysis
     */
    public String getRaw() {
        return raw;
    }

    /**
     * Returns the tag set.
     *
     * @return the tag set
     */
    public TagSet getTagSet() {
        return tagSet;
    }

    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("partOfSpeech", partOfSpeech)
                .add("lemma", lemma)
                .add("components", components)
                .add("raw", raw)
                .add("tagSet", tagSet);
    }

    @Override
    public String toString() {
        return toStringHelper().toString();
    }

    /**
     * Builder for {@link com.basistech.rosette.dm.MorphoAnalysis}.
     */
    public static class Builder<T extends MorphoAnalysis, B extends Builder<T, B>> extends BaseAttribute.Builder<MorphoAnalysis, MorphoAnalysis.Builder<T, B>>  {
        protected String partOfSpeech;
        protected String lemma;
        protected List<Token> components;
        protected String raw;
        protected TagSet tagSet;

        /**
         * Constructs a builder with default values.
         */
        public Builder() {
            super();
            components = Lists.newArrayList();
            raw = null;
            lemma = null;
            partOfSpeech = null;
            tagSet = null;
        }

        /**
         * Constructs a builder from an existing analysis.
         *
         * @param toCopy the object to copy
         */
        public Builder(MorphoAnalysis toCopy) {
            super(toCopy);
            components = Lists.newArrayList();
            partOfSpeech = toCopy.partOfSpeech;
            lemma = toCopy.lemma;
            addAllToList(components, toCopy.components);
            raw = toCopy.raw;
            tagSet = toCopy.tagSet;
        }

        /**
         * Specifies the part-of-speech.
         *
         * @param partOfSpeech the part-of-speech
         * @return this
         */
        public B partOfSpeech(String partOfSpeech) {
            this.partOfSpeech = partOfSpeech;
            return getThis();
        }

        /**
         * Specifies the lemma.
         *
         * @param lemma the lemma
         * @return this
         */
        public B lemma(String lemma) {
            this.lemma = lemma;
            return getThis();
        }

        /**
         * Specifies the raw analysis.
         *
         * @param raw the raw analysis
         * @return this
         */
        public B raw(String raw) {
            this.raw = raw;
            return getThis();
        }

        /**
         * Specifies the tag set.
         *
         * @param tagSet the tag set
         * @return this
         */
        public B tagSet(TagSet tagSet) {
            this.tagSet = tagSet;
            return getThis();
        }

        /**
         * Adds a compound component.
         *
         * @param component the component
         * @return this
         */
        public B addComponent(Token component) {
            this.components.add(component);
            return getThis();
        }

        /**
         * Sets all of the compound components.
         * @param components the components.
         * @return this.
         */
        public B components(List<Token> components) {
            this.components = nullOrList(components);
            return getThis();
        }

        /**
         * Builds a new immutable morpho analysis from the current state of the builder.
         *
         * @return the new analysis
         */
        public MorphoAnalysis build() {
            return new MorphoAnalysis(partOfSpeech, lemma, components, raw, tagSet, buildExtendedProperties());
        }

        // because this class is not abstract, we can't have an abstract method.
        @SuppressWarnings("unchecked")
        @Override
        protected B getThis() {
            return (B) this;
        }
    }
}
