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
 * A reference to a "real world" entity.  For example, an {@code EntityMention} of
 * "George" could refer to many different Georges.  A {@code ResolvedEntity}
 * associates the mention with an id from some external knowledge base, e.g.
 * <a href="http://www.wikidata.org/wiki/Q23">Q23</a> from Wikidata.
 * An entity may optionally have a sentiment category associated with it
 * (e.g. "positive", "negative", "neutral").
 * @deprecated replaced by {@link Entity}.
 */
@Deprecated
@EqualsAndHashCode(callSuper = true)
public class ResolvedEntity extends Attribute implements Serializable {
    // hmm, what *really* belongs in equals and hashCode?  maybe just entityId?
    // for now, I'm following suit by using all fields.
    private static final long serialVersionUID = 250L;
    private final String entityId;
    //I prefer 'chainId' over 'coreferenceChainId' but picked the latter to make it consistent with EntityMention
    private final Integer coreferenceChainId;
    private final Double confidence;
    private final CategorizerResult sentiment;

    // for json compatibility on ADM without sentiment fields
    protected ResolvedEntity(int startOffset, int endOffset, String entityId,
                             Integer coreferenceChainId, Double confidence,
                             Map<String, Object> extendedProperties) {
        this(startOffset, endOffset, entityId, coreferenceChainId, confidence,
            null, extendedProperties);
    }

    protected ResolvedEntity(int startOffset, int endOffset, String entityId,
                             Integer coreferenceChainId, Double confidence,
                             CategorizerResult sentiment,
                             Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
        this.entityId = entityId;
        this.coreferenceChainId = coreferenceChainId;
        this.confidence = confidence;
        this.sentiment = sentiment;
    }

    /**
     * Returns the unique identifier of this entity.
     *
     * @return the unique identifier of this entity
     */
    public String getEntityId() {
        return entityId;
    }

    /**
     * Returns the in-document coreference chain ID for this entity, or null if there is none.
     *
     * @return the in-document coreference chain ID for this entity, or null if there is none
     */

    public Integer getCoreferenceChainId() {
        return coreferenceChainId;
    }

    /**
     * Returns the confidence for this resolved entity, or null if there is none.
     *
     * @return the confidence for this resolved entity, or null if there is none
     */
    public Double getConfidence() {
        return confidence;
    }

    /**
     * Returns the sentiment of this entity, or null if not computed.
     * Only the top-ranked sentiment result is returned.  Use
     * {@link Entity#getSentiment()} to obtain all results.
     *
     * @return the sentiment of this entity, or null if not computed.
     */
    public CategorizerResult getSentiment() {
        return sentiment;
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("entityId", entityId)
                .add("confidence", confidence)
                .add("coreferenceChainId", coreferenceChainId)
                .add("sentiment", sentiment);
    }

    /**
     * A builder for resolved entities.
     */
    public static class Builder extends Attribute.Builder<ResolvedEntity, ResolvedEntity.Builder> {
        private String entityId;
        private Double confidence; // NAN?
        private Integer coreferenceChainId;
        private CategorizerResult sentiment;

        /**
         * Constructs a builder from the required values.
         *
         * @param startOffset the start offset in characters
         * @param endOffset the end offset in character
         * @param entityId the entity ID
         */
        public Builder(int startOffset, int endOffset, String entityId) {
            super(startOffset, endOffset);
            this.entityId = entityId;
        }

        /**
         * Constructs a builder by copying values from an existing resolved entity.
         *
         * @param toCopy the object to create
         * @adm.ignore
         */
        public Builder(ResolvedEntity toCopy) {
            super(toCopy);
            this.entityId = toCopy.entityId;
            this.confidence = toCopy.confidence;
            this.coreferenceChainId = toCopy.coreferenceChainId;
            this.sentiment = toCopy.sentiment;
        }

        /**
         * Specifies the entity unique ID.
         *
         * @param entityId the ID
         * @return this
         */
        public Builder entityId(String entityId) {
            this.entityId = entityId;
            return this;
        }

        /**
         * Specifies the confidence value.
         *
         * @param confidence the confidence value
         * @return this
         */
        public Builder confidence(double confidence) {
            this.confidence = confidence;
            return this;
        }

        /**
         * Specifies the coreference chain id.
         *
         * @param coreferenceChainId the chain id
         * @return this
         */
        public Builder coreferenceChainId(int coreferenceChainId) {
            this.coreferenceChainId = coreferenceChainId;
            return this;
        }

        /**
         * Specifies the sentiment.
         *
         * @param sentiment the sentiment
         * @return this
         */
        public Builder sentiment(CategorizerResult sentiment) {
            this.sentiment = sentiment;
            return this;
        }

        /**
         * Returns an immutable resolved entity from the current state of the builder.
         *
         * @return the new resolved entity
         */
        public ResolvedEntity build() {
            return new ResolvedEntity(startOffset, endOffset, entityId, coreferenceChainId, confidence,
                sentiment, buildExtendedProperties());
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }
}
