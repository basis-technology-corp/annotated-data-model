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

import com.basistech.util.ISO15924;

import java.util.Map;

/**
 * A script region.
 */
public class ScriptRegion extends Attribute {
    private final ISO15924 value;

    public ScriptRegion(int startOffset, int endOffset, ISO15924 value) {
        super(startOffset, endOffset);
        this.value = value;
    }

    public ScriptRegion(int startOffset, int endOffset, ISO15924 value, Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
        this.value = value;
    }

    public ISO15924 getValue() {
        return value;
    }
}
