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
