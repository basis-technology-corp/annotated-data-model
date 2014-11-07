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
