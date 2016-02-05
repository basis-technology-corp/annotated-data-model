/******************************************************************************
 * * This data and information is proprietary to, and a valuable trade secret
 * * of, Basis Technology Corp.  It is given in confidence by Basis Technology
 * * and may only be used as permitted under the license agreement under which
 * * it has been distributed, and in no other way.
 * *
 * * Copyright (c) 2015 Basis Technology Corporation All rights reserved.
 * *
 * * The technical data and information provided herein are provided with
 * * `limited rights', and the computer software provided herein is provided
 * * with `restricted rights' as those terms are defined in DAR and ASPR
 * * 7-104.9(a).
 ******************************************************************************/

package com.basistech.rosette.dm.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.VirtualBeanPropertyWriter;
import com.fasterxml.jackson.databind.util.Annotations;

/**
 * Created by benson on 2/5/16.
 */
class VersionProperty extends VirtualBeanPropertyWriter {
    private VersionProperty() {
        super();
    }

    private VersionProperty(BeanPropertyDefinition propDef, Annotations ctxtAnn, JavaType type) {
        super(propDef, ctxtAnn, type);
    }

    @Override
    protected Object value(Object bean, JsonGenerator jgen, SerializerProvider prov) {
        if (_name.toString().equals("version")) {
            return "1.0.0";
        }
        return null;
    }

    @Override
    public VirtualBeanPropertyWriter withConfig(MapperConfig<?> config,
                                                AnnotatedClass declaringClass, BeanPropertyDefinition propDef,
                                                JavaType type) {
        return new VersionProperty(propDef, declaringClass.getAnnotations(), type);
    }
}
