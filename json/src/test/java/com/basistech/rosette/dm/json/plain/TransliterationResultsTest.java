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

import com.basistech.rosette.dm.Transliteration;
import com.basistech.rosette.dm.TransliterationResults;
import com.basistech.rosette.dm.jackson.AnnotatedDataModelModule;
import com.basistech.util.LanguageCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TransliterationResultsTest {

    private static final ObjectMapper OBJ_MAP = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());

    private TransliterationResults throughString(TransliterationResults t) throws IOException {
        return OBJ_MAP.readValue(OBJ_MAP.writeValueAsString(t), TransliterationResults.class);
    }

    private TransliterationResults.Builder builder() {
        return new TransliterationResults.Builder();
    }

    @Test
    public void testEmpty() throws IOException {
        TransliterationResults t = builder().build();
        assertEquals(t, throughString(t));
    }

    @Test
    public void testClear() throws IOException {
        TransliterationResults empty = builder().build();
        TransliterationResults hopefullyEmpty = builder()
                .addTransliteration(LanguageCode.ARABIC, Transliteration.of("Latn", "Some stuff"))
                .clearResults()
                .build();
        assertEquals(empty, throughString(hopefullyEmpty));
    }

    @Test
    public void testTwoInOne() throws IOException {
        String one = "some stuff";
        String two = "other stuff";
        TransliterationResults t = builder()
                .addTransliteration(LanguageCode.ARABIC, Transliteration.of("Latn", one))
                .addTransliteration(LanguageCode.ARABIC, Transliteration.of("Arab", two))
                .build();
        assertEquals(t, throughString(t));

        assertEquals(ImmutableSet.of(LanguageCode.ARABIC), t.getResults().keySet());

        assertEquals(ImmutableSet.of("Latn", "Arab"),
                t.getTransliteration(LanguageCode.ARABIC).listScripts());

        assertEquals(one, t.getTransliterationInScript(LanguageCode.ARABIC, "Latn"));
        assertEquals(two, t.getTransliterationInScript(LanguageCode.ARABIC, "Arab"));

    }

    @Test
    public void testTwoLanguages() throws IOException {
        String one = "some stuff";
        String two = "other stuff";
        TransliterationResults t = builder()
                .addTransliteration(LanguageCode.ARABIC, Transliteration.of("Latn", one))
                .addTransliteration(LanguageCode.AFRIKAANS, Transliteration.of("Arab", two))
                .build();
        assertEquals(t, throughString(t));

        assertEquals(ImmutableSet.of(LanguageCode.ARABIC, LanguageCode.AFRIKAANS), t.getResults().keySet());

        assertEquals(ImmutableSet.of("Latn"), t.getTransliteration(LanguageCode.ARABIC).listScripts());

        assertEquals(ImmutableSet.of("Arab"),
                t.getTransliteration(LanguageCode.AFRIKAANS).listScripts());


        assertEquals(one, t.getTransliterationInScript(LanguageCode.ARABIC, "Latn"));
        assertEquals(two, t.getTransliterationInScript(LanguageCode.AFRIKAANS, "Arab"));

    }

}
