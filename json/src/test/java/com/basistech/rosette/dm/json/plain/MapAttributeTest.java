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
