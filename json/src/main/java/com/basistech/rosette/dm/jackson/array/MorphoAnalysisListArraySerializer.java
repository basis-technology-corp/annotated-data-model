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
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;

/**
 * array-based scheme to deal with morpho analysis polymorphism
 */
public class MorphoAnalysisListArraySerializer extends JsonSerializer<List<MorphoAnalysis>> {
    @Override
    public void serialize(List<MorphoAnalysis> value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartArray();
        MorphoAnalysisTypes curType = MorphoAnalysisTypes.PLAIN;
        for (MorphoAnalysis ma : value) {
            MorphoAnalysisTypes type = MorphoAnalysisTypes.byClass(ma.getClass());
            if (curType != type) {
                jgen.writeNumber(type.ordinal());
                curType = type;
            }
            jgen.writeObject(ma);
        }
        jgen.writeEndArray();
    }
}
