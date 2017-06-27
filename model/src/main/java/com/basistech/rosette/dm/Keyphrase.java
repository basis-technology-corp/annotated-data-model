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
 *     <li>the salience associated with the keyphrase</li>
 *     <li>a list of {@linkplain Extent}s marking each location of the keyphrase in the text</li>
 * </ul>
 */
public class Keyphrase extends BaseAttribute implements Serializable {
    private static final long serialVersionUID = 222L;
    private final String keyphrase;
    private final Double salience;
    private final List<Extent> extents;

    protected Keyphrase(String keyphrase, Double salience, List<Extent> extents, Map<String, Object> extendedProperties) {
        super(extendedProperties);
        this.keyphrase = keyphrase;
        this.salience = salience;
        this.extents = listOrNull(extents);
    }

    /**
     * Returns the name of the keyphrase
     * @return the name of the keyphrase
     */
    public String getKeyphrase() {
        return keyphrase;
    }

    /**
     * Returns the salience value associated with the keyphrase
     * @return the salience value
     */
    public Double getSalience() {
        return salience;
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
                .add("keyphrase", keyphrase)
                .add("salience", salience)
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

        if (keyphrase != null ? !keyphrase.equals(that.keyphrase) : that.keyphrase != null) {
            return false;
        }

        if (salience != null ? !salience.equals(that.salience) : that.salience != null) {
            return false;
        }

        return !(extents != null ? !extents.equals(that.extents) : that.extents != null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (keyphrase != null ? keyphrase.hashCode() : 0);
        result = 31 * result + (salience != null ? salience.hashCode() : 0);
        result = 31 * result + (extents != null ? extents.hashCode() : 0);
        return result;
    }

    /**
     * A builder for keyphrases
     */
    public static class Builder extends BaseAttribute.Builder<Keyphrase, Keyphrase.Builder> {
        private String keyphrase;
        private Double salience;
        private List<Extent> extents;

        /**
         * Constructs a builder from the required properties
         * @param keyphrase the name of the keyphrase
         * @param extents the list of extents marking offsets for each mention of the
         *                keyphrase in the text
         */
        public Builder(String keyphrase, List<Extent> extents) {
            this.keyphrase = keyphrase;
            this.extents = extents;
        }

        /**
         * Constructs a builder out of an existing Keyphrase
         * @param toCopy the keyphrase to copy
         */
        public Builder(Keyphrase toCopy) {
            super(toCopy);
            this.keyphrase = toCopy.getKeyphrase();
            this.salience = toCopy.getSalience();
            this.extents = toCopy.getExtents();
        }

        /**
         * Specify the name of the keyphrase
         * @param keyphrase the name of the keyphrase
         * @return this
         */
        public Builder keyphrase(String keyphrase) {
            this.keyphrase = keyphrase;
            return this;
        }

        /**
         * Specify the salience value for the keyphrase
         * @param salience the salience value
         * @return this
         */
        public Builder salience(Double salience) {
            this.salience = salience;
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
            return new Keyphrase(keyphrase, salience, extents, buildExtendedProperties());
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }
}
