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

package com.basistech.rosette.dm.json.plain;

import com.basistech.rosette.dm.jackson.EnumModule;
import com.basistech.util.ISO15924;
import com.basistech.util.LanguageCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URL;
import java.util.Map;
import java.util.Set;

import static junit.framework.TestCase.assertNotNull;
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
    public void defaultLanguageCodeMapper() throws Exception {
        // The default ObjectMapper does traffic in the enum constants:
        // LanguageCode.ARABIC.name()  --> "ARABIC"
        ObjectMapper myMapper = new ObjectMapper();
        LanguageCode langCode = myMapper.readValue("\"ARABIC\"", LanguageCode.class);
        assertEquals(LanguageCode.ARABIC, langCode);
        assertEquals("\"ARABIC\"", myMapper.writeValueAsString(LanguageCode.ARABIC));
    }

    @Test
    public void languageCode() throws Exception {
        // The EnumModule wires up the special LanguageCodeSerializer
        // and LanguageCodeDeserializer, which let use the ISO636_3 string
        // instead of the string of the enum constant.
        LanguageCode code = mapper.readValue("\"ara\"", LanguageCode.class);
        assertEquals(LanguageCode.ARABIC, code);
        assertEquals("\"ara\"", mapper.writeValueAsString(LanguageCode.ARABIC));
    }

    @Test
    public void languageCodeKey() throws Exception {
        Map<LanguageCode, String> map = Maps.newHashMap();
        map.put(LanguageCode.CHINESE, "dumpling");
        Map<LanguageCode, String> deser = mapper.readValue("{\"zho\": \"dumpling\"}", new TypeReference<Map<LanguageCode, String>>() { });
        assertEquals(map, deser);
        // Note: Jackson assumes that maps have homogeneous key types and does not notice the serializer without this extra level of spec.
        String json = mapper.writerFor(new TypeReference<Map<LanguageCode, String>>() { }).writeValueAsString(map);
        assertTrue(json.contains("zho")); // and not, by implication, CHINESE.
    }

    @Test
    public void iso15924() throws Exception {
        ISO15924 iso = mapper.readValue("\"Latn\"", ISO15924.class);
        assertEquals(ISO15924.Latn, iso);
    }

    @Ignore("jackson-databind issue 749")
    @Test
    public void troubleWithKeys() throws Exception {
        URL dataRes = Resources.getResource(EnumModuleTest.class, "enum-module-map.json");
        ObjectMapper plainObjectMapper = new ObjectMapper();
        JsonNode tree = plainObjectMapper.readTree(dataRes);
        ObjectMapper fancyObjectMapper = EnumModule.setupObjectMapper(new ObjectMapper());
        // this line is might throw with Jackson 2.6.2.
        Map<LanguageCode, Set<String>> map = fancyObjectMapper.convertValue(tree, new TypeReference<Map<LanguageCode, Set<String>>>() {
        });
        assertNotNull(map);
    }


    public enum TestEnum {
        replacements;
    }

    public static class TestBean {
        private Map<LanguageCode, Map<String, String>> replacements;

        public Map<LanguageCode, Map<String, String>> getReplacements() {
            return replacements;
        }

        public void setReplacements(Map<LanguageCode, Map<String, String>> replacements) {
            this.replacements = replacements;
        }
    }


    /* Note the use of an EnumMap in here to work around a Jackson 2.6.2 issue.
     * We could do better with a complex customer serializer for Map<Object, Object>
     * in the adm project. */
    @Test
    public void nestedMaps() throws Exception {
        Map<LanguageCode, Map<String, String>> replacements = Maps.newEnumMap(LanguageCode.class);
        Map<String, String> engRepl = Maps.newHashMap();
        engRepl.put("1", "one");
        engRepl.put("2", "two");
        replacements.put(LanguageCode.ENGLISH, engRepl);
        Map<String, String> fraRepl = Maps.newHashMap();
        fraRepl.put("1", "un");
        fraRepl.put("2", "deux");
        replacements.put(LanguageCode.FRENCH, fraRepl);
        Map<TestEnum, Object> factoryConfigMap = Maps.newHashMap();
        factoryConfigMap.put(TestEnum.replacements, replacements);
        ObjectMapper fancyObjectMapper = EnumModule.setupObjectMapper(new ObjectMapper());
        JsonNode tree = fancyObjectMapper.valueToTree(factoryConfigMap);
        assertNotNull(tree);
        TestBean bean = fancyObjectMapper.convertValue(tree, new TypeReference<TestBean>() {});
        assertEquals("one", bean.getReplacements().get(LanguageCode.ENGLISH).get("1"));
    }
}
