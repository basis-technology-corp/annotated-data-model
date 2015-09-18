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

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;

/**
 * Jackson custom type info resolver for the data model.
 */
public class DmTypeIdResolver extends TypeIdResolverBase {

    @Override
    public void init(JavaType javaType) {
        //
    }

    @Override
    public String idFromValue(Object o) {
        return idFromValueAndType(o, o.getClass());
    }

    @Override
    public String idFromValueAndType(Object o, Class<?> aClass) {
        KnownAttribute attribute = KnownAttribute.getAttributeForClass(o.getClass());
        return attribute.key();
    }

    @Override
    public JavaType typeFromId(DatabindContext context, String id) {
        KnownAttribute attribute = KnownAttribute.getAttributeForKey(id);
        if (attribute == null) {
            attribute = KnownAttribute.UNKNOWN; // extension mechanism, build a BaseAttribute.
        }
        return context.constructType(attribute.attributeClass());
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }
}
