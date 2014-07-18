/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2009 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/

package com.basistech.rosette.dm.internal;

import com.basistech.rlp.AbstractResultAccess;
import com.basistech.rlp.ResultAccessDeserializer;
import com.basistech.rlp.ResultAccessSerializedFormat;
import com.basistech.rosette.RosetteRuntimeException;
import com.basistech.rosette.dm.AnnotatedText;
import com.basistech.rosette.dm.Entity;
import com.basistech.rosette.dm.ListAttribute;
import com.basistech.rosette.dm.tools.AraDmConverter;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TextWrapperTest {
    private AbstractResultAccess getResults(String resource) throws IOException {
        ResultAccessDeserializer deserializer = new ResultAccessDeserializer();
        deserializer.setFormat(ResultAccessSerializedFormat.JSON);
        InputStream in = getClass().getResourceAsStream(resource);
        return deserializer.deserializeAbstractResultAccess(in);
    }

    @Test
    public void testLongestMentionIterable() throws Exception {
        AbstractResultAccess resultAccess = getResults("simple_doc0.json");
        TextWrapper textWrapper = new TextWrapper(AraDmConverter.convert(resultAccess));
        List<Integer> sortedChainIds = Lists.newArrayList();
        for (Mention m : textWrapper.headMentions()) {
            sortedChainIds.add(m.getIndocChainId());
        }
        int[] result = Ints.toArray(sortedChainIds);
        int[] expected = {0, 1, 4, 5};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testAllMentionsIterable() throws Exception {
        AbstractResultAccess resultAccess = getResults("simple_doc0.json");
        TextWrapper textWrapper = new TextWrapper(AraDmConverter.convert(resultAccess));
        List<Integer> chainIds = Lists.newArrayList();
        for (Mention m : textWrapper.mentions()) {
            chainIds.add(m.getIndocChainId());
        }
        int[] result = Ints.toArray(chainIds);
        int[] expected = {0, 1, 0, 5, 4, 5, 1};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testSubsetMentionsIterable() throws Exception {
        AbstractResultAccess resultAccess = getResults("simple_doc0.json");
        TextWrapper textWrapper = new TextWrapper(AraDmConverter.convert(resultAccess));
        List<Integer> chainIds = Lists.newArrayList();
        for (Mention m : textWrapper.mentions(2, 4)) {
            chainIds.add(m.getIndocChainId());
        }
        int[] result = Ints.toArray(chainIds);
        int[] expected = {0, 5};
        assertArrayEquals(expected, result);
    }

    @Test(expected = RosetteRuntimeException.class)
    public void testMissingTokens() throws Exception {
        // If no tokenizer was run, it doesn't make sense to ask for text
        // based on token indexes.
        AbstractResultAccess resultAccess = getResults("null_tokens.json");
        TextWrapper textWrapper = new TextWrapper(AraDmConverter.convert(resultAccess));
        textWrapper.getRawText(0, 1);
    }

    @Test
    public void testEmptyTokens() throws Exception {
        // If a tokenizer was run, but we got zero tokens (e.g. whitespace doc),
        // you can still ask for text.  Perhaps still a bit fishy, but that's
        // what we decided.
        AbstractResultAccess resultAccess = getResults("whitespace.json");
        TextWrapper textWrapper = new TextWrapper(AraDmConverter.convert(resultAccess));
        assertEquals("", textWrapper.getRawText(0, 1).trim());
    }

    @Test
    public void testGetMentionTokenIndexes() throws IOException {
        // 0: PERSON, [0, 2), Bill Clinton
        // 1: TITLE, [5, 6), president
        // 2: PERSON, [7, 8), Clinton
        // 3: PERSON, [10, 11), Hillary
        // 4: TITLE, [13, 16), Secretary of State
        // 5: PERSON, [17, 19), Hillary Clinton
        // 6: TITLE, [21, 22), president
        AbstractResultAccess resultAccess = getResults("simple_doc0.json");
        TextWrapper tw = new TextWrapper(AraDmConverter.convert(resultAccess));
        assertEquals(0, tw.getMentionStartTokenIndex(0));
        assertEquals(5, tw.getMentionStartTokenIndex(1));

        assertEquals(2, tw.getMentionEndTokenIndex(0));
        assertEquals(6, tw.getMentionEndTokenIndex(1));
    }

    @Test
    public void testGetMentionForToken() throws IOException {
        // 0: PERSON, [0, 2), Bill Clinton
        // 1: TITLE, [5, 6), president
        // 2: PERSON, [7, 8), Clinton
        // 3: PERSON, [10, 11), Hillary
        // 4: TITLE, [13, 16), Secretary of State
        // 5: PERSON, [17, 19), Hillary Clinton
        // 6: TITLE, [21, 22), president
        AbstractResultAccess resultAccess = getResults("simple_doc0.json");
        TextWrapper tw = new TextWrapper(AraDmConverter.convert(resultAccess));
        assertEquals(0, tw.getMentionForToken(0).getEntityIndex());
        assertEquals(0, tw.getMentionForToken(1).getEntityIndex());
        assertNull(tw.getMentionForToken(2));
        assertEquals(1, tw.getMentionForToken(5).getEntityIndex());
        assertNull(tw.getMentionForToken(6));
    }

    @Test
    public void testGetEntityForChainId() throws IOException {
        String rawText = "from Boston and Chicago";
        int[] tokenOffsets = {0, 4, 5, 11, 12, 15, 16, 23};
        AnnotatedText.Builder builder = new AnnotatedText.Builder().data(rawText);
        ListAttribute.Builder<Entity> entityListBuilder = new ListAttribute.Builder<Entity>(Entity.class);

        Entity.Builder entity1Builder = new Entity.Builder(5, 11, "Q100");
        entity1Builder.coreferenceChainId(0);
        entityListBuilder.add(entity1Builder.build());
        Entity.Builder entity2Builder = new Entity.Builder(16, 23, "Q1297");
        entity2Builder.coreferenceChainId(1);
        entityListBuilder.add(entity2Builder.build());

        builder.entities(entityListBuilder.build());
        AnnotatedText annotatedText = builder.build();
        TextWrapper tw = new TextWrapper(annotatedText);

        assertEquals("Q100", tw.getEntity(0).getEntityId());
        assertEquals("Q1297", tw.getEntity(1).getEntityId());
        assertNull(tw.getEntity(2));
    }

    @Test
    public void testGetChainForMention() throws IOException {
        // 0: PERSON, [0, 2), Bill Clinton
        // 1: TITLE, [5, 6), president
        // 2: PERSON, [7, 8), Clinton
        // 3: PERSON, [10, 11), Hillary
        // 4: TITLE, [13, 16), Secretary of State
        // 5: PERSON, [17, 19), Hillary Clinton
        // 6: TITLE, [21, 22), president
        AbstractResultAccess resultAccess = getResults("simple_doc0.json");
        TextWrapper tw = new TextWrapper(AraDmConverter.convert(resultAccess));

        List<Mention> chain = tw.getChainForMention(tw.getMention(3), false);
        assertEquals(2, chain.size());
        assertEquals(3, chain.get(0).getEntityIndex());
        assertEquals(5, chain.get(1).getEntityIndex());

        chain = tw.getChainForMention(tw.getMention(3), true);
        assertEquals(1, chain.size());
        assertEquals(3, chain.get(0).getEntityIndex());
    }
}
