/*
* Copyright 2014 Basis Technology Corp.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.basistech.rosette.dm.jackson.array;

import com.basistech.rosette.dm.ListAttribute;
import com.basistech.rosette.dm.jackson.KnownAttribute;
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
