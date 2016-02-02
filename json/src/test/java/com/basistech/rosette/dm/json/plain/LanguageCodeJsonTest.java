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

import com.basistech.rosette.dm.LanguageDetection;
import com.basistech.util.ISO15924;
import com.basistech.util.LanguageCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

/**
 * Test that language codes get mapped to and from ISO-639-3.
 */
public class LanguageCodeJsonTest extends AdmAssert {
    @Test
    public void testRoundTrip() throws Exception {

        List<LanguageDetection.DetectionResult> detectionResults = Lists.newArrayList();
        LanguageDetection.DetectionResult detectionResult = new LanguageDetection.DetectionResult.Builder(LanguageCode.KOREAN)
                .encoding("uff-8").script(ISO15924.Hang).confidence(1.0).build();
        detectionResults.add(detectionResult);
        LanguageDetection.Builder builder = new LanguageDetection.Builder(0, 100, detectionResults);
        LanguageDetection languageDetection = builder.build();

        ObjectMapper mapper = objectMapper();
        String json = mapper.writeValueAsString(languageDetection);
        // now read back as a tree.
        JsonNode tree = mapper.readTree(json);
        JsonNode resultsNode = tree.get("detectionResults");
        ArrayNode resultArray = (ArrayNode) resultsNode;
        ObjectNode detectionNode = (ObjectNode) resultArray.get(0);
        assertEquals("kor", detectionNode.get("language").textValue());
        assertEquals("Hang", detectionNode.get("script").textValue());

        languageDetection = mapper.readValue(json, LanguageDetection.class);
        assertSame(LanguageCode.KOREAN, languageDetection.getDetectionResults().get(0).getLanguage());
    }
}
