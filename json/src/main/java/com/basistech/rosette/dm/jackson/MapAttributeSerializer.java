/*
* Copyright 2018 Basis Technology Corp.
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
package com.basistech.rosette.dm.jackson;

import com.basistech.rosette.dm.MapAttribute;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.io.IOException;
import java.util.Map;

/**
 * Jackson serializer that that handles polymorphism in maps of homogeneous type without
 * writing out the type every time.
 */
public class MapAttributeSerializer extends JsonSerializer<MapAttribute> {
    @Override
    public void serialize(MapAttribute value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        gen.writeStringField("keyType", KnownKey.getKeyForClass(value.getKeyClass()).key());
        gen.writeStringField("valueType", KnownAttribute.getAttributeForClass(value.getValueClass()).key());
        gen.writeObjectField("items", value.getItems());
        writeExtendedProperties(value, gen);
        gen.writeEndObject();
    }

    private void writeExtendedProperties(MapAttribute value, JsonGenerator jgen) throws IOException {
        Map<String, Object> extendedProperties = value.getExtendedProperties();
        if (extendedProperties != null && !extendedProperties.isEmpty()) {
            for (Map.Entry<String, Object> entry : extendedProperties.entrySet()) {
                jgen.writeObjectField(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public void serializeWithType(MapAttribute value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
        typeSer.writeTypePrefixForObject(value, jgen);

        KnownKey keyForKeyClass = KnownKey.getKeyForClass(value.getKeyClass());
        KnownAttribute attributeForValueClass = KnownAttribute.getAttributeForClass(value.getValueClass());
        assert keyForKeyClass != null;
        assert attributeForValueClass != null;
        jgen.writeStringField("keyType", keyForKeyClass.key());
        jgen.writeStringField("valueType", attributeForValueClass.key());
        jgen.writeObjectField("items", value.getItems());
        writeExtendedProperties(value, jgen);
        typeSer.writeTypeSuffixForObject(value, jgen);
    }
}
