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

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Getter
public class EventRole extends Attribute {
    private final String name;
    private final String id;
    private final Double confidence;

    public EventRole(int startOffset, int endOffset, Map<String, Object> extendedProperties, String name, String id, Double confidence) {
        super(startOffset, endOffset, extendedProperties);
        this.name = name;
        this.id = id;
        this.confidence = confidence;
    }
}
