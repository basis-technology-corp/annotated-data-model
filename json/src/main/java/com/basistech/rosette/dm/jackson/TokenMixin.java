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
package com.basistech.rosette.dm.jackson;

import com.basistech.rosette.dm.MorphoAnalysis;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Map;

/**
 * {@link com.basistech.rosette.dm.Token}.
 */
public abstract class TokenMixin {
    @JsonDeserialize(using = MorphoAnalysisListDeserializer.class)
    List<MorphoAnalysis> analyses;

    @JsonCreator
    TokenMixin(@JsonProperty("startOffset") int startOffset,
               @JsonProperty("endOffset") int endOffset,
               @JsonProperty("text") String text,
               @JsonProperty("normalized") List<String> normalized,
               @JsonProperty("source") String source,
               @JsonProperty("analyses") List<MorphoAnalysis> analyses,
               @JsonProperty("extendedProperties") Map<String, Object> extendedProperties) {
        //
    }


}
