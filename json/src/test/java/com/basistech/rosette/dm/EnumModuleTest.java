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

package com.basistech.rosette.dm;

import com.basistech.util.ISO15924;
import com.basistech.util.LanguageCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the enum module.
 */
public class EnumModuleTest {
    private ObjectMapper mapper;

    @Before
    public void before() {
        mapper = EnumModule.setupObjectMapper(new ObjectMapper());
    }

    @Test
    public void languageCode() throws Exception {
        LanguageCode code = mapper.readValue("\"ara\"", LanguageCode.class);
        assertEquals(LanguageCode.ARABIC, code);
        assertEquals("\"ara\"", mapper.writeValueAsString(LanguageCode.ARABIC));
    }

    @Test
    public void languageCodeKey() throws Exception {
        Map<LanguageCode, String> map = Maps.newHashMap();
        map.put(LanguageCode.CHINESE, "dumpling");
        Map<LanguageCode, String> deser = mapper.readValue("{\"zho\": \"dumpling\"}", new TypeReference<Map<LanguageCode, String>>() {
        });
        assertEquals(map, deser);
        String json = mapper.writeValueAsString(map);
        assertTrue(json.contains("zho")); // and not, by implication, CHINESE.
    }

    @Test
    public void iso15924() throws Exception {
        ISO15924 iso = mapper.readValue("\"Latn\"", ISO15924.class);
        assertEquals(ISO15924.Latn, iso);
    }
}
