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
package com.basistech.rosette.dm;

import com.basistech.util.ISO15924;
import com.basistech.util.LanguageCode;
import com.google.common.collect.Lists;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test {@link Object#equals(Object)} and {@link Object#hashCode()} methods across the DM.
 * It seems clear that really comprehensive testing here should be automated somehow, either
 * with a code generator or some reflection. So, while the {@link #morphAnalysisBase()} is
 * manually comprehensive, the rest are focused on areas where we know that we have some
 * null pointer exposure. The first method could be thought of as a target for future automation.
 * <p/>
 * Note the extensive use of {@link org.junit.Assert#assertFalse} and {@link org.junit.Assert#assertTrue} in here.
 * First, this makes it explicit that we are, in fact, testing the result of {@link Object#equals(Object)}.
 * Then, in cases like {@code assertFalse(x1.hashCode() == x2.hashCode())}, neither value is 'correct', so
 * this seems more explicit to me than some use of {@code assertNotSame}.
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "deprecation"})
public class EqualsTest {

    /*
     * For testing morph analysis, assume that we can use quite basic tokens inside, since
     * we'll be testing token equality in detail further down.
     */
    private void componentList(MorphoAnalysis.Builder maBuilder, String ... strings) {
        for (String text : strings) {
            maBuilder.addComponent(new Token.Builder(0, text.length(), text).build());
        }
    }

    //CHECKSTYLE:OFF
    @Test
    public void morphAnalysisBase() throws Exception {
        MorphoAnalysis.Builder maBuilder = new MorphoAnalysis.Builder();
        componentList(maBuilder, "beam", "post");
        maBuilder.lemma("orange");
        maBuilder.partOfSpeech("woof");
        maBuilder.raw("cooked");
        MorphoAnalysis ma1 = maBuilder.build();

        assertTrue(ma1.equals(ma1));
        ma1.hashCode(); // please don't throw.

        maBuilder = new MorphoAnalysis.Builder();
        componentList(maBuilder, "door", "post");
        maBuilder.lemma("orange");
        maBuilder.partOfSpeech("woof");
        maBuilder.raw("cooked");
        MorphoAnalysis ma2 = maBuilder.build();
        ma2.hashCode();
        assertFalse(ma1.hashCode() == ma2.hashCode());

        assertFalse(ma1.equals(ma2));

        maBuilder = new MorphoAnalysis.Builder();
        componentList(maBuilder, "beam", "post");
        maBuilder.lemma("pear");
        maBuilder.partOfSpeech("woof");
        maBuilder.raw("cooked");
        ma2 = maBuilder.build();
        ma2.hashCode();
        assertFalse(ma1.hashCode() == ma2.hashCode());

        assertFalse(ma1.equals(ma2));

        maBuilder = new MorphoAnalysis.Builder();
        componentList(maBuilder, "beam", "post");
        maBuilder.lemma("orange");
        maBuilder.partOfSpeech("meow");
        maBuilder.raw("cooked");
        ma2 = maBuilder.build();
        ma2.hashCode();
        assertFalse(ma1.hashCode() == ma2.hashCode());

        assertFalse(ma1.equals(ma2));

        maBuilder = new MorphoAnalysis.Builder();
        componentList(maBuilder, "beam", "post");
        maBuilder.lemma("orange");
        maBuilder.partOfSpeech("woof");
        maBuilder.raw("hide");
        ma2 = maBuilder.build();
        ma2.hashCode();
        assertFalse(ma1.hashCode() == ma2.hashCode());

        assertFalse(ma1.equals(ma2));

        // ok, that's all the one-field-differences.

        // null tests

        maBuilder = new MorphoAnalysis.Builder();
        // components null
        maBuilder.lemma("orange");
        maBuilder.partOfSpeech("woof");
        maBuilder.raw("cooked");
        ma2 = maBuilder.build();
        assertNull(ma2.getComponents());
        ma2.hashCode();
        assertFalse(ma1.hashCode() == ma2.hashCode());

        assertFalse(ma1.equals(ma2));
        assertFalse(ma2.equals(ma1));

        maBuilder = new MorphoAnalysis.Builder();
        componentList(maBuilder, "beam", "post");
        maBuilder.lemma(null);
        maBuilder.partOfSpeech("woof");
        maBuilder.raw("cooked");
        ma2 = maBuilder.build();
        ma2.hashCode();
        assertFalse(ma1.hashCode() == ma2.hashCode());

        assertFalse(ma1.equals(ma2));
        assertFalse(ma2.equals(ma1));

        maBuilder = new MorphoAnalysis.Builder();
        componentList(maBuilder, "beam", "post");
        maBuilder.lemma("orange");
        maBuilder.partOfSpeech(null);
        maBuilder.raw("cooked");
        ma2 = maBuilder.build();
        ma2.hashCode();
        assertFalse(ma1.hashCode() == ma2.hashCode());

        assertFalse(ma1.equals(ma2));
        assertFalse(ma2.equals(ma1));

        maBuilder = new MorphoAnalysis.Builder();
        componentList(maBuilder, "beam", "post");
        maBuilder.lemma("orange");
        maBuilder.partOfSpeech("woof");
        maBuilder.raw(null);
        ma2 = maBuilder.build();
        ma2.hashCode();
        assertFalse(ma1.hashCode() == ma2.hashCode());

        assertFalse(ma1.equals(ma2));
        assertFalse(ma2.equals(ma1));
    }

    @Test
    public void hanMorphoAnalysis() throws Exception {
        HanMorphoAnalysis.Builder maBuilder = new HanMorphoAnalysis.Builder();
        maBuilder.addReading("r1");
        HanMorphoAnalysis ma1 = maBuilder.build();
        assertTrue(ma1.equals(ma1));
        ma1.hashCode();

        maBuilder = new HanMorphoAnalysis.Builder();
        maBuilder.addReading("r2");
        HanMorphoAnalysis ma2 = maBuilder.build();
        assertFalse(ma1.equals(ma2));
        assertFalse(ma2.equals(ma1));
        ma2.hashCode();
        assertFalse(ma1.hashCode() == ma2.hashCode());

        maBuilder = new HanMorphoAnalysis.Builder();
        // leave reading null
        ma2 = maBuilder.build();
        assertNull(ma2.getReadings());
        assertFalse(ma1.equals(ma2));
        assertFalse(ma2.equals(ma1));
        ma2.hashCode();
        assertFalse(ma1.hashCode() == ma2.hashCode());
    }

    @Test
    public void korMorphoAnalysis() throws Exception {
        KoreanMorphoAnalysis.Builder maBuilder = new KoreanMorphoAnalysis.Builder();
        maBuilder.addMorpheme("m1", "t1");
        KoreanMorphoAnalysis ma1 = maBuilder.build();
        assertTrue(ma1.equals(ma1));
        ma1.hashCode();

        maBuilder = new KoreanMorphoAnalysis.Builder();
        maBuilder.addMorpheme("m2", "t2");
        KoreanMorphoAnalysis ma2 = maBuilder.build();
        assertFalse(ma1.equals(ma2));
        assertFalse(ma2.equals(ma1));
        ma2.hashCode();
        assertFalse(ma1.hashCode() == ma2.hashCode());

        maBuilder = new KoreanMorphoAnalysis.Builder();
        // leave reading null
        ma2 = maBuilder.build();
        assertNull(ma2.getMorphemes());
        assertNull(ma2.getMorphemeTags());
        assertFalse(ma1.equals(ma2));
        assertFalse(ma2.equals(ma1));
        ma2.hashCode();
        assertFalse(ma1.hashCode() == ma2.hashCode());
    }

    @Test
    public void token() throws Exception {
        Token.Builder tokBuilder = new Token.Builder(0, 10, "token");
        tokBuilder.addNormalized("norm");
        MorphoAnalysis.Builder maBuilder = new MorphoAnalysis.Builder();
        componentList(maBuilder, "beam", "post");
        maBuilder.lemma("orange");
        maBuilder.partOfSpeech("woof");
        maBuilder.raw("cooked");
        MorphoAnalysis ma1 = maBuilder.build();
        tokBuilder.addAnalysis(ma1);
        tokBuilder.source("nile");
        Token tok1 = tokBuilder.build();
        tok1.hashCode();
        assertTrue(tok1.equals(tok1));

        tokBuilder = new Token.Builder(0, 10, "token");
        // no normalized
        tokBuilder.addAnalysis(ma1);
        tokBuilder.source("nile");
        Token tok2 = tokBuilder.build();
        assertFalse(tok1.equals(tok2));
        assertFalse(tok2.equals(tok1));
        assertFalse(tok1.hashCode() == tok2.hashCode());

        tokBuilder = new Token.Builder(0, 10, "token");
        tokBuilder.addNormalized("norm");
        // no analysis
        tokBuilder.source("nile");
        tok2 = tokBuilder.build();
        assertFalse(tok1.equals(tok2));
        assertFalse(tok2.equals(tok1));
        assertFalse(tok1.hashCode() == tok2.hashCode());
    }

    @Test
    public void entity() throws Exception {
        Entity e1 = new Entity.Builder()
            .confidence(1.0)
            .entityId("entity-id")
            .headMentionIndex(0)
            .salience(1.0)
            .type("type1")
            .mention(new Mention.Builder(1, 2).normalized("n1").source("s1").build())
            .mention(new Mention.Builder(3, 4).normalized("n1").source("s1").build())
            .build();

        Entity e2 = new Entity.Builder(e1).build();

        assertEquals(e1, e2);
        assertEquals(e1.getMentions(), e2.getMentions());
    }

    @Test
    public void mention() {
        Mention m1 = new Mention.Builder(2, 9)
            .confidence(1.0)
            .linkingConfidence(1.0)
            .normalized("nn")
            .source("s1")
            .subsource("ss")
            .build();

        Mention m2 = new Mention.Builder(m1).build();

        assertEquals(m1, m2);

        // these 2 should match entirely.
        assertTrue(m1.startOffset == m2.startOffset
            && m1.endOffset == m2.endOffset
            && m1.getLinkingConfidence().equals(m2.getLinkingConfidence())
            && m1.getConfidence().equals(m2.getConfidence())
            && m1.getNormalized().equals(m2.getNormalized())
            && m1.getSource().equals(m2.getSource())
            && m1.getSubsource().equals(m2.getSubsource())
        );
    }

    @Test
    public void entityMention() throws Exception {
        EntityMention.Builder emBuilder = new EntityMention.Builder(0, 10, "something");
        emBuilder.confidence(Double.MIN_VALUE);
        emBuilder.coreferenceChainId(42);
        emBuilder.flags(3);
        emBuilder.source("nile");
        emBuilder.subsource("alexandria");
        emBuilder.normalized("ab");
        EntityMention em1 = emBuilder.build();
        em1.hashCode();
        assertTrue(em1.equals(em1));

        emBuilder = new EntityMention.Builder(0, 10, "something");
        // no confidence
        emBuilder.coreferenceChainId(42);
        emBuilder.flags(3);
        emBuilder.source("nile");
        emBuilder.subsource("alexandria");
        emBuilder.normalized("ab");
        EntityMention em2 = emBuilder.build();
        assertNull(em2.getConfidence());
        assertFalse(em1.equals(em2));
        assertFalse(em2.equals(em1));
        assertFalse(em1.hashCode() == em2.hashCode());

        emBuilder = new EntityMention.Builder(0, 10, "something");
        emBuilder.confidence(Double.MIN_VALUE);
        // no coref
        emBuilder.flags(3);
        emBuilder.source("nile");
        emBuilder.subsource("alexandria");
        emBuilder.normalized("ab");
        em2 = emBuilder.build();
        assertNull(em2.getCoreferenceChainId());
        assertFalse(em1.equals(em2));
        assertFalse(em2.equals(em1));
        assertFalse(em1.hashCode() == em2.hashCode());

        emBuilder = new EntityMention.Builder(0, 10, "something");
        emBuilder.confidence(Double.MIN_VALUE);
        emBuilder.coreferenceChainId(42);
        // no flags.
        emBuilder.source("nile");
        emBuilder.subsource("alexandria");
        emBuilder.normalized("ab");
        em2 = emBuilder.build();
        assertNull(em2.getFlags());
        assertFalse(em1.equals(em2));
        assertFalse(em2.equals(em1));
        assertFalse(em1.hashCode() == em2.hashCode());
    }

    @Test
    public void languageDetection() throws Exception {
        LanguageDetection.DetectionResult.Builder lddrBuilder = new LanguageDetection.DetectionResult.Builder(LanguageCode.ESTONIAN);
        lddrBuilder.encoding("aes");
        lddrBuilder.script(ISO15924.Armn);
        lddrBuilder.confidence(Double.MIN_VALUE);
        LanguageDetection.DetectionResult dr1 = lddrBuilder.build();
        dr1.hashCode();
        assertTrue(dr1.equals(dr1));

        lddrBuilder = new LanguageDetection.DetectionResult.Builder(LanguageCode.ESTONIAN);
        lddrBuilder.encoding("aes");
        lddrBuilder.script(ISO15924.Armn);
        // no confidence
        LanguageDetection.DetectionResult dr2 = lddrBuilder.build();
        assertNull(dr2.getConfidence());
        assertFalse(dr1.equals(dr2));
        assertFalse(dr2.equals(dr1));
        assertFalse(dr1.hashCode() == dr2.hashCode());
    }

    @Test
    public void testArabicMorphoBuilderReuse() throws Exception {
        // COMN-101
        ArabicMorphoAnalysis.Builder builder = new ArabicMorphoAnalysis.Builder();
        ArabicMorphoAnalysis analysis1 = builder.build();
        Token token = new Token.Builder(0, 10, "token").build();
        builder.addComponent(token);
        ArabicMorphoAnalysis analysis2 = builder.build();
        assertFalse(analysis1.equals(analysis2));
    }

    @Test
    public void testArabicMorphoToStringIncludesBaseClass() throws Exception {
        // COMN-101
        ArabicMorphoAnalysis.Builder builder = new ArabicMorphoAnalysis.Builder();
        Token token = new Token.Builder(0, 10, "token").build();
        builder.addComponent(token);
        ArabicMorphoAnalysis analysis = builder.build();
        assertTrue(analysis.toString().contains("components"));
    }

    @Test
    public void testCategorizerResults() throws Exception {
        CategorizerResult cr1 = new CategorizerResult.Builder("POLITICS", -0.2)
            .confidence(0.1).build();
        cr1.hashCode();
        assertTrue(cr1.equals(cr1));

        CategorizerResult cr2 = new CategorizerResult.Builder("POLITICS", -0.2).build();
        assertNull(cr2.getConfidence());
        assertFalse(cr1.equals(cr2));
        assertFalse(cr2.equals(cr1));
        assertFalse(cr1.hashCode() == cr2.hashCode());
    }

    @Test
    public void testResolvedEntity() throws Exception {
        ResolvedEntity re1 = new ResolvedEntity.Builder(0, 10, "foo").build();
        re1.hashCode();
        assertTrue(re1.equals(re1));

        ResolvedEntity re2 = new ResolvedEntity.Builder(0, 10, "foo").coreferenceChainId(1).build();
        assertFalse(re2.equals(re1));

        re1 = new ResolvedEntity.Builder(0, 10, "foo").confidence(1.0d).build();
        re1.hashCode();
        assertTrue(re1.equals(re1));

        re2 = new ResolvedEntity.Builder(0, 10, "foo").confidence(2.0d).build();
        assertFalse(re1.equals(re2));

        re1 = new ResolvedEntity.Builder(0, 10, "foo").coreferenceChainId(3).build();
        re1.hashCode();
        assertTrue(re1.equals(re1));

        re1 = new ResolvedEntity.Builder(0, 10, "foo").confidence(1.0d).coreferenceChainId(3).build();
        re1.hashCode();
        assertTrue(re1.equals(re1));

        CategorizerResult sentiment = new CategorizerResult.Builder("positive", null).confidence(0.9).build();
        re1 = new ResolvedEntity.Builder(0, 10, "foo").confidence(1.0d).coreferenceChainId(3)
            .sentiment(sentiment).build();
        re1.hashCode();
        assertTrue(re1.equals(re1));
    }

    @Test
    public void testEvidence() throws Exception {
        Extent e1 = new Extent.Builder(0,1).build();
        e1.hashCode();
        assertTrue(e1.equals(e1));

        Extent e2 = new Extent.Builder(0,2).build();
        e2.hashCode();
        assertTrue(e2.equals(e2));

        assertFalse(e1.equals(e2));
        assertFalse(e2.equals(e1));
        assertFalse(e1.hashCode() == e2.hashCode());
    }

    @Test
    public void relationArgument() throws Exception {
        // this one has offsets, phrase and an argid,
        RelationshipComponent ra1 = new RelationshipComponent.Builder()
                .phrase("t")
                .identifier("1").build();
        ra1.hashCode();
        assertTrue(ra1.equals(ra1));

        // this one doesn't have an argid
        RelationshipComponent ra2 = new RelationshipComponent.Builder().phrase("t").build();
        ra2.hashCode();
        assertTrue(ra2.equals(ra2));

        assertNull(ra2.getIdentifier());
        assertFalse(ra1.equals(ra2));
        assertFalse(ra2.equals(ra1));
        assertFalse(ra1.hashCode() == ra2.hashCode());

        // this one has evidence
        ra2 = new RelationshipComponent.Builder()
                .phrase("t")
                .identifier("1")
                .extents(Lists.newArrayList(new Extent.Builder(0, 1).build()))
                .build();
        ra2.hashCode();
        assertTrue(ra2.equals(ra2));

        assertFalse(ra1.equals(ra2));
        assertFalse(ra2.equals(ra1));
        assertFalse(ra1.hashCode() == ra2.hashCode());
    }

    @Test
    public void relationMention() throws Exception {
        RelationshipComponent _ra1 = new RelationshipComponent.Builder().phrase("t").identifier("1").build();
        RelationshipComponent _ra2 = new RelationshipComponent.Builder().phrase("t").build();
        RelationshipComponent _ra3 = new RelationshipComponent.Builder().phrase("b").build();
        RelationshipComponent _p = new RelationshipComponent.Builder().phrase("p").build();
        RelationshipComponent _p2 = new RelationshipComponent.Builder().phrase("p")
                .extents(Lists.newArrayList(new Extent.Builder(2, 3).build())).build();

        // relId intentionally null, all other fields populated.
        RelationshipMention rm1 = new RelationshipMention.Builder(0, 12).predicate(_p).arg1(_ra1).arg2
                (_ra2).build();
        rm1.hashCode();
        assertTrue(rm1.equals(rm1));

        // this guy has different arguments defined
        RelationshipMention rm2 = new RelationshipMention.Builder(0, 12).predicate(_p).arg1(_ra3).build();
        rm2.hashCode();
        assertTrue(rm2.equals(rm2));
        assertFalse(rm1.equals(rm2));
        assertFalse(rm2.equals(rm1));
        assertFalse(rm1.hashCode() == rm2.hashCode());

        rm2 = new RelationshipMention.Builder(0, 12).predicate(_p2).arg1(_ra1).arg2(_ra2).build();

        rm2.hashCode();
        assertTrue(rm2.equals(rm2));
        assertFalse(rm1.equals(rm2));
        assertFalse(rm2.equals(rm1));
        assertFalse(rm1.hashCode() == rm2.hashCode());

        // this guy has relId and source
        RelationshipMention rm3 = new RelationshipMention.Builder(0, 12).predicate(_p2).arg1(_ra3).source("statistical rules:42").build();
        rm2.hashCode();
        assertTrue(rm3.equals(rm3));
        assertFalse(rm3.equals(rm2));
        assertFalse(rm3.equals(rm1));
        assertFalse(rm3.hashCode() == rm2.hashCode());
    }

    @Test
    public void dependency() throws Exception {
        // This is minimal -- this class is too simple to be anxious about.
        Dependency d1 = new Dependency.Builder("conj", 0, 1).build();
        Dependency d2 = new Dependency.Builder("conj", 1, 3).build();
        assertFalse(d1.equals(d2));
        assertFalse(d1.hashCode() == d2.hashCode());
        assertTrue(d1.equals(d1));

    }

    @Test
    public void transliteration() throws Exception {
        TransliterationResults t1 = new TransliterationResults.Builder()
                .addTransliteration(LanguageCode.ENGLISH, Transliteration.of(ISO15924.Latn, "one"))
                .build();

        TransliterationResults t2 = new TransliterationResults.Builder()
                .addTransliteration(LanguageCode.ENGLISH, Transliteration.of(ISO15924.Latn, "one"))
                .addTransliteration(LanguageCode.ENGLISH, Transliteration.of(ISO15924.Arab, "one"))
                .build();

        TransliterationResults t3 = new TransliterationResults.Builder()
                .addTransliteration(LanguageCode.ENGLISH, Transliteration.of(ISO15924.Latn, "on3"))
                .build();

        TransliterationResults t4 = new TransliterationResults.Builder()
                .addTransliteration(LanguageCode.ARABIC, Transliteration.of(ISO15924.Latn, "one"))
                .build();

        TransliterationResults t5 = new TransliterationResults.Builder()
                .addTransliteration(LanguageCode.ENGLISH, Transliteration.of(ISO15924.Arab, "one"))
                .build();

        // All different
        assertFalse(t1.equals(t2));
        assertFalse(t1.equals(t3));
        assertFalse(t1.equals(t4));
        assertFalse(t1.equals(t5));
        assertFalse(t2.equals(t3));
        assertFalse(t2.equals(t4));
        assertFalse(t2.equals(t5));
        assertFalse(t3.equals(t4));
        assertFalse(t3.equals(t5));
        assertFalse(t4.equals(t5));

        // All the same
        assertTrue(t1.equals(t1));
        assertTrue(t2.equals(t2));
        assertTrue(t3.equals(t3));
        assertTrue(t4.equals(t4));
        assertTrue(t5.equals(t5));
    }



}
