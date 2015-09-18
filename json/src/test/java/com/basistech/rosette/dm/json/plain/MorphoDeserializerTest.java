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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.junit.Test;

import java.io.File;

/**
 * Tests of the complex code that speeds up morpho analysis deserialization.
 */
public class MorphoDeserializerTest extends AdmAssert {

    @Test
    public void comn130() throws Exception {
        ObjectMapper mapper = objectMapper();
        ObjectReader reader = mapper.readerFor(AnnotatedText.class);
        // threw
        reader.readValue(new File("test-data/comn-130-adm.json"));
    }
}
