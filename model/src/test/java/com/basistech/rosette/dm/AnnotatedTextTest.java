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

package com.basistech.rosette.dm;

import com.basistech.util.ISO15924;
import com.basistech.util.LanguageCode;
import com.basistech.util.TextDomain;
import com.basistech.util.TransliterationScheme;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.hamcrest.MatcherAssert;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

public class AnnotatedTextTest {

    @Test
    public void testSentenceBoundaries() {
        //                0123456789012
        String rawText = "One.  Two.  ";
        AnnotatedText.Builder builder = new AnnotatedText.Builder().data(rawText);

        ListAttribute.Builder<Sentence> sentenceListBuilder = new ListAttribute.Builder<>(Sentence.class);
        Sentence.Builder sbBuilder = new Sentence.Builder(0, 6);
        sentenceListBuilder.add(sbBuilder.build());
        sbBuilder = new Sentence.Builder(6, 12);
        sentenceListBuilder.add(sbBuilder.build());

        builder.sentences(sentenceListBuilder.build());
        AnnotatedText text = builder.build();

        ListAttribute<Sentence> sents = text.getSentences();
        assertEquals(0, sents.get(0).getStartOffset());
        assertEquals(6, sents.get(0).getEndOffset());

        assertEquals(6, sents.get(1).getStartOffset());
        assertEquals(12, sents.get(1).getEndOffset());

    }

    @Test
    public void testTranslatedData() {
        String rawText = "One.  Two.";
        String germanText = "Ein.  Zwei.";
        String spanishText = "Uno.  Dos.";
        AnnotatedText.Builder builder = new AnnotatedText.Builder().data(rawText);

        ListAttribute.Builder<TranslatedData> translatedDataBuilder =
            new ListAttribute.Builder<>(TranslatedData.class);

        TextDomain domain = new TextDomain(ISO15924.Latn, LanguageCode.GERMAN, TransliterationScheme.NATIVE);
        TranslatedData.Builder tdBuilder = new TranslatedData.Builder(domain, germanText);
        translatedDataBuilder.add(tdBuilder.build());
        domain = new TextDomain(ISO15924.Latn, LanguageCode.SPANISH, TransliterationScheme.NATIVE);
        tdBuilder = new TranslatedData.Builder(domain, spanishText);
        translatedDataBuilder.add(tdBuilder.build());

        builder.translatedData(translatedDataBuilder.build());
        AnnotatedText text = builder.build();

        ListAttribute<TranslatedData> dataTranslations = text.getTranslatedData();

        TranslatedData data = dataTranslations.get(0);
        TextDomain dataDomain = data.getDomain();
        ISO15924 script = dataDomain.getScript();
        LanguageCode language = dataDomain.getLanguage();
        TransliterationScheme scheme = dataDomain.getTransliterationScheme();
        String translation = data.getTranslation();

        assertEquals(script, ISO15924.Latn);
        assertEquals(language, LanguageCode.GERMAN);
        assertEquals(scheme, TransliterationScheme.NATIVE);
        assertEquals(translation, germanText);

        data = dataTranslations.get(1);
        dataDomain = data.getDomain();
        script = dataDomain.getScript();
        language = dataDomain.getLanguage();
        scheme = dataDomain.getTransliterationScheme();
        translation = data.getTranslation();

        assertEquals(script, ISO15924.Latn);
        assertEquals(language, LanguageCode.SPANISH);
        assertEquals(scheme, TransliterationScheme.NATIVE);
        assertEquals(translation, spanishText);
    }

    @Test
    public void testTranslatedTokens() {
        //                0123456789012
        String rawText = "One.  Two.  ";
        AnnotatedText.Builder builder = new AnnotatedText.Builder().data(rawText);

        ListAttribute.Builder<Token> tokenListBuilder = new ListAttribute.Builder<>(Token.class);
        Token.Builder tbBuilder = new Token.Builder(0, 3, "One");
        tokenListBuilder.add(tbBuilder.build());
        tbBuilder = new Token.Builder(3, 4, ".");
        tokenListBuilder.add(tbBuilder.build());
        tbBuilder = new Token.Builder(6, 9, "Two");
        tokenListBuilder.add(tbBuilder.build());
        tbBuilder = new Token.Builder(9, 10, ".");
        tokenListBuilder.add(tbBuilder.build());

        builder.tokens(tokenListBuilder.build());

        ListAttribute.Builder<TranslatedTokens> translatedTokensListBuilder =
            new ListAttribute.Builder<>(TranslatedTokens.class);

        TextDomain domain = new TextDomain(ISO15924.Latn, LanguageCode.GERMAN, TransliterationScheme.NATIVE);
        TranslatedTokens.Builder ttBuilder = new TranslatedTokens.Builder(domain);
        ttBuilder.addTranslatedToken("Ein");
        ttBuilder.addTranslatedToken(".");
        ttBuilder.addTranslatedToken("Zwei");
        ttBuilder.addTranslatedToken(".");
        translatedTokensListBuilder.add(ttBuilder.build());
        domain = new TextDomain(ISO15924.Latn, LanguageCode.SPANISH, TransliterationScheme.NATIVE);
        ttBuilder = new TranslatedTokens.Builder(domain);
        ttBuilder.addTranslatedToken("Uno");
        ttBuilder.addTranslatedToken(".");
        ttBuilder.addTranslatedToken("Dos");
        ttBuilder.addTranslatedToken(".");
        translatedTokensListBuilder.add(ttBuilder.build());

        builder.translatedTokens(translatedTokensListBuilder.build());
        AnnotatedText text = builder.build();

        ListAttribute<Token> tokens = text.getTokens();
        ListAttribute<TranslatedTokens> transTokens = text.getTranslatedTokens();

        assertEquals("One", tokens.get(0).getText());
        assertEquals(".", tokens.get(1).getText());
        assertEquals("Two", tokens.get(2).getText());
        assertEquals(".", tokens.get(3).getText());

        TranslatedTokens translation = transTokens.get(0);
        domain = translation.getDomain();
        assertEquals(ISO15924.Latn, domain.getScript());
        assertEquals(LanguageCode.GERMAN, domain.getLanguage());
        assertEquals(TransliterationScheme.NATIVE, domain.getTransliterationScheme());
        List<String> translations = translation.getTranslations();
        assertEquals("Ein", translations.get(0));
        assertEquals(".", translations.get(1));
        assertEquals("Zwei", translations.get(2));
        assertEquals(".", translations.get(3));

        translation = transTokens.get(1);
        domain = translation.getDomain();
        assertEquals(ISO15924.Latn, domain.getScript());
        assertEquals(LanguageCode.SPANISH, domain.getLanguage());
        assertEquals(TransliterationScheme.NATIVE, domain.getTransliterationScheme());
        translations = translation.getTranslations();
        assertEquals("Uno", translations.get(0));
        assertEquals(".", translations.get(1));
        assertEquals("Dos", translations.get(2));
        assertEquals(".", translations.get(3));

        ttBuilder = new TranslatedTokens.Builder(domain);
        ttBuilder.translatedTokens(Lists.newArrayList("hello", "dilly"));
        MatcherAssert.assertThat(ttBuilder.build().getTranslations(), IsIterableContainingInOrder.contains("hello", "dilly"));
    }

    @Test
    public void testBaseNounPhrases() {
        //                012345678901
        String rawText = "Dog.  Book.";
        // The ARA gives bnp values in terms of token indexes.
        int[] bnpFromARA = {0, 1, 2, 3};
        int[] tokenOffsets = {0, 3, 3, 4, 6, 10, 10, 11};
        AnnotatedText.Builder builder = new AnnotatedText.Builder().data(rawText);
        ListAttribute.Builder<BaseNounPhrase> attrBuilder = new ListAttribute.Builder<>(BaseNounPhrase.class);
        for (int i = 0; i < bnpFromARA.length; i += 2) {
            BaseNounPhrase.Builder bnpBuilder = new BaseNounPhrase.Builder(tokenOffsets, bnpFromARA[i], bnpFromARA[i + 1]);
            attrBuilder.add(bnpBuilder.build());
        }
        builder.baseNounPhrases(attrBuilder.build());
        AnnotatedText text = builder.build();

        BaseNounPhrase bnp;
        assertEquals(2, text.getBaseNounPhrases().size());
        bnp = text.getBaseNounPhrases().get(0);
        assertEquals(0, bnp.getStartOffset());
        assertEquals(3, bnp.getEndOffset());
        bnp = text.getBaseNounPhrases().get(1);
        assertEquals(6, bnp.getStartOffset());
        assertEquals(10, bnp.getEndOffset());
    }

    @Test
    public void testEntityMentionsWithoutChains() {
        //                012345678901234
        String rawText = "Bill.  George.";
        AnnotatedText.Builder builder = new AnnotatedText.Builder().data(rawText);
        int[] entities = {0, 1, 65536, 2, 3, 65536};
        int[] tokenOffsets = {0, 3, 7, 13};
        ListAttribute.Builder<EntityMention> emListBuilder = new ListAttribute.Builder<>(EntityMention.class);
        for (int i = 0; i < entities.length / 3; i++) {
            int startOffset = tokenOffsets[i * 2];
            int endOffset = tokenOffsets[i * 2 + 1];
            EntityMention.Builder emBuilder = new EntityMention.Builder(startOffset, endOffset, "PERSON");
            emListBuilder.add(emBuilder.build());
        }
        builder.entityMentions(emListBuilder.build());
        AnnotatedText text = builder.build();

        Integer chainForBill = text.getEntityMentions().get(0).getCoreferenceChainId();
        Integer chainForGeorge = text.getEntityMentions().get(1).getCoreferenceChainId();
        assertNull(chainForBill);
        assertNull(chainForGeorge);
    }

    @Test
    public void testResolvedEntities() {
        //                012345678901234567890123
        String rawText = "from Boston and Chicago";
        //int[] tokenOffsets = {0, 4, 5, 11, 12, 15, 16, 23};
        AnnotatedText.Builder builder = new AnnotatedText.Builder().data(rawText);
        ListAttribute.Builder<ResolvedEntity> reListBuilder = new ListAttribute.Builder<>(ResolvedEntity.class);

        ResolvedEntity.Builder re1Builder = new ResolvedEntity.Builder(5, 11, "Q100");
        reListBuilder.add(re1Builder.build());

        ResolvedEntity.Builder re2Builder = new ResolvedEntity.Builder(16, 23, "Q1297");
        reListBuilder.add(re2Builder.build());

        builder.resolvedEntities(reListBuilder.build());
        AnnotatedText text = builder.build();

        ResolvedEntity resolvedEntity;
        assertEquals(2, text.getResolvedEntities().size());
        resolvedEntity = text.getResolvedEntities().get(0);
        assertEquals(5, resolvedEntity.getStartOffset());
        assertEquals(11, resolvedEntity.getEndOffset());
        resolvedEntity = text.getResolvedEntities().get(1);
        assertEquals(16, resolvedEntity.getStartOffset());
        assertEquals(23, resolvedEntity.getEndOffset());
    }

    @Test
    public void testEntityMentionComparison() {
        EntityMention mention1 = new EntityMention.Builder(0, 3, "PERSON").build();
        EntityMention mention2 = new EntityMention.Builder(0, 3, "PERSON").confidence(1.0).build();
        assertNotEquals(mention1, mention2);
    }

    @Test
    public void testLanguageDetectionComparison() {
        LanguageDetection.DetectionResult r1 = new LanguageDetection.DetectionResult.Builder(LanguageCode.FRENCH).build();
        LanguageDetection.DetectionResult r2 = new LanguageDetection.DetectionResult.Builder(LanguageCode.FRENCH).
            confidence(1.0).build();
        assertNotEquals(r1, r2);
    }

    @Test
    public void testCategorizerResults() {
        AnnotatedText.Builder builder = new AnnotatedText.Builder();
        ListAttribute.Builder<CategorizerResult> listBuilder
            = new ListAttribute.Builder<>(CategorizerResult.class);
        listBuilder.add(new CategorizerResult.Builder("SPORTS", 0.1).build());
        Map<String, Double> perFeatureScores = Maps.newHashMap();
        perFeatureScores.put("foo", 1.2);
        perFeatureScores.put("bar", -2.4);
        listBuilder.add(new CategorizerResult.Builder("POLITICS", -0.2)
            .confidence(0.3)
            .explanationSet(Lists.newArrayList("foo", "bar"))
            .perFeatureScores(perFeatureScores)
            .build());
        builder.categorizerResults(listBuilder.build());
        AnnotatedText text = builder.build();

        CategorizerResult r1 = text.getCategorizerResults().get(0);
        assertEquals("SPORTS", r1.getLabel());
        assertEquals(0.1, r1.getScore(), 0.000000001);
        assertNull(r1.getConfidence());
        assertNull(r1.getExplanationSet());
        assertNull(r1.getPerFeatureScores());

        CategorizerResult r2 = text.getCategorizerResults().get(1);
        assertEquals(0.3, r2.getConfidence(), 0.000000001);
        assertEquals(Lists.newArrayList("foo", "bar"), r2.getExplanationSet());
        assertEquals(-2.4, r2.getPerFeatureScores().get("bar"), 0.000000001);
    }

    @Test
    public void testSentimentResults() {
        AnnotatedText.Builder builder = new AnnotatedText.Builder();
        ListAttribute.Builder<CategorizerResult> listBuilder
            = new ListAttribute.Builder<>(CategorizerResult.class);
        listBuilder.add(new CategorizerResult.Builder("positive", 0.1).build());
        Map<String, Double> perFeatureScores = Maps.newHashMap();
        perFeatureScores.put("foo", 1.2);
        perFeatureScores.put("bar", -2.4);
        listBuilder.add(new CategorizerResult.Builder("negative", -0.2)
            .confidence(0.3)
            .explanationSet(Lists.newArrayList("foo", "bar"))
            .perFeatureScores(perFeatureScores)
            .build());
        builder.sentimentResults(listBuilder.build());
        AnnotatedText text = builder.build();

        CategorizerResult r1 = text.getSentimentResults().get(0);
        assertEquals("positive", r1.getLabel());
        assertEquals(0.1, r1.getScore(), 0.000000001);
        assertNull(r1.getConfidence());
        assertNull(r1.getExplanationSet());
        assertNull(r1.getPerFeatureScores());

        CategorizerResult r2 = text.getSentimentResults().get(1);
        assertEquals(0.3, r2.getConfidence(), 0.000000001);
        assertEquals(Lists.newArrayList("foo", "bar"), r2.getExplanationSet());
        assertEquals(-2.4, r2.getPerFeatureScores().get("bar"), 0.000000001);
    }

    @Test
    public void documentMetadata() throws Exception {
        AnnotatedText.Builder builder = new AnnotatedText.Builder();
        final ArrayList<String> listOfStrings = Lists.newArrayList("value1", "value2");
        builder.documentMetadata("key", listOfStrings);
        AnnotatedText text = builder.build();
        assertEquals(listOfStrings, text.getDocumentMetadata().get("key"));
        assertNotSame(listOfStrings, text.getDocumentMetadata().get("key"));
        builder = new AnnotatedText.Builder();
        Map<String, List<String>> wholeMap = Maps.newHashMap();
        wholeMap.put("key", listOfStrings);
        builder.documentMetadata(wholeMap);
        text = builder.build();
        assertEquals(listOfStrings, text.getDocumentMetadata().get("key"));
        assertNotSame(listOfStrings, text.getDocumentMetadata().get("key"));
        assertNotSame(wholeMap, text.getDocumentMetadata());
        builder = new AnnotatedText.Builder();
        builder.documentMetadata("key", "value");
        text = builder.build();
        assertEquals("value", text.getDocumentMetadata().get("key").get(0));
    }

    @Test
    public void resolvedEntity() throws Exception {
        ResolvedEntity re = new ResolvedEntity.Builder(0, 10, "foo").build();
        assertNull(re.getConfidence());
        assertNull(re.getCoreferenceChainId());
        re = new ResolvedEntity.Builder(0, 10, "foo").coreferenceChainId(22).build();
        assertEquals(Integer.valueOf(22), re.getCoreferenceChainId());
        re = new ResolvedEntity.Builder(0, 10, "foo").confidence(3d).build();
        assertEquals(3d, re.getConfidence(), 0);
    }

    @Test
    public void resolvedEntityWithSentiment() throws Exception {
        CategorizerResult sentiment = new CategorizerResult.Builder("positive", null)
            .confidence(0.9).build();
        ResolvedEntity re = new ResolvedEntity.Builder(0, 10, "foo")
            .sentiment(sentiment).build();
        assertNull(re.getConfidence());
        assertNull(re.getCoreferenceChainId());
        assertEquals("positive", re.getSentiment().getLabel());
        assertEquals(0.9, re.getSentiment().getConfidence(), 0);
    }

    @Test
    public void relationMentions() {
        String argumentId = "/resolved/argument/";
        String argPhrase = "some-noun";
        String relPhrase = "some-verb"; //

        AnnotatedText.Builder relationMentionBuilder = new AnnotatedText.Builder();
        ListAttribute.Builder<RelationshipMention> listBuilder
                = new ListAttribute.Builder<>(RelationshipMention.class);
        RelationshipComponent.Builder relationArgumentBuilder1 = new RelationshipComponent.Builder();
        RelationshipComponent.Builder relationArgumentBuilder2 = new RelationshipComponent.Builder();
        RelationshipComponent.Builder relationArgumentBuilder3 = new RelationshipComponent.Builder();
        relationArgumentBuilder1.phrase(argPhrase + "1").identifier(argumentId + "1");
        relationArgumentBuilder2.phrase(argPhrase + "2").identifier(argumentId + "2");
        relationArgumentBuilder3.phrase(relPhrase);

        listBuilder.add(new RelationshipMention.Builder(0, 12).predicate(relationArgumentBuilder3.build())
                .arg1(relationArgumentBuilder1.build()).arg2(relationArgumentBuilder2.build()).build());
        relationMentionBuilder.relationshipMentions(listBuilder.build());
        AnnotatedText text = relationMentionBuilder.build();

        RelationshipMention relationshipMention = text.getRelationshipMentions().get(0);
        assertEquals(relPhrase, relationshipMention.getPredicate().getPhrase());
        assertEquals(argPhrase + "1", relationshipMention.getArg1().getPhrase());
        assertEquals(argPhrase + "2", relationshipMention.getArg2().getPhrase());
    }

    @Test
    public void tokenLists() throws Exception {
        Token.Builder tokenBuilder = new Token.Builder(0, 1, "f");
        tokenBuilder.addNormalized("c");
        tokenBuilder.normalized(Lists.newArrayList("a", "b"));
        MatcherAssert.assertThat(tokenBuilder.build().getNormalized(), IsIterableContainingInOrder.contains("a", "b"));

        MorphoAnalysis.Builder maBuilder = new MorphoAnalysis.Builder();
        tokenBuilder = new Token.Builder(0, 1, "f");
        tokenBuilder.addAnalysis(maBuilder.build());
        tokenBuilder.analyses(Lists.<MorphoAnalysis>newArrayList());
        assertNull(tokenBuilder.build().getAnalyses());

        tokenBuilder = new Token.Builder(0, 1, "f");
        tokenBuilder.analyses(null);
        tokenBuilder.addAnalysis(maBuilder.build());
        assertEquals(1, tokenBuilder.build().getAnalyses().size());
    }

    @Test
    public void morphoAnalysisLists() throws Exception {
        MorphoAnalysis.Builder builder = new MorphoAnalysis.Builder();
        Token.Builder tokenBuilder = new Token.Builder(0, 1, "d");
        builder.addComponent(tokenBuilder.build())
                .components(Lists.<Token>newArrayList());
        assertNull(builder.build().getComponents());

        HanMorphoAnalysis.Builder hmaBuilder = new HanMorphoAnalysis.Builder();
        hmaBuilder.lemma("foo").addReading("bloop");
        hmaBuilder.readings(Lists.<String>newArrayList());
        assertNull(hmaBuilder.build().getReadings());

        KoreanMorphoAnalysis.Builder kmaBuilder = new KoreanMorphoAnalysis.Builder();
        kmaBuilder.addMorpheme("mor", "pheme");
        kmaBuilder.morphemes(Lists.<String>newArrayList(), Lists.<String>newArrayList());
        assertNull(kmaBuilder.build().getMorphemes());
        assertNull(kmaBuilder.build().getMorphemeTags());
    }

    @Test
    public void morphoBuilderOrder() {
        // RD-205
        assertEquals(new HanMorphoAnalysis.Builder().lemma("lemma").addReading("reading").build(),
            new HanMorphoAnalysis.Builder().addReading("reading").lemma("lemma").build());
        assertEquals(new KoreanMorphoAnalysis.Builder().lemma("lemma").addMorpheme("morpheme", "tag").build(),
            new KoreanMorphoAnalysis.Builder().addMorpheme("morpheme", "tag").lemma("lemma").build());
        assertEquals(new ArabicMorphoAnalysis.Builder().lemma("lemma").addPrefix("prefix", "tag").build(),
            new ArabicMorphoAnalysis.Builder().addPrefix("prefix", "tag").lemma("lemma").build());
    }

    @Test(expected = NullPointerException.class)
    public void nullDataIsNull() {
        new AnnotatedText.Builder().build().getData().length();
    }
}
