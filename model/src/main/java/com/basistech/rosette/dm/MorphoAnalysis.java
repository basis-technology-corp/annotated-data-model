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
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * A MorphoAnalysis contains all the results of analyzing a word, or something like a word.
 * This data model assumes that, presented with a token, an analyzer will produce one or more analyses,
 * where each analysis is a coordinated collection of attributes. The simplest example is a part of speech tag
 * and a lemma, but some languages yield more data.
 * <p/>
 * This one class contains the basic attributes that are more or less common to the languages supported by
 * Rosette Base Linguistics. There are subclasses for some specific languages.
 * <p/>
 * In some languages, words are decompounded into pieces that can, themselves, be analyzed.
 */
public class MorphoAnalysis extends BaseAttribute {
    // every language we support has part of speech and lemma
    private final String partOfSpeech;
    private final String lemma;
    // several language have compounds; we store a full token so that we can have analyses of the components.
    private final List<Token> components;
    private final String raw; // raw representation of the analysis; interpretation depends on the language and implementation.

    /**
     * Creates an analysis.
     *
     * @param partOfSpeech  part of speech
     * @param lemma lemma
     * @param components components
     * @param raw the raw analysis
     */
    MorphoAnalysis(String partOfSpeech,
                   String lemma,
                   List<Token> components,
                   String raw) {
        this.partOfSpeech = partOfSpeech;
        this.lemma = lemma;
        this.components = components;
        this.raw = raw;
    }

    /**
     * Creates an analysis.
     *
     * @param partOfSpeech part of speech
     * @param lemma the lemma
     * @param components compound components
     * @param raw raw analysis
     * @param extendedProperties extended properties
     */
    MorphoAnalysis(String partOfSpeech,
                   String lemma,
                   List<Token> components,
                   String raw,
                   Map<String, Object> extendedProperties) {
        super(extendedProperties);
        this.partOfSpeech = partOfSpeech;
        this.lemma = lemma;
        this.components = components;
        this.raw = raw;
    }

    protected MorphoAnalysis() {
        partOfSpeech = "";
        lemma = "";
        components = Lists.newArrayList();
        raw = "";
    }

    /**
     * Returns the part of speech.
     *
     * @return the part of speech
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MorphoAnalysis that = (MorphoAnalysis) o;

        if (components != null ? !components.equals(that.components) : that.components != null) {
            return false;
        }
        if (lemma != null ? !lemma.equals(that.lemma) : that.lemma != null) {
            return false;
        }
        if (partOfSpeech != null ? !partOfSpeech.equals(that.partOfSpeech) : that.partOfSpeech != null) {
            return false;
        }
        if (raw != null ? !raw.equals(that.raw) : that.raw != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = partOfSpeech != null ? partOfSpeech.hashCode() : 0;
        result = 31 * result + (lemma != null ? lemma.hashCode() : 0);
        result = 31 * result + (components != null ? components.hashCode() : 0);
        result = 31 * result + (raw != null ? raw.hashCode() : 0);
        return result;
    }

    protected Objects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("partOfSpeech", partOfSpeech)
                .add("lemma", lemma)
                .add("components", components)
                .add("raw", raw);
    }

    @Override
    public String toString() {
        return toStringHelper().toString();
    }

    /**
     * Builder for {@link com.basistech.rosette.dm.MorphoAnalysis}.
     */
    public static class Builder extends BaseAttribute.Builder {
        protected String partOfSpeech;
        protected String lemma;
        protected List<Token> components;
        protected String raw;

        /**
         * Constructs a builder with default values.
         */
        public Builder() {
            super();
            components = Lists.newArrayList();
            raw = null;
            lemma = null;
            partOfSpeech = null;
        }

        /**
         * Constructs a builder from an existing analysis.
         *
         * @param toCopy the object to copy
         * @adm.ignore
         */
        public Builder(MorphoAnalysis toCopy) {
            super(toCopy);
            partOfSpeech = toCopy.partOfSpeech;
            lemma = toCopy.lemma;
            components.addAll(toCopy.components);
            raw = toCopy.raw;
        }

        /**
         * Specifies the part of speech.
         *
         * @param partOfSpeech the part of speech
         * @return this
         */
        public Builder partOfSpeech(String partOfSpeech) {
            this.partOfSpeech = partOfSpeech;
            return this;
        }

        /**
         * Specifies the lemma.
         *
         * @param lemma the lemma
         * @return this
         */
        public Builder lemma(String lemma) {
            this.lemma = lemma;
            return this;
        }

        /**
         * Specifies the raw analysis.
         *
         * @param raw the raw analysis
         * @return this
         */
        public Builder raw(String raw) {
            this.raw = raw;
            return this;
        }

        /**
         * Adds a compound component.
         *
         * @param component the component
         * @return this
         */
        public Builder addComponent(Token component) {
            this.components.add(component);
            return this;
        }

        /**
         * Builds a new immutable morpho analysis from the current state of the builder.
         *
         * @return the new analysis
         */
        public MorphoAnalysis build() {
            List<Token> actualComponents = null;
            if (components.size() != 0) {
                actualComponents = components;
            }

            return new MorphoAnalysis(partOfSpeech, lemma, actualComponents, raw);
        }
    }
}
