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

package com.basistech.dm;

import com.basistech.rlp.AbstractResultAccess;
import com.basistech.rlp.ResultAccessDeserializer;
import com.basistech.rlp.ResultAccessSerializedFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Here's some testing using an ARA as in input.
 */
public class RoundTripTest extends Assert {

    @Test
    public void testEngRoundTrip() throws Exception {
        ResultAccessDeserializer rad = new ResultAccessDeserializer();
        rad.setFormat(ResultAccessSerializedFormat.JSON);
        InputStream input = null;
        AbstractResultAccess ara;
        try {
            input = new FileInputStream("../model/data/eng-ara.json");
            ara = rad.deserializeAbstractResultAccess(input);
        } finally {
            IOUtils.closeQuietly(input);
        }
        Text text = AraDmConverter.convert(ara);

        ByteArrayOutputStream jsonContainer = new ByteArrayOutputStream();

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        writer.writeValue(jsonContainer, text);

        //System.out.println(jsonContainer.toString("utf-8"));

        ObjectReader reader = mapper.reader(Text.class);
        Text deserializedText = reader.readValue(jsonContainer.toByteArray());

        assertEquals(text.length(), deserializedText.length());
        assertTrue(Arrays.equals(text.getData(), deserializedText.getData()));
        // we need about 1000 additional assertions in here.
    }

    @Test
    public void testAraRoundTrip() throws Exception {
        ResultAccessDeserializer rad = new ResultAccessDeserializer();
        rad.setFormat(ResultAccessSerializedFormat.JSON);
        InputStream input = null;
        AbstractResultAccess ara;
        try {
            input = new FileInputStream("../model/data/ara-ara.json");
            ara = rad.deserializeAbstractResultAccess(input);
        } finally {
            IOUtils.closeQuietly(input);
        }
        Text text = AraDmConverter.convert(ara);

        ByteArrayOutputStream jsonContainer = new ByteArrayOutputStream();

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        writer.writeValue(jsonContainer, text);

        //System.out.println(jsonContainer.toString("utf-8"));

        ObjectReader reader = mapper.reader(Text.class);
        Text deserializedText = reader.readValue(jsonContainer.toByteArray());

        assertEquals(text.length(), deserializedText.length());
        assertTrue(Arrays.equals(text.getData(), deserializedText.getData()));
    }
}
