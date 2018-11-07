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

import com.basistech.util.TextDomain;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * A list of translations for the tokens. Each translation matches the corresponding token.
 */
public class TranslatedTokens extends BaseAttribute implements Serializable {
    private static final long serialVersionUID = 250L;
    private final TextDomain domain;
    private final List<String> translations; // 1-1 with tokens

    protected TranslatedTokens(TextDomain domain, List<String> translations, Map<String, Object> extendedProperties) {
        super(extendedProperties);
        this.domain = domain;
        this.translations = listOrNull(translations);
    }

    /**
     * Returns the domain for this object.
     *
     * @return the domain for this object
     */
    public TextDomain getDomain() {
        return domain;
    }

    /**
     * Returns the translations for this object.
     *
     * @return the translations for this object
     */
    public List<String> getTranslations() {
        return translations;
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

        TranslatedTokens that = (TranslatedTokens) o;

        if (domain != null ? !domain.equals(that.domain) : that.domain != null) {
            return false;
        }
        return !(translations != null ? !translations.equals(that.translations) : that.translations != null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (domain != null ? domain.hashCode() : 0);
        result = 31 * result + (translations != null ? translations.hashCode() : 0);
        return result;
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return MoreObjects.toStringHelper(this)
            .add("domain", domain)
            .add("translations", translations);
    }

    /**
     * Builder class for TranslatedTokens.
     */
    public static class Builder extends BaseAttribute.Builder<TranslatedTokens, TranslatedTokens.Builder> {
        private TextDomain domain;
        private List<String> translations;

        /**
         * Constructs a builder from the required properties
         *
         * @param domain specifies the language and script of the translation
         */
        public Builder(TextDomain domain) {
            super();
            this.domain = domain;
            this.translations = Lists.newArrayList();
        }

        /**
         * Constructs a builder from an existing TranslatedTokens object
         *
         * @param toCopy the existing object
         */
        public Builder(TranslatedTokens toCopy) {
            super(toCopy);
            this.domain = toCopy.domain;
            translations = Lists.newArrayList();
            addAllToList(translations, toCopy.getTranslations());
        }

        /**
         * Adds the translation of one token to the list of translations.
         *
         * @param translatedToken the translation for one token
         * @return this
         */
        public Builder addTranslatedToken(String translatedToken) {
            this.translations.add(translatedToken);
            return this;
        }

        /**
         * Set all of the translations for this token.
         * @param translations the translations.
         * @return this.
         */
        public Builder translatedTokens(List<String> translations) {
            this.translations = nullOrList(translations);
            return this;
        }

        /**
         * Builds a new TranslatedTokens object from the current state of the builder.
         *
         * @return a new TranslatedTokens object.
         */
        public TranslatedTokens build() {
            return new TranslatedTokens(domain, translations, buildExtendedProperties());
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }
}
