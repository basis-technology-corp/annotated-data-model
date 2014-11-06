/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2009 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/

package com.basistech.rosette.dm.internal;

import com.basistech.rlp.AbstractResultAccess;
import com.basistech.rlp.ResultAccessDeserializer;
import com.basistech.rlp.ResultAccessSerializedFormat;
import com.basistech.rosette.dm.tools.AraDmConverter;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MentionTest {

    private AbstractResultAccess getResults(String resource) throws IOException {
        ResultAccessDeserializer deserializer = new ResultAccessDeserializer();
        deserializer.setFormat(ResultAccessSerializedFormat.JSON);
        InputStream in = getClass().getResourceAsStream(resource);
        return deserializer.deserializeAbstractResultAccess(in);
    }

    @Test
    public void testBasic() throws IOException {
        // 0: PERSON, [0, 2), Bill Clinton
        // 1: TITLE, [5, 6), president
        // 2: PERSON, [7, 8), Clinton
        // 3: PERSON, [10, 11), Hillary
        // 4: TITLE, [13, 16), Secretary of State
        // 5: PERSON, [17, 19), Hillary Clinton
        // 6: TITLE, [21, 22), president
        AbstractResultAccess resultAccess = getResults("simple_doc0.json");
        TextWrapper tw = new TextWrapper(AraDmConverter.convert(resultAccess));
        List<Mention> mentions = Mention.getMentions(tw);
        assertEquals(7, mentions.size());
        assertEquals("PERSON", mentions.get(0).getEntityType());
        assertEquals(0, mentions.get(0).getStartCharacterOffset());
        assertEquals(12, mentions.get(0).getEndCharacterOffset());
        assertEquals(0, mentions.get(0).getStartTokenIndex());
        assertEquals(2, mentions.get(0).getEndTokenIndex());
        assertEquals(Integer.valueOf(0), mentions.get(0).getIndocChainId());
    }
}
