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

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;

/**
 * Test serialization a-la-array.
 */
public class ListAttributeTest extends AdmAssert {
    @Test
    public void list() throws Exception {
        ListAttribute.Builder<Sentence> listBuilder = new ListAttribute.Builder<Sentence>(Sentence.class);
        listBuilder.add(new Sentence.Builder(0, 10).build());
        listBuilder.add(new Sentence.Builder(10, 20).build());
        listBuilder.extendedProperty("ek", "ev");
        ListAttribute<Sentence> sentList = listBuilder.build();
        String json = objectMapper().writeValueAsString(sentList);
        ListAttribute<Sentence> readBack = objectMapper().readValue(json, new TypeReference<ListAttribute<Sentence>>() {});
        // well, try round-trip.
        // this is the simple case, we'll also have to try it inside an AnnotatedText in another test case.
        assertEquals(sentList, readBack);
    }

    @Test
    public void inContext() throws Exception {
        ListAttribute.Builder<Sentence> listBuilder = new ListAttribute.Builder<Sentence>(Sentence.class);
        listBuilder.add(new Sentence.Builder(0, 10).build());
        listBuilder.add(new Sentence.Builder(10, 20).build());
        listBuilder.extendedProperty("ek", "ev");
        ListAttribute<Sentence> sentList = listBuilder.build();
        AnnotatedText.Builder atBuilder = new AnnotatedText.Builder();
        atBuilder.data("Some Text");
        atBuilder.sentences(sentList);
        AnnotatedText annotatedText = atBuilder.build();
        String json = objectMapper().writeValueAsString(annotatedText);
        AnnotatedText readBack = objectMapper().readValue(json, AnnotatedText.class);
        // we don't have an equals() that is useful for AnnotatedText
        assertEquals(annotatedText.getData(), readBack.getData());
        assertEquals(annotatedText.getAttributes(), readBack.getAttributes());
    }
}
