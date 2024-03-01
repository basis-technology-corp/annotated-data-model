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

import com.basistech.rosette.dm.BaseAttribute;
import com.basistech.rosette.dm.MapAttribute;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Jackson deserializer for MapAttributes that avoids writing out
 * the same type information repeatedly.
 */
public class MapAttributeDeserializer extends JsonDeserializer<MapAttribute> {
    @Override
    @SuppressWarnings("unchecked")
    public MapAttribute deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        if (jp.getCurrentToken() == JsonToken.START_OBJECT) { // this is what we expect.
            // we advance to be in the same place the 'else' will be -- the first FIELD_NAME.
            jp.nextToken();
        } else {
            /* In a full AnnotatedText, which is already doing some polymorphism shuffling, we end up here. */
            /* We find a FIELD_NAME for the first field of the object */
            if (jp.getCurrentToken() != JsonToken.FIELD_NAME) {
                throw ctxt.wrongTokenException(jp, (JavaType) null, JsonToken.START_OBJECT, "MapAttributeDeserializer called not at or FIELD_NAME of first field");
            }
            /* We are at the field name, ready for the loop. */
        }
        TokenBuffer tb = null;
        String keyName = null;
        String valueName = null;
        for (JsonToken t = jp.getCurrentToken(); t == JsonToken.FIELD_NAME; t = jp.nextToken()) {
            String name = jp.getCurrentName();
            if ("keyType".equals(name)) { // gotcha!
                keyName = getName(jp);
            }
            if ("valueType".equals(name)) {
                valueName = getName(jp);
            }
            if (keyName != null && valueName != null) {
                // both have been found
                return deserialize(jp, ctxt, tb, keyName, valueName);
            }
            if (tb == null) {
                tb = new TokenBuffer(null, false);
            }
            if (!("keyType".equals(name) || "valueType".equals(name))) {
                tb.copyCurrentStructure(jp);
            }
        }
        if (keyName == null) {
            throw JsonMappingException.from(ctxt.getParser(), "No keyType provided in a map");
        }
        throw JsonMappingException.from(ctxt.getParser(), "No valueType provided in a map");
    }

    private String getName(JsonParser jp) throws IOException {
        jp.nextToken();
        return jp.getText();
    }

    private <K, V> TypeReference<Map<K, V>> buildTypeReference(Class<K> keyClass, Class<V> valueClass) {
        // TypeToken lets us dynamically compute the parameters for this class.
        TypeToken<Map<K, V>> tok = new TypeToken<Map<K, V>>() { }
            .where(new TypeParameter<K>() { }, keyClass)
            .where(new TypeParameter<V>() { }, valueClass);
        return new TypeReference<Map<K, V>>() {
            @Override
            public Type getType() {
                return tok.getType();
            }
        };
    }

    // called with field_name for the itemType field as the current position, perhaps with a buffer to merge in after that.
    @SuppressWarnings("unchecked")
    private MapAttribute deserialize(JsonParser jp, DeserializationContext ctxt, TokenBuffer tb, String keyName, String valueName) throws IOException {

        if (tb != null) { // need to put back skipped properties?
            jp = putBackSkippedProperties(jp, tb);
        }
        // Must point to the next value; tb had no current, jp pointed to VALUE_STRING:

        KnownKey key = KnownKey.getKnownForKey(keyName);
        KnownAttribute attribute = KnownAttribute.getAttributeForKey(valueName);
        if (key == null) {
            key = KnownKey.UNKNOWN;
        }
        if (attribute == null) {
            attribute = KnownAttribute.UNKNOWN;
        }
        Class<?> keyClass = key.keyClass();
        Class<? extends BaseAttribute> valueClass = attribute.attributeClass();

        MapAttribute.Builder<Object, BaseAttribute> builder = new MapAttribute.Builder<>(keyClass, valueClass);
        Map<Object, BaseAttribute> items = Maps.newHashMap();

        JsonToken nextToken;
        while ((nextToken = jp.nextToken()) != JsonToken.END_OBJECT) {
            if (nextToken != JsonToken.FIELD_NAME) {
                throw ctxt.wrongTokenException(jp, (JavaType) null, JsonToken.END_OBJECT, "Expected field name.");
            } else {
                String name = jp.getCurrentName();
                if ("items".equals(name)) {
                    // the actual list items.
                    nextToken = jp.nextToken();
                    if (nextToken == JsonToken.VALUE_EMBEDDED_OBJECT) {
                        Object o = jp.getEmbeddedObject();
                        if (o instanceof Map) {
                            items.putAll((Map) o);
                        } else {
                            throw JsonMappingException.from(
                                    ctxt.getParser(),
                                    "Map contains VALUE_EMBEDDED_OBJECT for items, but it wasn't a map.");
                        }
                    } else if (nextToken != JsonToken.START_OBJECT) { // what about nothing?
                        throw ctxt.wrongTokenException(
                                jp, (JavaType) null, JsonToken.START_OBJECT, "Expected object of items");
                    } else {
                        // the START_OBJECT case, which is _normal_. Read the elements.
                        // Dynamically build the type reference and parse.
                        items.putAll(jp.readValueAs(buildTypeReference(keyClass, valueClass)));
                    }
                } else {
                    nextToken = jp.nextToken();
                    Object value;
                    if (nextToken == JsonToken.VALUE_EMBEDDED_OBJECT) {
                        value = jp.getEmbeddedObject();
                    } else {
                        value = jp.readValueAs(Object.class);
                    }
                    builder.extendedProperty(name, value);
                }
            }
        }
        builder.setItems(items);
        return builder.build();
    }

    private JsonParserSequence putBackSkippedProperties(final JsonParser jp, final TokenBuffer tb) {
        return JsonParserSequence.createFlattened(false, tb.asParser(jp), jp);
    }
}
