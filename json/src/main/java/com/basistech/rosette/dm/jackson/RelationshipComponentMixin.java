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

import com.basistech.rosette.dm.Extent;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * {@link com.basistech.rosette.dm.RelationshipComponent}
 */
public abstract class RelationshipComponentMixin {

    @JsonCreator
    public RelationshipComponentMixin(@JsonProperty("phrase") String phrase,
                                      @JsonProperty("extents") List<Extent> extents,
                                      @JsonProperty("identifier") String resolutionId,
                                      @JsonProperty("extendedProperties") Map<String, Object> extendedProperties) {

    }


}
