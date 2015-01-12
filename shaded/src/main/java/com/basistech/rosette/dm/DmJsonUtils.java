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

import com.basistech.rosette.dm.jackson.AnnotatedDataModelModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utility functions for serializing and deserializing the data model to and from Json.
 */
public final class DmJsonUtils {
    private DmJsonUtils() {
        //
    }

    /**
     * Serialize an {@link com.basistech.rosette.dm.AnnotatedText} to Json as a stream of bytes.
     * @param annotatedText the annotated text.
     * @param target the target
     * @throws IOException
     */
    public static void serialize(AnnotatedText annotatedText, OutputStream target) throws IOException {
        ObjectMapper objectMapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());
        ObjectWriter writer = objectMapper.writer();
        writer.writeValue(target, annotatedText);
    }

    /**
     * Deserialize an {@link com.basistech.rosette.dm.AnnotatedText} from Json.
     * @param source bytes of Json.
     * @return the annotated text
     * @throws IOException
     */
    public static AnnotatedText deserialize(InputStream source) throws IOException {
        ObjectMapper objectMapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());
        ObjectReader reader = objectMapper.reader(AnnotatedText.class);
        return reader.readValue(source);
    }
}
