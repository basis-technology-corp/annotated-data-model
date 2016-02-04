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
package com.basistech.rosette.dm.json.plain;

import com.basistech.rosette.dm.AnnotatedText;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.junit.Test;

import java.io.File;

/**
 * Tests of the complex code that speeds up morpho analysis deserialization.
 */
public class MorphoDeserializerTest extends AdmAssert {

    @Test
    public void comn130() throws Exception {
        ObjectMapper mapper = objectMapper();
        ObjectReader reader = mapper.readerFor(AnnotatedText.class);
        // threw
        reader.readValue(new File("test-data/comn-130-adm.json"));
    }
}
