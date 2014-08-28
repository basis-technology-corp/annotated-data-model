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

/**
 * Arabic morphological analysis.  An Arabic token is analyzed into a prefix,
 * a stem, and a suffix, where any of these components could be empty.  This
 * class stores the prefix length and the stem length.  The suffix length can
 * be deduced from these and the length of the original token.
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

    ArabicMorphoAnalysis(String partOfSpeech,
                         String lemma,
                         List<Token> components,
                         String raw,
                         int prefixLength,
                         int stemLength,
                         String root,
                         boolean definiteArticle,
                         boolean strippablePrefix,
                         List<String> prefixes,
                         List<String> stems,
                         List<String> suffixes,
                         List<String> prefixTags,
                         List<String> stemTags,
                         List<String> suffixTags) {
        super(partOfSpeech, lemma, components, raw);
        this.prefixLength = prefixLength;
        this.stemLength = stemLength;
        this.root = root;
        if (prefixes == null) {
            this.prefixes = null;
        } else {
            this.prefixes = ImmutableList.copyOf(prefixes);
        }

        if (stems == null) {
            this.stems = null;
        } else {
            this.stems = ImmutableList.copyOf(stems);
        }

        if (suffixes == null) {
            this.suffixes = null;
        } else {
            this.suffixes = ImmutableList.copyOf(suffixes);
        }

        if (prefixTags == null) {
            this.prefixTags = null;
        } else {
            this.prefixTags = ImmutableList.copyOf(prefixTags);
        }

        if (stemTags == null) {
            this.stemTags = null;
        } else {
            this.stemTags = ImmutableList.copyOf(stemTags);
        }

        if (suffixTags == null) {
            this.suffixTags = null;
        } else {
            this.suffixTags = ImmutableList.copyOf(suffixTags);
        }

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

    /**
     * Returns the number of characters in the prefix.
     *
     * @return the number of characters in the prefix
     */
    public int getPrefixLength() {
        return prefixLength;
    }

    /**
     * Returns the number of characters in the stem.
     *
     * @return the number of characters in the stem.
     */
    public int getStemLength() {
        return stemLength;
    }

    /**
     * Returns the root, according to semitic linguistics.
     *
     * @return the root, according to semitic linguistics
     */
    public String getRoot() {
        return root;
    }

    /**
     * Returns the prefixes, if any.
     *
     * @return the prefixes, if any.
     */
    public List<String> getPrefixes() {
        return prefixes;
    }

    /**
     * Returns the stems.
     *
     * @return the stems
     */
    public List<String> getStems() {
        return stems;
    }

    /**
     * Returns the suffixes, if any.
     *
     * @return the suffixes, if any
     */
    public List<String> getSuffixes() {
        return suffixes;
    }

    /**
     * Returns the part of speech tags for prefixes.
     *
     * @return the part of speech tags for prefixes
     */
    public List<String> getPrefixTags() {
        return prefixTags;
    }

    /**
     * Returns the part of speech tags for stems.
     *
     * @return the part of speech tags for stems
     */
    public List<String> getStemTags() {
        return stemTags;
    }

    /**
     * Returns the part of speech tags for suffixes.
     *
     * @return the part of speech tags for suffixes
     */
    public List<String> getSuffixTags() {
        return suffixTags;
    }

    /**
     * Returns true if this word has an attached definite article.
     *
     * @return true if this word has an attached definite article
     */
    public boolean isDefiniteArticle() {
        return definiteArticle;
    }

    /**
     * Returns true if the prefixes of this word can be stripped (e.g. prepositions).
     *
     * @return true if the prefixes of this word can be stripped (e.g. prepositions)
     */
    public boolean isStrippablePrefix() {
        return strippablePrefix;
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

        ArabicMorphoAnalysis that = (ArabicMorphoAnalysis) o;

        if (definiteArticle != that.definiteArticle) {
            return false;
        }
        if (prefixLength != that.prefixLength) {
            return false;
        }
        if (stemLength != that.stemLength) {
            return false;
        }
        if (strippablePrefix != that.strippablePrefix) {
            return false;
        }
        if (prefixTags != null ? !prefixTags.equals(that.prefixTags) : that.prefixTags != null) {
            return false;
        }
        if (prefixes != null ? !prefixes.equals(that.prefixes) : that.prefixes != null) {
            return false;
        }
        if (root != null ? !root.equals(that.root) : that.root != null) {
            return false;
        }
        if (stemTags != null ? !stemTags.equals(that.stemTags) : that.stemTags != null) {
            return false;
        }
        if (stems != null ? !stems.equals(that.stems) : that.stems != null) {
            return false;
        }
        if (suffixTags != null ? !suffixTags.equals(that.suffixTags) : that.suffixTags != null) {
            return false;
        }
        if (suffixes != null ? !suffixes.equals(that.suffixes) : that.suffixes != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + prefixLength;
        result = 31 * result + stemLength;
        result = 31 * result + (root != null ? root.hashCode() : 0);
        result = 31 * result + (definiteArticle ? 1 : 0);
        result = 31 * result + (strippablePrefix ? 1 : 0);
        result = 31 * result + (prefixes != null ? prefixes.hashCode() : 0);
        result = 31 * result + (stems != null ? stems.hashCode() : 0);
        result = 31 * result + (suffixes != null ? suffixes.hashCode() : 0);
        result = 31 * result + (prefixTags != null ? prefixTags.hashCode() : 0);
        result = 31 * result + (stemTags != null ? stemTags.hashCode() : 0);
        result = 31 * result + (suffixTags != null ? suffixTags.hashCode() : 0);
        return result;
    }

    @Override
    protected Objects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("prefixLength", prefixLength)
                .add("stemLength", stemLength)
                .add("root", root)
                .add("definiteArticle", definiteArticle)
                .add("strippablePrefix", strippablePrefix)
                .add("prefixes", prefixes)
                .add("stems", stems)
                .add("suffixes", suffixes)
                .add("prefixTags", prefixTags)
                .add("stemTags", stemTags)
                .add("suffixTags", suffixTags);
    }

    /**
     * Builder class for {@link com.basistech.rosette.dm.ArabicMorphoAnalysis}.
     */
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

        /**
         * Constructs an empty builder.
         */
        public Builder() {
            super();
            prefixes = Lists.newArrayList();
            stems = Lists.newArrayList();
            suffixes = Lists.newArrayList();
            prefixTags = Lists.newArrayList();
            stemTags = Lists.newArrayList();
            suffixes = Lists.newArrayList();
            suffixTags = Lists.newArrayList();
            root = null;
        }

        /**
         * Constructs a builder from an existing analysis.
         *
         * @param toCopy the analysis to copy.
         * @adm.ignore
         */
        public Builder(ArabicMorphoAnalysis toCopy) {
            super(toCopy);
            addAllToList(prefixes, toCopy.prefixes);
            addAllToList(stems, toCopy.stems);
            addAllToList(suffixes, toCopy.suffixes);
            addAllToList(prefixTags, toCopy.prefixTags);
            addAllToList(stemTags, toCopy.stemTags);
            addAllToList(suffixTags, toCopy.suffixTags);
        }

        /**
         * Sets the decomposition lengths.  The suffix length is implied by the
         * other two.
         *
         * @param prefixLength the number of characters in the prefix
         * @param stemLength the number of characters in the stem
         */
        public Builder lengths(int prefixLength, int stemLength) {
            this.prefixLength = prefixLength;
            this.stemLength = stemLength;
            return this;
        }

        /**
         * Sets the root for the word.
         *
         * @param root the root, according to semitic linguistics
         */
        public Builder root(String root) {
            this.root = root;
            return this;
        }

        /**
         * Specifies whether the word has an attached definite article.
         *
         * @param definiteArticle true for a definite article
         */
        public Builder definiteArticle(boolean definiteArticle) {
            this.definiteArticle = definiteArticle;
            return this;
        }

        /**
         * Specifies whether the prefixes are strippable (e.g. prepositions).
         *
         * @param strippablePrefix true if strippable
         */
        public Builder strippablePrefix(boolean strippablePrefix) {
            this.strippablePrefix = strippablePrefix;
            return this;
        }

        /**
         * Adds a prefix.
         *
         * @param prefix the prefix
         * @param prefixTag the part of speech for the prefix
         */
        public Builder addPrefix(String prefix, String prefixTag) {
            prefixes.add(prefix);
            prefixTags.add(prefixTag);
            return this;
        }

        /**
         * Adds a stem.
         *
         * @param stem the stem
         * @param stemTag the part of speech for the stem
         */
        public Builder addStem(String stem, String stemTag) {
            stems.add(stem);
            stemTags.add(stemTag);
            return this;
        }

        /**
         * Adds a suffix.
         *
         * @param suffix the suffix
         * @param suffixTag the part of speech for the suffix
         */
        public Builder addSuffix(String suffix, String suffixTag) {
            suffixes.add(suffix);
            suffixTags.add(suffixTag);
            return this;
        }

        /**
         * Constructs the analysis from the current state of the builder.
         *
         * @return the new analysis object
         */
        public ArabicMorphoAnalysis build() {
            return new ArabicMorphoAnalysis(partOfSpeech, lemma, listOrNull(components),
                    raw, prefixLength, stemLength, root, definiteArticle,
                    strippablePrefix, listOrNull(prefixes),
                    listOrNull(stems),
                    listOrNull(suffixes),
                    listOrNull(prefixTags),
                    listOrNull(stemTags),
                    listOrNull(suffixTags));
        }
    }
}
