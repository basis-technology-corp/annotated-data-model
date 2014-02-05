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

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * The token. A token carries multiple possible values for a set of
 * analytical attributes.
 */
public class Token extends Attribute {
    // we don't want to have to go look at the parent {@link Text}.
    private final String text;
    private final List<String> normalized;
    private final List<String> partOfSpeech;
    private final List<String> lemma;
    private final List<String> stem;
    private final List<List<String>> readings;
    // if components, they are represented as tokens, so that they can have offsets, POS, etc.
    private final List<List<Token>> components;
    private final String source;
    private final List<String> variations;

    public Token(int startOffset, int endOffset,
                 String text,
                 List<String> normalized,
                 List<String> partOfSpeech,
                 List<String> lemma,
                 List<String> stem,
                 List<List<String>> readings,
                 List<List<Token>> components,
                 String source,
                 List<String> variations) {
        super(startOffset, endOffset);
        this.text = text;
        this.normalized = normalized;
        this.partOfSpeech = partOfSpeech;
        this.lemma = lemma;
        this.stem = stem;
        this.readings = readings;
        this.components = components;
        this.source = source;
        this.variations = variations;
    }

    public Token(int startOffset, int endOffset, String text, List<String> normalized, List<String> partOfSpeech, List<String> lemma, List<String> stem, List<List<String>> readings, List<List<Token>> components, String source, List<String> variations, Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
        this.text = text;
        this.normalized = normalized;
        this.partOfSpeech = partOfSpeech;
        this.lemma = lemma;
        this.stem = stem;
        this.readings = readings;
        this.components = components;
        this.source = source;
        this.variations = variations;
    }

    protected Token() {
        this(0, 0, null, Lists.<String>newArrayList(),
            Lists.<String>newArrayList(),
            Lists.<String>newArrayList(),
            Lists.<String>newArrayList(),
            Lists.<List<String>>newArrayList(),
            Lists.<List<Token>>newArrayList(),
            null,
            Lists.<String>newArrayList());
    }

    public String getText() {
        return text;
    }

    public List<String> getNormalized() {
        return normalized;
    }

    public List<String> getPartOfSpeech() {
        return partOfSpeech;
    }

    public List<String> getLemma() {
        return lemma;
    }

    public List<String> getStem() {
        return stem;
    }

    public List<List<String>> getReadings() {
        return readings;
    }

    public List<List<Token>> getComponents() {
        return components;
    }

    public String getSource() {
        return source;
    }

    public List<String> getVariations() {
        return variations;
    }

    /**
     * Builder for tokens.
     */
    public static class Builder extends Attribute.Builder {
        private String text;
        private List<String> normalized;
        private List<String> partOfSpeech;
        private List<String> lemma;
        private List<String> stem;
        private List<List<String>> readings;
        private List<List<Token>> components;
        private String source;
        private List<String> variations;

        public Builder(int startOffset, int endOffset, String text) {
            super(startOffset, endOffset);
            this.text = text;
            normalized = Lists.newArrayList();
            partOfSpeech = Lists.newArrayList();
            lemma = Lists.newArrayList();
            stem = Lists.newArrayList();
            readings = Lists.newArrayList();
            components = Lists.newArrayList();
            variations = Lists.newArrayList();
        }

        public Builder(Token toCopy) {
            super(toCopy);
            text = toCopy.text;
            normalized.addAll(toCopy.normalized);
            partOfSpeech.addAll(toCopy.partOfSpeech);
            lemma.addAll(toCopy.lemma);
            stem.addAll(toCopy.stem);
            readings.addAll(toCopy.readings);
            components.addAll(toCopy.components);
            variations.addAll(toCopy.variations);
        }

        public void text(String text) {
            this.text = text;
        }

        public void addNormalized(String normalized) {
            this.normalized.add(normalized);
        }

        public void addPartOfSpeech(String partOfSpeech) {
            this.partOfSpeech.add(partOfSpeech);
        }

        public void addLemma(String lemma) {
            this.lemma.add(lemma);
        }

        public void addStem(String stem) {
            this.stem.add(stem);
        }

        public void addReadings(List<String> readings) {
            this.readings.add(readings);
        }

        public void addComponents(List<Token> components) {
            this.components.add(components);
        }

        public void source(String source) {
            this.source = source;
        }

        public void addVariation(String variation) {
            this.variations.add(variation);
        }

        public Token build() {
            return new Token(startOffset, endOffset, text, normalized, partOfSpeech, lemma, stem,
                readings, components, source,
                variations, extendedProperties);
        }
    }
}
