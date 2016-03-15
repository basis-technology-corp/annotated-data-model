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
import com.basistech.rosette.dm.jackson.AnnotatedDataModelModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

// From v0 to v1, we deprecated EntityMention and ResolvedEntity, and
// introduced Entity.  v0 json should be converted to the new Entity model.
//
//    mention            type      chainId
// 0  Bill Clinton       PERSON          0
// 1  president          TITLE           1
// 2  Clinton            PERSON          0
// 3  Hillary            PERSON          5
// 4  Secretary of State TITLE           4
// 5  Hillary Clinton    PERSON          5
// 6  president          TITLE           1
//
// In the old model, chainId referred to the index of the head mention in
// the list of all mentions.  In the new model, each entity has a headMention,
// which is the index into each entity's private list of mentions.  The order
// of the entities must be the document order of the head mentions.  The order
// of mentions within an entity should follow document order.
public class CompatibilityV100ToV110Test {
    @Test
    public void mentionsWithoutChains() throws Exception {
        ObjectMapper mapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());
        AnnotatedText text = mapper.readValue(new File("test-data/simple_doc0_no_chains_adm_v100.json"),
                AnnotatedText.class);
        assertEquals(7, text.getEntities().size());
        assertEquals(null, text.getEntities().get(3).getEntityId());
        assertEquals("PERSON", text.getEntities().get(3).getType()); // even if RES wasn't involved, we take the type from the head mention.
        assertEquals(null, text.getEntities().get(3).getConfidence());
        assertEquals(null, text.getEntities().get(3).getSentiment());
        assertEquals(null, text.getEntities().get(3).getHeadMentionIndex());
        assertEquals("Hillary", text.getEntities().get(3).getMentions().get(0).getNormalized());
    }

    @Test
    public void mentionsWithChains() throws Exception {
        ObjectMapper mapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());
        AnnotatedText text = mapper.readValue(new File("test-data/simple_doc0_with_chains_adm_v100.json"),
                AnnotatedText.class);
        assertEquals(4, text.getEntities().size());
        // Hillary should be entity index 3, based on doc order of head mentions.
        assertEquals(null, text.getEntities().get(3).getEntityId());
        assertEquals("PERSON", text.getEntities().get(3).getType());
        assertEquals(null, text.getEntities().get(3).getConfidence());
        assertEquals(null, text.getEntities().get(3).getSentiment());
        assertEquals(1, (int) text.getEntities().get(3).getHeadMentionIndex());
        assertEquals("Hillary", text.getEntities().get(3).getMentions().get(0).getNormalized());
        assertEquals("Hillary Clinton", text.getEntities().get(3).getMentions().get(1).getNormalized());
    }

    @Test
    public void mentionsWithResolvedEntities() throws Exception {
        ObjectMapper mapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());
        AnnotatedText text = mapper.readValue(new File("test-data/simple_doc0_resolved_adm_v100.json"),
                AnnotatedText.class);
        assertEquals(4, text.getEntities().size());
        // Hillary should be entity index 3, based on doc order of head mentions.
        assertEquals("Q6294", text.getEntities().get(3).getEntityId());
        assertEquals("PERSON", text.getEntities().get(3).getType());
        assertEquals(1.0, text.getEntities().get(3).getConfidence(), 0.00001);
        assertEquals(null, text.getEntities().get(3).getSentiment());
        assertEquals(1, (int) text.getEntities().get(3).getHeadMentionIndex());
        assertEquals("Hillary", text.getEntities().get(3).getMentions().get(0).getNormalized());
        assertEquals("Hillary Clinton", text.getEntities().get(3).getMentions().get(1).getNormalized());
    }
}
