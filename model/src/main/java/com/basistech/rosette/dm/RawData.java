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
package com.basistech.rosette.dm;

import com.google.common.collect.ImmutableMap;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
