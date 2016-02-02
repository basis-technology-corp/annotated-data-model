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

package com.basistech.rosette.dm.jackson.array;

import com.basistech.rosette.dm.LanguageDetection;
import com.basistech.util.ISO15924;
import com.basistech.util.LanguageCode;
import com.basistech.util.jackson.LanguageCodeDeserializer;
import com.basistech.util.jackson.LanguageCodeSerializationConverter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;
import java.util.Map;

/**
 * {@link com.basistech.rosette.dm.LanguageDetection}
 */
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder(alphabetic = true)
public abstract class LanguageDetectionArrayMixin {

    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    @JsonPropertyOrder(alphabetic = true)
    public abstract static class DetectionResultMixin {
        @JsonDeserialize(using = LanguageCodeDeserializer.class)
        LanguageCode language;

        @JsonCreator
        public DetectionResultMixin(@JsonProperty("language")
                                    @JsonDeserialize(using = LanguageCodeDeserializer.class)
                                    LanguageCode language,
                                    @JsonProperty("encoding") String encoding,
                                    @JsonProperty("script") ISO15924 script,
                                    @JsonProperty("confidence") Double confidence,
                                    @JsonProperty("extendedProperties") Map<String, Object> extendedProperties) {
            //
        }

        @JsonSerialize(converter = LanguageCodeSerializationConverter.class)
        public abstract LanguageCode getLanguage();
    }

    @JsonCreator
    LanguageDetectionArrayMixin(@JsonProperty("startOffset") int startOffset,
                                @JsonProperty("endOffset") int endOffset,
                                @JsonProperty("detectionResults") List<LanguageDetection.DetectionResult> detectionResults,
                                @JsonProperty("extendedProperties") Map<String, Object> extendedProperties) {
        //
    }
}
