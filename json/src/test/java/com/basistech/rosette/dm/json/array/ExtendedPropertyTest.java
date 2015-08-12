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

package com.basistech.rosette.dm.json.array;

import com.basistech.rosette.dm.AnnotatedText;
import com.basistech.rosette.dm.ArabicMorphoAnalysis;
import com.basistech.rosette.dm.EntityMention;
import com.basistech.rosette.dm.HanMorphoAnalysis;
import com.basistech.rosette.dm.ListAttribute;
import com.basistech.rosette.dm.Token;
import com.basistech.rosette.dm.jackson.array.AnnotatedDataModelArrayModule;
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

        ObjectReader reader = mapper.reader(Token.class);
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

        ObjectMapper mapper = AnnotatedDataModelArrayModule.setupObjectMapper(new ObjectMapper());

        ObjectWriter objectWriter = mapper.writerWithDefaultPrettyPrinter();
        String json = objectWriter.writeValueAsString(text);
        System.out.println(json);
        AnnotatedText deserialized = mapper.readValue(json, AnnotatedText.class);
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

        ObjectMapper mapper = AnnotatedDataModelArrayModule.setupObjectMapper(new ObjectMapper());
        ObjectWriter objectWriter = mapper.writerWithDefaultPrettyPrinter();
        Writer sw = new StringWriter();
        objectWriter.writeValue(sw, text);
        ObjectReader reader = mapper.reader(AnnotatedText.class);
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
        ObjectMapper mapper = AnnotatedDataModelArrayModule.setupObjectMapper(new ObjectMapper());
        ObjectWriter objectWriter = mapper.writerWithDefaultPrettyPrinter();
        Writer sw = new StringWriter();
        objectWriter.writeValue(sw, tokBuilder.build());
        ObjectReader reader = mapper.reader(Token.class);
        Token deserialized = reader.readValue(sw.toString());
        HanMorphoAnalysis ma1 = (HanMorphoAnalysis)deserialized.getAnalyses().get(0);
        assertEquals("ink", ma1.getExtendedProperties().get("spill"));
        ArabicMorphoAnalysis ma2 = (ArabicMorphoAnalysis)deserialized.getAnalyses().get(1);
        assertEquals("apples", ma2.getExtendedProperties().get("some"));
    }
}
