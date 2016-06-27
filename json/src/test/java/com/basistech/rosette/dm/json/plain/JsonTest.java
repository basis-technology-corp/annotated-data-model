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
import com.basistech.rosette.dm.BaseAttribute;
import com.basistech.rosette.dm.BaseNounPhrase;
import com.basistech.rosette.dm.CategorizerResult;
import com.basistech.rosette.dm.Entity;
import com.basistech.rosette.dm.Extent;
import com.basistech.rosette.dm.HanMorphoAnalysis;
import com.basistech.rosette.dm.KoreanMorphoAnalysis;
import com.basistech.rosette.dm.LanguageDetection;
import com.basistech.rosette.dm.ListAttribute;
import com.basistech.rosette.dm.Mention;
import com.basistech.rosette.dm.MorphoAnalysis;
import com.basistech.rosette.dm.RelationshipComponent;
import com.basistech.rosette.dm.RelationshipMention;
import com.basistech.rosette.dm.ScriptRegion;
import com.basistech.rosette.dm.Sentence;
import com.basistech.rosette.dm.Token;
import com.basistech.rosette.dm.TranslatedData;
import com.basistech.rosette.dm.TranslatedTokens;
import com.basistech.rosette.dm.jackson.AnnotatedDataModelModule;
import com.basistech.util.ISO15924;
import com.basistech.util.LanguageCode;
import com.basistech.util.TextDomain;
import com.basistech.util.TransliterationScheme;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

/**
 *
 */
//CHECKSTYLE:OFF
@SuppressWarnings("deprecation")
public class JsonTest extends AdmAssert {

    public static final String THIS_IS_THE_TERRIER_SHOT_TO_BOSTON = "This is the terrier shot to Boston.";
    private BaseNounPhrase baseNounPhrase;
    private com.basistech.rosette.dm.EntityMention entityMention;
    private RelationshipMention relationshipMention;
    private com.basistech.rosette.dm.ResolvedEntity resolvedEntity;
    private LanguageDetection languageDetectionRegion;
    private LanguageDetection languageDetection;
    private ScriptRegion scriptRegion;
    private Sentence sentence;
    private MorphoAnalysis morphoAnalysis;
    private TextDomain germanDomain;
    private TextDomain spanishDomain;
    private Token token;
    private TranslatedData germanTranslatedData;
    private TranslatedData spanishTranslatedData;
    private TranslatedTokens germanTranslation;
    private TranslatedTokens spanishTranslation;
    private CategorizerResult categoryResult;
    private CategorizerResult sentimentResult;
    private AnnotatedText referenceTextOldEntities;
    private AnnotatedText referenceText;
    private Entity entity;

    @Before
    public void oneWithEverythingOldEntities() {
        AnnotatedText.Builder builder = new AnnotatedText.Builder();
        builder.data(THIS_IS_THE_TERRIER_SHOT_TO_BOSTON);
        /* Zen text: make me one with everything. */
        ListAttribute.Builder<BaseNounPhrase> bnpListBuilder = new ListAttribute.Builder<>(BaseNounPhrase.class);
        BaseNounPhrase.Builder bnpBuilder = new BaseNounPhrase.Builder(8, 19);
        bnpBuilder.extendedProperty("bnp-ex", "bnp-ex-val");
        baseNounPhrase = bnpBuilder.build();
        bnpListBuilder.add(baseNounPhrase);
        builder.baseNounPhrases(bnpListBuilder.build());

        ListAttribute.Builder<com.basistech.rosette.dm.EntityMention> emListBuilder = new ListAttribute.Builder<>(com.basistech.rosette.dm.EntityMention.class);
        com.basistech.rosette.dm.EntityMention.Builder emBuilder = new com.basistech.rosette.dm.EntityMention.Builder(27, 33, "place");
        emBuilder.flags(42);
        emBuilder.normalized("bahston");
        emBuilder.source("testsource");
        emBuilder.subsource("testsubsource");
        emBuilder.confidence(1.0);
        // we cannot have a completely arbitrary chain ID and have all the compatibility work out.
        emBuilder.coreferenceChainId(0);
        emBuilder.extendedProperty("em-ex", "em-ex-val");
        entityMention = emBuilder.build();
        emListBuilder.add(entityMention);
        builder.entityMentions(emListBuilder.build());

        // Build two relation arguments
        RelationshipComponent.Builder raBuilder = new RelationshipComponent.Builder();
        raBuilder.phrase("bla");
        raBuilder.identifier("/free/base/1");
        raBuilder.extents(Lists.newArrayList(new Extent.Builder(0, 4).build()));
        RelationshipComponent arg1 = raBuilder.build();

        raBuilder = new RelationshipComponent.Builder();
        raBuilder.phrase("blu");
        raBuilder.identifier("/free/base/2");
        RelationshipComponent arg2 = raBuilder.build();

        raBuilder = new RelationshipComponent.Builder();
        raBuilder.phrase("bli");
        raBuilder.identifier("/free/base/3");
        raBuilder.extents(Lists.newArrayList(new Extent.Builder(5, 6).build(), new Extent.Builder(6, 7).build()));
        RelationshipComponent pred = raBuilder.build();

        // Build a relation
        ListAttribute.Builder<RelationshipMention> rmListBuilder = new ListAttribute.Builder<>(RelationshipMention.class);
        RelationshipMention.Builder rmBuilder = new RelationshipMention.Builder(0, 12).predicate(pred).arg1(arg1).arg2
                (arg2);
        rmBuilder.extendedProperty("rm-ex", "rm-ex-val");
        rmBuilder.source("statistical rules:42");
        rmBuilder.confidence(1.0);
        relationshipMention = rmBuilder.build();
        rmListBuilder.add(relationshipMention);
        builder.relationshipMentions(rmListBuilder.build());

        ListAttribute.Builder<com.basistech.rosette.dm.ResolvedEntity> reListBuilder = new ListAttribute.Builder<>(com.basistech.rosette.dm.ResolvedEntity.class);
        com.basistech.rosette.dm.ResolvedEntity.Builder reBuilder = new com.basistech.rosette.dm.ResolvedEntity.Builder(27, 33, "Q100");
        reBuilder.coreferenceChainId(0);
        reBuilder.confidence(1.0);
        reBuilder.sentiment(new CategorizerResult.Builder("positive", null).confidence(1.0).build());
        reBuilder.extendedProperty("re-ex", "re-ex-val");
        resolvedEntity = reBuilder.build();
        reListBuilder.add(resolvedEntity);
        builder.resolvedEntities(reListBuilder.build());

        ListAttribute.Builder<LanguageDetection> ldListBuilder = new ListAttribute.Builder<>(LanguageDetection.class);
        List<LanguageDetection.DetectionResult> dets = Lists.newArrayList();
        dets.add(new LanguageDetection.DetectionResult.Builder(LanguageCode.ENGLISH).encoding("utf-8").script(ISO15924.Latn).confidence(1.0).build());
        LanguageDetection.Builder ldBuilder = new LanguageDetection.Builder(0, builder.data().length(), dets);
        ldBuilder.extendedProperty("ld-ex", "ld-ex-val");
        languageDetectionRegion = ldBuilder.build();
        ldListBuilder.add(languageDetectionRegion);
        builder.languageDetectionRegions(ldListBuilder.build());

        dets = Lists.newArrayList();
        dets.add(new LanguageDetection.DetectionResult.Builder(LanguageCode.FRENCH).encoding("utf-8").script(ISO15924.Latn).confidence(1.0).build());
        ldBuilder = new LanguageDetection.Builder(0, builder.data().length(), dets);
        ldBuilder.extendedProperty("ldw-ex", "ldw-ex-val");
        languageDetection = ldBuilder.build();
        builder.wholeDocumentLanguageDetection(ldBuilder.build());

        ListAttribute.Builder<ScriptRegion> srListBuilder = new ListAttribute.Builder<>(ScriptRegion.class);
        ScriptRegion.Builder srBuilder = new ScriptRegion.Builder(0, builder.data().length(), ISO15924.Latn);
        srBuilder.extendedProperty("sr-ex", "sr-ex-val");
        scriptRegion = srBuilder.build();
        srListBuilder.add(scriptRegion);
        builder.scriptRegions(srListBuilder.build());

        ListAttribute.Builder<Sentence> sentListBuilder = new ListAttribute.Builder<>(Sentence.class);
        Sentence.Builder sentBuilder = new Sentence.Builder(0, 8);
        sentBuilder.extendedProperty("sb-ex", "sb-ex-val");
        sentence = sentBuilder.build();
        sentListBuilder.add(sentence);
        builder.sentences(sentListBuilder.build());

        ListAttribute.Builder<Token> tokenListBuilder = new ListAttribute.Builder<>(Token.class);
        Token.Builder tokenBuilder = new Token.Builder(0, 4, "This");
        tokenBuilder.source("test");
        tokenBuilder.addNormalized("abnormal");
        tokenBuilder.extendedProperty("tok-ex", "tok-ex-val");

        MorphoAnalysis.Builder maBuilder = new MorphoAnalysis.Builder();
        maBuilder.raw("cooked");
        maBuilder.partOfSpeech("+woof");
        Token.Builder compTokBuilder = new Token.Builder(0, 2, "Th");
        maBuilder.addComponent(compTokBuilder.build());
        morphoAnalysis = maBuilder.build();
        tokenBuilder.addAnalysis(morphoAnalysis);

        ArabicMorphoAnalysis.Builder araMaBuilder = new ArabicMorphoAnalysis.Builder();
        araMaBuilder.addPrefix("pre", "PRE");
        araMaBuilder.addStem("stem", "STEM");
        araMaBuilder.addSuffix("suff", "SUFF");
        araMaBuilder.definiteArticle(true);
        araMaBuilder.strippablePrefix(true);
        araMaBuilder.root("root");
        araMaBuilder.lengths(2, 3);
        araMaBuilder.lemma("lemma");
        araMaBuilder.partOfSpeech("pos");
        araMaBuilder.raw("raw");
        tokenBuilder.addAnalysis(araMaBuilder.build());

        HanMorphoAnalysis.Builder hanMaBuilder = new HanMorphoAnalysis.Builder();
        hanMaBuilder.addReading("proust");
        hanMaBuilder.lemma("lemma");
        hanMaBuilder.partOfSpeech("pos");
        tokenBuilder.addAnalysis(hanMaBuilder.build());

        KoreanMorphoAnalysis.Builder korMaBuilder = new KoreanMorphoAnalysis.Builder();
        korMaBuilder.addMorpheme("m1", "t1");
        korMaBuilder.addMorpheme("m2", "t2");
        korMaBuilder.partOfSpeech("korean");
        korMaBuilder.lemma("koreanLemma");
        tokenBuilder.addAnalysis(korMaBuilder.build());

        token = tokenBuilder.build();
        tokenListBuilder.add(token);
        builder.tokens(tokenListBuilder.build());

        ListAttribute.Builder<TranslatedData> translatedDataBuilder =
            new ListAttribute.Builder<>(TranslatedData.class);

        germanDomain = new TextDomain(ISO15924.Latn, LanguageCode.GERMAN, TransliterationScheme.NATIVE);
        String germanText = "Ein.  Zwei.";
        TranslatedData.Builder tdBuilder = new TranslatedData.Builder(germanDomain, germanText);
        germanTranslatedData = tdBuilder.build();
        translatedDataBuilder.add(germanTranslatedData);
        spanishDomain = new TextDomain(ISO15924.Latn, LanguageCode.SPANISH, TransliterationScheme.NATIVE);
        String spanishText = "Uno.  Dos.";
        tdBuilder = new TranslatedData.Builder(spanishDomain, spanishText);
        spanishTranslatedData = tdBuilder.build();
        translatedDataBuilder.add(spanishTranslatedData);
        builder.translatedData(translatedDataBuilder.build());

        ListAttribute.Builder<TranslatedTokens> translatedTokensListBuilder =
            new ListAttribute.Builder<>(TranslatedTokens.class);

        TranslatedTokens.Builder ttBuilder = new TranslatedTokens.Builder(germanDomain);
        ttBuilder.addTranslatedToken("Ein");
        ttBuilder.addTranslatedToken(".");
        ttBuilder.addTranslatedToken("Zwei");
        ttBuilder.addTranslatedToken(".");
        germanTranslation = ttBuilder.build();
        translatedTokensListBuilder.add(germanTranslation);
        spanishDomain = new TextDomain(ISO15924.Latn, LanguageCode.SPANISH, TransliterationScheme.NATIVE);
        ttBuilder = new TranslatedTokens.Builder(spanishDomain);
        ttBuilder.addTranslatedToken("Uno");
        ttBuilder.addTranslatedToken(".");
        ttBuilder.addTranslatedToken("Dos");
        ttBuilder.addTranslatedToken(".");
        spanishTranslation = ttBuilder.build();
        translatedTokensListBuilder.add(spanishTranslation);
        builder.translatedTokens(translatedTokensListBuilder.build());

        ListAttribute.Builder<CategorizerResult> crBuilder
            = new ListAttribute.Builder<>(CategorizerResult.class);
        Map<String, Double> perFeatureScores = Maps.newHashMap();
        perFeatureScores.put("foo", 1.2);
        perFeatureScores.put("bar", -2.4);
        categoryResult = new CategorizerResult.Builder("POLITICS", -0.2)
            .confidence(0.3)
            .explanationSet(Lists.newArrayList("foo", "bar"))
            .perFeatureScores(perFeatureScores).build();
        crBuilder.add(categoryResult);
        builder.categorizerResults(crBuilder.build());

        crBuilder = new ListAttribute.Builder<>(CategorizerResult.class);
        sentimentResult = new CategorizerResult.Builder("negative", -0.2)
            .confidence(0.3)
            .explanationSet(Lists.newArrayList("foo", "bar"))
            .perFeatureScores(perFeatureScores).build();
        crBuilder.add(sentimentResult);
        builder.sentimentResults(crBuilder.build());

        referenceTextOldEntities = builder.build();
    }

    @Before
    public void oneWithEverything() {
        AnnotatedText.Builder builder = new AnnotatedText.Builder();
        builder.data(THIS_IS_THE_TERRIER_SHOT_TO_BOSTON);
        /* Zen text: make me one with everything. */
        ListAttribute.Builder<BaseNounPhrase> bnpListBuilder = new ListAttribute.Builder<>(BaseNounPhrase.class);
        BaseNounPhrase.Builder bnpBuilder = new BaseNounPhrase.Builder(8, 19);
        bnpBuilder.extendedProperty("bnp-ex", "bnp-ex-val");
        baseNounPhrase = bnpBuilder.build();
        bnpListBuilder.add(baseNounPhrase);
        builder.baseNounPhrases(bnpListBuilder.build());

        ListAttribute.Builder<Entity> entityListBuilder = new ListAttribute.Builder<>(Entity.class);
        Entity.Builder entityBuilder = new Entity.Builder();
        entityBuilder.headMentionIndex(0);
        entityBuilder.type("PERSON");
        entityBuilder.entityId("Q100");
        CategorizerResult.Builder crBuilder = new CategorizerResult.Builder("negative", 0.4);
        entityBuilder.sentiment(crBuilder.build());
        Mention.Builder mentionBuilder = new Mention.Builder(0, 10);
        mentionBuilder.normalized("bahston");
        mentionBuilder.source("testsource");
        mentionBuilder.subsource("testsubsource");
        mentionBuilder.confidence(1.0);
        mentionBuilder.extendedProperty("em-ex", "em-ex-val");
        entityBuilder.mention(mentionBuilder.build());
        entityBuilder.confidence(0.5);
        entity = entityBuilder.build();
        entityListBuilder.add(entity);
        builder.entities(entityListBuilder.build());

        // Build two relation arguments
        RelationshipComponent.Builder raBuilder = new RelationshipComponent.Builder();
        raBuilder.phrase("bla");
        raBuilder.identifier("/free/base/1");
        raBuilder.extents(Lists.newArrayList(new Extent.Builder(0, 4).build()));
        RelationshipComponent arg1 = raBuilder.build();

        raBuilder = new RelationshipComponent.Builder();
        raBuilder.phrase("blu");
        raBuilder.identifier("/free/base/2");
        RelationshipComponent arg2 = raBuilder.build();

        raBuilder = new RelationshipComponent.Builder();
        raBuilder.phrase("bli");
        raBuilder.identifier("/free/base/3");
        raBuilder.extents(Lists.newArrayList(new Extent.Builder(5, 6).build(), new Extent.Builder(6, 7).build()));
        RelationshipComponent pred = raBuilder.build();

        // Build a relation
        ListAttribute.Builder<RelationshipMention> rmListBuilder = new ListAttribute.Builder<>(RelationshipMention.class);
        RelationshipMention.Builder rmBuilder = new RelationshipMention.Builder(0, 12).predicate(pred).arg1(arg1).arg2
                (arg2);
        rmBuilder.extendedProperty("rm-ex", "rm-ex-val");
        rmBuilder.source("statistical rules:42");
        rmBuilder.confidence(1.0);
        relationshipMention = rmBuilder.build();
        rmListBuilder.add(relationshipMention);
        builder.relationshipMentions(rmListBuilder.build());

        ListAttribute.Builder<LanguageDetection> ldListBuilder = new ListAttribute.Builder<>(LanguageDetection.class);
        List<LanguageDetection.DetectionResult> dets = Lists.newArrayList();
        dets.add(new LanguageDetection.DetectionResult.Builder(LanguageCode.ENGLISH).encoding("utf-8").script(ISO15924.Latn).confidence(1.0).build());
        LanguageDetection.Builder ldBuilder = new LanguageDetection.Builder(0, builder.data().length(), dets);
        ldBuilder.extendedProperty("ld-ex", "ld-ex-val");
        languageDetectionRegion = ldBuilder.build();
        ldListBuilder.add(languageDetectionRegion);
        builder.languageDetectionRegions(ldListBuilder.build());

        dets = Lists.newArrayList();
        dets.add(new LanguageDetection.DetectionResult.Builder(LanguageCode.FRENCH).encoding("utf-8").script(ISO15924.Latn).confidence(1.0).build());
        ldBuilder = new LanguageDetection.Builder(0, builder.data().length(), dets);
        ldBuilder.extendedProperty("ldw-ex", "ldw-ex-val");
        languageDetection = ldBuilder.build();
        builder.wholeDocumentLanguageDetection(ldBuilder.build());

        ListAttribute.Builder<ScriptRegion> srListBuilder = new ListAttribute.Builder<>(ScriptRegion.class);
        ScriptRegion.Builder srBuilder = new ScriptRegion.Builder(0, builder.data().length(), ISO15924.Latn);
        srBuilder.extendedProperty("sr-ex", "sr-ex-val");
        scriptRegion = srBuilder.build();
        srListBuilder.add(scriptRegion);
        builder.scriptRegions(srListBuilder.build());

        ListAttribute.Builder<Sentence> sentListBuilder = new ListAttribute.Builder<>(Sentence.class);
        Sentence.Builder sentBuilder = new Sentence.Builder(0, 8);
        sentBuilder.extendedProperty("sb-ex", "sb-ex-val");
        sentence = sentBuilder.build();
        sentListBuilder.add(sentence);
        builder.sentences(sentListBuilder.build());

        ListAttribute.Builder<Token> tokenListBuilder = new ListAttribute.Builder<>(Token.class);
        Token.Builder tokenBuilder = new Token.Builder(0, 4, "This");
        tokenBuilder.source("test");
        tokenBuilder.addNormalized("abnormal");
        tokenBuilder.extendedProperty("tok-ex", "tok-ex-val");

        MorphoAnalysis.Builder maBuilder = new MorphoAnalysis.Builder();
        maBuilder.raw("cooked");
        maBuilder.partOfSpeech("+woof");
        Token.Builder compTokBuilder = new Token.Builder(0, 2, "Th");
        maBuilder.addComponent(compTokBuilder.build());
        morphoAnalysis = maBuilder.build();
        tokenBuilder.addAnalysis(morphoAnalysis);

        ArabicMorphoAnalysis.Builder araMaBuilder = new ArabicMorphoAnalysis.Builder();
        araMaBuilder.addPrefix("pre", "PRE");
        araMaBuilder.addStem("stem", "STEM");
        araMaBuilder.addSuffix("suff", "SUFF");
        araMaBuilder.definiteArticle(true);
        araMaBuilder.strippablePrefix(true);
        araMaBuilder.root("root");
        araMaBuilder.lengths(2, 3);
        araMaBuilder.lemma("lemma");
        araMaBuilder.partOfSpeech("pos");
        araMaBuilder.raw("raw");
        tokenBuilder.addAnalysis(araMaBuilder.build());

        HanMorphoAnalysis.Builder hanMaBuilder = new HanMorphoAnalysis.Builder();
        hanMaBuilder.addReading("proust");
        hanMaBuilder.lemma("lemma");
        hanMaBuilder.partOfSpeech("pos");
        tokenBuilder.addAnalysis(hanMaBuilder.build());

        KoreanMorphoAnalysis.Builder korMaBuilder = new KoreanMorphoAnalysis.Builder();
        korMaBuilder.addMorpheme("m1", "t1");
        korMaBuilder.addMorpheme("m2", "t2");
        korMaBuilder.partOfSpeech("korean");
        korMaBuilder.lemma("koreanLemma");
        tokenBuilder.addAnalysis(korMaBuilder.build());

        token = tokenBuilder.build();
        tokenListBuilder.add(token);
        builder.tokens(tokenListBuilder.build());

        ListAttribute.Builder<TranslatedData> translatedDataBuilder =
                new ListAttribute.Builder<>(TranslatedData.class);

        germanDomain = new TextDomain(ISO15924.Latn, LanguageCode.GERMAN, TransliterationScheme.NATIVE);
        String germanText = "Ein.  Zwei.";
        TranslatedData.Builder tdBuilder = new TranslatedData.Builder(germanDomain, germanText);
        germanTranslatedData = tdBuilder.build();
        translatedDataBuilder.add(germanTranslatedData);
        spanishDomain = new TextDomain(ISO15924.Latn, LanguageCode.SPANISH, TransliterationScheme.NATIVE);
        String spanishText = "Uno.  Dos.";
        tdBuilder = new TranslatedData.Builder(spanishDomain, spanishText);
        spanishTranslatedData = tdBuilder.build();
        translatedDataBuilder.add(spanishTranslatedData);
        builder.translatedData(translatedDataBuilder.build());

        ListAttribute.Builder<TranslatedTokens> translatedTokensListBuilder =
                new ListAttribute.Builder<>(TranslatedTokens.class);

        TranslatedTokens.Builder ttBuilder = new TranslatedTokens.Builder(germanDomain);
        ttBuilder.addTranslatedToken("Ein");
        ttBuilder.addTranslatedToken(".");
        ttBuilder.addTranslatedToken("Zwei");
        ttBuilder.addTranslatedToken(".");
        germanTranslation = ttBuilder.build();
        translatedTokensListBuilder.add(germanTranslation);
        spanishDomain = new TextDomain(ISO15924.Latn, LanguageCode.SPANISH, TransliterationScheme.NATIVE);
        ttBuilder = new TranslatedTokens.Builder(spanishDomain);
        ttBuilder.addTranslatedToken("Uno");
        ttBuilder.addTranslatedToken(".");
        ttBuilder.addTranslatedToken("Dos");
        ttBuilder.addTranslatedToken(".");
        spanishTranslation = ttBuilder.build();
        translatedTokensListBuilder.add(spanishTranslation);
        builder.translatedTokens(translatedTokensListBuilder.build());

        ListAttribute.Builder<CategorizerResult> crListBuilder = new ListAttribute.Builder<>(CategorizerResult.class);
        Map<String, Double> perFeatureScores = Maps.newHashMap();
        perFeatureScores.put("foo", 1.2);
        perFeatureScores.put("bar", -2.4);
        categoryResult = new CategorizerResult.Builder("POLITICS", -0.2)
                .confidence(0.3)
                .explanationSet(Lists.newArrayList("foo", "bar"))
                .perFeatureScores(perFeatureScores).build();
        crListBuilder.add(categoryResult);
        builder.categorizerResults(crListBuilder.build());

        crListBuilder = new ListAttribute.Builder<>(CategorizerResult.class);
        sentimentResult = new CategorizerResult.Builder("negative", -0.2)
                .confidence(0.3)
                .explanationSet(Lists.newArrayList("foo", "bar"))
                .perFeatureScores(perFeatureScores).build();
        crListBuilder.add(sentimentResult);
        builder.sentimentResults(crListBuilder.build());

        referenceText = builder.build();
    }

    /* See ROS-76. */
    @Test
    public void dataPlainString() throws Exception {
        CharSequence fancyCharSequence = new CharSequence() {
            private final String data = "Hello Polly";

            public String getExtraneousInfo() {
                return "What is this doing here?";
            }

            @Override
            public int length() {
                return data.length();
            }

            @Override
            public char charAt(int index) {
                return data.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return data.substring(start, end);
            }

            @Override
            public String toString() {
                return data;
            }
        };

        AnnotatedText.Builder builder = new AnnotatedText.Builder();
        builder.data(fancyCharSequence);
        AnnotatedText text = builder.build();
        StringWriter writer = new StringWriter();
        ObjectMapper mapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());
        ObjectWriter objectWriter = mapper.writer();
        objectWriter.writeValue(writer, text);
        assertFalse(writer.toString().contains("What is this doing here"));
        assertTrue(writer.toString().contains("Hello Polly"));
    }

    @Test
    public void versionInjected() throws Exception {
        StringWriter writer = new StringWriter();
        ObjectMapper mapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());
        ObjectWriter objectWriter = mapper.writer();
        objectWriter.writeValue(writer, referenceTextOldEntities);
        // to tell that the version is there, we read as a tree
        JsonNode tree = mapper.readTree(writer.toString());
        assertEquals("1.1.0", tree.get("version").asText());
    }

    @Test
    public void versionCheckPasses() throws Exception {
        StringWriter writer = new StringWriter();
        ObjectMapper mapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());
        ObjectWriter objectWriter = mapper.writer();
        objectWriter.writeValue(writer, referenceTextOldEntities);
        mapper.readValue(writer.toString(), AnnotatedText.class);
    }

    @Test
    public void noVersionAtAll() throws Exception {
        ObjectMapper mapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());
        mapper.readValue(new File("test-data/ordered-list-in-annotated-text.json"), AnnotatedText.class);
    }

    @Test(expected = InvalidFormatException.class)
    public void futureVersionThrows() throws Exception {
        String v2 = "{ \"version\": \"2.0.0\" }";
        ObjectMapper mapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());
        mapper.readValue(v2, AnnotatedText.class);
    }

    @Test
    public void roundTripOldEntities() throws Exception {
        StringWriter writer = new StringWriter();
        ObjectMapper mapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());
        ObjectWriter objectWriter = mapper.writer();
        objectWriter.writeValue(writer, referenceTextOldEntities);

        ObjectReader reader = mapper.readerFor(AnnotatedText.class);
        AnnotatedText read = reader.readValue(writer.toString());

        ListAttribute<BaseNounPhrase> bnpList = read.getBaseNounPhrases();
        assertNotNull(bnpList);
        assertEquals(1, bnpList.size());
        BaseNounPhrase bnp = bnpList.get(0);
        assertEquals(baseNounPhrase, bnp);

        ListAttribute<com.basistech.rosette.dm.EntityMention> emList = read.getEntityMentions();
        assertNotNull(emList);
        assertEquals(1, emList.size());
        com.basistech.rosette.dm.EntityMention em = emList.get(0);
        // Test the things we care about, but avoid the extended props and the coref chain id.
        assertEquals(entityMention.getEntityType(), em.getEntityType());
        assertEquals(entityMention.getNormalized(), em.getNormalized());
        assertEquals(entityMention.getStartOffset(), em.getStartOffset());
        assertEquals(entityMention.getEndOffset(), em.getEndOffset());

        ListAttribute<RelationshipMention> rmList = read.getRelationshipMentions();
        assertNotNull(rmList);
        assertEquals(1, rmList.size());
        RelationshipMention rm = rmList.get(0);
        assertEquals(relationshipMention, rm);

        // don't check the coref chain id, it's no longer round-tripped to avoid noise in json.
        ListAttribute<com.basistech.rosette.dm.ResolvedEntity> resolvedEntityList = read.getResolvedEntities();
        assertNotNull(resolvedEntityList);
        assertEquals(1, resolvedEntityList.size());
        com.basistech.rosette.dm.ResolvedEntity e = resolvedEntityList.get(0);
        assertEquals(resolvedEntity.getStartOffset(), e.getStartOffset());
        assertEquals(resolvedEntity.getEndOffset(), e.getEndOffset());
        assertEquals(resolvedEntity.getEntityId(), e.getEntityId());
        assertEquals(resolvedEntity.getExtendedProperties(), e.getExtendedProperties());
        assertEquals(resolvedEntity.getSentiment(), e.getSentiment());
        assertEquals(resolvedEntity.getConfidence(), e.getConfidence(), 0.001);

        ListAttribute<LanguageDetection> languageDetectionList = read.getLanguageDetectionRegions();
        assertNotNull(languageDetectionList);
        assertEquals(1, languageDetectionList.size());

        assertEquals(languageDetectionRegion, languageDetectionList.get(0));
        assertEquals(languageDetection,  read.getWholeTextLanguageDetection());

        ListAttribute<ScriptRegion> scriptRegionList = read.getScriptRegions();
        assertNotNull(scriptRegionList);
        assertEquals(1, scriptRegionList.size());

        assertEquals(scriptRegion, scriptRegionList.get(0));

        ListAttribute<Sentence> sentences = read.getSentences();
        assertNotNull(sentences);

        assertEquals(sentence, sentences.get(0));

        ListAttribute<Token> tokenList = read.getTokens();
        assertNotNull(tokenList);
        assertEquals(1, tokenList.size());
        assertEquals(token, tokenList.get(0));

        ListAttribute<TranslatedData> dataTranslations = read.getTranslatedData();
        assertEquals(germanTranslatedData, dataTranslations.get(0));
        assertEquals(spanishTranslatedData, dataTranslations.get(1));

        ListAttribute<TranslatedTokens> translatedTokens = read.getTranslatedTokens();
        assertEquals(germanTranslation, translatedTokens.get(0));
        assertEquals(spanishTranslation, translatedTokens.get(1));

        assertEquals(categoryResult, read.getCategorizerResults().get(0));

        assertEquals(sentimentResult, read.getSentimentResults().get(0));
    }

    @Test
    public void roundTrip() throws Exception {
        StringWriter writer = new StringWriter();
        ObjectMapper mapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());
        ObjectWriter objectWriter = mapper.writer();
        objectWriter.writeValue(writer, referenceText);

        ObjectReader reader = mapper.readerFor(AnnotatedText.class);
        AnnotatedText read = reader.readValue(writer.toString());

        ListAttribute<BaseNounPhrase> bnpList = read.getBaseNounPhrases();
        assertNotNull(bnpList);
        assertEquals(1, bnpList.size());
        BaseNounPhrase bnp = bnpList.get(0);
        assertEquals(baseNounPhrase, bnp);

        ListAttribute<Entity> entityList = read.getEntities();
        assertNotNull(entityList);
        assertEquals(1, entityList.size());
        Entity en = entityList.get(0);
        assertEquals(entity, en);

        ListAttribute<RelationshipMention> rmList = read.getRelationshipMentions();
        assertNotNull(rmList);
        assertEquals(1, rmList.size());
        RelationshipMention rm = rmList.get(0);
        assertEquals(relationshipMention, rm);

        ListAttribute<LanguageDetection> languageDetectionList = read.getLanguageDetectionRegions();
        assertNotNull(languageDetectionList);
        assertEquals(1, languageDetectionList.size());

        assertEquals(languageDetectionRegion, languageDetectionList.get(0));
        assertEquals(languageDetection,  read.getWholeTextLanguageDetection());

        ListAttribute<ScriptRegion> scriptRegionList = read.getScriptRegions();
        assertNotNull(scriptRegionList);
        assertEquals(1, scriptRegionList.size());

        assertEquals(scriptRegion, scriptRegionList.get(0));

        ListAttribute<Sentence> sentences = read.getSentences();
        assertNotNull(sentences);

        assertEquals(sentence, sentences.get(0));

        ListAttribute<Token> tokenList = read.getTokens();
        assertNotNull(tokenList);
        assertEquals(1, tokenList.size());
        assertEquals(token, tokenList.get(0));

        ListAttribute<TranslatedData> dataTranslations = read.getTranslatedData();
        assertEquals(germanTranslatedData, dataTranslations.get(0));
        assertEquals(spanishTranslatedData, dataTranslations.get(1));

        ListAttribute<TranslatedTokens> translatedTokens = read.getTranslatedTokens();
        assertEquals(germanTranslation, translatedTokens.get(0));
        assertEquals(spanishTranslation, translatedTokens.get(1));

        assertEquals(categoryResult, read.getCategorizerResults().get(0));

        assertEquals(sentimentResult, read.getSentimentResults().get(0));
    }

    @Test
    public void testForwardCompatibilitySimple() throws Exception {
        ObjectMapper mapper = objectMapper();
        AnnotatedText.Builder builder = new AnnotatedText.Builder();
        builder.data(THIS_IS_THE_TERRIER_SHOT_TO_BOSTON);
        AnnotatedText simpleText = builder.build();

        JsonNode tree = mapper.valueToTree(simpleText);
        ObjectNode attributes = (ObjectNode) tree.get("attributes");
        ObjectNode extendedObject = attributes.putObject("novelty");
        extendedObject.put("type", "novelty");
        extendedObject.put("startOffset", 4);
        extendedObject.put("endOffset", 8);
        extendedObject.put("color", "pari");

        AnnotatedText read = mapper.treeToValue(tree, AnnotatedText.class);
        BaseAttribute novelty = read.getAttributes().get("novelty");
        assertNotNull(novelty);
        assertEquals(4, novelty.getExtendedProperties().get("startOffset"));
        assertEquals(8, novelty.getExtendedProperties().get("endOffset"));
        assertEquals("pari", novelty.getExtendedProperties().get("color"));
    }

    @Test
    public void testForwardCompatibilityNoisy() throws Exception {
        ObjectMapper mapper = objectMapper();
        JsonNode tree = mapper.valueToTree(referenceTextOldEntities);
        ObjectNode attributes = (ObjectNode) tree.get("attributes");
        ObjectNode extendedObject = attributes.putObject("novelty");
        extendedObject.put("type", "novelty");
        extendedObject.put("startOffset", 4);
        extendedObject.put("endOffset", 8);
        extendedObject.put("color", "pari");

        AnnotatedText read = mapper.treeToValue(tree, AnnotatedText.class);
        BaseAttribute novelty = read.getAttributes().get("novelty");
        assertNotNull(novelty);
        assertEquals(4, novelty.getExtendedProperties().get("startOffset"));
        assertEquals(8, novelty.getExtendedProperties().get("endOffset"));
        assertEquals("pari", novelty.getExtendedProperties().get("color"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testForwardCompatibilityList() throws Exception {
        ObjectMapper mapper = objectMapper();
        AnnotatedText.Builder builder = new AnnotatedText.Builder();
        builder.data(THIS_IS_THE_TERRIER_SHOT_TO_BOSTON);
        AnnotatedText simpleText = builder.build();

        JsonNode tree = mapper.valueToTree(simpleText);
        ObjectNode attributes = (ObjectNode) tree.get("attributes");
        ObjectNode extendedObject = attributes.putObject("novelty");
        extendedObject.put("type", "list");
        extendedObject.put("itemType", "noveltyItem");
        ArrayNode items = extendedObject.putArray("items");
        ObjectNode item0 = items.addObject();
        item0.put("startOffset", 4);
        item0.put("endOffset", 8);
        item0.put("color", "pari");
        ObjectNode item1 = items.addObject();
        item1.put("startOffset", 10);
        item1.put("endOffset", 12);
        item1.put("color", "off");

        AnnotatedText read = mapper.treeToValue(tree, AnnotatedText.class);
        ListAttribute<BaseAttribute> novelty = (ListAttribute<BaseAttribute>) read.getAttributes().get("novelty");
        assertNotNull(novelty);
        assertEquals(2, novelty.size());
        BaseAttribute item = novelty.get(0);
        assertEquals(4, item.getExtendedProperties().get("startOffset"));
        assertEquals(8, item.getExtendedProperties().get("endOffset"));
        assertEquals("pari", item.getExtendedProperties().get("color"));

        item = novelty.get(1);
        assertEquals(10, item.getExtendedProperties().get("startOffset"));
        assertEquals(12, item.getExtendedProperties().get("endOffset"));
        assertEquals("off", item.getExtendedProperties().get("color"));

    }

    @Test
    public void emptyAdm() throws Exception {
        /* We want to be able to read an empty object as an ADM */
        /* No asserts needed, what we need here is a lack of a throw. */
        objectMapper().readValue("{}", AnnotatedText.class).toString();
    }
}
