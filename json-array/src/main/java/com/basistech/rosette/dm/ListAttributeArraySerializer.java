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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.io.IOException;
import java.util.Map;

/**
 * Jackson serializer that that handles polymorphism in lists of homogeneous type without
 * writing out the type every time.
 */
public class ListAttributeArraySerializer extends JsonSerializer<ListAttribute> {
    @Override
    public void serialize(ListAttribute value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartArray();
        jgen.writeString(KnownAttribute.getAttributeForClass(value.getItemClass()).key());
        writeItems(value, jgen, provider);
        writeExtendedProperties(value, jgen);
        jgen.writeEndArray();
    }

    private void writeItems(ListAttribute value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartArray();

        for (Object attr : value) {
            provider.defaultSerializeValue(attr, jgen);
        }

        jgen.writeEndArray();
    }

    private void writeExtendedProperties(ListAttribute value, JsonGenerator jgen) throws IOException {
        Map<String, Object> extendedProperties = value.getExtendedProperties();
        jgen.writeStartObject();
        for (Map.Entry<String, Object> entry : extendedProperties.entrySet()) {
            jgen.writeObjectField(entry.getKey(), entry.getValue());
        }
        jgen.writeEndObject();
    }

    @Override
    public void serializeWithType(ListAttribute value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
        typeSer.writeTypePrefixForArray(value, jgen);
        jgen.writeString(KnownAttribute.getAttributeForClass(value.getItemClass()).key());
        writeItems(value, jgen, provider);
        writeExtendedProperties(value, jgen);
        typeSer.writeTypeSuffixForArray(value, jgen);
    }
}
