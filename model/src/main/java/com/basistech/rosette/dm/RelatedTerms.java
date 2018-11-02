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

import com.basistech.util.LanguageCode;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A collection of terms with some semantic similarity to an {@link AnnotatedText}.
 * Lists of terms are grouped together by language.
 */
public class RelatedTerms extends BaseAttribute implements Serializable {
    private final Map<LanguageCode, List<RelatedTerm>> terms;

    protected RelatedTerms(Map<LanguageCode, List<RelatedTerm>> terms, Map<String, Object> extendedProperties) {
        super(extendedProperties);
        this.terms = mapOrNull(terms);
    }

    /**
     * Returns the related terms for the given language
     * @param language The language to look up
     * @return the list of related terms in that language
     */
    public List<RelatedTerm> get(LanguageCode language) {
        return terms.get(language);
    }

    /**
     * Returns the related terms.
     * @return a map from languages to related terms in that language
     */
    public Map<LanguageCode, List<RelatedTerm>> getTerms() {
        return terms;
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
        RelatedTerms that = (RelatedTerms) o;
        return Objects.equals(terms, that.terms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), terms);
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
            .add("terms", terms);
    }

    /**
     * Builder class for RelatedTerms
     */
    public static class Builder extends BaseAttribute.Builder<RelatedTerms, RelatedTerms.Builder> {
        private Map<LanguageCode, List<RelatedTerm>> terms;

        public Builder() {
            super();
            terms = new HashMap<>();
        }

        public Builder(RelatedTerms toCopy) {
            super();
            terms = Maps.newHashMap(toCopy.terms);
        }

        /**
         * Add a list of related terms
         * @param language The language of the terms
         * @param termList The list of terms
         * @return this
         */
        public Builder put(LanguageCode language, List<RelatedTerm> termList) {
            terms.put(language, termList);
            return this;
        }

        /**
         * Build the list of related terms
         * @return the build terms
         */
        public RelatedTerms build() {
            return new RelatedTerms(terms, buildExtendedProperties());
        }

        @Override
        public Builder getThis() {
            return this;
        }
    }
}
