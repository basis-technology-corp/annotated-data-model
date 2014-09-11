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

package com.basistech.rosette.dm.test;

import com.basistech.rosette.dm.AnnotatedText;
import com.basistech.rosette.dm.Annotator;
import com.basistech.rosette.dm.RawData;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;

/**
 * Make sure that the shaded POM is not missing critical things.
 */
public class ShadeIsNotCrazyTest {

    static Class<Annotator> annotatorClass;

    @Test
    public void checkForMissingBits() {
        annotatorClass = Annotator.class; // blow up if Annotator is missing.
        new AnnotatedText.Builder().data("foo").build();
        new RawData(ByteBuffer.allocate(100), new HashMap<String, List<String>>());
    }
}
