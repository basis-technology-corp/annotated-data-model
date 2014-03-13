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

package com.basistech.dm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * A MorphoAnalysis contains all the results of analyzing a word, or something like a word.
 * This data model assumes that, presented with a token, an analyzer will produce one or more analyses,
 * where each analysis is a coordinated collection of attributes. The simplest example is a part of speech tag
 * and a lemma, but some languages yield more data.
 * <br/>
 * This one class contains the basic attributes that are more or less common to the languages supported by
 * Rosette Base Linguistics. There are subclasses for some specific languages. This class does not declare
 * Jackson polymorhphism directly; since all of the analyses of a token will be for the same language,
 * there is custom support for the list of them in the token to avoid redundantly storing the information.
 * <br/>
 * In some languages, words are decompounded into pieces that can, themselves, be analyzed.
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class MorphoAnalysis {
    // every language we support has part of speech and lemma
    private final String partOfSpeech;
    private final String lemma;
    // several language have compounds; we store a full token so that we can have analyses of the components.
    private final List<Token> components;
    private final String raw; // raw representation of the analysis; interpretation depends on the language and implementation.

    /**
     * Create an analysis.
     * @param partOfSpeech  part of speech
     * @param lemma lemma
     * @param components components.
     */
    public MorphoAnalysis(String partOfSpeech, String lemma, List<Token> components, String raw) {
        this.partOfSpeech = partOfSpeech;
        this.lemma = lemma;
        this.components = components == null ? Lists.<Token>newArrayList() : components;
        this.raw = raw;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public String getLemma() {
        return lemma;
    }

    public List<Token> getComponents() {
        return components;
    }

    public String getRaw() {
        return raw;
    }

    /**
     * Builder for MorphoAnalysis.
     */
    public static class Builder {
        protected String partOfSpeech;
        protected String lemma;
        protected List<Token> components;
        protected String raw;

        public Builder() {
            components = Lists.newArrayList();
        }

        public Builder(MorphoAnalysis toCopy) {
            this();
            partOfSpeech = toCopy.partOfSpeech;
            lemma = toCopy.lemma;
            components.addAll(toCopy.components);
        }

        public void partOfSpeech(String partOfSpeech) {
            this.partOfSpeech = partOfSpeech;
        }

        public void lemma(String lemma) {
            this.lemma = lemma;
        }

        public void raw(String raw) {
            this.raw = raw;
        }

        public void addComponent(Token component) {
            this.components.add(component);
        }

        public MorphoAnalysis build() {
            return new MorphoAnalysis(partOfSpeech, lemma, components, raw);
        }
    }
}
