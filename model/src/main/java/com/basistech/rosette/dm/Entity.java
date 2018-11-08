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
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A reference to a "real world" entity. Each entity in a document is
 * the result of resolving one or more {@link Mention}s -- a {@linkplain Mention}
 * is a span of text that mentions an entity, while an {@linkplain Entity}
 * describes the entity itself.
 * <p>
 * Each {@linkplain Entity} provides:
 * <ul>
 *     <li>a list of {@linkplain Mention}s</li>
 *     <li>the index of the head {@linkplain Mention} in the mentions' list</li>
 *     <li>the type of the entity, e.g. PERSON</li>
 *     <li>(optionally) a salience score</li>
 *     <li>(optionally) an ID that associates the entity some external knowledge base, e.g.
 * <a href="http://www.wikidata.org/wiki/Q23">Q23</a> from Wikidata.</li>
 * <li>(optionally) a sentiment category associated with it
 * (e.g. "positive", "negative", "neutral").</li>
 * </ul>
 * Entities are not spans of text.
 * The mentions in an entity are in document order.
 */
public class Entity extends BaseAttribute implements Serializable {
    private static final long serialVersionUID = 250L;
    private List<Mention> mentions;
    private final Integer headMentionIndex;
    private final String type;
    private final String entityId;
    private final Double confidence;
    private final List<CategorizerResult> sentiment;
    private final Double salience;

    protected Entity(List<Mention> mentions,
                     Integer headMentionIndex,
                     String type,
                     String entityId,
                     Double confidence,
                     List<CategorizerResult> sentiment,
                     Double salience,
                     Map<String, Object> extendedProperties) {
        super(extendedProperties);
        this.mentions = listOrNull(mentions);
        this.headMentionIndex = headMentionIndex;
        this.type = type;
        this.entityId = entityId;
        this.confidence = confidence;
        this.sentiment = listOrNull(sentiment);
        this.salience = salience;
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
     * @return the list of mentions that support this entity.
     */
    public List<Mention> getMentions() {
        return mentions;
    }

    /**
     * Return the head mention index, if any. The head mention is the mention judged to be
     * the best representation of the entity itself. This returns {@code null} if no
     * mention is designated as head.
     * @return the index of the head mention.
     */
    public Integer getHeadMentionIndex() {
        return headMentionIndex;
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
     *
     * @return the sentiment of this entity, or null if not computed.
     */
    public List<CategorizerResult> getSentiment() {
        return sentiment;
    }

    /**
     * If there is a type established for the entity, return the type.
     * @return the entity type, or {@code null} if none was established.
     */
    public String getType() {
        return type;
    }

    /**
     * If salience was computed for this entity, return it.
     * @return the salience value, or {@code null} if none was calculated.
     */
    public Double getSalience() {
        return salience;
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
        Entity entity = (Entity) o;
        return Objects.equals(mentions, entity.mentions)
                && Objects.equals(headMentionIndex, entity.headMentionIndex)
                && Objects.equals(type, entity.type)
                && Objects.equals(entityId, entity.entityId)
                && Objects.equals(confidence, entity.confidence)
                && Objects.equals(sentiment, entity.sentiment)
                && Objects.equals(salience, entity.salience);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), mentions, headMentionIndex, type, entityId, confidence, sentiment, salience);
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("type", type)
                .add("entityId", entityId)
                .add("mentions", mentions)
                .add("confidence", confidence)
                .add("sentiment", sentiment)
                .add("salience", salience);
    }

    /**
     * Factory method for {@link Builder} instances.
     *
     * @return the new builder
     * @see Builder#Builder()
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Factory method for {@link Builder} instances.
     *
     * @param toCopy the entity to copy
     * @return the new builder
     * @see Builder#Builder(Entity)
     */
    public static Builder builder(Entity toCopy) {
        return new Builder(toCopy);
    }

    /**
     * A builder for resolved entities.
     */
    public static class Builder extends BaseAttribute.Builder<Entity, Entity.Builder> {
        private String entityId;
        private String type;
        private List<Mention> mentions;
        private Integer headMentionIndex;
        private Double confidence;
        private List<CategorizerResult> sentiment;
        private Double salience;

        /**
         * Constructs a builder from the required values.
         */
        public Builder() {
            mentions = Lists.newArrayList();
            sentiment = Lists.newArrayList();
        }

        /**
         * Constructs a builder by copying values from an existing resolved entity.
         *
         * @param toCopy the object to create
         * @adm.ignore
         */
        public Builder(Entity toCopy) {
            super(toCopy);
            mentions = Lists.newArrayList();
            addAllToList(mentions, toCopy.mentions);
            this.entityId = toCopy.entityId;
            this.confidence = toCopy.confidence;
            sentiment = Lists.newArrayList();
            addAllToList(sentiment, toCopy.sentiment);
            this.type = toCopy.type;
            this.headMentionIndex = toCopy.headMentionIndex;
            this.salience = toCopy.salience;
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
         * Add one mention to the mentions.
         * @param mention the mention.
         * @return this.
         */
        public Builder mention(Mention mention) {
            this.mentions.add(mention);
            return this;
        }

        /**
         * Add one mention to the mentions.
         * @param builder the mention builder.
         * @param <B> the class of the mention builder.
         * @return this.
         * @see Builder#mention(Mention)
         */
        public <B extends Attribute.Builder<Mention, ?>> Builder mention(B builder) {
            return mention(builder.build());
        }

        /**
         * Specifies the index of the head mention in the list of mentions, if any.
         * @param headMentionIndex the index.
         * @return this.
         */
        public Builder headMentionIndex(Integer headMentionIndex) {
            this.headMentionIndex = headMentionIndex;
            return this;
        }

        /**
         * Specifies the confidence value of the resolved entity.
         *
         * @param confidence the confidence value
         * @return this
         */
        public Builder confidence(Double confidence) {
            this.confidence = confidence;
            return this;
        }

        /**
         * Specifies the sentiment.
         *
         * @param sentiment the sentiment
         * @return this
         */
        public Builder sentiment(CategorizerResult sentiment) {
            this.sentiment.add(sentiment);
            return this;
        }

        /**
         * Specifies the sentiment.
         *
         * @param builder the sentiment builder
         * @param <B> the class of the sentiment builder
         * @return this
         * @see Builder#sentiment(CategorizerResult)
         */
        public <B extends BaseAttribute.Builder<CategorizerResult, ?>> Builder sentiment(B builder) {
            return sentiment(builder.build());
        }

        /**
         * Specify the type for this entity.
         * @param type the type
         * @return this
         */
        public Builder type(String type) {
            this.type = type;
            return this;
        }

        /**
         * Specify the salience for this entity.
         * @param salience salience value.
         * @return this
         */
        public Builder salience(Double salience) {
            this.salience = salience;
            return this;
        }

        /**
         * Returns an immutable resolved entity from the current state of the builder.
         *
         * @return the new resolved entity
         */
        public Entity build() {
            return new Entity(mentions, headMentionIndex, type, entityId, confidence,
                            sentiment, salience, buildExtendedProperties());
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }
}
