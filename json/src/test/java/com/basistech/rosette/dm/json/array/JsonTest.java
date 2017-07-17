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
import com.basistech.rosette.dm.ArabicMorphoAnalysis;
import com.basistech.rosette.dm.BaseNounPhrase;
import com.basistech.rosette.dm.CategorizerResult;
import com.basistech.rosette.dm.Extent;
import com.basistech.rosette.dm.HanMorphoAnalysis;
import com.basistech.rosette.dm.Keyphrase;
import com.basistech.rosette.dm.KoreanMorphoAnalysis;
import com.basistech.rosette.dm.LanguageDetection;
import com.basistech.rosette.dm.ListAttribute;
import com.basistech.rosette.dm.MorphoAnalysis;
import com.basistech.rosette.dm.RelationshipComponent;
import com.basistech.rosette.dm.RelationshipMention;
import com.basistech.rosette.dm.ScriptRegion;
import com.basistech.rosette.dm.Sentence;
import com.basistech.rosette.dm.Token;
import com.basistech.rosette.dm.Concept;
import com.basistech.rosette.dm.TranslatedData;
import com.basistech.rosette.dm.TranslatedTokens;
import com.basistech.rosette.dm.jackson.AnnotatedDataModelModule;
import com.basistech.rosette.dm.jackson.array.AnnotatedDataModelArrayModule;
import com.basistech.util.ISO15924;
import com.basistech.util.LanguageCode;
import com.basistech.util.TextDomain;
import com.basistech.util.TransliterationScheme;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
//CHECKSTYLE:OFF too messy to bother with.
@SuppressWarnings("deprecation")
public class JsonTest extends AdmAssert {

    private static final String THIS_IS_THE_TERRIER_SHOT_TO_BOSTON = "This is the terrier shot to Boston.";
    private BaseNounPhrase baseNounPhrase;
    private com.basistech.rosette.dm.EntityMention entityMention;
    private com.basistech.rosette.dm.ResolvedEntity resolvedEntity;
    private RelationshipMention relationshipMention;
    private LanguageDetection languageDetectionRegion;
    private LanguageDetection languageDetection;
    private ScriptRegion scriptRegion;
    private Sentence sentence;
    private Token token;
    private TranslatedData germanTranslatedData;
    private TranslatedData spanishTranslatedData;
    private TranslatedTokens germanTranslation;
    private TranslatedTokens spanishTranslation;
    private CategorizerResult categoryResult;
    private CategorizerResult sentimentResult;
    private CategorizerResult topicResult;
    private Concept concept;
    private Keyphrase keyphrase;
    private AnnotatedText referenceText;

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

        ListAttribute.Builder<com.basistech.rosette.dm.EntityMention> emListBuilder = new ListAttribute.Builder<>(com.basistech.rosette.dm.EntityMention.class);
        com.basistech.rosette.dm.EntityMention.Builder emBuilder = new com.basistech.rosette.dm.EntityMention.Builder(27, 33, "place");
        emBuilder.flags(42);
        emBuilder.normalized("bahston");
        emBuilder.source("testsource");
        emBuilder.subsource("testsubsource");
        emBuilder.confidence(1.0);
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
        Set<String> modalityValue = new HashSet<>();
        modalityValue.add("subjunctive");
        modalityValue.add("negated");
        rmBuilder.modality(modalityValue);
        rmBuilder.salience(0.0);
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
        MorphoAnalysis morphoAnalysis = maBuilder.build();
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

        TextDomain germanDomain = new TextDomain(ISO15924.Latn, LanguageCode.GERMAN, TransliterationScheme.NATIVE);
        String germanText = "Ein.  Zwei.";
        TranslatedData.Builder tdBuilder = new TranslatedData.Builder(germanDomain, germanText);
        germanTranslatedData = tdBuilder.build();
        translatedDataBuilder.add(germanTranslatedData);
        TextDomain spanishDomain = new TextDomain(ISO15924.Latn, LanguageCode.SPANISH, TransliterationScheme.NATIVE);
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

        crBuilder = new ListAttribute.Builder<>(CategorizerResult.class);
        topicResult = new CategorizerResult.Builder("basketball", 0.5)
                .confidence(0.3).build();
        crBuilder.add(topicResult);
        builder.topicResults(crBuilder.build());

        ListAttribute.Builder<Concept> conceptBuilder = new ListAttribute.Builder<>(Concept.class);
        concept = new Concept.Builder("phrase", "Q100").salience(0.7).build();
        conceptBuilder.add(concept);
        builder.concepts(conceptBuilder.build());

        ListAttribute.Builder<Keyphrase> keyphraseBuilder = new ListAttribute.Builder<>(Keyphrase.class);
        keyphrase = new Keyphrase.Builder("phrase",
                Lists.newArrayList(new Extent.Builder(5, 6).build(), new Extent.Builder(6, 7).build()))
                .salience(1.2)
                .build();
        keyphraseBuilder.add(keyphrase);
        builder.keyphrases(keyphraseBuilder.build());

        referenceText = builder.build();
    }

    @Test
    public void roundTrip() throws Exception {
        ObjectMapper mapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());
        ObjectWriter objectWriter = mapper.writer();
        String json = objectWriter.writeValueAsString(referenceText);

        ObjectReader reader = mapper.readerFor(AnnotatedText.class);
        AnnotatedText read = reader.readValue(json);

        ListAttribute<BaseNounPhrase> bnpList = read.getBaseNounPhrases();
        assertNotNull(bnpList);
        assertEquals(1, bnpList.size());
        BaseNounPhrase bnp = bnpList.get(0);
        assertEquals(baseNounPhrase, bnp);

        ListAttribute<com.basistech.rosette.dm.EntityMention> emList = read.getEntityMentions();
        assertNotNull(emList);
        assertEquals(1, emList.size());
        com.basistech.rosette.dm.EntityMention em = emList.get(0);
        assertEquals(entityMention, em);

        ListAttribute<RelationshipMention> rmList = read.getRelationshipMentions();
        assertNotNull(rmList);
        assertEquals(1, rmList.size());
        RelationshipMention rm = rmList.get(0);
        assertEquals(relationshipMention, rm);

        ListAttribute<com.basistech.rosette.dm.ResolvedEntity> resolvedEntityList = read.getResolvedEntities();
        assertNotNull(resolvedEntityList);
        assertEquals(1, resolvedEntityList.size());
        com.basistech.rosette.dm.ResolvedEntity e = resolvedEntityList.get(0);
        assertEquals(resolvedEntity, e);

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

        assertEquals(topicResult, read.getTopicResults().get(0));
    }

    @Test
    public void versionInjected() throws Exception {
        StringWriter writer = new StringWriter();
        ObjectMapper mapper = AnnotatedDataModelArrayModule.setupObjectMapper(new ObjectMapper());
        ObjectWriter objectWriter = mapper.writer();
        objectWriter.writeValue(writer, referenceText);
        // to tell that the version is there, we read as a tree
        JsonNode tree = mapper.readTree(writer.toString());
        // Property is injected at the end.
        assertEquals("1.1.0", tree.get(tree.size() - 1).asText());
    }

    @Test
    public void versionCheckPasses() throws Exception {
        StringWriter writer = new StringWriter();
        ObjectMapper mapper = AnnotatedDataModelArrayModule.setupObjectMapper(new ObjectMapper());
        ObjectWriter objectWriter = mapper.writer();
        objectWriter.writeValue(writer, referenceText);
        mapper.readValue(writer.toString(), AnnotatedText.class);
    }
}
