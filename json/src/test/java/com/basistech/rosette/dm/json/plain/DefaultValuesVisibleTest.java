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
package com.basistech.rosette.dm.json.plain;

import com.basistech.rosette.dm.AnnotatedText;
import com.basistech.rosette.dm.Entity;
import com.basistech.rosette.dm.ListAttribute;
import com.basistech.rosette.dm.Mention;
import com.basistech.rosette.dm.jackson.KnownAttribute;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

/**
 * Test that default values are rendered into the Json.
 */
public class DefaultValuesVisibleTest extends AdmAssert {

    @Test
    public void checkVisible() throws Exception {
        AnnotatedText.Builder builder = new AnnotatedText.Builder();
        builder.data("George Washington slept here.");
        ListAttribute.Builder<Entity> entityListBuilder = new ListAttribute.Builder<>(Entity.class);
        //int startOffset, int endOffset, String entityType)
        Mention.Builder mentionBuilder = new Mention.Builder(0, 17);
        mentionBuilder.confidence(null); // null is the official default, but null is never rendered, default or not.
        mentionBuilder.linkingConfidence(null); // null is the official default, but null is never rendered, default or not.
        Entity.Builder entityBuilder = new Entity.Builder();
        entityBuilder.mention(mentionBuilder.build());
        entityListBuilder.add(entityBuilder.build());
        builder.entities(entityListBuilder.build());

        AnnotatedText text = builder.build();

        ObjectMapper mapper = objectMapper();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        mapper.writeValue(byteArrayOutputStream, text); // serialize

        // now bring it back
        JsonNode tree = mapper.readTree(byteArrayOutputStream.toByteArray());
        // and navigate to the problem at hand.
        ObjectNode attrsNode = (ObjectNode) tree.get("attributes");
        ObjectNode mentionsNode = (ObjectNode) attrsNode.get(KnownAttribute.ENTITY.key());
        ObjectNode mentionNode = (ObjectNode) mentionsNode.get("items").get(0);
        assertFalse(mentionNode.has("confidence"));
    }
}
