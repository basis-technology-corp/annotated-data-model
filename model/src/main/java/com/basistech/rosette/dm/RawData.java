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

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

/**
 * A container for incoming raw data.
 * {@link AnnotatedText} objects don't carry their
 * original bytes around, so this class provides a standard container.
 */
public class RawData {
    private final ByteBuffer data;
    // a compromise between String and Object
    private final Map<String, List<String>> metadata;

    /**
     * @param data the binary data.
     * @param metadata metadata, such as mime type, creation date, or the like.
     */
    public RawData(ByteBuffer data, Map<String, List<String>> metadata) {
        this.data = data.asReadOnlyBuffer();
        Map<String, List<String>> tmpMetaData;
        if (metadata == null) {
            tmpMetaData = new HashMap<String, List<String>>();
        } else {
            tmpMetaData = metadata;
        }
        this.metadata = ImmutableMap.<String, List<String>>builder().putAll(tmpMetaData).build();
    }

    public ByteBuffer getData() {
        return data;
    }

    public Map<String, List<String>> getMetadata() {
        return metadata;
    }
}
