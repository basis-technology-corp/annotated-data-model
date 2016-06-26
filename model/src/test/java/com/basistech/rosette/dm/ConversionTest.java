/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2015 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/

package com.basistech.rosette.dm;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Home for tests related to old/new data model issues.
 */
@SuppressWarnings("deprecation")
public class ConversionTest {

    @Test
    public void corefChainToEntityId() throws Exception {
        AnnotatedText.Builder builder = new AnnotatedText.Builder();
        builder.data("ignore");
        ListAttribute.Builder<EntityMention> emListBuilder = new ListAttribute.Builder<>(EntityMention.class);
        EntityMention.Builder emBuilder = new EntityMention.Builder(0, 10, "PARSON");
        emBuilder.coreferenceChainId(0);
        emListBuilder.add(emBuilder.build());
        builder.entityMentions(emListBuilder.build());
        AnnotatedText text = builder.build();
        assertEquals("T0", text.getEntities().get(0).getEntityId());
    }
}
