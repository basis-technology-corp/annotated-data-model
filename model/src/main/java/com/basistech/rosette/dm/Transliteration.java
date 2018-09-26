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
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class Transliteration extends BaseAttribute {

    private Map<ISO15924, String> scriptMap;

    protected Transliteration(Map<ISO15924, String> scriptMap,
                              Map<String, Object> extendedAttributes) {
        super(extendedAttributes);
        this.scriptMap = ImmutableMap.copyOf(scriptMap);
    }

    /**
     * Gets the transliterated text in the given script
     * @param script The script to look for.
     * @return The transliterated text, or {@code null} if no mapping was found.
     */
    public String get(ISO15924 script) {
        return scriptMap.get(script);
    }

    /**
     * Convenience method to create a transliteration with the given script and value.
     * @param script The script the transliteration is in
     * @param value The transliterated text.
     * @return A newly built immutable Transliteration containing the given transliterated text
     */
    public static Transliteration of(ISO15924 script, String value) {
        return Builder.of(script, value).build();
    }

    /**
     * Lists all the scripts this transliteration has text for.
     * @return An immutable set of all the scripts.
     */
    public Set<ISO15924> listScripts() {
        return ImmutableSet.copyOf(scriptMap.keySet());
    }

    /**
     * Gives all the mappings that this transliteration has.
     * @return An immutable map containing the transliterations
     */
    public Map<ISO15924, String> getAll() {
        return scriptMap;
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

        Transliteration that = (Transliteration) o;

        return scriptMap != null ? scriptMap.equals(that.scriptMap) : that.scriptMap == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (scriptMap != null ? scriptMap.hashCode() : 0);
        return result;
    }

    @Override
    public MoreObjects.ToStringHelper toStringHelper() {
        return MoreObjects.toStringHelper(this)
                .add("scriptMap", scriptMap);
    }

    public static final class Builder extends BaseAttribute.Builder<Transliteration, Transliteration.Builder> {
        private Map<ISO15924, String> scriptMap;

        /**
         * Creates a builder with the given transliteration text, and script pair.
         * @param script The script.
         * @param value The transliterated text.
         */
        public Builder(ISO15924 script, String value) {
            scriptMap = new HashMap<>();
            scriptMap.put(script, value);
        }

        /**
         * Creates a builder that starts out with the same transliterations as the given {@link Transliteration}.
         * @param other The transliteration to base off of.
         */
        public Builder(Transliteration other) {
            scriptMap = new HashMap<>(other.scriptMap);
        }

        /**
         * Creates a builder without any transliterated text.
         */
        public Builder() {
            scriptMap = new HashMap<>();
        }

        /**
         * Adds a transliterated text to this builder.
         * @param script The script of the transliteration.
         * @param value The transliterated text itself.
         * @return {@code this}.
         */
        public Builder add(ISO15924 script, String value) {
            scriptMap.put(script, value);
            return this;
        }

        /**
         * Adds all the transliterations within the given {@link Transliteration} to this builder.
         * @param transliteration The transliteration.
         * @return {@code this}.
         */
        public Builder add(Transliteration transliteration) {
            scriptMap.putAll(transliteration.scriptMap);
            return this;
        }

        /**
         * Clears all the transliterated texts within the current builder.
         * @return {@code this}.
         */
        public Builder clearTransliterations() {
            scriptMap.clear();
            return this;
        }

        /**
         * Sets the internal script to transliterated text mapping to a copy of the given map.
         * @param forLang The map to copy.
         * @return {@code this}.
         */
        public Builder transliterations(Map<ISO15924, String> forLang) {
            scriptMap = new HashMap<>(forLang);
            return this;
        }

        /**
         * Creates a new builder with a transliterated text under the given script.
         * @param script The script.
         * @param transliteration The transliterated text.
         * @return A builder that starts with the given mapping.
         */
        public static Builder of(ISO15924 script, String transliteration) {
            return new Builder(script, transliteration);
        }

        /**
         * Creates a builder whose contents are a copy of the given {@link Transliteration}.
         * @param transliteration The transliteration to copy.
         * @return A new builder that starts out the same as the given transliteration.
         */
        public static Builder of(Transliteration transliteration) {
            return new Builder(transliteration);
        }

        /**
         * Creates a new {@link Transliteration} from the current state of this builder.
         * @return The newly created {@link Transliteration}
         */
        public Transliteration build() {
            return new Transliteration(scriptMap, buildExtendedProperties());
        }

        @Override
        protected Builder getThis() {
            return this;
        }

    }
}
