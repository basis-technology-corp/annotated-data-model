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

package com.basistech.rosette.dm.jackson.array;

import com.basistech.rosette.dm.MorphoAnalysis;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.List;

/**
 * read [n, {}, m, {}, ... ] for n, m ordinals from MorphoAnalysisTypes.
 */
public final class MorphoAnalysisListArrayDeserializer extends JsonDeserializer<List<MorphoAnalysis>> {

    @Override
    public List<MorphoAnalysis> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        if (jp.getCurrentToken() != JsonToken.START_ARRAY) {
            throw ctxt.wrongTokenException(jp, (JavaType) null, JsonToken.START_ARRAY, "Expected array of items");
        }
        List<MorphoAnalysis> results = Lists.newArrayList();
        MorphoAnalysisTypes type = MorphoAnalysisTypes.PLAIN;
        while (jp.nextToken() != JsonToken.END_ARRAY) {
            if (jp.getCurrentToken() == JsonToken.VALUE_NUMBER_INT) {
                type = MorphoAnalysisTypes.byOrdinal(jp.getIntValue());
                jp.nextToken();
            }
            results.add(jp.readValueAs(type.getMorphoAnalysisClass()));
        }
        return ImmutableList.copyOf(results);
    }
}
