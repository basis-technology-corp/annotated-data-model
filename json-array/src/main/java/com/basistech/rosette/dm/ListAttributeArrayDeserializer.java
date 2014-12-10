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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
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
            throw ctxt.wrongTokenException(jp, JsonToken.START_ARRAY, "ListAttributeDeserializer called not array start.");
        }

        if (jp.getCurrentToken() != JsonToken.VALUE_STRING) {
            throw ctxt.mappingException("Expected VALUE_STRING for item type.");
        }
        String itemTypeKeyName = jp.getText();

        KnownAttribute attribute = KnownAttribute.getAttributeForKey(itemTypeKeyName);
        if (attribute == null) {
            attribute = KnownAttribute.UNKNOWN;
        }
        Class<? extends BaseAttribute> itemClass = attribute.attributeClass();

        ListAttribute.Builder<BaseAttribute> builder = new ListAttribute.Builder<BaseAttribute>(attribute.attributeClass());
        List<BaseAttribute> items = Lists.newArrayList();

        if (jp.nextToken() != JsonToken.START_ARRAY) {
            throw ctxt.wrongTokenException(jp, JsonToken.START_ARRAY, "No array of values for list.");
        }

        // we just read the elements as we see them,
        while (jp.nextToken() != JsonToken.END_ARRAY) {
            // the START_ARRAY case, which is _normal_. Read the elements.
            items.add(jp.readValueAs(itemClass));
        }
        builder.setItems(items);
        // we are still in the top-level array ...
        if (jp.nextToken() != JsonToken.START_OBJECT) {
            throw ctxt.wrongTokenException(jp, JsonToken.START_OBJECT, "No extended properties for list.");
        }
        Map<String, Object> props = jp.readValueAs(new TypeReference<Map<String, Object>>() {});
        for (Map.Entry<String, Object> me : props.entrySet()) {
            builder.extendedProperty(me.getKey(), me.getValue());
        }
        return builder.build();
    }
}
