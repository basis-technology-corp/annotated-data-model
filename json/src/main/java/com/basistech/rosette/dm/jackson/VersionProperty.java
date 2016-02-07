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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.VirtualBeanPropertyWriter;
import com.fasterxml.jackson.databind.util.Annotations;

/**
 * Custom injected property that injects the version of the ADM _as a Json data model_.
 */
public final class VersionProperty extends VirtualBeanPropertyWriter {
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
