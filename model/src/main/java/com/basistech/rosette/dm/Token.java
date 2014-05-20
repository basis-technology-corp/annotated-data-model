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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * The token. The token is a word in the language.
 */
public class Token extends Attribute {
    // we don't want to have to go look at the parent {@link AnnotatedText}.
    private final String text;
    private final List<String> normalized;
    private final List<MorphoAnalysis> analyses;
    private final String source;
    private final List<String> variations;

    Token(int startOffset,
          int endOffset,
          String text,
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

    protected Token() {
        text = "";
        normalized = ImmutableList.of();
        analyses = ImmutableList.of();
        source = "";
        variations = ImmutableList.of();
    }

    // Do not include extendedProperties in creator; leave that for the special annotations
    // in the base lass.
    Token(int startOffset,
          int endOffset,
          String text,
          List<String> normalized,
          String source,
          List<String> variations,
          List<MorphoAnalysis> analyses) {
        super(startOffset, endOffset);
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
     * is Arabic or Chinese, some of the items in this list may be {@link com.basistech.rosette.dm.MorphoAnalysis}, <strong>not</strong>
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

        Token token = (Token) o;

        if (!analyses.equals(token.analyses)) {
            return false;
        }
        if (!normalized.equals(token.normalized)) {
            return false;
        }
        if (!source.equals(token.source)) {
            return false;
        }
        if (!text.equals(token.text)) {
            return false;
        }
        if (!variations.equals(token.variations)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + text.hashCode();
        result = 31 * result + normalized.hashCode();
        result = 31 * result + analyses.hashCode();
        result = 31 * result + source.hashCode();
        result = 31 * result + variations.hashCode();
        return result;
    }

    @Override
    protected Objects.ToStringHelper toStringHelper() {
        return Objects.toStringHelper(this)
                .add("text", text)
                .add("normalized", normalized)
                .add("analyses", analyses)
                .add("source", source)
                .add("variations", variations);
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
