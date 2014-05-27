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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

/**
 * See if the extension mechanism works round-trip.
 * This does not require a full AnnotatedText object.
 */
public class ExtendedPropertyTest extends AdmAssert {
    @Test
    public void textExtendedRoundTrip() throws Exception {
        Token.Builder builder = new Token.Builder(0, 5, "abcdefg");
        builder.extendedProperty("veloci", "raptor");
        Token token = builder.build();
        ObjectMapper mapper = objectMapper();
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        ByteArrayOutputStream jsonBlob = new ByteArrayOutputStream();
        writer.writeValue(jsonBlob, token);

        ObjectReader reader = mapper.reader(Token.class);
        // just see if we get an exception for now.
        Token token2 = reader.readValue(jsonBlob.toByteArray());
        assertEquals("abcdefg", token2.getText());
        assertEquals(0, token2.getStartOffset());
        assertEquals(5, token2.getEndOffset());
        assertEquals("raptor", token2.getExtendedProperties().get("veloci"));
    }
}
