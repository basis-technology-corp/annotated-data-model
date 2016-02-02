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
package com.basistech.rosette.dm.json.array;

import com.basistech.rosette.dm.Sentence;
import org.junit.Test;

/**
 * Some basic tests that the array mechanism works.
 */
public class SimpleArrayTest extends AdmAssert {
    @Test
    public void sentence() throws Exception {
        Sentence attr = new Sentence.Builder(0, 10).build();
        String json = objectMapper().writeValueAsString(attr);

        Sentence readBack = objectMapper().readValue(json, Sentence.class);
        assertEquals(attr, readBack);
    }

    @Test
    public void extendedProps() throws Exception {
        Sentence.Builder builder = (Sentence.Builder) new Sentence.Builder(10, 20).extendedProperty("ext", "prop");
        Sentence sent = builder.build();
        String json = objectMapper().writeValueAsString(sent);

        Sentence readBack = objectMapper().readValue(json, Sentence.class);
        assertEquals(sent, readBack);
    }
}
