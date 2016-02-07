/*
* Copyright 2016 Basis Technology Corp.
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


package com.basistech.rosette.dm.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * At some point in the future, this library may include custom deserialization
 * to allow reading of old versions of the data structure. Until then,
 * this class simply catches incompatible versions. Note that this can't
 * catch 'no version at all', so it's mostly useful to prevent someone
 * trying to feed (e.g.) a Version 2 ADM into a Version 1 library, and not
 * the other way around.
 *
 */
public class VersionCheckDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.getCurrentToken() != JsonToken.VALUE_STRING) {
            throw ctxt.wrongTokenException(p, JsonToken.VALUE_STRING, "The value of 'version' must be a string");
        }
        String version = p.readValueAs(String.class);
        String[] bits = version.split("\\.");
        if (bits.length < 3) { // allow for a fourth digit for some reason some day.
            throw ctxt.weirdStringException(version, String.class, "Versions must be of the form x.y.z");
        }
        if (!"1".equals(bits[0])) {
            throw ctxt.weirdStringException(version, String.class, String.format("Incompatible ADM version %s", version));
        }
        return version;
    }
}
