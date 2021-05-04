/*******************************************************************************
 * Copyright 2021 Basis Technology Corp.
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
 ******************************************************************************/

package com.basistech.rosette.dm.json.schema;

import com.basistech.rosette.dm.AnnotatedText;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;

import java.io.File;

/**
 * CLI helper to run JsonSchemaGenerator for AnnotatedText.
 */
public final class AdmSchemaGenerator {
    private AdmSchemaGenerator() {

    }

    /**
     * Entrypoint to run JsonSchemaGenerator for AnnotatedText.
     * @param args string output file path for the JSON schema output.
     * @throws Exception processing exception may be thrown.
     */
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonSchemaGenerator generator = new JsonSchemaGenerator(mapper);

        JsonNode jsonSchema = generator.generateJsonSchema(AnnotatedText.class);
        mapper.writeValue(new File(args[0]), jsonSchema);

    }

}
