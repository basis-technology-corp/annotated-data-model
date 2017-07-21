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

import com.basistech.rosette.dm.Transliteration;
import com.basistech.util.LanguageCode;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Map;

public class TransliterationResultsSerializer extends JsonSerializer<Map<LanguageCode, Transliteration>> {
    @Override
    public void serialize(Map<LanguageCode, Transliteration> value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException, JsonProcessingException {
        gen.writeStartObject();
        for (LanguageCode key : value.keySet()) {
            gen.writeObjectField(key.ISO639_3(), value.get(key).getAll());
        }
        gen.writeEndObject();
    }
}
