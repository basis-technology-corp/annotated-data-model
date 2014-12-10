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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;

/**
 * array-based scheme to deal with morpho analysis polymorphism
 */
public class MorphoAnalysisListArraySerializer extends JsonSerializer<List<MorphoAnalysis>> {
    @Override
    public void serialize(List<MorphoAnalysis> value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartArray();
        MorphoAnalysisTypes curType = MorphoAnalysisTypes.PLAIN;
        for (MorphoAnalysis ma : value) {
            MorphoAnalysisTypes type = MorphoAnalysisTypes.byClass(ma.getClass());
            if (curType != type) {
                jgen.writeNumber(type.ordinal());
                curType = type;
            }
            jgen.writeObject(ma);
        }
        jgen.writeEndArray();
    }
}
