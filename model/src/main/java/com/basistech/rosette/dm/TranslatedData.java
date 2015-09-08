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

import com.basistech.util.TextDomain;
import com.google.common.base.Objects;

import java.util.Map;

/**
 * A translation of the text.  "Translation" may include script conversion,
 * for example the original text may be Traditional Chinese, and the
 * translation may be Simplified Chinese.
 */
public class TranslatedData extends BaseAttribute {
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
    protected Objects.ToStringHelper toStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper(this)
                .add("domain", domain)
                .add("translation", translation);
        if (confidence != null) {
            helper.add("confidence", confidence);
        }
        return helper;
    }

    /**
     * Builder class for TranslatedData.
     */
    public static class Builder extends BaseAttribute.Builder {
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
    }
}
