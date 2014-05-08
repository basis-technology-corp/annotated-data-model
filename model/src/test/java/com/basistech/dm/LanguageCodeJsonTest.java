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

package com.basistech.dm;

import com.basistech.util.ISO15924;
import com.basistech.util.LanguageCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Test that language codes get mapped to and from ISO-639-3.
 */
public class LanguageCodeJsonTest extends Assert {
    @Test
    public void testRoundTrip() throws Exception {

        List<LanguageDetection.DetectionResult> detectionResults = Lists.newArrayList();
        LanguageDetection.DetectionResult detectionResult = new LanguageDetection.DetectionResult(LanguageCode.KOREAN,
            "uff-8", ISO15924.Hang, 1.0);
        detectionResults.add(detectionResult);
        LanguageDetection.Builder builder = new LanguageDetection.Builder(0, 100, detectionResults);
        LanguageDetection languageDetection = builder.build();

        ObjectMapper mapper = new ObjectMapper();
        byte[] bytes = mapper.writeValueAsBytes(languageDetection);
        // now read back as a tree.
        JsonNode tree = mapper.readTree(bytes);
        JsonNode resultsNode = tree.get("detectionResults");
        ArrayNode resultArray = (ArrayNode) resultsNode;
        ObjectNode detectionNode = (ObjectNode) resultArray.get(0);
        assertEquals("kor", detectionNode.get("language").textValue());
        assertEquals("Hang", detectionNode.get("script").textValue());

        languageDetection = mapper.readValue(bytes, LanguageDetection.class);
        assertSame(LanguageCode.KOREAN, languageDetection.getDetectionResults().get(0).getLanguage());
    }
}
