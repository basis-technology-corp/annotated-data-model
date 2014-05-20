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

import com.basistech.util.LanguageCode;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

/**
 * Arrange for {@link com.basistech.util.LanguageCode} to serialize as its ISO-639-3 code.
 */
class LanguageCodeDeserializer extends StdDeserializer<LanguageCode> {

    public LanguageCodeDeserializer() {
        super(LanguageCode.class);
    }

    @Override
    public LanguageCode deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String code = jp.getText();
        return LanguageCode.lookupByISO639(code);
    }
}
