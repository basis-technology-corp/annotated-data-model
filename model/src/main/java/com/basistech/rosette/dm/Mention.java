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

import com.google.common.base.MoreObjects;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Map;

/**
 * A mention of a entity in the text.  For example, "George" and
 * "George Washington" are mentions of type "PERSON".
 */
@EqualsAndHashCode(callSuper = true)
public class Mention extends Attribute implements Serializable {
    private static final long serialVersionUID = 250L;
    private final Double confidence;
    private final Double linkingConfidence;
    private final String source;
    private final String subsource;
    private final String normalized;

    protected Mention(int startOffset, int endOffset,
                      Double confidence,
                      Double linkingConfidence,
                      String source,
                      String subsource,
                      String normalized,
                      Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
        this.confidence = confidence;
        this.linkingConfidence = linkingConfidence;
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
     * Returns the linking confidence of the kb-linker in link this mention and entity id.
     *
     * @return the linking confidence of the kb-linker in link this mention and entity id.
     * This value will be null if there is no calculated confidence value.
     */
    public Double getLinkingConfidence() {
        return linkingConfidence;
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
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("confidence", confidence)
                .add("linkingConfidence", linkingConfidence)
                .add("source", source)
                .add("subsource", subsource)
                .add("normalized", normalized);
    }

    /**
     * A builder for entity mentions.
     */
    public static class Builder extends Attribute.Builder<Mention, Mention.Builder>  {
        private Double confidence;
        private Double linkingConfidence;
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
         */
        public Builder(Mention toCopy) {
            super(toCopy);
            this.confidence = toCopy.confidence;
            this.source = toCopy.source;
            this.subsource = toCopy.subsource;
            this.normalized = toCopy.normalized;
            this.linkingConfidence = toCopy.linkingConfidence;
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
         * Specifies the linking confidence.
         *
         * @param linkingConfidence the linking confidence, or null to indicate that no confidence is available.
         * @return this
         */
        public Builder linkingConfidence(Double linkingConfidence) {
            this.linkingConfidence = linkingConfidence;
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
            return new Mention(startOffset, endOffset, confidence, linkingConfidence, source,
                subsource, normalized, buildExtendedProperties());
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }
}
