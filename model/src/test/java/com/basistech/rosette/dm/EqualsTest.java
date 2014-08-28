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

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test {@link Object#equals(Object)} methods across the DM.
 */
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
        assertThat(ma1.hashCode(), not(equalTo(ma2.hashCode())));

        assertFalse(ma1.equals(ma2));

        maBuilder = new MorphoAnalysis.Builder();
        componentList(maBuilder, "beam", "post");
        maBuilder.lemma("pear");
        maBuilder.partOfSpeech("woof");
        maBuilder.raw("cooked");
        ma2 = maBuilder.build();
        ma2.hashCode();
        assertThat(ma1.hashCode(), not(equalTo(ma2.hashCode())));

        assertFalse(ma1.equals(ma2));

        maBuilder = new MorphoAnalysis.Builder();
        componentList(maBuilder, "beam", "post");
        maBuilder.lemma("orange");
        maBuilder.partOfSpeech("meow");
        maBuilder.raw("cooked");
        ma2 = maBuilder.build();
        ma2.hashCode();
        assertThat(ma1.hashCode(), not(equalTo(ma2.hashCode())));

        assertFalse(ma1.equals(ma2));

        maBuilder = new MorphoAnalysis.Builder();
        componentList(maBuilder, "beam", "post");
        maBuilder.lemma("orange");
        maBuilder.partOfSpeech("woof");
        maBuilder.raw("hide");
        ma2 = maBuilder.build();
        ma2.hashCode();
        assertThat(ma1.hashCode(), not(equalTo(ma2.hashCode())));

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
        assertThat(ma1.hashCode(), not(equalTo(ma2.hashCode())));

        assertFalse(ma1.equals(ma2));
        assertFalse(ma2.equals(ma1));

        maBuilder = new MorphoAnalysis.Builder();
        componentList(maBuilder, "beam", "post");
        maBuilder.lemma(null);
        maBuilder.partOfSpeech("woof");
        maBuilder.raw("cooked");
        ma2 = maBuilder.build();
        ma2.hashCode();
        assertThat(ma1.hashCode(), not(equalTo(ma2.hashCode())));

        assertFalse(ma1.equals(ma2));
        assertFalse(ma2.equals(ma1));

        maBuilder = new MorphoAnalysis.Builder();
        componentList(maBuilder, "beam", "post");
        maBuilder.lemma("orange");
        maBuilder.partOfSpeech(null);
        maBuilder.raw("cooked");
        ma2 = maBuilder.build();
        ma2.hashCode();
        assertThat(ma1.hashCode(), not(equalTo(ma2.hashCode())));

        assertFalse(ma1.equals(ma2));
        assertFalse(ma2.equals(ma1));

        maBuilder = new MorphoAnalysis.Builder();
        componentList(maBuilder, "beam", "post");
        maBuilder.lemma("orange");
        maBuilder.partOfSpeech("woof");
        maBuilder.raw(null);
        ma2 = maBuilder.build();
        ma2.hashCode();
        assertThat(ma1.hashCode(), not(equalTo(ma2.hashCode())));

        assertFalse(ma1.equals(ma2));
        assertFalse(ma2.equals(ma1));
    }

    // Oh, gosh, the Arabic case. Not to worry until COMN-101

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
        assertThat(ma1.hashCode(), not(equalTo(ma2.hashCode())));

        maBuilder = new HanMorphoAnalysis.Builder();
        // leave reading null
        ma2 = maBuilder.build();
        assertNull(ma2.getReadings());
        assertFalse(ma1.equals(ma2));
        assertFalse(ma2.equals(ma1));
        ma2.hashCode();
        assertThat(ma1.hashCode(), not(equalTo(ma2.hashCode())));
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
        assertNotSame(tok1.hashCode(), tok2.hashCode());

        tokBuilder = new Token.Builder(0, 10, "token");
        tokBuilder.addNormalized("norm");
        // no analysis
        tokBuilder.source("nile");
        tok2 = tokBuilder.build();
        assertFalse(tok1.equals(tok2));
        assertFalse(tok2.equals(tok1));
        assertNotSame(tok1.hashCode(), tok2.hashCode());
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
        assertNotSame(em1.hashCode(), em2.hashCode());

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
        assertNotSame(em1.hashCode(), em2.hashCode());

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
        assertNotSame(em1.hashCode(), em2.hashCode());
    }
}
