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

package com.basistech.rosette.dm.jackson;

import com.basistech.rosette.dm.BaseAttribute;
import com.basistech.rosette.dm.ListAttribute;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.List;

/**
 * Jackson deserializer for ListAttributes that avoids writing out
 * the same type information repeatedly.
 */
public class ListAttributeDeserializer extends JsonDeserializer<ListAttribute> {
    @Override
    @SuppressWarnings("unchecked")
    public ListAttribute deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        if (jp.getCurrentToken() == JsonToken.START_OBJECT) { // this is what we expect.
            // we advance to be in the same place the 'else' will be -- the first FIELD_NAME.
            jp.nextToken();
        } else {
            /* In a full AnnotatedText, which is already doing some polymorphism shuffling, we end up here. */
            /* We find a FIELD_NAME for the first field of the object */
            if (jp.getCurrentToken() != JsonToken.FIELD_NAME) {
                throw ctxt.wrongTokenException(jp, (JavaType) null, JsonToken.START_OBJECT, "ListAttributeDeserializer called not at or FIELD_NAME of first field");
            }
            /* We are at the field name, ready for the loop. */
        }
        TokenBuffer tb = null;
        for (JsonToken t = jp.getCurrentToken(); t == JsonToken.FIELD_NAME; t = jp.nextToken()) {
            String name = jp.getCurrentName();
            if ("itemType".equals(name)) { // gotcha!
                return deserialize(jp, ctxt, tb);
            }
            if (tb == null) {
                tb = new TokenBuffer(null, false);
            }
            tb.copyCurrentStructure(jp);
        }
        throw JsonMappingException.from(ctxt.getParser(), "No itemType provided in a list");
    }

    // called with field_name for the itemType field as the current position, perhaps with a buffer to merge in after that.
    @SuppressWarnings("unchecked")
    private ListAttribute deserialize(JsonParser jp, DeserializationContext ctxt, TokenBuffer tb) throws IOException {
        jp.nextToken();
        String keyName = jp.getText();

        if (tb != null) { // need to put back skipped properties?
            jp = putBackSkippedProperties(jp, tb);
        }
        // Must point to the next value; tb had no current, jp pointed to VALUE_STRING:

        KnownAttribute attribute = KnownAttribute.getAttributeForKey(keyName);
        if (attribute == null) {
            attribute = KnownAttribute.UNKNOWN;
        }
        Class<? extends BaseAttribute> itemClass = attribute.attributeClass();

        ListAttribute.Builder<BaseAttribute> builder = new ListAttribute.Builder<>(attribute.attributeClass());
        List<BaseAttribute> items = Lists.newArrayList();

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
                        if (o instanceof List) { // could it be an array, also?!?
                            // when using JsonTree, sometimes Jackson just sticks the entire Java object in here.
                            items.addAll((List) o);
                        } else {
                            throw JsonMappingException.from(
                                    ctxt.getParser(),
                                    "List contains VALUE_EMBEDDED_OBJECT for items, but it wasn't a list.");
                        }
                    } else if (nextToken != JsonToken.START_ARRAY) { // what about nothing?
                        throw ctxt.wrongTokenException(
                                jp, (JavaType) null, JsonToken.START_ARRAY, "Expected array of items");
                    } else {
                        // the START_ARRAY case, which is _normal_. Read the elements.
                        while (jp.nextToken() != JsonToken.END_ARRAY) {
                            items.add(jp.readValueAs(itemClass));
                        }
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
