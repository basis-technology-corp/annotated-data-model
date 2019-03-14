/*
* Copyright 2018 Basis Technology Corp.
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

package com.basistech.rosette.dm.json.plain;

import com.basistech.rosette.dm.MapAttribute;
import com.basistech.rosette.dm.Sentence;
import com.basistech.rosette.dm.jackson.AnnotatedDataModelModule;
import com.basistech.util.LanguageCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

public class MapAttributeTest extends AdmAssert {

    private ObjectMapper mapper;

    @Before
    public void before() throws Exception {
        mapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());
    }

    @Test
    public void map() throws Exception {
        MapAttribute.Builder<LanguageCode, Sentence> mapBuilder = new MapAttribute.Builder<>(LanguageCode.class, Sentence.class);
        mapBuilder.put(LanguageCode.ENGLISH, new Sentence.Builder(0, 10).build());
        mapBuilder.put(LanguageCode.SPANISH, new Sentence.Builder(10, 20).build());
        mapBuilder.extendedProperty("ek", "ev");
        MapAttribute<LanguageCode, Sentence> sentMap = mapBuilder.build();
        String json = mapper.writeValueAsString(sentMap);
        System.err.println(String.format("As string: %s", json));
        MapAttribute<LanguageCode, Sentence> readBack = mapper.readValue(json, new TypeReference<MapAttribute<LanguageCode, Sentence>>() { });
        // well, try round-trip.
        // this is the simple case, we'll also have to try it inside an AnnotatedText in another test case.
        assertEquals(sentMap, readBack);
    }
}
