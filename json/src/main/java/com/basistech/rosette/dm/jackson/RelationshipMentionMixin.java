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

import com.basistech.rosette.dm.RelationshipComponent;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Set;

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
                             @JsonProperty("adjuncts") Set<RelationshipComponent> adjuncts,
                             @JsonProperty("locatives") Set<RelationshipComponent> locatives,
                             @JsonProperty("temporals") Set<RelationshipComponent> temporals,
                             @JsonProperty("source") String source,
                             @JsonProperty("confidence") Double confidence,
                             @JsonProperty("extendedProperties") Map<String, Object> extendedProperties) {

    }
}
