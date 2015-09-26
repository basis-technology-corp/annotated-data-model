/******************************************************************************
 * * This data and information is proprietary to, and a valuable trade secret
 * * of, Basis Technology Corp.  It is given in confidence by Basis Technology
 * * and may only be used as permitted under the license agreement under which
 * * it has been distributed, and in no other way.
 * *
 * * Copyright (c) 2015 Basis Technology Corporation All rights reserved.
 * *
 * * The technical data and information provided herein are provided with
 * * `limited rights', and the computer software provided herein is provided
 * * with `restricted rights' as those terms are defined in DAR and ASPR
 * * 7-104.9(a).
 ******************************************************************************/

package com.basistech.rosette.dm.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;

import java.io.IOException;

/**
 * A key serializer that we can register against 'Object.class' to notice the type of the key and respect
 * custom serializers.
 * This is part of a workaround to https://github.com/FasterXML/jackson-databind/issues/943, in which
 * Jackson does not examine the specific type of each Map key for possible custom serialization
 * when it has no field/TypeReference type information. We hope for a fix in 2.6.3 but there are no guarantees.
 * This slows down all map serialization for Map&lt;?, ?&gt;, since each key has to be looked up.
 */
class DynamicKeySerializer extends JsonSerializer<Object> {
    private final StdKeySerializer stdKeySerializer = new StdKeySerializer();

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        Class<?> valueClass = value.getClass();
        JavaType type = serializers.getTypeFactory().constructType(valueClass);
        JsonSerializer<Object> ser = serializers.findKeySerializer(type, null);
        if (ser.getClass() == DynamicKeySerializer.class) {
            stdKeySerializer.serialize(value, gen, serializers);;
        } else {
            ser.serialize(value, gen, serializers);
        }
    }

    @Override
    public Class<Object> handledType() {
        return Object.class;
    }
}
