/*
* Copyright 2014 Basis Technology Corp.
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
import java.util.Map;

/**
 * A mention of a entity in the text.  For example, "George" and
 * "George Washington" are mentions of type "PERSON".
 */
public class Mention extends Attribute implements Serializable {
    private static final long serialVersionUID = 222L;
    private final Double confidence;
    private final String source;
    private final String subsource;
    private final String normalized;

    protected Mention(int startOffset, int endOffset,
                      Double confidence,
                      String source,
                      String subsource,
                      String normalized,
                      Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
        this.confidence = confidence;
        this.source = source;
        this.subsource = subsource;
        this.normalized = normalized;
    }

    /**
     * Returns the confidence of the entity extractor in identifying this mention.
     *
     * @return the confidence of the entity extractor in identifying this mention.
     * This value will be null if there is no calculated confidence value.
     */
    public Double getConfidence() {
        return confidence;
    }

    /**
     * Returns the entity extraction source that produced this entity.  For
     * example, "statistical", "regex", "gazetteer".
     *
     * @return the entity extraction source
     */
    public String getSource() {
        return source;
    }

    /**
     * Returns the entity extraction subsource that produced this entity.
     * This is usually filename for the regular expression file or gazetteer in which the entity appears.
     * For example, "./data/gazetteer/eng/accept/gaz-LE.bin"
     *
     * @return the entity extraction subsource
     */
    public String getSubsource() {
        return subsource;
    }

    /**
     * Returns the normalized form of the mention.  This form typically
     * normalizes spaces spaces and removes embedded newlines.  It may omit
     * prefixes in languages like Arabic.  This is not a canonical way to
     * refer to the entity (see {@link ResolvedEntity})
     * but rather a simplified form of this particular mention text.
     *
     * @return the normalized form of the mention
     */
    public String getNormalized() {
        return normalized;
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

        Mention that = (Mention) o;

        if (confidence != null ? !confidence.equals(that.confidence) : that.confidence != null) {
            return false;
        }

        if (normalized != null ? !normalized.equals(that.normalized) : that.normalized != null) {
            return false;
        }

        if (source != null ? !source.equals(that.source) : that.source != null) {
            return false;
        }
        return !(subsource != null ? !subsource.equals(that.subsource) : that.subsource != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (confidence != null ? confidence.hashCode() : 0);
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (subsource != null ? subsource.hashCode() : 0);
        result = 31 * result + (normalized != null ? normalized.hashCode() : 0);
        return result;
    }

    @Override
    protected Objects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("confidence", confidence)
                .add("source", source)
                .add("subsource", subsource)
                .add("normalized", normalized);
    }

    /**
     * A builder for entity mentions.
     */
    public static class Builder extends Attribute.Builder<Mention, Mention.Builder>  {
        private Double confidence;
        private String source;
        private String subsource;
        private String normalized;

        /**
         * Constructs a builder with the minimal required information for an entity mention.
         *
         * @param startOffset the start offset in the text, in characters
         * @param endOffset the end offset in the text, in characters
         */
        public Builder(int startOffset, int endOffset) {
            super(startOffset, endOffset);
        }

        /**
         * Constructs a builder initialized with information from an existing entity mention.
         *
         * @param toCopy the mention to copy.
         * @adm.ignore
         */
        public Builder(Mention toCopy) {
            super(toCopy);
            this.confidence = toCopy.confidence;
            this.source = toCopy.source;
            this.subsource = toCopy.subsource;
            this.normalized = toCopy.normalized;
        }

        /**
         * Specifies the confidence.
         *
         * @param confidence the confidence, or null to indicate that no confidence is available.
         * @return this
         */
        public Builder confidence(Double confidence) {
            this.confidence = confidence;
            return this;
        }

        /**
         * Specifies the source of this mention.
         *
         * @param source the source
         * @return this
         */
        public Builder source(String source) {
            this.source = source;
            return this;
        }

        /**
         * Specifies the subsource of this mention.
         *
         * @param subsource the subsource
         * @return this
         */
        public Builder subsource(String subsource) {
            this.subsource = subsource;
            return this;
        }

        /**
         * Specifies the normalized form of this mention.
         *
         * @param normalized the normalized form
         * @return this
         */
        public Builder normalized(String normalized) {
            this.normalized = normalized;
            return this;
        }

        /**
         * Builds the immutable mention.
         *
         * @return the mention
         */
        public Mention build() {
            return new Mention(startOffset, endOffset, confidence, source,
                subsource, normalized, buildExtendedProperties());
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }
}
