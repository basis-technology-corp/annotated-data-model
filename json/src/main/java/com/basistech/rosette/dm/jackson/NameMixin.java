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
package com.basistech.rosette.dm.jackson;

import com.basistech.util.ISO15924;
import com.basistech.util.LanguageCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Jackson mixin for {@link com.basistech.rosette.dm.Name}.
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public abstract class NameMixin {
    // Due to an apparent Jackson bug these are not used to define defaults.
    // Thus there's a no-args ctor, which we would rather not have, on the actual class.
    String text = "";
    ISO15924 script = ISO15924.Zyyy;
    LanguageCode languageOfUse = LanguageCode.UNKNOWN;
    LanguageCode languageOfOrigin = LanguageCode.UNKNOWN;

    @JsonCreator
    public NameMixin() {
        //
    }

    @JsonCreator
    public NameMixin(@JsonProperty("text") String text,
                     @JsonProperty("type") String type,
                     @JsonProperty("script") ISO15924 script,
                     @JsonProperty("languageOfOrigin") LanguageCode languageOfOrigin,
                     @JsonProperty("languageOfUse") LanguageCode languageOfUse,
                     @JsonProperty("extendedProperties") Map<String, Object> extendedProperties) {
        //
    }

}
