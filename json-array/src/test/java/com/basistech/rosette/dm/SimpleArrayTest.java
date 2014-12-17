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

/**
 * Some basic tests that the array mechanism works.
 */
public class SimpleArrayTest extends AdmAssert {
    @Test
    public void sentence() throws Exception {
        Sentence attr = new Sentence.Builder(0, 10).build();
        String json = objectMapper().writeValueAsString(attr);

        Sentence readBack = objectMapper().readValue(json, Sentence.class);
        assertEquals(attr, readBack);
    }

    @Test
    public void extendedProps() throws Exception {
        Sentence.Builder builder = (Sentence.Builder) new Sentence.Builder(10, 20).extendedProperty("ext", "prop");
        Sentence sent = builder.build();
        String json = objectMapper().writeValueAsString(sent);

        Sentence readBack = objectMapper().readValue(json, Sentence.class);
        assertEquals(sent, readBack);
    }
}
