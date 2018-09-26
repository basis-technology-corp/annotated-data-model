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
 * Morphological analysis objects for Korean.
 * Korean morphology decomposes each word into a collection of tagged morphemes.
 */
public class KoreanMorphoAnalysis extends MorphoAnalysis implements Serializable {
    private static final long serialVersionUID = 222L;
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
        return !(morphemes != null ? !morphemes.equals(that.morphemes) : that.morphemes != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (morphemes != null ? morphemes.hashCode() : 0);
        result = 31 * result + (morphemeTags != null ? morphemeTags.hashCode() : 0);
        return result;
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("morphemes", morphemes)
                .add("morphemeTags", morphemeTags);
    }

    /**
     * A builder for {@link com.basistech.rosette.dm.KoreanMorphoAnalysis}.
     */
    public static class Builder extends MorphoAnalysis.Builder<KoreanMorphoAnalysis, KoreanMorphoAnalysis.Builder> {
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
            return new KoreanMorphoAnalysis(partOfSpeech, lemma, components, raw, morphemes, morphemeTags,
                    buildExtendedProperties());
        }
    }
}
