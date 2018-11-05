/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2018 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/
package com.basistech.rosette.dm.json.plain;

import com.basistech.rosette.dm.MapAttribute;
import com.basistech.rosette.dm.Token;
import com.basistech.rosette.dm.jackson.AnnotatedDataModelModule;
import com.basistech.util.LanguageCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

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
}
