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

package com.basistech.rosette.dm.jackson.array;

import com.basistech.rosette.dm.MorphoAnalysis;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
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
            throw ctxt.wrongTokenException(jp, JsonToken.START_ARRAY, "Expected array of items");
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
