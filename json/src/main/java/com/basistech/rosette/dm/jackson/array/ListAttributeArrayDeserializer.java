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

import com.basistech.rosette.dm.BaseAttribute;
import com.basistech.rosette.dm.ListAttribute;
import com.basistech.rosette.dm.jackson.KnownAttribute;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Jackson deserializer for ListAttributes that avoids writing out
 * the same type information repeatedly.
 */
public class ListAttributeArrayDeserializer extends JsonDeserializer<ListAttribute> {
    @Override
    @SuppressWarnings("unchecked")
    public ListAttribute deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        if (jp.getCurrentToken() == JsonToken.START_ARRAY) { // this is what we expect.
            // we advance to be in the same place the 'else' will be -- the first FIELD_NAME.
            jp.nextToken();
        } else {
            throw ctxt.wrongTokenException(jp, (JavaType) null, JsonToken.START_ARRAY, "ListAttributeDeserializer called not array start.");
        }

        if (jp.getCurrentToken() != JsonToken.VALUE_STRING) {
            throw JsonMappingException.from(ctxt.getParser(), "Expected VALUE_STRING for item type.");
        }
        String itemTypeKeyName = jp.getText();

        KnownAttribute attribute = KnownAttribute.getAttributeForKey(itemTypeKeyName);
        if (attribute == null) {
            attribute = KnownAttribute.UNKNOWN;
        }
        Class<? extends BaseAttribute> itemClass = attribute.attributeClass();

        ListAttribute.Builder<BaseAttribute> builder = new ListAttribute.Builder<>(attribute.attributeClass());
        List<BaseAttribute> items = Lists.newArrayList();

        if (jp.nextToken() != JsonToken.START_ARRAY) {
            throw ctxt.wrongTokenException(jp, (JavaType) null, JsonToken.START_ARRAY, "No array of values for list.");
        }

        // we just read the elements as we see them,
        while (jp.nextToken() != JsonToken.END_ARRAY) {
            // the START_ARRAY case, which is _normal_. Read the elements.
            items.add(jp.readValueAs(itemClass));
        }
        builder.setItems(items);
        // we are still in the top-level array ...
        if (jp.nextToken() != JsonToken.START_OBJECT) {
            throw ctxt.wrongTokenException(
                    jp, (JavaType) null, JsonToken.START_OBJECT, "No extended properties for list.");
        }
        Map<String, Object> props = jp.readValueAs(new TypeReference<Map<String, Object>>() {
        });
        for (Map.Entry<String, Object> me : props.entrySet()) {
            builder.extendedProperty(me.getKey(), me.getValue());
        }
        jp.nextToken(); // consume the END_OBJECT of the extended props
        return builder.build();
    }
}
