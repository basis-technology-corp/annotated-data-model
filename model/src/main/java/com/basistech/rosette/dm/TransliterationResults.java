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

import com.basistech.util.ISO15924;
import com.basistech.util.LanguageCode;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableMap;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
public final class TransliterationResults extends BaseAttribute {

    private final Map<LanguageCode, Transliteration> results;

    protected TransliterationResults(Map<LanguageCode, Transliteration> results,
                                     Map<String, Object> extendedAttributes) {

        super(extendedAttributes);
        ImmutableMap.Builder<LanguageCode, Transliteration> mapBuilder = ImmutableMap.builder();
        for (LanguageCode lc : results.keySet()) {
            mapBuilder.put(lc, results.get(lc));
        }
        this.results = mapBuilder.build();
    }

    /**
     * Gets all the {@link Transliteration}s inside {@code this}
     * @return An immutable view of the transliterations.
     */
    public Map<LanguageCode, Transliteration> getResults() {
        // This is immutable
        return results;
    }

    /**
     * Gets the {@link Transliteration} for the given {@link LanguageCode}.
     * @param code The code to check
     * @return The transliteration, or {@code null} if there isn't one with the given language code.
     */
    public Transliteration getTransliteration(LanguageCode code) {
        return getResults().get(code);
    }

    /**
     * Gets the actual transliteration for the given {@link LanguageCode} in the given {@code script}
     * @param code The language code to check
     * @param script The script to check
     * @return The transliteration, or null if one doesn't exist matching the given criteria.
     */
    public String getTransliterationInScript(LanguageCode code, ISO15924 script) {
        Transliteration t = getTransliteration(code);
        if (t == null) {
            return null;
        } else {
            return t.get(script);
        }
    }

    @Override
    public MoreObjects.ToStringHelper toStringHelper() {
        return MoreObjects.toStringHelper(this)
                .add("results", results);
    }

    /**
     * Builder for immutable {@link TransliterationResults}
     */
    public static class Builder extends BaseAttribute.Builder<TransliterationResults, TransliterationResults.Builder> {

        private Map<LanguageCode, Transliteration.Builder> results;

        /**
         * Creates a new builder with a {@link Transliteration} containing the given transliteration under the
         * language {@code languageCode}
         * @param languageCode The language code to put the new transliteration under.
         * @param script The script for the new transliteration.
         * @param transliteration The text for the new transliteration.
         */
        public Builder(LanguageCode languageCode, ISO15924 script, String transliteration) {
            this.results = new HashMap<>();
            results.put(languageCode, Transliteration.Builder.of(script, transliteration));

        }

        /**
         * Creates a new empty builder (one with no {@link Transliteration}s)
         */
        public Builder() {
            results = new HashMap<>();
        }

        /**
         * Creates a new builder whose state starts out with a copy of the given {@link TransliterationResults}
         * @param other The transliteration to start from.
         */
        public Builder(TransliterationResults other) {
            results = copy(other.results);
        }

        /**
         * Creates a copy of the given map, all {@link Transliteration}s are converted to
         * {@link Transliteration.Builder}s so that they can be modified during the use of this builder.
         * @param otherResults The map to copy
         * @return The copied map
         */
        private Map<LanguageCode, Transliteration.Builder> copy(Map<LanguageCode, Transliteration> otherResults) {
            Map<LanguageCode, Transliteration.Builder> output = new HashMap<>(otherResults.size());
            for (LanguageCode lang : otherResults.keySet()) {
                output.put(lang, Transliteration.Builder.of(otherResults.get(lang)));
            }
            return output;
        }

        /**
         * Adds the given {@link Transliteration} under the given {@link LanguageCode}
         * @param languageCode The language code to add under
         * @param transliteration The transliteration to add
         * @return {@code this}
         */
        public Builder addTransliteration(LanguageCode languageCode, Transliteration transliteration) {
            if (results.containsKey(languageCode)) {
                results.get(languageCode).add(transliteration);
            } else {
                results.put(languageCode, Transliteration.Builder.of(transliteration));
            }
            return this;
        }

        /**
         * Sets this builder's internal transliterations mapping to a copy of the given one.
         * @param transliterations The new transliterations
         * @return {@code this}
         */
        public Builder transliteration(Map<LanguageCode, Transliteration> transliterations) {
            this.results = copy(transliterations);
            return this;
        }

        /**
         * Clears the current transliterations for all languages
         * @return this
         */
        public Builder clearResults() {
            results.clear();
            return this;
        }

        /**
         * Creates a new immutable {@link TransliterationResults} from the internal state of this builder.
         * @return The new {@link TransliterationResults}
         */
        public TransliterationResults build() {
            Map<LanguageCode, Transliteration> finalResults = new HashMap<>();
            for (LanguageCode l : results.keySet()) {
                finalResults.put(l, results.get(l).build());
            }
            return new TransliterationResults(finalResults, buildExtendedProperties());
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }

}
