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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
        ListAttribute.Builder<EntityMention> entityListBuilder = new ListAttribute.Builder<EntityMention>(EntityMention.class);
        for (int i = 0; i < entities.length / 3; i++) {
            int startOffset = tokenOffsets[i * 2];
            int endOffset = tokenOffsets[i * 2 + 1];
            EntityMention.Builder emBuilder = new EntityMention.Builder(startOffset, endOffset, "PERSON");
            entityListBuilder.add(emBuilder.build());
        }
        builder.entityMentions(entityListBuilder.build());
        AnnotatedText text = builder.build();

        int chainForBill = text.getEntityMentions().get(0).getCoreferenceChainId();
        int chainForGeorge = text.getEntityMentions().get(1).getCoreferenceChainId();
        assertEquals(-1, chainForBill);
        assertEquals(-1, chainForGeorge);
    }

    @Test
    public void testEntities() {
        String rawText = "from Boston and Chicago";
        int[] tokenOffsets = {0, 4, 5, 11, 12, 15, 16, 23};
        AnnotatedText.Builder builder = new AnnotatedText.Builder().data(rawText);
        ListAttribute.Builder<Entity> entityListBuilder = new ListAttribute.Builder<Entity>(Entity.class);

        Entity.Builder entity1Builder = new Entity.Builder(5, 11, "Q100");
        entityListBuilder.add(entity1Builder.build());

        Entity.Builder entity2Builder = new Entity.Builder(16, 23, "Q1297");
        entityListBuilder.add(entity2Builder.build());

        builder.entities(entityListBuilder.build());
        AnnotatedText text = builder.build();

        Entity entity;
        assertEquals(2, text.getEntities().size());
        entity = text.getEntities().get(0);
        assertEquals(5, entity.getStartOffset());
        assertEquals(11, entity.getEndOffset());
        entity = text.getEntities().get(1);
        assertEquals(16, entity.getStartOffset());
        assertEquals(23, entity.getEndOffset());
    }

}
