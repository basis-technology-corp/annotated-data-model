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

import com.basistech.util.ISO15924;
import com.basistech.util.LanguageCode;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * The results of running language detection on a region of text.
 * The results are composed as a list of {@link com.basistech.rosette.dm.LanguageDetection.DetectionResult},
 * to reflect the multiple alternatives produced by language detectors.
 */
@EqualsAndHashCode(callSuper = true)
public class LanguageDetection extends Attribute implements Serializable {
    private static final long serialVersionUID = 250L;

    /**
     * A single result from language detection.
     * Typically, the language identifier produces multiple results with
     * different confidence values.
     */
    @EqualsAndHashCode(callSuper = true)
    public static class DetectionResult extends BaseAttribute {
        private final LanguageCode language;
        private final String encoding;
        private final ISO15924 script;
        private final Double confidence;

        protected DetectionResult(LanguageCode language,
                                   String encoding,
                                   ISO15924 script,
                                   Double confidence,
                                   Map<String, Object> extendedProperties) {
            super(extendedProperties);
            this.language = language;
            this.encoding = encoding;
            this.script = script;
            this.confidence = confidence;
        }

        /**
         * Returns the detected language.
         *
         * @return the detected language
         */
        public LanguageCode getLanguage() {
            return language;
        }

        /**
         * Returns the detected encoding.
         *
         * @return the detected encoding, or null if none was detected
         */
        public String getEncoding() {
            return encoding;
        }

        /**
         * Returns the script.
         *
         * @return the script, or null of none was detected
         */
        public ISO15924 getScript() {
            return script;
        }

        /**
         * Returns the confidence of this detection.
         *
         * @return the confidence of this detection alternative, or
         * null if not available.
         */
        public Double getConfidence() {
            return confidence;
        }

        @Override
        protected MoreObjects.ToStringHelper toStringHelper() {
            return super.toStringHelper()
                    .add("language", language)
                    .add("encoding", encoding)
                    .add("script", script)
                    .add("confidence", confidence);
        }

        /**
         * Builder for detection results.
         */
        public static class Builder extends BaseAttribute.Builder<DetectionResult, DetectionResult.Builder> {
            private LanguageCode language;
            private String encoding;
            private ISO15924 script;
            private Double confidence;

            /**
             * Constructs a builder with default values.
             *
             * @param language the detected language
             */
            public Builder(LanguageCode language) {
                this.language = language;
            }

            /**
             * Constructs a builder initialized from an existing detection result.
             *
             * @param toCopy the item to copy
             */
            public Builder(DetectionResult toCopy) {
                super(toCopy);
                language = toCopy.getLanguage();
                encoding = toCopy.getEncoding();
                script = toCopy.getScript();
                confidence = toCopy.getConfidence();
            }

            /**
             * Specifies the language.
             *
             * @param language the language
             * @return this
             */
            public Builder language(LanguageCode language) {
                this.language = language;
                return this;
            }

            /**
             * Specifies the encoding.
             *
             * @param encoding the encoding
             * @return this
             */
            public Builder encoding(String encoding) {
                this.encoding = encoding;
                return this;
            }

            /**
             * Specifies the script.
             *
             * @param script the script
             * @return this
             */
            public Builder script(ISO15924 script) {
                this.script = script;
                return this;
            }

            /**
             * Specifies the confidence.
             *
             * @param confidence the confidence; null if no confidence is available.
             * @return this.
             */
            public Builder confidence(Double confidence) {
                this.confidence = confidence;
                return this;
            }

            /**
             * Build an immutable detection result from the current state of the builder.
             *
             * @return the detection result
             */
            public DetectionResult build() {
                return new DetectionResult(language, encoding, script, confidence, buildExtendedProperties());
            }

            @Override
            protected Builder getThis() {
                return this;
            }
        }
    }

    private final List<DetectionResult> detectionResults;

    protected LanguageDetection(int startOffset, int endOffset, List<DetectionResult> detectionResults, Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
        this.detectionResults = listOrNull(detectionResults);
    }

    /**
     * Returns the detection results, in order from best to worst confidence.
     *
     * @return the detection results, in order from best to worst confidence
     */
    public List<DetectionResult> getDetectionResults() {
        return detectionResults;
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("detectionResults", detectionResults);
    }

    /**
     * A builder for language detection results.
     */
    public static class Builder extends Attribute.Builder<LanguageDetection, LanguageDetection.Builder> {
        private List<DetectionResult> detectionResults;

        /**
         * Constructs a builder from the required properties.
         *
         * @param startOffset the start offset of the region in characters
         * @param endOffset the end offset of the region in characters
         * @param detectionResults the list of detection results
         */
        public Builder(int startOffset, int endOffset, List<DetectionResult> detectionResults) {
            super(startOffset, endOffset);
            this.detectionResults = detectionResults;
        }

        /**
         * Constructs a builder by copying the values from an existing language detection.
         *
         * @param toCopy the object to copy
         */
        public Builder(LanguageDetection toCopy) {
            super(toCopy);
            this.detectionResults = Lists.newArrayList();
            addAllToList(this.detectionResults, toCopy.detectionResults);
        }

        /**
         * Constructs an immutable language detection result from the current state of the builder.
         *
         * @return the new language detection
         */
        public LanguageDetection build() {
            // we do not null this list when empty. Should we?
            return new LanguageDetection(startOffset, endOffset, detectionResults, buildExtendedProperties());
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }
}
