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

package com.basistech.dm;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.io.IOException;

/**
 * Jackson serializer that that handles our slightly manual polymorphism.
 * Instead of letting Jackson write out the class as an attribute of each list item,
 * we write it out once.
 */
public class MorphoAnalysisListSerializer extends JsonSerializer<MorphoAnalysisList> {
    @Override
    public void serialize(MorphoAnalysisList value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("itemClass", value.getItemClass().getName());
        jgen.writeObjectField("items", value.getItems());
        jgen.writeEndObject();
    }

    @Override
    public void serializeWithType(MorphoAnalysisList value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
        typeSer.writeTypePrefixForObject(value, jgen);
        jgen.writeStringField("itemClass", value.getItemClass().getName());
        jgen.writeObjectField("items", value.getItems());
        typeSer.writeTypeSuffixForObject(value, jgen);
    }
}
