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

package com.basistech.rosette.dm.jackson;

import com.basistech.util.LanguageCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;

/**
 * Module for common enum types when not embedded in the rest of the ADM.
 * This provides specialized serialization when the enum constants themselves
 * are not desirable.  For example, {@code LanguageCode} is usually
 * deserialized from "ARABIC", but this module deserializes it from "ara"
 * instead.
 */
public class EnumModule extends SimpleModule {

    public EnumModule() {
        super(ModuleVersion.VERSION);
    }

    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(LanguageCode.class, LanguageCodeMixin.class);
        SimpleSerializers keySerializers = new SimpleSerializers();
        keySerializers.addSerializer(new LanguageCodeKeySerializer());
        context.addKeySerializers(keySerializers);
        // We don't need one for ISO15924.
        // We might want one some day for TextDomain?
    }

    /**
     * Register a Jackson module for Rosette's top-level enums an {@link com.fasterxml.jackson.databind.ObjectMapper}.
     * @param mapper the mapper.
     * @return the same mapper, for convenience.
     */
    public static ObjectMapper setupObjectMapper(ObjectMapper mapper) {
        final EnumModule module = new EnumModule();
        mapper.registerModule(module);
        return mapper;
    }
}
