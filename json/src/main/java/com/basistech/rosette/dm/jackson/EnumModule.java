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
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ContextualKeyDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.CreatorCollector;
import com.fasterxml.jackson.databind.deser.std.MapDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleKeyDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.fasterxml.jackson.databind.type.MapType;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Module for common enum types when not embedded in the rest of the ADM.
 * This provides specialized serialization when the enum constants themselves
 * are not desirable.  For example, {@code LanguageCode} is usually
 * deserialized from "ARABIC", but this module deserializes it from "ara"
 * instead.
 */
public class EnumModule extends SimpleModule {



    /**
     * This is a version of the MapDeserializer that  works around
     * https://github.com/FasterXML/jackson-databind/issues/944 -- failure to use
     * the correct deserializer when deserializing a map where there is a customized
     * deserializer for the key. The code in createContextual is a copy of the
     * method sitting in the Jackson repo teed up for 2.6.3. We can get rid of this when
     * that is released. */
    private static class PatchedMapDeserializer extends MapDeserializer {

        public PatchedMapDeserializer(JavaType mapType, ValueInstantiator valueInstantiator, KeyDeserializer keyDeser, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser) {
            super(mapType, valueInstantiator, keyDeser, valueDeser, valueTypeDeser);
        }

        @Override
        public JsonDeserializer<?> createContextual(DeserializationContext ctxt,
                                                    BeanProperty property) throws JsonMappingException
        {
            KeyDeserializer kd = _keyDeserializer;
            if (kd == null) {
                if (_mapType.getKeyType().getRawClass() == LanguageCode.class) {
                    kd = new LanguageCodeKeyDeserializer();
                } else {
                    kd = ctxt.findKeyDeserializer(_mapType.getKeyType(), property);
                }
            } else {
                if (kd instanceof ContextualKeyDeserializer) {
                    kd = ((ContextualKeyDeserializer) kd).createContextual(ctxt, property);
                }
            }
            JsonDeserializer<?> vd = _valueDeserializer;
            // #125: May have a content converter
            if (property != null) {
                vd = findConvertingContentDeserializer(ctxt, property, vd);
            }
            final JavaType vt = _mapType.getContentType();
            if (vd == null) {
                vd = ctxt.findContextualValueDeserializer(vt, property);
            } else { // if directly assigned, probably not yet contextual, so:
                vd = ctxt.handleSecondaryContextualization(vd, property, vt);
            }
            TypeDeserializer vtd = _valueTypeDeserializer;
            if (vtd != null) {
                vtd = vtd.forProperty(property);
            }
            HashSet<String> ignored = _ignorableProperties;
            AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
            if (intr != null && property != null) {
                AnnotatedMember member = property.getMember();
                if (member != null) {
                    String[] moreToIgnore = intr.findPropertiesToIgnore(member, false);
                    if (moreToIgnore != null) {
                        ignored = (ignored == null) ? new HashSet<String>() : new HashSet<String>(ignored);
                        for (String str : moreToIgnore) {
                            ignored.add(str);
                        }
                    }
                }
            }
            return withResolved(kd, vtd, vd, ignored);
        }
    }

    /**
     * This plugs in the above.
     */
    @SuppressWarnings("unchecked")
    private class CustomDeserializers extends SimpleDeserializers {
        @Override
        public JsonDeserializer<?> findMapDeserializer(MapType type,
                                                       DeserializationConfig config,
                                                       BeanDescription beanDesc,
                                                       KeyDeserializer keyDeserializer,
                                                       TypeDeserializer elementTypeDeserializer,
                                                       JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
            if (type.getKeyType().getRawClass() == LanguageCode.class) {
                JavaType linkedHashMap = config.getTypeFactory().constructSpecializedType(type, LinkedHashMap.class);
                beanDesc = config.introspectForCreation(linkedHashMap);

                List<AnnotatedConstructor> ctors = beanDesc.getConstructors();
                CreatorCollector creators =  new CreatorCollector(beanDesc, false);
                for (AnnotatedConstructor ctor : ctors) {
                    if (ctor.getParameterCount() == 1 && ctor.getRawParameterType(0) == int.class) {
                        creators.addIntCreator(ctor, false);
                    }
                }
                AnnotatedConstructor defaultCtor = beanDesc.findDefaultConstructor();
                creators.setDefaultCreator(defaultCtor);
                ValueInstantiator valueInstantiator = creators.constructValueInstantiator(config);
                return new PatchedMapDeserializer(linkedHashMap, valueInstantiator, keyDeserializer, (JsonDeserializer<Object>) elementDeserializer, elementTypeDeserializer);
            } else {
                return null;
            }
        }
    }

    public EnumModule() {
        super(ModuleVersion.VERSION);
    }

    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(LanguageCode.class, LanguageCodeMixin.class);
        SimpleSerializers keySerializers = new SimpleSerializers();
        keySerializers.addSerializer(new LanguageCodeKeySerializer());
        keySerializers.addSerializer(Object.class, new DynamicKeySerializer());
        context.addKeySerializers(keySerializers);
        SimpleKeyDeserializers keyDeserializers = new SimpleKeyDeserializers();
        keyDeserializers.addDeserializer(LanguageCode.class, new LanguageCodeKeyDeserializer());
        context.addDeserializers(new CustomDeserializers());
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
