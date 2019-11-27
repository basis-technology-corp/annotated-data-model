/*
* Copyright 2019 Basis Technology Corp.
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
 * Morphological analysis objects for Chinese and Japanese.
 * Chinese and Japanese add the concept of readings
 * to {@link com.basistech.rosette.dm.MorphoAnalysis}.  Readings provide
 * the pronunciation of ideographic strings.  They are typically written in
 * Pinyin for Chinese and Hiragana for Japanese.  A single surface form may have
 * multiple possible readings.
 */
public class HanMorphoAnalysis extends MorphoAnalysis implements Serializable {
    private static final long serialVersionUID = 250L;
    private final List<String> readings;

    protected HanMorphoAnalysis(String partOfSpeech,
                                String lemma,
                                List<Token> components,
                                String raw,
                                List<String> readings,
                                TagSet tagSet,
                                Map<String, Object> extendedProperties) {
        super(partOfSpeech, lemma, components, raw, tagSet, extendedProperties);
        this.readings = listOrNull(readings);
    }

    protected HanMorphoAnalysis(String partOfSpeech,
                                String lemma,
                                List<Token> components,
                                String raw,
                                List<String> readings,
                                Map<String, Object> extendedProperties) {
        this(partOfSpeech, lemma, components, raw, readings, null, extendedProperties);
    }

    /**
     * Returns the readings.
     *
     * @return the readings
     */
    public List<String> getReadings() {
        return readings;
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

        HanMorphoAnalysis that = (HanMorphoAnalysis) o;

        if (readings == null) {
            return that.readings == null;
        }

        return readings.equals(that.readings);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        if (readings != null) {
            result = 31 * result + readings.hashCode();
        }
        return result;
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("readings", readings);
    }

    /**
     * A builder for {@link com.basistech.rosette.dm.HanMorphoAnalysis}.
     */
    public static class Builder extends MorphoAnalysis.Builder<HanMorphoAnalysis, HanMorphoAnalysis.Builder> {
        private List<String> readings;

        /**
         * Constructs a builder with default values.
         */
        public Builder() {
            super();
            readings = Lists.newArrayList();
        }

        /**
         * Constructs a builder initialized from an existing analysis.
         *
         * @param toCopy the analysis to copy
         * @adm.ignore
         */
        public Builder(HanMorphoAnalysis toCopy) {
            super(toCopy);
            readings = Lists.newArrayList();
            addAllToList(readings, toCopy.getReadings());
        }

        /**
         * Adds a reading.
         *
         * @param reading the reading
         * @return this
         */
        public Builder addReading(String reading) {
            readings.add(reading);
            return this;
        }

        /**
         * Set all the readings for this analysis.
         * @param readings the readings.
         * @return this
         */
        public Builder readings(List<String> readings) {
            this.readings = nullOrList(readings);
            return this;
        }

        /**
         * Builds an immutable analysis object from the current state of this builder.
         *
         * @return the analysis
         */
        public HanMorphoAnalysis build() {
            return new HanMorphoAnalysis(partOfSpeech, lemma, components, raw, readings, tagSet, buildExtendedProperties());
        }
    }
}
