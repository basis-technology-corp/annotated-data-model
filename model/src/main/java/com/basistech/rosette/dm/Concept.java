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
import java.util.Map;

/**
 * A reference to a high-level "concept" of a document.  A concept can be an abstract
 * or concrete topic that is highly relevant to the document in question.  Concepts
 * may or may not be explicitly referenced in the document, and as such do not refer to
 * any specific span of text.
 * Each {@linkplain Concept} provides:
 * <ul>
 *     <li>the name of the specific concept</li>
 *     <li>a salience value associated with the topic</li>
 *     <li>an ID that associates the topic some external knowledge base, e.g.
 * <a href="http://www.wikidata.org/wiki/Q23">Q23</a> from Wikidata.</li>
 * </ul>
 */
public class Concept extends BaseAttribute implements Serializable {
    private static final long serialVersionUID = 222L;
    private final String topic;
    private final Double salience;
    private final String conceptId;

    protected Concept(String topic, Double salience, String conceptId, Map<String, Object> extendedProperties) {
        super(extendedProperties);
        this.topic = topic;
        this.salience = salience;
        this.conceptId = conceptId;
    }

    /**
     * Returns the name for the concept
     * @return topic the name of the concept
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Returns the salience associated with this concept
     * @return the salience
     */
    public Double getSalience() {
        return salience;
    }

    /**
     * Returns the ID associated with the concept
     * @return the ID associated with the concept
     */
    public String getConceptId() {
        return conceptId;
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
        Concept concept = (Concept) o;
        return java.util.Objects.equals(concept, concept.topic)
                && java.util.Objects.equals(salience, concept.salience)
                && java.util.Objects.equals(conceptId, concept.conceptId);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), topic, salience, conceptId);
    }

    @Override
    protected Objects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("topic", topic)
                .add("salience", salience)
                .add("conceptId", conceptId);
    }

    /**
     * A Builder for topics
     */
    public static class Builder extends BaseAttribute.Builder<Concept, Concept.Builder> {
        protected String topic;
        protected Double salience;
        protected String conceptId;

        /**
         * Construct a builder out of the required properties
         * @param topic the name of the concept
         * @param salience the salience associated with the topic
         * @param conceptId the ID associated with the topic
         */
        public Builder(String topic, Double salience, String conceptId) {
            this.topic = topic;
            this.salience = salience;
            this.conceptId = conceptId;
        }

        /**
         * Constructs a builder by copying values from an existing topic
         *
         * @param toCopy the object to copy from
         * @adm.ignore
         */
        public Builder(Concept toCopy) {
            super(toCopy);
            this.topic = toCopy.getTopic();
            this.salience = toCopy.getSalience();
            this.conceptId = toCopy.getConceptId();
        }

        /**
         * Specify the name of the concept
         * @param topic the name of the concept
         * @return this
         */
        public Builder topic(String topic) {
            this.topic = topic;
            return this;
        }

        /**
         * Specify the salience associated with this topic
         * @param salience the salience associated with the topic
         * @return this
         */
        public Builder salience(Double salience) {
            this.salience = salience;
            return this;
        }

        /**
         * Specify the unique ID associated with this topic
         * @param conceptId the unique ID associated with the topic
         * @return this
         */
        public Builder conceptId(String conceptId) {
            this.conceptId = conceptId;
            return this;
        }

        /**
         * Returns an immutable topic based on the content of the builder
         * @return the new topic
         */
        public Concept build() {
            return new Concept(topic, salience, conceptId, buildExtendedProperties());
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }

}
