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

import com.google.common.collect.ImmutableMap;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * A stock container for incoming raw data{@link Text} objects don't carry their
 * original bytes around, so this class provides a standard container.
 */
public class RawData {
    private final ByteBuffer data;
    // a compromise between String and Object
    private final Map<String, String[]> metadata;

    public RawData(ByteBuffer data, Map<String, String[]> metadata) {
        this.data = data.asReadOnlyBuffer();
        this.metadata = ImmutableMap.<String, String[]>builder().putAll(metadata).build();
    }

    public ByteBuffer getData() {
        return data;
    }

    public Map<String, String[]> getMetadata() {
        return metadata;
    }
}
