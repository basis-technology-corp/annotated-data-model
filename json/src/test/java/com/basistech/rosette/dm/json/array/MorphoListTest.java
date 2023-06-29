/*
* Copyright 2023 Basis Technology Corp.
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
package com.basistech.rosette.dm.json.array;

import com.basistech.rosette.dm.ArabicMorphoAnalysis;
import com.basistech.rosette.dm.HanMorphoAnalysis;
import com.basistech.rosette.dm.KoreanMorphoAnalysis;
import com.basistech.rosette.dm.MorphoAnalysis;
import com.basistech.rosette.dm.Token;
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
        MorphoAnalysis ma = new MorphoAnalysis.Builder().lemma("plainLemma").secondaryPartOfSpeech("2pos").build();
        String json = objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(ma);
        System.out.println(json);
        MorphoAnalysis readBack = objectMapper().readValue(json, MorphoAnalysis.class);
        assertEquals(ma, readBack);

        // an uncustomized itemList ...
        List<MorphoAnalysis> list = Lists.newArrayList(ma);
        json = objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(list);
        System.out.println(json);
        List<MorphoAnalysis> readBackList = objectMapper().readValue(json, new TypeReference<List<MorphoAnalysis>>() { });
        assertEquals(list, readBackList);
    }

    @Test
    public void morphoAnalysisList() throws Exception {
        Token.Builder tokenBuilder = new Token.Builder(0, 10, "Hello");
        final MorphoAnalysis.Builder plainMaBuilder = new MorphoAnalysis.Builder().lemma("plainLemma");
        plainMaBuilder.secondaryPartOfSpeech("2pos");
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
