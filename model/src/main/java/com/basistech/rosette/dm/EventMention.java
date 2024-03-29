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
import java.util.ArrayList;

@EqualsAndHashCode(callSuper = true)
@Getter
public class EventMention extends Attribute {
    private final List<EventRole> roles;
    private final Double confidence;
    private final String polarity;
    private final List<NegationCue> negationCues;

    EventMention(int startOffset, int endOffset, Map<String, Object> extendedProperties, List<EventRole> roles, Double confidence) {
        super(startOffset, endOffset, extendedProperties);
        this.roles = roles;
        this.confidence = confidence;
        this.polarity = "";
        this.negationCues = new ArrayList<>();
    }

    EventMention(int startOffset, int endOffset, Map<String, Object> extendedProperties, List<EventRole> roles, Double confidence,
                 String polarity, List<NegationCue> negationCues) {
        super(startOffset, endOffset, extendedProperties);
        this.roles = roles;
        this.confidence = confidence;
        this.polarity = polarity;
        this.negationCues = negationCues;
    }
}
