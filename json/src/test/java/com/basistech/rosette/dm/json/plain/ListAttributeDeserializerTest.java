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

package com.basistech.rosette.dm.json.plain;

import com.basistech.rosette.dm.AnnotatedText;
import com.basistech.rosette.dm.ListAttribute;
import com.basistech.rosette.dm.Token;
import com.basistech.rosette.dm.jackson.AnnotatedDataModelModule;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * Test handling of the polymorphism customization in there.
 */
public class ListAttributeDeserializerTest extends AdmAssert {

    private ObjectMapper mapper;

    @Before
    public void before() throws Exception {
        mapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());
    }

    @Test
    public void isolatedInOrder() throws Exception {
        /*
         * a list of tokens not in an AnnotatedText. There's no 'type' handling, just our
         * custom itemType to worry over.
         */
        ListAttribute<Token> tokens = mapper.readValue(new File("test-data/ordered-list.json"), new TypeReference<ListAttribute<Token>>() { });
        assertEquals(3, tokens.size());
        for (Object token : tokens) {
            assertTrue(token instanceof Token);
        }
        assertEquals(Lists.newArrayList("val1"), tokens.getExtendedProperties().get("ext1"));
        assertEquals("val2", tokens.getExtendedProperties().get("ext2"));
    }

    @Test
    public void annotatedTextInOrder() throws Exception {
        /*
         * a list of tokens not in an AnnotatedText.
         */
        AnnotatedText text = mapper.readValue(new File("test-data/ordered-list-in-annotated-text.json"), AnnotatedText.class);
        ListAttribute<Token> tokens = text.getTokens();
        assertEquals(3, tokens.size());
        for (Object token : tokens) {
            assertTrue(token instanceof Token);
        }
        assertEquals("val1", tokens.getExtendedProperties().get("ext1"));
        assertEquals("val2", tokens.getExtendedProperties().get("ext2"));
    }

    @Test
    public void isolatedOutOfOrder() throws Exception {
        /*
         * a list of tokens not in an AnnotatedText. There's no 'type' handling, just our
         * custom itemType to worry over.
         */
        ListAttribute<Token> tokens = mapper.readValue(new File("test-data/disordered-list.json"), new TypeReference<ListAttribute<Token>>() { });
        assertEquals(3, tokens.size());
        for (Object token : tokens) {
            assertTrue(token instanceof Token);
        }
        assertEquals("val1", tokens.getExtendedProperties().get("ext1"));
        assertEquals("val2", tokens.getExtendedProperties().get("ext2"));
    }

    @Test
    public void annotatedTextOutOfOrder() throws Exception {
        AnnotatedText text = mapper.readValue(new File("test-data/disordered-list-in-annotated-text.json"), AnnotatedText.class);
        ListAttribute<Token> tokens = text.getTokens();
        assertEquals(3, tokens.size());
        for (Object token : tokens) {
            assertTrue(token instanceof Token);
        }
        assertEquals("ext-val1", tokens.getExtendedProperties().get("ext1"));
        assertEquals("ext-val2", tokens.getExtendedProperties().get("ext2"));

    }
}
