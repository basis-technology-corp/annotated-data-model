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
 * Morphological analysis objects for Korean.
 * Korean morphology decomposes each word into a collection of tagged morphemes.
 */
public class KoreanMorphoAnalysis extends MorphoAnalysis {
    private final List<String> morphemes;
    private final List<String> morphemeTags;

    protected KoreanMorphoAnalysis(String partOfSpeech,
                                   String lemma,
                                   List<Token> components,
                                   String raw,
                                   List<String> morphemes,
                                   List<String> morphemeTags,
                                   Map<String, Object> extendedProperties) {
        super(partOfSpeech, lemma, components, raw, extendedProperties);
        this.morphemes = listOrNull(morphemes);
        this.morphemeTags = listOrNull(morphemeTags);
    }

    /**
     * @return Morphemes
     */
    public List<String> getMorphemes() {
        return morphemes;
    }

    /**
     * @return Morpheme tags.
     */
    public List<String> getMorphemeTags() {
        return morphemeTags;
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

        KoreanMorphoAnalysis that = (KoreanMorphoAnalysis) o;

        if (morphemeTags != null ? !morphemeTags.equals(that.morphemeTags) : that.morphemeTags != null) {
            return false;
        }
        if (morphemes != null ? !morphemes.equals(that.morphemes) : that.morphemes != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (morphemes != null ? morphemes.hashCode() : 0);
        result = 31 * result + (morphemeTags != null ? morphemeTags.hashCode() : 0);
        return result;
    }

    @Override
    protected Objects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("morphemes", morphemes)
                .add("morphemeTags", morphemeTags);
    }

    /**
     * A builder for {@link com.basistech.rosette.dm.KoreanMorphoAnalysis}.
     */
    public static class Builder extends MorphoAnalysis.Builder {
        private List<String> morphemes;
        private List<String> morphemeTags;

        /**
         * Constructs a builder with default values.
         */
        public Builder() {
            super();
            morphemes = Lists.newArrayList();
            morphemeTags = Lists.newArrayList();
        }

        /**
         * Constructs a builder initialized from an existing analysis.
         *
         * @param toCopy the analysis to copy
         * @adm.ignore
         */
        public Builder(KoreanMorphoAnalysis toCopy) {
            super(toCopy);
            morphemes = Lists.newArrayList();
            morphemeTags = Lists.newArrayList();
            addAllToList(morphemes, toCopy.getMorphemes());
            addAllToList(morphemeTags, toCopy.getMorphemeTags());
        }

        /**
         * add a morpheme/tag pair.
         * @param morpheme the morpheme.
         * @param tag its tag.
         * @return this.
         */
        public Builder addMorpheme(String morpheme, String tag) {
            morphemes.add(morpheme);
            morphemeTags.add(tag);
            return this;
        }

        /**
         * Sets the morphemes and their tags. The lists must be the same length.
         * @param morphemes the morphemes.
         * @param morphemeTags their tags.
         * @return this.
         */
        public Builder morphemes(List<String> morphemes, List<String> morphemeTags) {
            this.morphemes = nullOrList(morphemes);
            this.morphemeTags = nullOrList(morphemeTags);
            return this;
        }

        /**
         * Builds an immutable analysis object from the current state of this builder.
         *
         * @return the analysis
         */
        public KoreanMorphoAnalysis build() {
            return new KoreanMorphoAnalysis(partOfSpeech, lemma, components, raw, morphemes, morphemeTags, extendedProperties);
        }
    }
}
