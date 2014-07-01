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
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.List;

/**
 * Jackson deserializer for ListAttributes that avoids writing out
 * the same type information repeatedly.
 */
public class ListAttributeDeserializer extends JsonDeserializer<ListAttribute> {

    static final SerializedString ITEMS = new SerializedString("items");

    @Override
    @SuppressWarnings("unchecked")
    public ListAttribute deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ListAttribute.Builder<BaseAttribute> builder;

        /*
         * There are two cases. In the 'normal' case, we arrive pointing to START_OBJECT, and expect itemClass: to come next.
         * In the 'polymorphism' case, Jackson has already read the START_OBJECT (and the @class: and the class name) and
         * even the "itemClass:" which it looked at to decide where it was.
         */
        if (jp.getCurrentToken() == JsonToken.START_OBJECT) {
            jp.nextToken();
        }
        // Jackson 2.3.0 leaves us pointing to the field name for itemClass.
        jp.nextToken();
        String keyName = jp.getText();
        KnownAttribute attribute = KnownAttribute.getAttributeForKey(keyName);
        if (attribute == null) {
            attribute = KnownAttribute.UNKNOWN;
        }

        Class<? extends BaseAttribute> itemClass = attribute.attributeClass();
        builder = new ListAttribute.Builder<BaseAttribute>(attribute.attributeClass());

        if (!jp.nextFieldName(ITEMS)) {
            throw ctxt.wrongTokenException(jp, JsonToken.FIELD_NAME, "Expected items");
        }

        List<BaseAttribute> items = Lists.newArrayList();
        JsonToken nextToken = jp.nextToken();
        if (nextToken == JsonToken.VALUE_EMBEDDED_OBJECT) {
            Object o = jp.getEmbeddedObject();
            if (o instanceof List) { // could it be an array, also?!?
                // when using JsonTree, sometimes Jackson just sticks the entire Java object in here.
                items.addAll((List)o);
            } else {
                throw ctxt.mappingException("List contains VALUE_EMBEDDED_OBJECT for items, but it wasn't a list.");
            }
        } else if (nextToken != JsonToken.START_ARRAY) { // what about nothing?
            throw ctxt.wrongTokenException(jp, JsonToken.START_ARRAY, "Expected array of items");
        } else {
            // the START_ARRAY case, which is _normal_. Read the elements.
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                items.add(jp.readValueAs(itemClass));
            }
        }

       // everything else is extended properties, if there's anything else.

        while ((nextToken = jp.nextToken()) != JsonToken.END_OBJECT) {
            if (nextToken != JsonToken.FIELD_NAME) {
                throw ctxt.wrongTokenException(jp, JsonToken.END_OBJECT, "Expected end of ListAttribute object.");
            }
            String extKeyName = jp.getText();
            nextToken = jp.nextToken();
            Object value;
            if (nextToken == JsonToken.VALUE_EMBEDDED_OBJECT) {
                value = jp.getEmbeddedObject();
            } else {
                value = jp.readValueAs(Object.class);
            }
            builder.extendedProperty(extKeyName, value);
        }

        builder.setItems(items);
        return builder.build();
    }
}
