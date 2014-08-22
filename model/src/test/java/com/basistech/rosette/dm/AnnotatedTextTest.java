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
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AnnotatedTextTest {

    @Test
    public void testSentenceBoundaries() {
        //                0123456789012
        String rawText = "One.  Two.  ";
        AnnotatedText.Builder builder = new AnnotatedText.Builder().data(rawText);

        ListAttribute.Builder<Sentence> sentenceListBuilder = new ListAttribute.Builder<Sentence>(Sentence.class);
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
        String translatedText = "Ein.  Zwei.";
        AnnotatedText.Builder builder = new AnnotatedText.Builder().data(rawText);

        TextDomain domain = new TextDomain(ISO15924.Latn, LanguageCode.GERMAN, TransliterationScheme.NATIVE);
        TranslatedData.Builder tdBuilder = new TranslatedData.Builder(domain, translatedText);

        builder.translatedData(tdBuilder.build());
        AnnotatedText text = builder.build();

        TranslatedData data = text.getTranslatedData();
        TextDomain dataDomain = data.getDomain();
        ISO15924 script = dataDomain.getScript();
        LanguageCode language = dataDomain.getLanguage();
        TransliterationScheme scheme = dataDomain.getTransliterationScheme();
        String translation = data.getTranslation();

        assertEquals(script, ISO15924.Latn);
        assertEquals(language, LanguageCode.GERMAN);
        assertEquals(scheme, TransliterationScheme.NATIVE);
        assertEquals(translation, translatedText);
    }

    @Test
    public void testTranslatedTokens() {
        //                0123456789012
        String rawText = "One.  Two.  ";
        AnnotatedText.Builder builder = new AnnotatedText.Builder().data(rawText);

        ListAttribute.Builder<Token> tokenListBuilder = new ListAttribute.Builder<Token>(Token.class);
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
            new ListAttribute.Builder<TranslatedTokens>(TranslatedTokens.class);

        TextDomain domain = new TextDomain(ISO15924.Latn, LanguageCode.GERMAN, TransliterationScheme.NATIVE);
        List<String> germanTranslations = Lists.newArrayList();
        germanTranslations.add("Ein");
        germanTranslations.add(".");
        germanTranslations.add("Zwei");
        germanTranslations.add(".");
        TranslatedTokens.Builder ttBuilder = new TranslatedTokens.Builder(domain, germanTranslations);
        translatedTokensListBuilder.add(ttBuilder.build());
        domain = new TextDomain(ISO15924.Latn, LanguageCode.SPANISH, TransliterationScheme.NATIVE);
        List<String> spanishTranslations = Lists.newArrayList();
        spanishTranslations.add("Uno");
        spanishTranslations.add(".");
        spanishTranslations.add("Dos");
        spanishTranslations.add(".");
        ttBuilder = new TranslatedTokens.Builder(domain, spanishTranslations);
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
    }

    @Test
    public void testBaseNounPhrases() {
        //                012345678901
        String rawText = "Dog.  Book.";
        // The ARA gives bnp values in terms of token indexes.
        int[] bnpFromARA = {0, 1, 2, 3};
        int[] tokenOffsets = {0, 3, 3, 4, 6, 10, 10, 11};
        AnnotatedText.Builder builder = new AnnotatedText.Builder().data(rawText);
        // TODO: we want to use a .class here for Generic reasons, but we want to end up with a vanilla String in the Json.
        ListAttribute.Builder<BaseNounPhrase> attrBuilder = new ListAttribute.Builder<BaseNounPhrase>(BaseNounPhrase.class);
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
        ListAttribute.Builder<EntityMention> emListBuilder = new ListAttribute.Builder<EntityMention>(EntityMention.class);
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
        ListAttribute.Builder<ResolvedEntity> reListBuilder = new ListAttribute.Builder<ResolvedEntity>(ResolvedEntity.class);

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

}
