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

package com.basistech.rosette.dm.json.plain;

import com.basistech.rosette.dm.AnnotatedText;
import com.basistech.rosette.dm.ArabicMorphoAnalysis;
import com.basistech.rosette.dm.BaseAttribute;
import com.basistech.rosette.dm.BaseNounPhrase;
import com.basistech.rosette.dm.CategorizerResult;
import com.basistech.rosette.dm.EntityMention;
import com.basistech.rosette.dm.Extent;
import com.basistech.rosette.dm.HanMorphoAnalysis;
import com.basistech.rosette.dm.KoreanMorphoAnalysis;
import com.basistech.rosette.dm.LanguageDetection;
import com.basistech.rosette.dm.ListAttribute;
import com.basistech.rosette.dm.MorphoAnalysis;
import com.basistech.rosette.dm.RelationshipComponent;
import com.basistech.rosette.dm.RelationshipMention;
import com.basistech.rosette.dm.ResolvedEntity;
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
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;

/**
 *
 */
//CHECKSTYLE:OFF
public class JsonTest extends AdmAssert {

    public static final String THIS_IS_THE_TERRIER_SHOT_TO_BOSTON = "This is the terrier shot to Boston.";
    private BaseNounPhrase baseNounPhrase;
    private EntityMention entityMention;
    private RelationshipMention relationshipMention;
    private ResolvedEntity resolvedEntity;
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

        ListAttribute.Builder<EntityMention> emListBuilder = new ListAttribute.Builder<>(EntityMention.class);
        EntityMention.Builder emBuilder = new EntityMention.Builder(27, 33, "place");
        emBuilder.flags(42);
        emBuilder.normalized("bahston");
        emBuilder.source("testsource");
        emBuilder.subsource("testsubsource");
        emBuilder.confidence(1.0);
        emBuilder.coreferenceChainId(43);
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

        ListAttribute.Builder<ResolvedEntity> reListBuilder = new ListAttribute.Builder<>(ResolvedEntity.class);
        ResolvedEntity.Builder reBuilder = new ResolvedEntity.Builder(27, 33, "Q100");
        reBuilder.coreferenceChainId(43);
        reBuilder.confidence(1.0);
        reBuilder.sentimentCategory("positive");
        reBuilder.sentimentConfidence(1.0);
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

        ListAttribute<EntityMention> emList = read.getEntityMentions();
        assertNotNull(emList);
        assertEquals(1, emList.size());
        EntityMention em = emList.get(0);
        assertEquals(entityMention, em);

        ListAttribute<RelationshipMention> rmList = read.getRelationshipMentions();
        assertNotNull(rmList);
        assertEquals(1, rmList.size());
        RelationshipMention rm = rmList.get(0);
        assertEquals(relationshipMention, rm);

        ListAttribute<ResolvedEntity> resolvedEntityList = read.getResolvedEntities();
        assertNotNull(resolvedEntityList);
        assertEquals(1, resolvedEntityList.size());
        ResolvedEntity e = resolvedEntityList.get(0);
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
        assertEquals(Integer.valueOf(4), novelty.getExtendedProperties().get("startOffset"));
        assertEquals(Integer.valueOf(8), novelty.getExtendedProperties().get("endOffset"));
        assertEquals("pari", novelty.getExtendedProperties().get("color"));
    }

    @Test
    public void testForwardCompatibilityNoisy() throws Exception {
        ObjectMapper mapper = objectMapper();
        JsonNode tree = mapper.valueToTree(referenceText);
        ObjectNode attributes = (ObjectNode) tree.get("attributes");
        ObjectNode extendedObject = attributes.putObject("novelty");
        extendedObject.put("type", "novelty");
        extendedObject.put("startOffset", 4);
        extendedObject.put("endOffset", 8);
        extendedObject.put("color", "pari");

        AnnotatedText read = mapper.treeToValue(tree, AnnotatedText.class);
        BaseAttribute novelty = read.getAttributes().get("novelty");
        assertNotNull(novelty);
        assertEquals(Integer.valueOf(4), novelty.getExtendedProperties().get("startOffset"));
        assertEquals(Integer.valueOf(8), novelty.getExtendedProperties().get("endOffset"));
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
        assertEquals(Integer.valueOf(4), item.getExtendedProperties().get("startOffset"));
        assertEquals(Integer.valueOf(8), item.getExtendedProperties().get("endOffset"));
        assertEquals("pari", item.getExtendedProperties().get("color"));

        item = novelty.get(1);
        assertEquals(Integer.valueOf(10), item.getExtendedProperties().get("startOffset"));
        assertEquals(Integer.valueOf(12), item.getExtendedProperties().get("endOffset"));
        assertEquals("off", item.getExtendedProperties().get("color"));

    }

    @Test
    public void emptyAdm() throws Exception {
        /* We want to be able to read an empty object as an ADM */
        /* No asserts needed, what we need here is a lack of a throw. */
        objectMapper().readValue("{}", AnnotatedText.class).toString();
    }
}
