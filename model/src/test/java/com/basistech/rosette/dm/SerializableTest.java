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
package com.basistech.rosette.dm;

import com.basistech.util.ISO15924;
import com.basistech.util.LanguageCode;
import com.basistech.util.TextDomain;
import com.basistech.util.TransliterationScheme;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 *
 */
//CHECKSTYLE:OFF
@SuppressWarnings("deprecation")
public class SerializableTest {

    private static final String THIS_IS_THE_TERRIER_SHOT_TO_BOSTON = "This is the terrier shot to Boston.";
    private BaseNounPhrase baseNounPhrase;
    private RelationshipMention relationshipMention;
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
    private AnnotatedText referenceText;
    private Entity entity;
    private Embeddings embeddings;

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
        mentionBuilder.linkingConfidence(0.5);
        mentionBuilder.extendedProperty("em-ex", "em-ex-val");
        entityBuilder.mention(mentionBuilder.build());
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

        referenceText = builder.build();


    }



    @Test
    public void roundTrip() throws Exception {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (ObjectOutputStream objectOutput = new ObjectOutputStream(baos)) {
                objectOutput.writeObject(referenceText);
            }

            AnnotatedText read;
            try (ObjectInputStream objectInput = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
                read = (AnnotatedText) objectInput.readObject();
            }

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
        }
    }


}
