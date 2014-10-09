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

import java.util.List;
import java.util.Map;

/**
 * {@link CategorizerResult}
 */
public class CategorizerResultMixin {
    @JsonCreator
    CategorizerResultMixin(@JsonProperty("label") String label,
                           @JsonProperty("score") Double score,
                           @JsonProperty("confidence") Double confidence,
                           @JsonProperty("explanationSet") List<String> explanationSet,
                           @JsonProperty("perFeatureScores") Map<String, Double> perFeatureScores,
                           @JsonProperty("extendedProperties") Map<String, Object> extendedProperties) {

        // empty
    }
}
