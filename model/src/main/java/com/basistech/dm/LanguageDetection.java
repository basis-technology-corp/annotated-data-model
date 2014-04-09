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

package com.basistech.dm;

import com.basistech.util.LanguageCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * The results of running language detection on a region of text.
 * Typically, there will be multiple of these since detectors
 * return multiple possible answers.
 */
public class LanguageDetection extends Attribute {

    public static class DetectionResult {
        @JsonDeserialize(using = LanguageCodeDeserializer.class)
        private final LanguageCode language;
        private final String encoding;
        private final double confidence;

        protected DetectionResult() {
            language = LanguageCode.UNKNOWN;
            encoding = null;
            confidence = 0.0;
        }

        @JsonCreator
        public DetectionResult(@JsonProperty("language")
                               @JsonDeserialize(using = LanguageCodeDeserializer.class)
                               LanguageCode language,
                               @JsonProperty("encoding")
                               String encoding,
                               @JsonProperty("confidence")
                               double confidence) {
            this.language = language;
            this.encoding = encoding;
            this.confidence = confidence;
        }

        @JsonSerialize(converter = LanguageCodeSerializationConverter.class)
        public LanguageCode getLanguage() {
            return language;
        }

        public String getEncoding() {
            return encoding;
        }

        public double getConfidence() {
            return confidence;
        }
    }

    private final List<DetectionResult> detectionResults;

    public LanguageDetection() {
        // make Jackson happy
        super();
        this.detectionResults = Lists.newArrayList();
    }

    public LanguageDetection(int startOffset, int endOffset, List<DetectionResult> detectionResults) {
        super(startOffset, endOffset);
        this.detectionResults = detectionResults;
    }

    public LanguageDetection(int startOffset, int endOffset, List<DetectionResult> detectionResults, Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
        this.detectionResults = detectionResults;
    }

    public List<DetectionResult> getDetectionResults() {
        return detectionResults;
    }

    /**
     * A builder for language detection results.
     */
    public static class Builder extends Attribute.Builder {
        private List<DetectionResult> detectionResults;

        public Builder(int startOffset, int endOffset, List<DetectionResult> detectionResults) {
            super(startOffset, endOffset);
            this.detectionResults = detectionResults;
        }

        public Builder(LanguageDetection toCopy) {
            super(toCopy);
            this.detectionResults = toCopy.detectionResults;
        }

        public LanguageDetection build() {
            return new LanguageDetection(startOffset, endOffset, detectionResults, extendedProperties);
        }
    }
}
