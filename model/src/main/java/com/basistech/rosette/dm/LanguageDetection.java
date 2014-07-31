/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2014 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/

package com.basistech.rosette.dm;

import com.basistech.util.ISO15924;
import com.basistech.util.LanguageCode;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * The results of running language detection on a region of text.
 * The results are composed as a list of {@link com.basistech.rosette.dm.LanguageDetection.DetectionResult},
 * to reflect the multiple alternatives produced by language detectors.
 */
public class LanguageDetection extends Attribute {

    /**
     * A single result from language detection.
     * Typically, the language identifier produces multiple results with
     * different confidence values.
     */
    public static class DetectionResult extends BaseAttribute {
        private final LanguageCode language;
        private final String encoding;
        private final ISO15924 script;
        private final double confidence;

        protected DetectionResult() {
            language = LanguageCode.UNKNOWN;
            encoding = null;
            script = ISO15924.Zyyy;
            confidence = 0.0;
        }

        protected DetectionResult(LanguageCode language,
                                   String encoding,
                                   ISO15924 script,
                                   double confidence,
                                   Map<String, Object> extendedProperties) {
            super(extendedProperties);
            this.language = language;
            this.encoding = encoding;
            this.script = script;
            this.confidence = confidence;
        }

        /**
         * @deprecated this will be deprecated in a future version. Use the builder.
         */
        @Deprecated
        public DetectionResult(LanguageCode language,
                               String encoding,
                               ISO15924 script,
                               double confidence) {
            this.language = language;
            this.encoding = encoding;
            this.script = script;
            this.confidence = confidence;
        }

        /**
         * @return the detected language.
         */
        public LanguageCode getLanguage() {
            return language;
        }

        /**
         * @return the detected encoding, or null if none was detected.
         */
        public String getEncoding() {
            return encoding;
        }

        /**
         * @return the script, or null of none was detected.
         */
        public ISO15924 getScript() {
            return script;
        }

        /**
         * @return the confidence of this detection alternative, or
         * Not-a-Number if not available.
         */
        public double getConfidence() {
            return confidence;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            DetectionResult that = (DetectionResult) o;

            if (Double.compare(that.confidence, confidence) != 0) {
                return false;
            }
            if (encoding != null ? !encoding.equals(that.encoding) : that.encoding != null) {
                return false;
            }
            if (language != that.language) {
                return false;
            }
            if (script != that.script) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            result = language.hashCode();
            result = 31 * result + (encoding != null ? encoding.hashCode() : 0);
            result = 31 * result + (script != null ? script.hashCode() : 0);
            temp = Double.doubleToLongBits(confidence);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }

        @Override
        protected Objects.ToStringHelper toStringHelper() {
            return super.toStringHelper()
                    .add("language", language)
                    .add("encoding", encoding)
                    .add("script", script)
                    .add("confidence", confidence);
        }

        /**
         * Builder for detection results.
         */
        public static class Builder extends BaseAttribute.Builder {
            private LanguageCode language;
            private String encoding;
            private ISO15924 script;
            private double confidence;

            /**
             * Construct a builder with default values.
             * @param language the detected language.
             */
            public Builder(LanguageCode language) {
                this.language = language;
                encoding = null;
                script = ISO15924.Zyyy;
                confidence = Double.NaN;
            }

            /**
             * Construct a builder initialized from an existing detection result.
             * @param toCopy the item to copy.
             * @adm.ignore
             */
            public Builder(DetectionResult toCopy) {
                super(toCopy);
                language = toCopy.getLanguage();
                encoding = toCopy.getEncoding();
                script = toCopy.getScript();
                confidence = toCopy.getConfidence();
            }

            /**
             * Set the language.
             * @param language the language.
             * @return this.
             */
            public Builder language(LanguageCode language) {
                this.language = language;
                return this;
            }

            /**
             * Specify the encoding.
             * @param encoding the encoding.
             * @return this.
             */
            public Builder encoding(String encoding) {
                this.encoding = encoding;
                return this;
            }

            /**
             * Specify the script.
             * @param script the script.
             * @return this
             */
            public Builder script(ISO15924 script) {
                this.script = script;
                return this;
            }

            /**
             * Specify the confidence.
             * @param confidence the confidence.
             * @return this.
             */
            public Builder confidence(double confidence) {
                this.confidence = confidence;
                return this;
            }

            /**
             * Build an immutable detection results from the current state of the builder.
             * @return the detection result.
             */
            @SuppressWarnings("deprecation")
            public DetectionResult build() {
                return new DetectionResult(language, encoding, script, confidence);
            }
        }
    }

    private final List<DetectionResult> detectionResults;

    LanguageDetection() {
        // make Jackson happy
        super();
        this.detectionResults = Lists.newArrayList();
    }

    LanguageDetection(int startOffset, int endOffset, List<DetectionResult> detectionResults, Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
        this.detectionResults = detectionResults;
    }

    LanguageDetection(int startOffset,
                      int endOffset,
                      List<DetectionResult> detectionResults) {
        super(startOffset, endOffset);
        this.detectionResults = detectionResults;
    }

    /**
     * @return the detection results, in order from best to worst confidence.
     */
    public List<DetectionResult> getDetectionResults() {
        return detectionResults;
    }

    @Override
    protected Objects.ToStringHelper toStringHelper() {
        return Objects.toStringHelper(this)
                .add("detectionResults", detectionResults);
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

        LanguageDetection that = (LanguageDetection) o;

        if (!detectionResults.equals(that.detectionResults)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + detectionResults.hashCode();
        return result;
    }

    /**
     * A builder for language detection results.
     */
    public static class Builder extends Attribute.Builder {
        private List<DetectionResult> detectionResults;

        /**
         * Construct a builder from the required properties.
         * @param startOffset the start offset of the region in characters.
         * @param endOffset the end offset of the region in characters.
         * @param detectionResults the list of detection results.
         */
        public Builder(int startOffset, int endOffset, List<DetectionResult> detectionResults) {
            super(startOffset, endOffset);
            this.detectionResults = detectionResults;
        }

        /**
         * Construct a builder by copying the values from an existing language detection.
         * @param toCopy the object to copy.
         */
        public Builder(LanguageDetection toCopy) {
            super(toCopy);
            this.detectionResults = toCopy.detectionResults;
        }

        /**
         * Construct an immutable language detection result from the current state of the builder.
         * @return the new language detection.
         */
        public LanguageDetection build() {
            return new LanguageDetection(startOffset, endOffset, detectionResults, extendedProperties);
        }
    }
}
