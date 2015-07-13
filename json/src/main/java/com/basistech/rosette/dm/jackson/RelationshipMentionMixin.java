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

package com.basistech.rosette.dm.jackson;

import com.basistech.rosette.dm.Evidence;
import com.basistech.rosette.dm.RelationshipArgument;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * {@link com.basistech.rosette.dm.RelationshipMention}
 */
public abstract class RelationshipMentionMixin {

    @JsonCreator
    RelationshipMentionMixin(@JsonProperty("startOffset") int startOffset,
                             @JsonProperty("endOffset") int endOffset,
                             @JsonProperty("predPhrase") String predPhrase,
                             @JsonProperty("evidences") List<Evidence> evidences,
                             @JsonProperty("arg1") RelationshipArgument arg1,
                             @JsonProperty("arg2") RelationshipArgument arg2,
                             @JsonProperty("arg3") RelationshipArgument arg3,
                             @JsonProperty("adjuncts") List<RelationshipArgument> adjuncts,
                             @JsonProperty("locatives") List<RelationshipArgument> locatives,
                             @JsonProperty("temporals") List<RelationshipArgument> temporals,
                             @JsonProperty("relationshipSource") String relationshipSource,
                             @JsonProperty("relId") String relId,
                             @JsonProperty("extendedProperties") Map<String, Object> extendedProperties) {

    }
}
