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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

/**
 * Tests of the complex code that speeds up morpho analysis deserialization.
 */
public class MorphoListTest extends AdmAssert {

    @Test
    public void oneMorphoAnalysis() throws Exception {
        MorphoAnalysis ma = new MorphoAnalysis.Builder().lemma("plainLemma").build();
        String json = objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(ma);
        System.out.println(json);
        MorphoAnalysis readBack = objectMapper().readValue(json, MorphoAnalysis.class);
        assertEquals(ma, readBack);

        // an uncustomized itemList ...
        List<MorphoAnalysis> list = Lists.newArrayList(ma);
        json = objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(list);
        System.out.println(json);
        List<MorphoAnalysis> readBackList = objectMapper().readValue(json, new TypeReference<List<MorphoAnalysis>>() {});
        assertEquals(list, readBackList);
    }

    @Test
    public void morphoAnalysisList() throws Exception {
        Token.Builder tokenBuilder = new Token.Builder(0, 10, "Hello");
        final MorphoAnalysis.Builder plainMaBuilder = new MorphoAnalysis.Builder().lemma("plainLemma");
        plainMaBuilder.extendedProperty("plain", "simple");
        tokenBuilder.addAnalysis(plainMaBuilder.build());
        tokenBuilder.addAnalysis(new HanMorphoAnalysis.Builder().addReading("areading").lemma("han").build());
        tokenBuilder.addAnalysis(new ArabicMorphoAnalysis.Builder().addPrefix("prefix", "ptype").lemma("arabic").build());
        tokenBuilder.addAnalysis(new MorphoAnalysis.Builder().lemma("plainLemma2").build());
        tokenBuilder.addAnalysis(new KoreanMorphoAnalysis.Builder().addMorpheme("mor", "pheme").lemma("korean").build());
        Token token = tokenBuilder.build();
        String json = objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(token);
        Token readBack = objectMapper().readValue(json, Token.class);
        assertEquals(token, readBack);
    }
}
