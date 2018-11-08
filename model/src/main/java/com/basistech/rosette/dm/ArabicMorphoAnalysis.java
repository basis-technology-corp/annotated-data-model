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
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Arabic morphological analysis.  An Arabic token is analyzed into a prefix,
 * a stem, and a suffix, where any of these components could be empty.  This
 * class stores the prefix length and the stem length.  The suffix length can
 * be deduced from these and the length of the original token.
 * <br>
 * The component parts themselves can be subdivided into sub-components.  Each
 * sub-component has an associated tag.  For example, one of the possible
 * analyses for "wAlktb" (Buckwalter transliteration for "and the books") looks
 * like:
 *
 * <pre>
 * prefix:         wAl
 * stem:           ktb
 * suffix:
 * part-of-speech: NOUN
 * prefix:         [w, Al]
 * prefixTags:     [CONJ, DET]
 * stems:          [ktb]
 * stemTags:       [NOUN]
 * suffixes:       []
 * suffixTags:     [NO_FUNC]
 * </pre>
 */
public class ArabicMorphoAnalysis extends MorphoAnalysis implements Serializable {
    private static final long serialVersionUID = 250L;
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

    protected ArabicMorphoAnalysis(String partOfSpeech,
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
                         List<String> suffixTags,
                         Map<String, Object> extendedProperties) {
        super(partOfSpeech, lemma, components, raw, extendedProperties);
        this.prefixLength = prefixLength;
        this.stemLength = stemLength;
        this.root = root;
        this.prefixes = listOrNull(prefixes);
        this.stems = listOrNull(stems);
        this.suffixes = listOrNull(suffixes);
        this.prefixTags = listOrNull(prefixTags);
        this.stemTags = listOrNull(stemTags);
        this.suffixTags = listOrNull(suffixTags);
        this.definiteArticle = definiteArticle;
        this.strippablePrefix = strippablePrefix;
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
     * Returns the components of the prefix, if any.
     *
     * @return the components of the prefix, if any
     */
    public List<String> getPrefixes() {
        return prefixes;
    }

    /**
     * Returns the components of the stem.
     *
     * @return the components of the stem
     */
    public List<String> getStems() {
        return stems;
    }

    /**
     * Returns the components of the suffix, if any.
     *
     * @return the components of the suffix, if any
     */
    public List<String> getSuffixes() {
        return suffixes;
    }

    /**
     * Returns the part-of-speech tags for the prefix components.
     *
     * @return the part-of-speech tags for prefix components
     */
    public List<String> getPrefixTags() {
        return prefixTags;
    }

    /**
     * Returns the part-of-speech tags for stem components.
     *
     * @return the part-of-speech tags for stem components
     */
    public List<String> getStemTags() {
        return stemTags;
    }

    /**
     * Returns the part-of-speech tags for suffix components.
     *
     * @return the part-of-speech tags for suffix components.
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
        return !(suffixes != null ? !suffixes.equals(that.suffixes) : that.suffixes != null);

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
    protected MoreObjects.ToStringHelper toStringHelper() {
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
     * Factory method for {@link Builder} instances.
     *
     * @return the new builder
     * @see Builder#Builder()
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Factory method for {@link Builder} instances.
     *
     * @param toCopy the morpho analysis to copy
     * @return the new builder
     * @see Builder#Builder(ArabicMorphoAnalysis)
     */
    public static Builder builder(ArabicMorphoAnalysis toCopy) {
        return new Builder(toCopy);
    }

    /**
     * Builder class for {@link com.basistech.rosette.dm.ArabicMorphoAnalysis}.
     */
    public static class Builder extends MorphoAnalysis.Builder<ArabicMorphoAnalysis, ArabicMorphoAnalysis.Builder> {
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
            prefixes = Lists.newArrayList();
            stems = Lists.newArrayList();
            suffixes = Lists.newArrayList();
            prefixTags = Lists.newArrayList();
            stemTags = Lists.newArrayList();
            suffixTags = Lists.newArrayList();
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
         * @return this
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
         * @return this
         */
        public Builder root(String root) {
            this.root = root;
            return this;
        }

        /**
         * Specifies whether the word has an attached definite article.
         *
         * @param definiteArticle true for a definite article
         * @return this
         */
        public Builder definiteArticle(boolean definiteArticle) {
            this.definiteArticle = definiteArticle;
            return this;
        }

        /**
         * Specifies whether the prefixes are strippable (e.g. prepositions).
         *
         * @param strippablePrefix true if strippable
         * @return this
         */
        public Builder strippablePrefix(boolean strippablePrefix) {
            this.strippablePrefix = strippablePrefix;
            return this;
        }

        /**
         * Adds a prefix.
         *
         * @param prefix the prefix
         * @param prefixTag the part-of-speech for the prefix
         * @return this
         */
        public Builder addPrefix(String prefix, String prefixTag) {
            prefixes.add(prefix);
            prefixTags.add(prefixTag);
            return this;
        }

        /**
         * Set all the prefixes and their tags. The two lists must be the same length.
         * @param prefixes  the prefixes.
         * @param prefixTags the tags for the prefixes.
         * @return this
         */
        public Builder prefixes(List<String> prefixes, List<String> prefixTags) {
            this.prefixes = nullOrList(prefixes);
            this.prefixTags = nullOrList(prefixTags);
            return this;
        }

        /**
         * Adds a stem.
         *
         * @param stem the stem
         * @param stemTag the part-of-speech for the stem
         * @return this
         */
        public Builder addStem(String stem, String stemTag) {
            stems.add(stem);
            stemTags.add(stemTag);
            return this;
        }

        /**
         * Set all the stems and their tags. The two lists must be the same length.
         * @param stems  the prefixes.
         * @param stemTags the tags for the prefixes.
         * @return this
         */
        public Builder stems(List<String> stems, List<String> stemTags) {
            this.stems = nullOrList(stems);
            this.stemTags = nullOrList(stemTags);
            return this;
        }

        /**
         * Adds a suffix.
         *
         * @param suffix the suffix
         * @param suffixTag the part-of-speech for the suffix
         * @return this
         */
        public Builder addSuffix(String suffix, String suffixTag) {
            suffixes.add(suffix);
            suffixTags.add(suffixTag);
            return this;
        }

        /**
         * Set all the suffixes and their tags. The two lists must be the same length.
         * @param suffixes  the prefixes.
         * @param suffixTags the tags for the prefixes.
         * @return this
         */
        public Builder suffixes(List<String> suffixes, List<String> suffixTags) {
            this.suffixes = nullOrList(suffixes);
            this.suffixTags = nullOrList(suffixTags);
            return this;
        }

        /**
         * Constructs the analysis from the current state of the builder.
         *
         * @return the new analysis object
         */
        public ArabicMorphoAnalysis build() {
            return new ArabicMorphoAnalysis(partOfSpeech, lemma, components,
                    raw, prefixLength, stemLength, root, definiteArticle,
                    strippablePrefix, prefixes,
                    stems,
                    suffixes,
                    prefixTags,
                    stemTags,
                    suffixTags,
                    buildExtendedProperties());
        }
    }
}
