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

package com.basistech.dm;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.List;

/**
 * Jackson deserialization that handles polymorphism of MorphoAnalysis without writing
 * out the type in each one.
 */
public class MorphoAnalysisListDeserializer extends JsonDeserializer<MorphoAnalysisList> {
    /*
     * Note the use of 'unchecked' here. If there's a way to to do this that is
     * completely OK with the compiler, I don't know what it would be.
     */

    static final SerializedString ITEMS = new SerializedString("items");

    @Override
    @SuppressWarnings("unchecked")
    public MorphoAnalysisList deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
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
        String itemClassName = jp.getText();
        if (itemClassName == null || "".equals(itemClassName)) {
            throw new JsonMappingException("item class name missing");
        }

        Class<? extends MorphoAnalysis> itemClass;
        try {
            // here is the seemingly unavoidable unchecked cast.
            itemClass = (Class<? extends MorphoAnalysis>) ctxt.findClass(itemClassName);
        } catch (ClassNotFoundException e) {
            throw new JsonMappingException("Failed to find class " + itemClassName);
        }
        if (!jp.nextFieldName(ITEMS)) {
            throw ctxt.wrongTokenException(jp, JsonToken.FIELD_NAME, "Expected items");
        }
        List<MorphoAnalysis> items = Lists.newArrayList();
        if (jp.nextToken() != JsonToken.START_ARRAY) { // what about nothing?
            throw ctxt.wrongTokenException(jp, JsonToken.START_ARRAY, "Expected array of items");
        }
        while (jp.nextToken() != JsonToken.END_ARRAY) {
            items.add(jp.readValueAs(itemClass));
        }
        if (jp.nextToken() != JsonToken.END_OBJECT) {
            throw ctxt.wrongTokenException(jp, JsonToken.END_OBJECT, "Expected end of MorphoAnalysisList object.");
        }

        MorphoAnalysisList.Builder builder = new MorphoAnalysisList.Builder(itemClass);
        builder.setItems(items);
        return builder.build();
    }
}
