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
 * A container for incoming raw data (bytes).
 * {@link com.basistech.rosette.dm.AnnotatedText} does not store bytes; this class
 * serves components that consume bytes, e.g. encoding identification.
 * @adm.ignore
 */
public class RawData {
    private final ByteBuffer data;
    // a compromise between String and Object
    private final Map<String, List<String>> metadata;

    /**
     * Constructs from a buffer of data and some metadata.
     *
     * @param data the binary data
     * @param metadata metadata, such as mime type, creation date, or the like
     */
    public RawData(ByteBuffer data, Map<String, List<String>> metadata) {
        this.data = data.asReadOnlyBuffer();
        Map<String, List<String>> tmpMetaData;
        if (metadata == null) {
            tmpMetaData = new HashMap<>();
        } else {
            tmpMetaData = metadata;
        }
        this.metadata = ImmutableMap.<String, List<String>>builder().putAll(tmpMetaData).build();
    }

    /**
     * Returns the raw data.
     *
     * @return the raw data
     */
    public ByteBuffer getData() {
        return data;
    }

    /**
     * Returns the metadata.
     *
     * @return the metadata
     */
    public Map<String, List<String>> getMetadata() {
        return metadata;
    }
}
