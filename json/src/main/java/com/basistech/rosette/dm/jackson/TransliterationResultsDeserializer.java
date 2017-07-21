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
import com.basistech.util.ISO15924;
import com.basistech.util.LanguageCode;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TransliterationResultsDeserializer extends JsonDeserializer<Map<LanguageCode, Transliteration>> {
    private static final TypeReference<Map<String, Map<ISO15924, String>>> REF =
            new TypeReference<Map<String, Map<ISO15924, String>>>() { };
    @Override
    public Map<LanguageCode, Transliteration> deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        Map<String, Map<ISO15924, String>> val = p.readValueAs(REF);
        Map<LanguageCode, Transliteration> output = new HashMap<>();
        for (String key : val.keySet()) {
            Map<ISO15924, String> forLang = val.get(key);
            Transliteration.Builder b = new Transliteration.Builder();
            b.transliterations(forLang);
            output.put(LanguageCode.lookupByISO639(key), b.build());
        }

        return output;
    }
}
