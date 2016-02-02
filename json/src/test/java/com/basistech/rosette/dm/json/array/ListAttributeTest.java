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
package com.basistech.rosette.dm.json.array;

import com.basistech.rosette.dm.AnnotatedText;
import com.basistech.rosette.dm.LanguageDetection;
import com.basistech.rosette.dm.ListAttribute;
import com.basistech.rosette.dm.Sentence;
import com.basistech.util.ISO15924;
import com.basistech.util.LanguageCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

/**
 * Test serialization a-la-array.
 */
public class ListAttributeTest extends AdmAssert {
    @Test
    public void list() throws Exception {
        ListAttribute.Builder<Sentence> listBuilder = new ListAttribute.Builder<>(Sentence.class);
        listBuilder.add(new Sentence.Builder(0, 10).build());
        listBuilder.add(new Sentence.Builder(10, 20).build());
        listBuilder.extendedProperty("ek", "ev");
        ListAttribute<Sentence> sentList = listBuilder.build();
        String json = objectMapper().writeValueAsString(sentList);
        ListAttribute<Sentence> readBack = objectMapper().readValue(json, new TypeReference<ListAttribute<Sentence>>() { });
        // well, try round-trip.
        // this is the simple case, we'll also have to try it inside an AnnotatedText in another test case.
        assertEquals(sentList, readBack);
    }

    @Test
    public void inContext() throws Exception {
        ListAttribute.Builder<Sentence> listBuilder = new ListAttribute.Builder<>(Sentence.class);
        listBuilder.add(new Sentence.Builder(0, 10).build());
        listBuilder.add(new Sentence.Builder(10, 20).build());
        listBuilder.extendedProperty("ek", "ev");
        ListAttribute<Sentence> sentList = listBuilder.build();
        AnnotatedText.Builder atBuilder = new AnnotatedText.Builder();
        atBuilder.data("Some Text");
        atBuilder.sentences(sentList);
        AnnotatedText annotatedText = atBuilder.build();
        String json = objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(annotatedText);
        AnnotatedText readBack = objectMapper().readValue(json, AnnotatedText.class);
        // we don't have an equals() that is useful for AnnotatedText
        assertEquals(annotatedText.getData(), readBack.getData());
        assertEquals(annotatedText.getAttributes(), readBack.getAttributes());
    }

    @Test
    public void languageDetectionInText() throws Exception {
        AnnotatedText.Builder builder = new AnnotatedText.Builder();
        builder.data("Some Text");

        List<LanguageDetection.DetectionResult> dets;
        LanguageDetection.Builder ldBuilder;

        dets = Lists.newArrayList();
        dets.add(new LanguageDetection.DetectionResult.Builder(LanguageCode.FRENCH).encoding("utf-8").script(ISO15924.Latn).confidence(1.0).build());
        ldBuilder = new LanguageDetection.Builder(0, builder.data().length(), dets);
        ldBuilder.extendedProperty("ldw-ex", "ldw-ex-val");
        LanguageDetection languageDetection = ldBuilder.build();
        builder.wholeDocumentLanguageDetection(languageDetection);

        AnnotatedText annotatedText = builder.build();

        String json = objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(annotatedText);
        AnnotatedText readBack = objectMapper().readValue(json, AnnotatedText.class);
        // we don't have an equals() that is useful for AnnotatedText
        assertEquals(annotatedText.getData(), readBack.getData());
        assertEquals(annotatedText.getAttributes(), readBack.getAttributes());

    }

    @Test
    public void languageDetection() throws Exception {
        List<LanguageDetection.DetectionResult> dets;
        LanguageDetection.Builder ldBuilder;

        dets = Lists.newArrayList();
        dets.add(new LanguageDetection.DetectionResult.Builder(LanguageCode.FRENCH).encoding("utf-8").script(ISO15924.Latn).confidence(1.0).build());
        ldBuilder = new LanguageDetection.Builder(0, 100, dets);
        ldBuilder.extendedProperty("ldw-ex", "ldw-ex-val");
        LanguageDetection languageDetection = ldBuilder.build();
        String json = objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(languageDetection);
        LanguageDetection readBack = objectMapper().readValue(json, LanguageDetection.class);
        assertEquals(languageDetection, readBack);
    }
}
