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

import java.io.Serializable;
import java.util.Objects;

/**
 * A term with some semantic similarity to an {@link AnnotatedText}.
 */
public class RelatedTerm extends BaseAttribute implements Serializable {
    private final String term;
    private final Double similarity;

    protected RelatedTerm(String term, Double similarity) {
        this.term = term;
        this.similarity = similarity;
    }

    /**
     * Returns the text of this term
     * @return the text of this term
     */
    public String getTerm() {
        return term;
    }

    /**
     * Returns the similarity between this term and the document
     * @return the similarity between this term and the document
     */
    public Double getSimilarity() {
        return similarity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RelatedTerm that = (RelatedTerm) o;
        return Objects.equals(term, that.term) && Objects.equals(similarity, that.similarity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(term, similarity);
    }

    @Override
    public String toString() {
        return String.format("RelatedTerm{term=%s; similarity=%s}", term, similarity);
    }

    /**
     * Builder class for RelatedTerm
     */
    public static class Builder extends BaseAttribute.Builder<RelatedTerm, RelatedTerm.Builder> {
        private String term;
        private Double similarity;

        public Builder() {
            term = null;
            similarity = null;
        }

        /**
         * Specifies the text of this term
         * @param term the text of this term
         * @return this
         */
        public Builder term(String term) {
            this.term = term;
            return this;
        }

        /**
         * Specifies the similarity of this term
         * @param similarity the similarity of this term
         * @return this
         */
        public Builder similarity(Double similarity) {
            this.similarity = similarity;
            return this;
        }

        /**
         * Returns an immutable RelatedTerm from the current state of this builder
         * @return the new RelatedTerm instance
         */
        public RelatedTerm build() {
            return new RelatedTerm(term, similarity);
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }
}
