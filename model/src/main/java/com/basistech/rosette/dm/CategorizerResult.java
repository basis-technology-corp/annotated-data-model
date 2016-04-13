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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

/**
 * A result from running Categorization. These objects are used to represent
 * several different categorization processes, including sentiment.
 * A Categorizer typically emits
 * a ranked list of {@code CategorizerResult}s.  Each result has a label
 * and a confidence.  Optional fields include an explanation set, and
 * per-feature scores.  This result holds the results for document
 * categorization and sentiment analysis.
 */
public class CategorizerResult extends BaseAttribute {
    private final String label;
    private final Double score;
    private final Double confidence;
    private final List<String> explanationSet;
    private final Map<String, Double> perFeatureScores;

    CategorizerResult(String label,
                      Double score,
                      Double confidence,
                      List<String> explanationSet,
                      Map<String, Double> perFeatureScores) {

        this(label, score, confidence, explanationSet, perFeatureScores, null);
    }

    CategorizerResult(String label,
                      Double score,
                      Double confidence,
                      List<String> explanationSet,
                      Map<String, Double> perFeatureScores,
                      Map<String, Object> extendedProperties) {

        super(extendedProperties);
        this.label = label;
        this.score = score;
        this.confidence = confidence;
        if (explanationSet == null) {
            this.explanationSet = null;
        } else {
            this.explanationSet = ImmutableList.copyOf(explanationSet);
        }
        if (perFeatureScores == null) {
            this.perFeatureScores = null;
        } else {
            this.perFeatureScores = ImmutableMap.copyOf(perFeatureScores);
        }
    }

    /**
     * Returns the label of this result.
     *
     * @return the label of this result
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns the raw score of this result.  The meaning of this value is not
     * documented.  Typical users should use {@link #getConfidence()} instead.
     *
     * @return the raw score of this result
     */
    public Double getScore() {
        return score;
    }

    /**
     * Returns the confidence this result.  If confidence calculations were
     * enabled, the value returned will be in the range [0.0, 1.0].  Otherwise
     * null will be returned.
     *
     * @return the confidence of this result
     */
    public Double getConfidence() {
        return confidence;
    }

    /**
     * Returns a list of the N highest positive features that contributed to
     * the categorization (the 'explanation set'), ordered by descending
     * score.  Returns null explanations are disabled.  The value of N is
     * determined by the calling Categorizer.
     *
     * @return the explanation set
     */
    public List<String> getExplanationSet() {
        return explanationSet;
    }

    /**
     * Returns a map of feature string to raw score for this result.  The
     * meaning of a score is not documented.  This is an expert-level feature
     * used for debugging and error analysis.
     *
     * @return the per-feature scores
     */
    public Map<String, Double> getPerFeatureScores() {
        return perFeatureScores;
    }

    @Override
    protected Objects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
            .add("label", label)
            .add("score", score)
            .add("confidence", confidence)
            .add("explanationSet", explanationSet)
            .add("perFeatureScores", perFeatureScores);
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

        CategorizerResult that = (CategorizerResult) o;

        if (confidence != null ? !confidence.equals(that.confidence) : that.confidence != null) {
            return false;
        }
        if (explanationSet != null ? !explanationSet.equals(that.explanationSet) : that.explanationSet != null) {
            return false;
        }
        if (label != null ? !label.equals(that.label) : that.label != null) {
            return false;
        }
        if (perFeatureScores != null ? !perFeatureScores.equals(that.perFeatureScores) : that.perFeatureScores != null) {
            return false;
        }
        return !(score != null ? !score.equals(that.score) : that.score != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (score != null ? score.hashCode() : 0);
        result = 31 * result + (confidence != null ? confidence.hashCode() : 0);
        result = 31 * result + (explanationSet != null ? explanationSet.hashCode() : 0);
        result = 31 * result + (perFeatureScores != null ? perFeatureScores.hashCode() : 0);
        return result;
    }

    /**
     * A builder for classifier results.
     */
    public static class Builder extends BaseAttribute.Builder<CategorizerResult, CategorizerResult.Builder> {
        private String label;
        private Double score;
        private Double confidence;
        private List<String> explanationSet;
        private Map<String, Double> perFeatureScores;

        /**
         * Constructs a builder from the required values.
         *
         * @param label the label
         * @param score the raw score
         */
        public Builder(String label, Double score) {
            super();
            this.label = label;
            // in retrospect, we probably should have made score optional
            this.score = score;
        }

        /**
         * Constructs a builder by copying values from an existing categorizer
         * result.
         *
         * @param toCopy the object to create
         * @adm.ignore
         */
        public Builder(CategorizerResult toCopy) {
            super(toCopy);
            this.label = toCopy.label;
            this.score = toCopy.score;
            this.confidence = toCopy.confidence;
            this.explanationSet = toCopy.explanationSet;
            this.perFeatureScores = toCopy.perFeatureScores;
        }

        /**
         * Specifies the confidence value.
         *
         * @param confidence the confidence value
         * @return this
         */
        public Builder confidence(Double confidence) {
            this.confidence = confidence;
            return this;
        }

        /**
         * Specifies the explanation set.
         *
         * @param explanationSet the explanation set
         * @return this
         */
        public Builder explanationSet(List<String> explanationSet) {
            this.explanationSet = explanationSet;
            return this;
        }

        /**
         * Specifies the per-feature scores.
         *
         * @param perFeatureScores the per-feature scores
         * @return this
         */
        public Builder perFeatureScores(Map<String, Double> perFeatureScores) {
            this.perFeatureScores = perFeatureScores;
            return this;
        }

        /**
         * Returns an immutable categorizer result from the current state of
         * the builder.
         *
         * @return the new categorizer result
         */
        public CategorizerResult build() {
            return new CategorizerResult(label, score, confidence,
                explanationSet, perFeatureScores, buildExtendedProperties());
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }
}
