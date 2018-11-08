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

import java.io.Serializable;
import java.util.Map;

/**
 * A translation of the text.  "Translation" may include script conversion,
 * for example the original text may be Traditional Chinese, and the
 * translation may be Simplified Chinese.
 */
public class TranslatedData extends BaseAttribute implements Serializable {
    private static final long serialVersionUID = 250L;
    private final TextDomain domain;
    private final String translation;
    private final Double confidence;

    protected TranslatedData(TextDomain domain, String translation, Double confidence, Map<String, Object> extendedProperties) {
        super(extendedProperties);
        this.domain = domain;
        this.translation = translation;
        this.confidence = confidence;
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
     * Returns the translation for this object.
     *
     * @return the translation for this object
     */
    public String getTranslation() {
        return translation;
    }

    /**
     * Returns the confidence value for this translation, nor null if there is none.
     *
     * @return the confidence value for this translation, or null if there is none
     */
    public Double getConfidence() {
        return confidence;
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

        TranslatedData that = (TranslatedData) o;

        if (confidence != null ? !confidence.equals(that.confidence) : that.confidence != null) {
            return false;
        }
        if (!domain.equals(that.domain)) {
            return false;
        }
        return translation.equals(that.translation);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + domain.hashCode();
        result = 31 * result + translation.hashCode();
        result = 31 * result + (confidence != null ? confidence.hashCode() : 0);
        return result;
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper(this)
                .add("domain", domain)
                .add("translation", translation);
        if (confidence != null) {
            helper.add("confidence", confidence);
        }
        return helper;
    }

    /**
     * Factory method for {@link Builder} instances.
     *
     * @param domain specifies the language and script of the translation
     * @param translation the translation for the text
     * @return the new builder
     * @see Builder#Builder(TextDomain, String)
     */
    public static Builder builder(TextDomain domain, String translation) {
        return new Builder(domain, translation);
    }

    /**
     * Factory method for {@link Builder} instances.
     *
     * @param toCopy the translated data to copy
     * @return the new builder
     * @see Builder#Builder(TranslatedData)
     */
    public static Builder builder(TranslatedData toCopy) {
        return new Builder(toCopy);
    }

    /**
     * Builder class for TranslatedData.
     */
    public static class Builder extends BaseAttribute.Builder<TranslatedData, TranslatedData.Builder> {
        private TextDomain domain;
        private String translation;
        private Double confidence;

        /**
         * Constructs a builder from the required properties
         *
         * @param domain specifies the language and script of the translation
         * @param translation the translation for the text
         */
        public Builder(TextDomain domain, String translation) {
            this.domain = domain;
            this.translation = translation;
        }

        /**
         * Constructs a builder from an existing TranslatedData object
         *
         * @param toCopy the existing object
         */
        public Builder(TranslatedData toCopy) {
            super(toCopy);
            this.domain = toCopy.domain;
            this.translation = toCopy.getTranslation();
        }


        /**
         * Specify the confidence value for this translation.
         * @param confidence the confidence, or null to mean that there is associated confidence value.
         * @return this.
         */
        public Builder confidence(Double confidence) {
            this.confidence = confidence;
            return this;
        }

        /**
         * Builds a new TranslatedData object from the current state of the builder.
         *
         * @return a new TranslatedData object.
         */
        public TranslatedData build() {
            return new TranslatedData(domain, translation, confidence, buildExtendedProperties());
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }
}
