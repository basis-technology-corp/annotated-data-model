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

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

public class RawDataTest {

    @Test
    public void rawDataNullMetadataTest() throws Exception {
        ByteBuffer bytes = ByteBuffer.allocate(10);
        for (int i = 0; i < 10; i++) {
            bytes.put((byte)'a');
        }
        RawData rawData = new RawData(bytes, null);
        assertEquals(bytes, rawData.getData());
    }
}
