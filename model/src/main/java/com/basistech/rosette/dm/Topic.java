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

public class Topic extends BaseAttribute implements Serializable {
    private static final long serialVersionUID = 222L;
    private final String concept;
    private final Double confidence;
    private final String conceptId;

    public Topic() throws Exception {
        throw new Exception("default constructor");
    }
    protected Topic(String concept, Double confidence, String conceptId, Map<String, Object> extendedProperties) {
        super(extendedProperties);
        this.concept = concept;
        this.confidence = confidence;
        this.conceptId = conceptId;
    }

    public String getConcept() {
        return concept;
    }

    public Double getConfidence() {
        return confidence;
    }

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
                && java.util.Objects.equals(confidence, topic.confidence)
                && java.util.Objects.equals(conceptId, topic.conceptId);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), concept, confidence, conceptId);
    }

    @Override
    protected Objects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("concept", concept)
                .add("confidence", confidence)
                .add("conceptId", conceptId);
    }

    public static class Builder extends BaseAttribute.Builder<Topic, Topic.Builder> {
        protected String concept;
        protected Double confidence;
        protected String conceptId;

        public Builder(String concept, Double confidence, String conceptId) {
            this.concept = concept;
            this.confidence = confidence;
            this.conceptId = conceptId;
        }

        public Builder(Topic toCopy) {
            super(toCopy);
            this.concept = toCopy.getConcept();
            this.confidence = toCopy.getConfidence();
            this.conceptId = toCopy.getConceptId();
        }

        public Builder concept(String concept) {
            this.concept = concept;
            return this;
        }

        public Builder confidence(Double confidence) {
            this.confidence = confidence;
            return this;
        }

        public Builder conceptId(String conceptId) {
            this.conceptId = conceptId;
            return this;
        }

        public Topic build() {
            return new Topic(concept, confidence, conceptId, buildExtendedProperties());
        }

        @Override
        public Builder getThis() {
            return this;
        }
    }

}
