/*
* Copyright 2017 Basis Technology Corp.
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

import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * A reference to a "keyphrase" of a document. Each keyphrase is a specific
 * span of text that is highly relevant to the document.
 * Each {@linkplain Keyphrase} provides:
 * <ul>
 *     <li>the name of the specific keyphrase</li>
 *     <li>the confidence associated with the keyphrase</li>
 *     <li>a list of {@linkplain Extent}s marking each location of the keyphrase in the text</li>
 * </ul>
 */
public class Keyphrase extends BaseAttribute implements Serializable {
    private static final long serialVersionUID = 222L;
    private final String concept;
    private final Double confidence;
    private final List<Extent> extents;

    protected Keyphrase(String concept, Double confidence, List<Extent> extents, Map<String, Object> extendedProperties) {
        super(extendedProperties);
        this.concept = concept;
        this.confidence = confidence;
        this.extents = listOrNull(extents);
    }

    /**
     * Returns the name of the keyphrase
     * @return the name of the keyphrase
     */
    public String getConcept() {
        return concept;
    }

    /**
     * Returns the confidence value associated with the keyphrase
     * @return the confidence value
     */
    public Double getConfidence() {
        return confidence;
    }

    /**
     * Returns the list of extents marking the offsets of each instance
     * of the keyphrase in the text
     * @return the list of extents
     */
    public List<Extent> getExtents() {
        return extents;
    }

    @Override
    protected Objects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("concepts", concept)
                .add("confidence", confidence)
                .add("extents", extents);
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

        Keyphrase that = (Keyphrase) o;

        if (concept != null ? !concept.equals(that.concept) : that.concept != null) {
            return false;
        }

        if (confidence != null ? !confidence.equals(that.confidence) : that.confidence != null) {
            return false;
        }

        return !(extents != null ? !extents.equals(that.extents) : that.extents != null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (concept != null ? concept.hashCode() : 0);
        result = 31 * result + (confidence != null ? confidence.hashCode() : 0);
        result = 31 * result + (extents != null ? extents.hashCode() : 0);
        return result;
    }

    /**
     * A builder for keyphrases
     */
    public static class Builder extends BaseAttribute.Builder<Keyphrase, Keyphrase.Builder> {
        private String concept;
        private Double confidence;
        private List<Extent> extents;

        /**
         * Constructs a builder from the required properties
         * @param concept the name of the keyphrase
         * @param confidence the confidence associated with the keyphrase
         * @param extents the list of extents marking offsets for each mention of the
         *                keyphrase in the text
         */
        public Builder(String concept, Double confidence, List<Extent> extents) {
            this.concept = concept;
            this.confidence = confidence;
            this.extents = extents;
        }

        /**
         * Constructs a builder out of an existing Keyphrase
         * @param toCopy the keyphrase to copy
         */
        public Builder(Keyphrase toCopy) {
            super(toCopy);
            this.concept = toCopy.getConcept();
            this.confidence = toCopy.getConfidence();
            this.extents = toCopy.getExtents();
        }

        /**
         * Specify the name of the keyphrase
         * @param concept the name of the keyphrase
         * @return this
         */
        public Builder concept(String concept) {
            this.concept = concept;
            return this;
        }

        /**
         * Specify the confidence value for the keyphrase
         * @param confidence the confidence value
         * @return this
         */
        public Builder confidence(Double confidence) {
            this.confidence = confidence;
            return this;
        }

        /**
         * Specify the list of offsets of each mention of the keyphrase
         * @param extents the list of extents
         * @return this
         */
        public Builder extents(List<Extent> extents) {
            this.extents = extents;
            return this;
        }

        /**
         * Returns an immutable Keyphrase out of the builder contents
         * @return the new Keyphrase
         */
        public Keyphrase build() {
            return new Keyphrase(concept, confidence, extents, buildExtendedProperties());
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }
}
