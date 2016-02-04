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

import com.basistech.rosette.dm.AnnotatedText;
import com.basistech.rosette.dm.ArabicMorphoAnalysis;
import com.basistech.rosette.dm.EntityMention;
import com.basistech.rosette.dm.HanMorphoAnalysis;
import com.basistech.rosette.dm.ListAttribute;
import com.basistech.rosette.dm.Token;
import com.basistech.rosette.dm.jackson.AnnotatedDataModelModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.io.Writer;

/**
 * See if the extension mechanism works round-trip.
 * This does not require a full AnnotatedText object.
 */
public class ExtendedPropertyTest extends AdmAssert {
    @Test
    public void textExtendedRoundTrip() throws Exception {
        Token.Builder builder = new Token.Builder(0, 5, "abcdefg");
        builder.extendedProperty("veloci", "raptor");
        Token token = builder.build();
        ObjectMapper mapper = objectMapper();
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        ByteArrayOutputStream jsonBlob = new ByteArrayOutputStream();
        writer.writeValue(jsonBlob, token);

        ObjectReader reader = mapper.readerFor(Token.class);
        // just see if we get an exception for now.
        Token token2 = reader.readValue(jsonBlob.toByteArray());
        assertEquals("abcdefg", token2.getText());
        assertEquals(0, token2.getStartOffset());
        assertEquals(5, token2.getEndOffset());
        assertEquals("raptor", token2.getExtendedProperties().get("veloci"));
    }

    @Test
    public void testExtendedPropertyOnAttribute() throws Exception {
        //                012345678901234567890
        String rawText = "Cuthbert Girdlestone";
        AnnotatedText.Builder builder = new AnnotatedText.Builder().data(rawText);
        ListAttribute.Builder<EntityMention> emListBuilder = new ListAttribute.Builder<>(EntityMention.class);
        EntityMention.Builder emBuilder = new EntityMention.Builder(0, 20, "PERSON");
        emBuilder.extendedProperty("extra_key", "extra_value");
        emListBuilder.add(emBuilder.build());
        builder.entityMentions(emListBuilder.build());
        AnnotatedText text = builder.build();

        ObjectMapper mapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());
        ObjectWriter objectWriter = mapper.writerWithDefaultPrettyPrinter();
        Writer sw = new StringWriter();
        objectWriter.writeValue(sw, text);
        ObjectReader reader = mapper.readerFor(AnnotatedText.class);
        AnnotatedText deserialized = reader.readValue(sw.toString());
        assertEquals("extra_value", deserialized.getEntityMentions().get(0).getExtendedProperties().get("extra_key"));
    }

    @Test
    public void testExtendedPropertyOnListAttribute() throws Exception {
        //                012345678901234567890
        String rawText = "Cuthbert Girdlestone";
        AnnotatedText.Builder builder = new AnnotatedText.Builder().data(rawText);
        ListAttribute.Builder<EntityMention> emListBuilder = new ListAttribute.Builder<>(EntityMention.class);
        EntityMention.Builder emBuilder = new EntityMention.Builder(0, 20, "PERSON");
        emListBuilder.extendedProperty("extra_key", "extra_value");
        emListBuilder.add(emBuilder.build());
        builder.entityMentions(emListBuilder.build());
        AnnotatedText text = builder.build();

        ObjectMapper mapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());
        ObjectWriter objectWriter = mapper.writerWithDefaultPrettyPrinter();
        Writer sw = new StringWriter();
        objectWriter.writeValue(sw, text);
        ObjectReader reader = mapper.readerFor(AnnotatedText.class);
        AnnotatedText deserialized = reader.readValue(sw.toString());
        assertEquals("extra_value", deserialized.getEntityMentions().getExtendedProperties().get("extra_key"));
    }

    @Test
    public void morphoAnalysisListExtProps() throws Exception {
        Token.Builder tokBuilder = new Token.Builder(0, 0, "nothing");
        HanMorphoAnalysis.Builder hmaBuilder = new HanMorphoAnalysis.Builder();
        hmaBuilder.addReading("Proust")
                .extendedProperty("spill", "ink");
        tokBuilder.addAnalysis(hmaBuilder.build());
        ArabicMorphoAnalysis.Builder armaBuilder = new ArabicMorphoAnalysis.Builder();
        armaBuilder.definiteArticle(true)
                .extendedProperty("some", "apples");
        tokBuilder.addAnalysis(armaBuilder.build());
        ObjectMapper mapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());
        ObjectWriter objectWriter = mapper.writerWithDefaultPrettyPrinter();
        Writer sw = new StringWriter();
        objectWriter.writeValue(sw, tokBuilder.build());
        ObjectReader reader = mapper.readerFor(Token.class);
        Token deserialized = reader.readValue(sw.toString());
        HanMorphoAnalysis ma1 = (HanMorphoAnalysis)deserialized.getAnalyses().get(0);
        assertEquals("ink", ma1.getExtendedProperties().get("spill"));
        ArabicMorphoAnalysis ma2 = (ArabicMorphoAnalysis)deserialized.getAnalyses().get(1);
        assertEquals("apples", ma2.getExtendedProperties().get("some"));
    }
}
