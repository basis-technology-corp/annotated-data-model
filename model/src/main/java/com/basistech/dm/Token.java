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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * The token. The token is a word in the language.
 */
public class Token extends Attribute {
    // we don't want to have to go look at the parent {@link Text}.
    private final String text;
    private final List<String> normalized;
    @JsonDeserialize(using = MorphoAnalysisListDeserializer.class)
    private final List<MorphoAnalysis> analyses;
    private final String source;
    private final List<String> variations;

    public Token(int startOffset, int endOffset,
                 String text,
                 List<String> normalized,
                 String source,
                 List<MorphoAnalysis> analyses,
                 List<String> variations) {
        super(startOffset, endOffset);
        this.text = text;
        this.normalized = ImmutableList.copyOf(normalized);
        this.source = source;
        this.variations = ImmutableList.copyOf(variations);
        this.analyses = ImmutableList.copyOf(analyses);
    }

    protected Token() {
        text = "";
        normalized = ImmutableList.of();
        analyses = ImmutableList.of();
        source = "";
        variations = ImmutableList.of();
    }

    public Token(int startOffset, int endOffset, String text,
                 List<String> normalized,
                 String source,
                 List<String> variations,
                 List<MorphoAnalysis> analyses,
                 Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
        this.text = text;
        this.normalized = ImmutableList.copyOf(normalized);
        this.source = source;
        this.variations = ImmutableList.copyOf(variations);
        this.analyses = ImmutableList.copyOf(analyses);
    }

    public String getText() {
        return text;
    }

    public List<String> getNormalized() {
        return normalized;
    }

    /**
     * @return a list of analyses. Note: the items of this list are of the smallest type needed. So, even if the text
     * is Arabic or Chinese, some of the items in this list may be {@link com.basistech.dm.MorphoAnalysis}, <strong>not</strong>
     * the corresponding subclass. Callers must use instanceof to check if a particular item is of the subclass.
     */
    public List<MorphoAnalysis> getAnalyses() {
        return analyses;
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
        private List<MorphoAnalysis> analyses;

        private String source;
        private List<String> variations;

        public Builder(int startOffset, int endOffset, String text) {
            super(startOffset, endOffset);
            this.text = text;
            this.analyses = Lists.newArrayList();
            this.source = "";
            variations = Lists.newArrayList();
            normalized = Lists.newArrayList();
        }

        public Builder(Token toCopy) {
            super(toCopy);
            text = toCopy.text;
            normalized = toCopy.normalized;
            variations.addAll(toCopy.variations);
            analyses = toCopy.getAnalyses();
        }

        public void text(String text) {
            this.text = text;
        }

        public void addNormalized(String normalized) {
            this.normalized.add(normalized);
        }

        public void source(String source) {
            this.source = source;
        }

        public void addVariation(String variation) {
            this.variations.add(variation);
        }

        public void addAnalysis(MorphoAnalysis analysis) {
            this.analyses.add(analysis);
        }

        public Token build() {
            return new Token(startOffset, endOffset, text, normalized, source, variations, analyses, extendedProperties);
        }
    }
}
