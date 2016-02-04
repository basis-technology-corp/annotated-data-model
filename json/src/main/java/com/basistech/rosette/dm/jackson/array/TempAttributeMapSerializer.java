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

package com.basistech.rosette.dm.jackson.array;

import com.basistech.rosette.dm.Attribute;
import com.basistech.rosette.dm.jackson.DmTypeIdResolver;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Map;

/**
 * Until we get to the bottom of <a href="https://github.com/FasterXML/jackson-databind/issues/646">This Jackson Bug</a>.
 */
public class TempAttributeMapSerializer extends JsonSerializer<Map<String, Attribute>> {

    private final DmTypeIdResolver resolver;

    public TempAttributeMapSerializer() {
        resolver = new DmTypeIdResolver();
    }

    @Override
    public void serialize(Map<String, Attribute> value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        for (Map.Entry<String, Attribute> me : value.entrySet()) {
            jgen.writeFieldName(me.getKey());
            jgen.writeStartArray();
            jgen.writeString(resolver.idFromValue(me.getValue()));
            // this will write out in array notation due to the annotation
            provider.defaultSerializeValue(me.getValue(), jgen);
            jgen.writeEndArray();
        }
        jgen.writeEndObject();
    }
}
