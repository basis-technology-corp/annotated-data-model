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
    private final String normalized;
    private final MorphoAnalysisList analyses;
    private final String source;
    private final List<String> variations;

    public Token(int startOffset, int endOffset,
                 String text,
                 String normalized,
                 String source,
                 List<MorphoAnalysis> analyses,
                 List<String> variations) {
        super(startOffset, endOffset);
        this.text = text;
        this.normalized = normalized;
        this.source = source;
        this.variations = variations;
        Class<? extends MorphoAnalysis> analysisClass;
        if (analyses == null || analyses.size() == 0) {
            analysisClass = MorphoAnalysis.class;
            this.analyses = new MorphoAnalysisList(analysisClass, Lists.<MorphoAnalysis>newArrayList());
        } else {
            analysisClass = analyses.get(0).getClass();
            this.analyses = new MorphoAnalysisList(analysisClass, analyses);
        }

    }

    public Token(int startOffset, int endOffset, String text,
                 String normalized,
                 String source,
                 List<String> variations,
                 List<MorphoAnalysis> analyses,
                 Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
        this.text = text;
        this.normalized = normalized;
        this.variations = variations;
        this.source = source;
        Class<? extends MorphoAnalysis> analysisClass;
        if (analyses == null || analyses.size() == 0) {
            analysisClass = MorphoAnalysis.class;
            this.analyses = new MorphoAnalysisList(analysisClass, Lists.<MorphoAnalysis>newArrayList());
        } else {
            analysisClass = analyses.get(0).getClass();
            this.analyses = new MorphoAnalysisList(analysisClass, analyses);
        }

    }

    public String getText() {
        return text;
    }

    public String getNormalized() {
        return normalized;
    }

    public List<MorphoAnalysis> getAnalyses() {
        return analyses.getItems();
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
        private String normalized;
        private List<MorphoAnalysis> analyses;

        private String source;
        private List<String> variations;

        public Builder(int startOffset, int endOffset, String text) {
            super(startOffset, endOffset);
            this.text = text;
            this.analyses = Lists.newArrayList();
            variations = Lists.newArrayList();
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

        public void normalized(String normalized) {
            this.normalized = normalized;
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
