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

package com.basistech.rosette.dm.jackson.array;

import com.basistech.rosette.dm.Attribute;
import com.basistech.rosette.dm.jackson.DmTypeIdResolver;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Map;

/**
 * Until we get to the bottom of <a href="https://github.com/FasterXML/jackson-databind/issues/646">This Jackson Bug</a>.
 */
public class TempAttributeMapSerializer extends JsonSerializer<Map<String, Attribute>> {

    private final DmTypeIdResolver resolver;

    public TempAttributeMapSerializer() {
        resolver = new DmTypeIdResolver();
    }

    @Override
    public void serialize(Map<String, Attribute> value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        for (Map.Entry<String, Attribute> me : value.entrySet()) {
            jgen.writeFieldName(me.getKey());
            jgen.writeStartArray();
            jgen.writeString(resolver.idFromValue(me.getValue()));
            // this will write out in array notation due to the annotation
            provider.defaultSerializeValue(me.getValue(), jgen);
            jgen.writeEndArray();
        }
        jgen.writeEndObject();
    }
}
