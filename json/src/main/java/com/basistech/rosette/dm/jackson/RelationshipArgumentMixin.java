/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2015 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/

package com.basistech.rosette.dm.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * {@link com.basistech.rosette.dm.RelationshipArgument}
 */
public abstract class RelationshipArgumentMixin {

    @JsonCreator
    public RelationshipArgumentMixin(@JsonProperty("startOffset") int startOffset,
                                     @JsonProperty("endOffset") int endOffset,
                                     @JsonProperty("type") String type,
                                     @JsonProperty("argumentId") String argumentId,
                                     @JsonProperty("extendedProperties") Map<String, Object> extendedProperties) {

    }


}
