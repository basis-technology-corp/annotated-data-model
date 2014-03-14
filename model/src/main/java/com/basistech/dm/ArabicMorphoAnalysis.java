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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Arabic morphological analysis.
 */
public class ArabicMorphoAnalysis extends MorphoAnalysis {
    private final int prefixLength;
    private final int stemLength;
    private final String root;
    private final boolean definiteArticle;
    private final boolean strippablePrefix;

    private final List<String> prefixes;
    private final List<String> stems;
    private final List<String> suffixes;

    private final List<String> prefixTags;
    private final List<String> stemTags;
    private final List<String> suffixTags;

    public ArabicMorphoAnalysis(String partOfSpeech, String lemma, List<Token> components, String raw,
                                int prefixLength, int stemLength,
                                String root,
                                boolean definiteArticle,
                                boolean strippablePrefix,
                                List<String> prefixes, List<String> stems, List<String> suffixes,
                                List<String> prefixTags, List<String> stemTags,
                                List<String> suffixTags) {
        super(partOfSpeech, lemma, components, raw);
        this.prefixLength = prefixLength;
        this.stemLength = stemLength;
        this.root = root;
        this.prefixes = ImmutableList.copyOf(prefixes);
        this.stems = ImmutableList.copyOf(stems);
        this.suffixes = ImmutableList.copyOf(suffixes);
        this.prefixTags = ImmutableList.copyOf(prefixTags);
        this.stemTags = ImmutableList.copyOf(stemTags);
        this.suffixTags = ImmutableList.copyOf(suffixTags);
        this.definiteArticle = definiteArticle;
        this.strippablePrefix = strippablePrefix;
    }

    protected ArabicMorphoAnalysis() {
        this.prefixLength = 0;
        this.stemLength = 0;
        this.root = "";
        this.prefixes = ImmutableList.of();
        this.stems = ImmutableList.of();
        this.suffixes = ImmutableList.of();
        this.prefixTags = ImmutableList.of();
        this.stemTags = ImmutableList.of();
        this.suffixTags = ImmutableList.of();
        this.definiteArticle = false;
        this.strippablePrefix = false;
    }

    public int getPrefixLength() {
        return prefixLength;
    }

    public int getStemLength() {
        return stemLength;
    }

    public String getRoot() {
        return root;
    }

    public List<String> getPrefixes() {
        return prefixes;
    }

    public List<String> getStems() {
        return stems;
    }

    public List<String> getSuffixes() {
        return suffixes;
    }

    public List<String> getPrefixTags() {
        return prefixTags;
    }

    public List<String> getStemTags() {
        return stemTags;
    }

    public List<String> getSuffixTags() {
        return suffixTags;
    }

    public boolean isDefiniteArticle() {
        return definiteArticle;
    }

    public boolean isStrippablePrefix() {
        return strippablePrefix;
    }

    public static class Builder extends MorphoAnalysis.Builder {
        private int prefixLength;
        private int stemLength;
        private String root;
        private boolean definiteArticle;
        private boolean strippablePrefix;

        private List<String> prefixes;
        private List<String> stems;
        private List<String> suffixes;

        private List<String> prefixTags;
        private List<String> stemTags;
        private List<String> suffixTags;

        public Builder() {
            super();
            prefixes = Lists.newArrayList();
            stems = Lists.newArrayList();
            suffixes = Lists.newArrayList();
            prefixTags = Lists.newArrayList();
            stemTags = Lists.newArrayList();
            suffixes = Lists.newArrayList();
            suffixTags = Lists.newArrayList();
        }

        public Builder(ArabicMorphoAnalysis toCopy) {
            super(toCopy);
            prefixes = toCopy.prefixes;
            stems = toCopy.stems;
            suffixes = toCopy.suffixes;
            prefixTags = toCopy.prefixTags;
            stemTags = toCopy.stemTags;
            suffixTags = toCopy.suffixTags;
        }

        public void lengths(int prefixLength, int stemLength) {
            this.prefixLength = prefixLength;
            this.stemLength = stemLength;
        }

        public void root(String root) {
            this.root = root;
        }

        public void definiteArticle(boolean definiteArticle) {
            this.definiteArticle = definiteArticle;
        }

        public void strippablePrefix(boolean strippablePrefix) {
            this.strippablePrefix = strippablePrefix;
        }

        public void addPrefix(String prefix, String prefixTag) {
            prefixes.add(prefix);
            prefixTags.add(prefixTag);
        }

        public void addStem(String stem, String stemTag) {
            stems.add(stem);
            stemTags.add(stemTag);
        }

        public void addSuffix(String suffix, String suffixTag) {
            suffixes.add(suffix);
            suffixTags.add(suffixTag);
        }

        public ArabicMorphoAnalysis build() {
            return new ArabicMorphoAnalysis(partOfSpeech, lemma, components, raw, prefixLength, stemLength, root, definiteArticle, strippablePrefix, prefixes, stems, suffixes, prefixTags, stemTags, suffixTags);
        }
    }

}
