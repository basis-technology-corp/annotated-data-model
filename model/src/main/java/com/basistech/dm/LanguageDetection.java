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

import java.util.Map;

/**
 * A detected language.
 */
public class LanguageDetection extends Attribute {

    public static class DetectionResult {
        private final LanguageCode language;
        private final String encoding;
        private final double confidence;

        public DetectionResult() {
            // make Jackson happy
            language = LanguageCode.UNKNOWN;
            encoding = null;
            confidence = 0.0;
        }

        public DetectionResult(LanguageCode language, String encoding, double confidence) {
            this.language = language;
            this.encoding = encoding;
            this.confidence = confidence;
        }

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

    private final DetectionResult[] detectionResults;

    public LanguageDetection() {
        // make Jackson happy
        super();
        this.detectionResults = new DetectionResult[0];
    }

    public LanguageDetection(int startOffset, int endOffset, DetectionResult[] detectionResults) {
        super(startOffset, endOffset);
        this.detectionResults = detectionResults;
    }

    public LanguageDetection(int startOffset, int endOffset, DetectionResult[] detectionResults, Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
        this.detectionResults = detectionResults;
    }

    public DetectionResult[] getDetectionResults() {
        return detectionResults;
    }

    public static class Builder extends Attribute.Builder {
        private DetectionResult[] detectionResults;

        public Builder(int startOffset, int endOffset, DetectionResult[] detectionResults) {
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
