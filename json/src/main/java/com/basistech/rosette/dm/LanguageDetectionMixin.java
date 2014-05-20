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
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

/**
 * {@link com.basistech.rosette.dm.LanguageDetection}
 */
public abstract class LanguageDetectionMixin {
    public abstract static class DetectionResultMixin {
        @JsonDeserialize(using = LanguageCodeDeserializer.class)
        LanguageCode language;

        @JsonCreator
        public DetectionResultMixin(@JsonProperty("language")
                                    @JsonDeserialize(using = LanguageCodeDeserializer.class)
                                    LanguageCode language,
                                    @JsonProperty("encoding") String encoding,
                                    @JsonProperty("script") ISO15924 script,
                                    @JsonProperty("confidence") double confidence) {
            //
        }

        @JsonSerialize(converter = LanguageCodeSerializationConverter.class)
        public abstract LanguageCode getLanguage();
    }

    @JsonCreator
    LanguageDetectionMixin(@JsonProperty("startOffset") int startOffset,
                           @JsonProperty("endOffset") int endOffset,
                           @JsonProperty("detectionResults") List<LanguageDetection.DetectionResult> detectionResults) {
        //
    }


}
