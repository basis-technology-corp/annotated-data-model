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

import com.basistech.rosette.dm.AnnotatedText;
import com.basistech.rosette.dm.ListAttribute;
import com.basistech.rosette.dm.MapAttribute;
import com.basistech.rosette.dm.SimilarTerm;
import com.basistech.rosette.dm.Token;
import com.basistech.rosette.dm.jackson.AnnotatedDataModelModule;
import com.basistech.util.LanguageCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class MapAttributeDeserializerTest extends AdmAssert {

    private ObjectMapper mapper;

    @Before
    public void before() throws Exception {
        mapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());
    }

    @Test
    public void isolatedOutOfOrder() throws Exception {
        /*
         * a map of tokens not in an AnnotatedText. There's no 'type' handling, just our
         * custom keyType/valueType to worry over.
         */
        MapAttribute<LanguageCode, Token> tokens = mapper.readValue(new File("test-data/disordered-map.json"), new TypeReference<MapAttribute<LanguageCode, Token>>() { });
        assertEquals(3, tokens.size());
        for (Object token : tokens.values()) {
            assertTrue(token instanceof Token);
        }
        assertEquals("The", tokens.get(LanguageCode.ENGLISH).getText());
        assertEquals("Boston", tokens.get(LanguageCode.SPANISH).getText());
        assertEquals("Red", tokens.get(LanguageCode.FRENCH).getText());
        assertEquals("val1", tokens.getExtendedProperties().get("ext1"));
        assertEquals("val2", tokens.getExtendedProperties().get("ext2"));
    }

    @Test
    public void annotatedTextOutOfOrder() throws Exception {
        final AnnotatedText text =
                mapper.readValue(new File("test-data/disordered-map-in-annotated-text.json"), AnnotatedText.class);
        final MapAttribute<LanguageCode, ListAttribute<SimilarTerm>> similarTerms = text.getSimilarTerms();
        assertEquals(1, similarTerms.size());
        for (final Object similarTermList : similarTerms.values()) {
            assertTrue(similarTermList instanceof List);
        }
        assertEquals(1, similarTerms.get(LanguageCode.ENGLISH).size());
        assertEquals("element", similarTerms.get(LanguageCode.ENGLISH).get(0).getTerm());
        assertEquals(0.8, similarTerms.get(LanguageCode.ENGLISH).get(0).getSimilarity(), 0.0001);
    }
}
