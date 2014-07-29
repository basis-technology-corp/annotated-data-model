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

package com.basistech.rosette.dm.tools;

import com.basistech.rlp.AbstractResultAccess;
import com.basistech.rlp.ResultAccessDeserializer;
import com.basistech.rlp.ResultAccessSerializedFormat;
import com.basistech.rosette.dm.AnnotatedDataModelModule;
import com.basistech.rosette.dm.AnnotatedText;
import com.basistech.rosette.dm.LanguageDetection;
import com.basistech.util.ISO15924;
import com.basistech.util.LanguageCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.base.Charsets;
import com.google.common.io.Closeables;
import com.google.common.io.Resources;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Here's some testing using an ARA as in input.
 */
public class AdmConversionTest extends Assert {

    private ObjectMapper objectMapper;

    @Before
    public void before() {
        objectMapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());
    }

    private AbstractResultAccess deserialize(File file) throws IOException {
        ResultAccessDeserializer deserializer = new ResultAccessDeserializer();
        deserializer.setFormat(ResultAccessSerializedFormat.JSON);
        InputStream in = new FileInputStream(file);
        AbstractResultAccess results;
        try {
            results = deserializer.deserializeAbstractResultAccess(in);
        } finally {
            Closeables.close(in, true);
        }
        return results;
    }

    private AbstractResultAccess deserialize(String s) throws IOException {
        ResultAccessDeserializer deserializer = new ResultAccessDeserializer();
        deserializer.setFormat(ResultAccessSerializedFormat.JSON);
        return deserializer.deserializeAbstractResultAccess(new ByteArrayInputStream(s.getBytes(Charsets.UTF_8)));
    }

    @Test
    public void sentenceEndOffset() throws Exception {
        URL tcUrl = Resources.getResource(getClass(), "token_offset_crash.json");
        String tcJson = Resources.toString(tcUrl, Charsets.UTF_8);
        AbstractResultAccess tc = deserialize(tcJson);
        AnnotatedText text = AraDmConverter.convert(tc);
        assertEquals(12, text.getSentences().get(0).getEndOffset());
    }

    @Test
    public void testEngRoundTrip() throws Exception {
        AbstractResultAccess ara = deserialize(new File("../model/data/eng-ara.json"));
        AnnotatedText text = AraDmConverter.convert(ara);

        ByteArrayOutputStream jsonContainer = new ByteArrayOutputStream();

        ObjectMapper mapper = objectMapper;
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        writer.writeValue(jsonContainer, text);

        //System.out.println(jsonContainer.toString("utf-8"));

        ObjectReader reader = mapper.reader(AnnotatedText.class);
        AnnotatedText deserializedText = reader.readValue(jsonContainer.toByteArray());

        assertEquals(text.length(), deserializedText.length());
        assertEquals(text.getData(), deserializedText.getData());
        // we need about 1000 additional assertions in here.
    }

    @Test
    public void testAraRoundTrip() throws Exception {
        AbstractResultAccess ara = deserialize(new File("../model/data/ara-ara.json"));
        AnnotatedText text = AraDmConverter.convert(ara);

        ByteArrayOutputStream jsonContainer = new ByteArrayOutputStream();

        ObjectMapper mapper = objectMapper;
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        writer.writeValue(jsonContainer, text);

        //System.out.println(jsonContainer.toString("utf-8"));

        ObjectReader reader = mapper.reader(AnnotatedText.class);
        AnnotatedText deserializedText = reader.readValue(jsonContainer.toByteArray());

        assertEquals(text.length(), deserializedText.length());
        assertEquals(text.getData(), deserializedText.getData());
    }

    @Test
    public void testJpnRoundTrip() throws Exception {
        AbstractResultAccess ara = deserialize(new File("../model/data/jpn-ara.json"));

        AnnotatedText text = AraDmConverter.convert(ara);

        ByteArrayOutputStream jsonContainer = new ByteArrayOutputStream();

        ObjectMapper mapper = objectMapper;
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        writer.writeValue(jsonContainer, text);

        //System.out.println(jsonContainer.toString("utf-8"));

        ObjectReader reader = mapper.reader(AnnotatedText.class);
        AnnotatedText deserializedText = reader.readValue(jsonContainer.toByteArray());

        assertEquals(text.length(), deserializedText.length());
        assertEquals(text.getData(), deserializedText.getData());
    }

    @Test
    public void testMissingDetectedLanguage() throws IOException {
        String json = "{'RawText':'Cambridge, MA',"
            + "'Tokens':['Cambridge', ',', 'MA'],"
            + "'TokenOffset':[0,9,9,10,11,13],"
            + "'Lemma':['Cambridge',',','MA'],"
            + "'NamedEntity':[0,3,196608],'NamedEntityChainId':[0],'NormalizedNamedEntity':['Cambridge, MA'],"
            + "'SentenceBoundaries':[3]"
            + "}";
        json = json.replace("'", "\"");
        AbstractResultAccess ara = deserialize(json);
        AraDmConverter.convert(ara);
        // The test is that we don't encounter a RuntimeException.
    }

    // The following all come from ARA json files used in RES tests.
    // TODO: once the issues are examined, consider renaming the files and
    // tests, and adding appropriate asserts.  Currently they fail because
    // they throw exceptions.

    @Test
    public void testEmptyString() throws IOException {
        AbstractResultAccess ara = deserialize(new File("../model/data/empty.json"));
        AnnotatedText text = AraDmConverter.convert(ara);
        assertEquals(0, text.length());
        assertEquals(0, text.getTokens().size());
    }

    @Test
    public void testWhitespace() throws IOException {
        AbstractResultAccess ara = deserialize(new File("../model/data/whitespace.json"));
        AnnotatedText text = AraDmConverter.convert(ara);
        assertTrue(text.length() > 0);
        assertEquals(0, text.getTokens().size());
    }

    @Test
    public void testBadAra1() throws IOException {
        AbstractResultAccess ara = deserialize(new File("../model/data/bad-and-good-names.json"));
        AraDmConverter.convert(ara);
    }

    @Test
    public void testBadAra2() throws IOException {
        AbstractResultAccess ara = deserialize(new File("../model/data/no-interesting-mentions.json"));
        AraDmConverter.convert(ara);
    }

    @Test
    public void testBadAra3() throws IOException {
        AbstractResultAccess ara = deserialize(new File("../model/data/shimon-peres-ara.json"));
        AraDmConverter.convert(ara);
    }

    @Test
    public void testBadAra4() throws IOException {
        AbstractResultAccess ara = deserialize(new File("../model/data/shimon-peres-zho.json"));
        AraDmConverter.convert(ara);
    }

    @Test
    public void testLanguageDetectionResults() throws IOException {
        AbstractResultAccess ara = deserialize(new File("../model/data/rli-only.json"));
        AnnotatedText text = AraDmConverter.convert(ara);
        LanguageDetection result = text.getWholeTextLanguageDetection();
        assertEquals(0, result.getStartOffset());
        assertEquals(text.length(), result.getEndOffset());

        assertEquals(1, result.getDetectionResults().size());
        assertEquals(LanguageCode.ENGLISH, result.getDetectionResults().get(0).getLanguage());
        assertEquals(ISO15924.Latn, result.getDetectionResults().get(0).getScript());
        assertEquals(0.0, result.getDetectionResults().get(0).getConfidence(), 0.000001);
    }

    @Test
    public void testLanguageDetectionResultsWithRLBL() throws IOException {
        // This file contains the results of running RLP/RLI and RLBL.  The
        // full doc is detected as Chinese.  The first region is English,
        // (but detected as Catalan) and the second is Chinese.  We expect 3
        // results, where the first represents the full document.
        AbstractResultAccess ara = deserialize(new File("../model/data/rli-and-rlbl.json"));
        AnnotatedText text = AraDmConverter.convert(ara);
        assertEquals(2, text.getLanguageDetectionRegions().size());

        LanguageDetection.DetectionResult result;

        int fullLength = text.getWholeTextLanguageDetection().getEndOffset() -
                text.getWholeTextLanguageDetection().getStartOffset();
        result = text.getWholeTextLanguageDetection().getDetectionResults().get(0);
        assertEquals(LanguageCode.SIMPLIFIED_CHINESE, result.getLanguage());
        assertEquals(ISO15924.Hans, result.getScript());

        int length0 = text.getLanguageDetectionRegions().get(0).getEndOffset() -
            text.getLanguageDetectionRegions().get(0).getStartOffset();
        result = text.getLanguageDetectionRegions().get(0).getDetectionResults().get(0);
        assertEquals(LanguageCode.CATALAN, result.getLanguage());

        int length1 = text.getLanguageDetectionRegions().get(1).getEndOffset() -
            text.getLanguageDetectionRegions().get(1).getStartOffset();
        result = text.getLanguageDetectionRegions().get(1).getDetectionResults().get(0);
        assertEquals(LanguageCode.SIMPLIFIED_CHINESE, result.getLanguage());

        assertEquals(fullLength, length0 + length1);
    }

    @Test
    public void testSentencesFromSentenceNotice() throws IOException {
        //                         012345678901234
        String json = "{'RawText':'Hello.  World ',"
            + "'TokenOffset':[0,5,5,6,8,13],"
            + "'SentenceBoundaries':[2,3]"
            + "}";
        json = json.replace("'", "\"");
        AbstractResultAccess ara = deserialize(json);
        AnnotatedText text = AraDmConverter.convert(ara);
        assertEquals(2, text.getSentences().size());
        assertEquals(0, text.getSentences().get(0).getStartOffset());
        assertEquals(8, text.getSentences().get(0).getEndOffset());
        assertEquals(8, text.getSentences().get(1).getStartOffset());
        assertEquals(14, text.getSentences().get(1).getEndOffset());
    }

    @Test
    public void testSentencesFromTextNotice() throws IOException {
        //                         012345678901234
        String json = "{'RawText':'Hello.  World ',"
            + "'TokenOffset':[0,5,5,6,8,13],"
            + "'TextBoundaries':[8,14]"
            + "}";
        json = json.replace("'", "\"");
        AbstractResultAccess ara = deserialize(json);
        AnnotatedText text = AraDmConverter.convert(ara);
        assertEquals(2, text.getSentences().size());
        assertEquals(0, text.getSentences().get(0).getStartOffset());
        assertEquals(8, text.getSentences().get(0).getEndOffset());
        assertEquals(8, text.getSentences().get(1).getStartOffset());
        assertEquals(14, text.getSentences().get(1).getEndOffset());
    }

    @Test
    public void testNamedEntityTokenConfidences() throws IOException {
        // The reported confidence should be the minimum confidence of the tokens
        // that make up the mention.
        String json = "{'RawText':'Cambridge, MA',"
            + "'Tokens':['Cambridge', ',', 'MA'],"
            + "'TokenOffset':[0,9,9,10,11,13],"
            + "'NamedEntity':[0,3,196608],'NamedEntityChainId':[0],'NormalizedNamedEntity':['Cambridge, MA'],"
            + "'NamedEntityTokenConfidence':[0.4,0.2,0.3]"
            + "}";
        json = json.replace("'", "\"");
        AbstractResultAccess ara = deserialize(json);
        AnnotatedText text = AraDmConverter.convert(ara);
        assertEquals(0.2, text.getEntityMentions().get(0).getConfidence(), 0.00001);
    }
}
