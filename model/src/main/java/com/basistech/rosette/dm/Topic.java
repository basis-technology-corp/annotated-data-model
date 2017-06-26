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
 * A reference to a high-level "topic" of a document.  A topic can be an abstract
 * or concrete concept that is highly relevant to the document in question.  Topics
 * may or may not be explicitly referenced in the document, and as such do not refer to
 * any specific span of text.
 * Each {@linkplain Topic} provides:
 * <ul>
 *     <li>the name of the specific topic</li>
 *     <li>a salience value associated with the topic</li>
 *     <li>an ID that associates the topic some external knowledge base, e.g.
 * <a href="http://www.wikidata.org/wiki/Q23">Q23</a> from Wikidata.</li>
 * </ul>
 */
public class Topic extends BaseAttribute implements Serializable {
    private static final long serialVersionUID = 222L;
    private final String concept;
    private final Double salience;
    private final String conceptId;

    protected Topic(String concept, Double salience, String conceptId, Map<String, Object> extendedProperties) {
        super(extendedProperties);
        this.concept = concept;
        this.salience = salience;
        this.conceptId = conceptId;
    }

    /**
     * Returns the name for the topic
     * @return concept the name of the topic
     */
    public String getConcept() {
        return concept;
    }

    /**
     * Returns the salience associated with this topic
     * @return the salience
     */
    public Double getSalience() {
        return salience;
    }

    /**
     * Returns the ID associated with the topic
     * @return the ID associated with the topic
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
        Topic topic = (Topic) o;
        return java.util.Objects.equals(topic, topic.concept)
                && java.util.Objects.equals(salience, topic.salience)
                && java.util.Objects.equals(conceptId, topic.conceptId);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), concept, salience, conceptId);
    }

    @Override
    protected Objects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("concept", concept)
                .add("salience", salience)
                .add("conceptId", conceptId);
    }

    /**
     * A Builder for topics
     */
    public static class Builder extends BaseAttribute.Builder<Topic, Topic.Builder> {
        protected String concept;
        protected Double salience;
        protected String conceptId;

        /**
         * Construct a builder out of the required properties
         * @param concept the name of the topic
         * @param salience the salience associated with the topic
         * @param conceptId the ID associated with the topic
         */
        public Builder(String concept, Double salience, String conceptId) {
            this.concept = concept;
            this.salience = salience;
            this.conceptId = conceptId;
        }

        /**
         * Constructs a builder by copying values from an existing topic
         *
         * @param toCopy the object to copy from
         * @adm.ignore
         */
        public Builder(Topic toCopy) {
            super(toCopy);
            this.concept = toCopy.getConcept();
            this.salience = toCopy.getSalience();
            this.conceptId = toCopy.getConceptId();
        }

        /**
         * Specify the name of the topic
         * @param concept the name of the topic
         * @return this
         */
        public Builder concept(String concept) {
            this.concept = concept;
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
        public Topic build() {
            return new Topic(concept, salience, conceptId, buildExtendedProperties());
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }

}
