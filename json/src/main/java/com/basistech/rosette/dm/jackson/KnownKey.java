/*
* Copyright 2018 Basis Technology Corp.
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

import com.basistech.rosette.dm.UnknownAttribute;
import com.basistech.util.LanguageCode;

/**
 * The attributes currently defined in the data model.
 * Applications may extend the model with additional attributes;
 * this provides keys for {@link com.basistech.rosette.dm.AnnotatedText}
 * for the known map keys.
 */
public enum KnownKey {
    // TODO: Can we remove this enum somehow?
    STRING("string", String.class),
    LANGUAGE_CODE("languageCode", LanguageCode.class),
    UNKNOWN("unknown", UnknownAttribute.class);

    private final String jsonTag;
    private final Class<?> attributeClass;

    KnownKey(String jsonTag, Class<?> keyClass) {
        this.jsonTag = jsonTag;
        this.attributeClass = keyClass;
    }
    /**
     * @return a value used as a key in {@link com.basistech.rosette.dm.AnnotatedText} or
     * in json serialization.
     */
    public String key() {
        return jsonTag;
    }

    public Class<?> keyClass() {
        return attributeClass;
    }

    public static KnownKey getKnownForKey(String key) {
        for (KnownKey item : values()) {
            if (item.key().equals(key)) {
                return item;
            }
        }
        return null;
    }

    public static KnownKey getKeyForClass(Class<?> attributeClass) {
        for (KnownKey item : values()) {
            if (item.keyClass().equals(attributeClass)) {
                return item;
            }
        }
        return null;
    }
}
