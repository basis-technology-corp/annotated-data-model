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

package com.basistech.rosette.dm;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Getter
public class Event extends BaseAttribute {
    private final String eventType;
    private List<EventMention> mentions;
    private final Double confidence;

    Event(String eventType, List<EventMention> mentions, Double confidence, Map<String, Object> extendedProperties) {
        super(extendedProperties);
        this.eventType = eventType;
        this.mentions = mentions;
        this.confidence = confidence;
    }
}
