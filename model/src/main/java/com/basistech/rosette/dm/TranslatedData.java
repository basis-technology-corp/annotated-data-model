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
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Map;

/**
 * A translation of the text.  "Translation" may include script conversion,
 * for example the original text may be Traditional Chinese, and the
 * translation may be Simplified Chinese.
 */
@EqualsAndHashCode(callSuper = true)
public class TranslatedData extends BaseAttribute implements Serializable {
    private static final long serialVersionUID = 250L;
    private final TextDomain sourceDomain;
    private final TextDomain targetDomain;
    private final String translation;
    private final Double confidence;


    /**
     * @deprecated sourceDomain should be specified
     */
    @Deprecated(forRemoval = true)
    protected TranslatedData(TextDomain targetDomain, String translation, Double confidence, Map<String, Object> extendedProperties) {
        this(null, targetDomain, translation, confidence, extendedProperties);
    }

    protected TranslatedData(TextDomain sourceDomain, TextDomain targetDomain, String translation, Double confidence, Map<String, Object> extendedProperties) {
        super(extendedProperties);
        this.sourceDomain = sourceDomain;
        this.targetDomain = targetDomain;
        this.translation = translation;
        this.confidence = confidence;
    }

    /**
     * Returns the sourceDomain for this object.
     *
     * @return the sourceDomain for this object
     */
    public TextDomain getSourceDomain() { return sourceDomain; }

    /**
     * Returns the domain for this object.
     *
     * @deprecated use targetDomain and getTargetDomain instead
     * @return the domain for this object
     */
    @Deprecated(forRemoval = true)
    public TextDomain getDomain() {
        return targetDomain;
    }

    /**
     * Returns the targetDomain for this object.
     *
     * @return the targetDomain for this object
     */
    public TextDomain getTargetDomain() { return targetDomain; }

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
    protected MoreObjects.ToStringHelper toStringHelper() {
        MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper(this)
                .add("sourceDomain", sourceDomain)
                .add("targetDomain", targetDomain)
                .add("translation", translation);
        if (confidence != null) {
            helper.add("confidence", confidence);
        }
        return helper;
    }

    /**
     * Builder class for TranslatedData.
     */
    public static class Builder extends BaseAttribute.Builder<TranslatedData, TranslatedData.Builder> {
        private final TextDomain sourceDomain;
        private final TextDomain targetDomain;
        private final String translation;
        private Double confidence;

        /**
         * Constructs a builder from the required properties
         *
         * @param targetDomain specifies the language and script of the translation
         * @param translation the translation for the text
         */
        public Builder(TextDomain targetDomain, String translation) {
            this.sourceDomain = null;
            this.targetDomain = targetDomain;
            this.translation = translation;
        }

        /**
         * Constructs a builder from the required properties
         *
         * @param sourceDomain specifies the language and script of the text
         * @param targetDomain specifies the language and script of the translation
         * @param translation the translation for the text
         */
        public Builder(TextDomain sourceDomain, TextDomain targetDomain, String translation) {
            this.sourceDomain = sourceDomain;
            this.targetDomain = targetDomain;
            this.translation = translation;
        }

        /**
         * Constructs a builder from an existing TranslatedData object
         *
         * @param toCopy the existing object
         */
        public Builder(TranslatedData toCopy) {
            super(toCopy);
            this.sourceDomain = toCopy.sourceDomain;
            this.targetDomain = toCopy.targetDomain;
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
            return new TranslatedData(sourceDomain, targetDomain, translation, confidence, buildExtendedProperties());
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }
}
