/*******************************************************************************
 * Copyright 2021 Basis Technology Corp.
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
 ******************************************************************************/

package com.basistech.rosette.dm.jackson;

import com.basistech.rosette.dm.EventRole;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public abstract class EventMentionMixin {
    @JsonCreator
    EventMentionMixin(
        @JsonProperty("startOffset") int startOffset,
        @JsonProperty("endOffset") int endOffset,
        @JsonProperty("extendedProperties") Map<String, Object> extendedProperties,
        @JsonProperty("roles") List<EventRole> roles,
        @JsonProperty("confidence") Double confidence
    ) {
        //
    }
}
