/*
* Copyright 2019 Basis Technology Corp.
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
import com.basistech.rosette.dm.Concept;
import com.basistech.rosette.dm.Dependency;
import com.basistech.rosette.dm.EmbeddingCollection;
import com.basistech.rosette.dm.Embeddings;
import com.basistech.rosette.dm.Entity;
import com.basistech.rosette.dm.EntityMention;
import com.basistech.rosette.dm.Extent;
import com.basistech.rosette.dm.HanMorphoAnalysis;
import com.basistech.rosette.dm.Keyphrase;
import com.basistech.rosette.dm.KoreanMorphoAnalysis;
import com.basistech.rosette.dm.LanguageDetection;
import com.basistech.rosette.dm.LayoutRegion;
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
import com.basistech.rosette.dm.Transliteration;
import com.basistech.rosette.dm.TransliterationResults;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
//CHECKSTYLE:OFF
@SuppressWarnings("deprecation")
public class JsonTest extends AdmAssert {

    private static final String THIS_IS_THE_TERRIER_SHOT_TO_BOSTON = "This is the terrier shot to Boston.";
    private static final String THIS_IS_THE_TERRIER_SHOT_TO_BOSTON_ARABIC = "هذا هو النار ضربة الى بوسطن.";
    private BaseNounPhrase baseNounPhrase;
    private com.basistech.rosette.dm.EntityMention entityMention;
    private RelationshipMention relationshipMention;
    private com.basistech.rosette.dm.ResolvedEntity resolvedEntity;
    private LanguageDetection languageDetectionRegion;
    private LanguageDetection languageDetection;
    private ScriptRegion scriptRegion;
    private Sentence sentence;
    private LayoutRegion layoutRegion;
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
    private CategorizerResult topicResult;
    private AnnotatedText referenceTextOldEntities;
    private AnnotatedText referenceText;
    private Entity entity;
    private Embeddings embeddings;
    private Concept concept;
    private Keyphrase keyphrase;

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
        emBuilder.confidence(1.123456789);
        emBuilder.linkingConfidence(1.123);
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
        rmBuilder.salience(0.0);
        Set<String> modalityValue = new HashSet<>();
        modalityValue.add("subjunctive");
        modalityValue.add("negated");
        rmBuilder.modality(modalityValue);
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

        ListAttribute.Builder<LayoutRegion> dtrListBuilder = new ListAttribute.Builder<>(LayoutRegion.class);
        LayoutRegion.Builder dtrBuilder = new LayoutRegion.Builder(0, builder.data().length(), LayoutRegion.Layout.UNSTRUCTURED);
        layoutRegion = dtrBuilder.build();
        dtrListBuilder.add(layoutRegion);
        builder.layoutRegions(dtrListBuilder.build());

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
        tdBuilder = new TranslatedData.Builder(germanDomain, spanishDomain, spanishText);
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

        Embeddings.Builder embeddingsBuilder = new Embeddings.Builder();
        EmbeddingCollection.Builder embeddingCollectionBuilder = new EmbeddingCollection.Builder();
        embeddingCollectionBuilder.put(0, new float[]{0, 1});
        embeddingsBuilder.put(Embeddings.Name.TEXT, embeddingCollectionBuilder.build());
        embeddings = embeddingsBuilder.build();
        builder.embeddings(embeddingsBuilder.build());

        ListAttribute.Builder<Concept> conceptBuilder = new ListAttribute.Builder<>(Concept.class);
        concept = new Concept.Builder("phrase", "Q100").salience(0.7).build();
        conceptBuilder.add(concept);
        builder.concepts(conceptBuilder.build());

        ListAttribute.Builder<Keyphrase> keyphraseBuilder = new ListAttribute.Builder<>(Keyphrase.class);
        keyphrase = new Keyphrase.Builder("phrase",
                Lists.newArrayList(new Extent.Builder(5, 6).build(), new Extent.Builder(6, 7).build()))
                .salience(10000.2)
                .build();
        keyphraseBuilder.add(keyphrase);
        builder.keyphrases(keyphraseBuilder.build());

        TransliterationResults.Builder transliterationBuilder =
                new TransliterationResults.Builder(LanguageCode.ENGLISH,
                        ISO15924.Latn,
                        THIS_IS_THE_TERRIER_SHOT_TO_BOSTON);

        // Translation: This is the terrier shot to boston
        transliterationBuilder.addTransliteration(
                LanguageCode.ARABIC, Transliteration.of(ISO15924.Arab, THIS_IS_THE_TERRIER_SHOT_TO_BOSTON_ARABIC));

        builder.transliteration(transliterationBuilder.build());

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
        mentionBuilder.confidence(1.123456789); // 10 digits below decimal
        mentionBuilder.linkingConfidence(1.123); // 10 digits below decimal
        mentionBuilder.extendedProperty("em-ex", "em-ex-val");
        entityBuilder.mention(mentionBuilder.build());
        // null mention except start and end
        entityBuilder.mention(new Mention.Builder(11, 12).build());
        entityBuilder.confidence(0.5);
        entityBuilder.salience(0.4);
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
        rmBuilder.salience(0.0);
        Set<String> modalityValue = new HashSet<>();
        modalityValue.add("subjunctive");
        modalityValue.add("negated");
        rmBuilder.modality(modalityValue);
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

        ListAttribute.Builder<LayoutRegion> dtrListBuilder = new ListAttribute.Builder<>(LayoutRegion.class);
        LayoutRegion.Builder dtrBuilder = new LayoutRegion.Builder(0, builder.data().length(), LayoutRegion.Layout.UNSTRUCTURED);
        layoutRegion = dtrBuilder.build();
        dtrListBuilder.add(layoutRegion);
        builder.layoutRegions(dtrListBuilder.build());

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
        tdBuilder = new TranslatedData.Builder(germanDomain, spanishDomain, spanishText);
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

        ListAttribute.Builder<Dependency> depListBuilder = new ListAttribute.Builder<>(Dependency.class);
        depListBuilder.add(new Dependency.Builder("V", -1, 0).build());
        builder.dependencies(depListBuilder.build());

        crListBuilder = new ListAttribute.Builder<>(CategorizerResult.class);
        topicResult = new CategorizerResult.Builder("basketball", 0.5)
                .confidence(0.3).build();
        crListBuilder.add(topicResult);
        builder.topicResults(crListBuilder.build());

        Embeddings.Builder embeddingsBuilder = new Embeddings.Builder();
        EmbeddingCollection.Builder embeddingCollectionBuilder = new EmbeddingCollection.Builder();
        embeddingCollectionBuilder.put(0, new float[]{0, 1});
        embeddingsBuilder.put(Embeddings.Name.TEXT, embeddingCollectionBuilder.build());
        embeddings = embeddingsBuilder.build();
        builder.embeddings(embeddingsBuilder.build());

        TransliterationResults.Builder transliterationBuilder =
                new TransliterationResults.Builder(LanguageCode.ENGLISH,
                        ISO15924.Latn,
                        THIS_IS_THE_TERRIER_SHOT_TO_BOSTON);

        transliterationBuilder.addTransliteration(
                LanguageCode.ARABIC, Transliteration.of(ISO15924.Arab, THIS_IS_THE_TERRIER_SHOT_TO_BOSTON_ARABIC));

        builder.transliteration(transliterationBuilder.build());

        ListAttribute.Builder<Concept> conceptBuilder = new ListAttribute.Builder<>(Concept.class);
        concept = new Concept.Builder("phrase", "Q100").salience(0.7).build();
        conceptBuilder.add(concept);
        builder.concepts(conceptBuilder.build());

        ListAttribute.Builder<Keyphrase> keyphraseBuilder = new ListAttribute.Builder<>(Keyphrase.class);
        keyphrase = new Keyphrase.Builder("phrase",
                Lists.newArrayList(new Extent.Builder(5, 6).build(), new Extent.Builder(6, 7).build()))
                .salience(10000.2)
                .build();
        keyphraseBuilder.add(keyphrase);
        builder.keyphrases(keyphraseBuilder.build());

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
        String version = tree.get("version").asText();
        assertEquals("1.1.0", version);
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
        // 8 digits after decimal. 1.123456789 now ends with "..79"
        assertEquals(entityMention.toString().replace("1.123456789", "1.12345679"), em.toString());

        // make sure OK to serialize null confidence.
        EntityMention nullConfidenceEM = new EntityMention.Builder(0, 1, "E").build();

        EntityMention em1 = mapper.readValue(mapper.writeValueAsString(nullConfidenceEM), EntityMention.class);
        assertNull(em1.getConfidence());

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

        ListAttribute<LayoutRegion> layoutRegions = read.getLayoutRegions();
        assertNotNull(layoutRegions);
        assertEquals(1, layoutRegions.size());
        assertEquals(layoutRegion, layoutRegions.get(0));

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

        Transliteration engTransliteration =
                read.getTransliteration().getTransliteration(LanguageCode.ENGLISH);
        assertEquals(THIS_IS_THE_TERRIER_SHOT_TO_BOSTON, engTransliteration.get(ISO15924.Latn));

        Transliteration araTransliteration =
                read.getTransliteration().getTransliteration(LanguageCode.ARABIC);
        assertEquals(THIS_IS_THE_TERRIER_SHOT_TO_BOSTON_ARABIC, araTransliteration.get(ISO15924.Arab));

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
        assertEquals(0.4, en.getSalience(), 0.0001); // just make sure the salience field works all around.

        // The deserialized entity (en) matches the original (entity) except the first mention's confidence which is
        // truncated to 8 digits after decimal point.
        assertEquals(entity.toString().replace("1.123456789", "1.12345679"), en.toString());

        // also look for "score":-0.2 to make sure no trailing 0s
        assertTrue(writer.toString().contains("\"score\":-0.2"));

        // also make sure "score":"-0.2" isn't there (i.e. doubles aren't strings)
        assertFalse(writer.toString().contains("\"score\":\"-0.2\""));

        ListAttribute<RelationshipMention> rmList = read.getRelationshipMentions();
        assertNotNull(rmList);
        assertEquals(1, rmList.size());
        RelationshipMention rm = rmList.get(0);
        assertEquals(relationshipMention, rm);

        ListAttribute<LanguageDetection> languageDetectionList = read.getLanguageDetectionRegions();
        assertNotNull(languageDetectionList);
        assertEquals(1, languageDetectionList.size());

        assertEquals(languageDetectionRegion, languageDetectionList.get(0));
        assertEquals(languageDetection, read.getWholeTextLanguageDetection());

        ListAttribute<ScriptRegion> scriptRegionList = read.getScriptRegions();
        assertNotNull(scriptRegionList);
        assertEquals(1, scriptRegionList.size());

        assertEquals(scriptRegion, scriptRegionList.get(0));

        ListAttribute<Sentence> sentences = read.getSentences();
        assertNotNull(sentences);

        assertEquals(sentence, sentences.get(0));

        ListAttribute<LayoutRegion> layoutRegions = read.getLayoutRegions();
        assertNotNull(layoutRegions);
        assertEquals(1, layoutRegions.size());
        assertEquals(layoutRegion, layoutRegions.get(0));

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

        assertEquals("V", read.getDependencies().get(0).getRelationship());
        assertEquals(-1, read.getDependencies().get(0).getGovernorTokenIndex());
        assertEquals(0, read.getDependencies().get(0).getDependencyTokenIndex());

        assertEquals(topicResult, read.getTopicResults().get(0));

        Embeddings readEmbeddings = read.getEmbeddings();
        assertEquals(embeddings, readEmbeddings);

        Transliteration engTransliteration =
                read.getTransliteration().getTransliteration(LanguageCode.ENGLISH);
        assertEquals(THIS_IS_THE_TERRIER_SHOT_TO_BOSTON, engTransliteration.get(ISO15924.Latn));

        Transliteration araTransliteration =
                read.getTransliteration().getTransliteration(LanguageCode.ARABIC);
        assertEquals(THIS_IS_THE_TERRIER_SHOT_TO_BOSTON_ARABIC, araTransliteration.get(ISO15924.Arab));

        assertEquals(keyphrase, read.getKeyphrases().get(0));
        assertEquals(concept, read.getConcepts().get(0));
        // We would've failed already, but make sure doubles don't have commas
        assertTrue(writer.toString().contains("\"salience\":10000.2"));
        assertFalse(writer.toString().contains("\"salience\":10,000.2"));
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
        // since there is no data, toString return null.
        assertNull(objectMapper().readValue("{}", AnnotatedText.class).toString());
    }
}
