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

import com.basistech.rosette.dm.RelationshipComponent;
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
                             @JsonProperty("predicate") RelationshipComponent predicate,
                             @JsonProperty("arg1") RelationshipComponent arg1,
                             @JsonProperty("arg2") RelationshipComponent arg2,
                             @JsonProperty("arg3") RelationshipComponent arg3,
                             @JsonProperty("adjuncts") List<RelationshipComponent> adjuncts,
                             @JsonProperty("locatives") List<RelationshipComponent> locatives,
                             @JsonProperty("temporals") List<RelationshipComponent> temporals,
                             @JsonProperty("relationshipSource") String relationshipSource,
                             @JsonProperty("extendedProperties") Map<String, Object> extendedProperties) {

    }
}
