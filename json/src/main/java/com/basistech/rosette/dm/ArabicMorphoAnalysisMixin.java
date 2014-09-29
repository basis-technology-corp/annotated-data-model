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
 * {@link com.basistech.rosette.dm.ArabicMorphoAnalysis}
 */
public abstract class ArabicMorphoAnalysisMixin {
    @JsonCreator
    ArabicMorphoAnalysisMixin(@JsonProperty("partOfSpeech") String partOfSpeech,
                              @JsonProperty("lemma") String lemma,
                              @JsonProperty("components") List<Token> components,
                              @JsonProperty("raw") String raw,
                              @JsonProperty("prefixLength") int prefixLength,
                              @JsonProperty("stemLength") int stemLength,
                              @JsonProperty("root") String root,
                              @JsonProperty("definiteArticle") boolean definiteArticle,
                              @JsonProperty("strippablePrefix") boolean strippablePrefix,
                              @JsonProperty("prefixes") List<String> prefixes,
                              @JsonProperty("stems") List<String> stems,
                              @JsonProperty("suffixes") List<String> suffixes,
                              @JsonProperty("prefixTags") List<String> prefixTags,
                              @JsonProperty("stemTags") List<String> stemTags,
                              @JsonProperty("suffixTags") List<String> suffixTags,
                              @JsonProperty("extendedProperties") Map<String, Object> extendedProperties) {
    }
}
