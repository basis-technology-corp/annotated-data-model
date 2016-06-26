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

import com.basistech.rosette.dm.ArabicMorphoAnalysis;
import com.basistech.rosette.dm.HanMorphoAnalysis;
import com.basistech.rosette.dm.KoreanMorphoAnalysis;
import com.basistech.rosette.dm.MorphoAnalysis;
import com.basistech.rosette.dm.Token;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Jackson deserialization that handles polymorphism of MorphoAnalysis without writing
 * out the type in each one.
 */
public final class MorphoAnalysisListDeserializer extends JsonDeserializer<List<MorphoAnalysis>> implements ContextualDeserializer {
    private static final Set<String> ARABIC_FIELDS;

    static {
        ImmutableSet.Builder<String> builder = new ImmutableSet.Builder<>();
        for (Field field : ArabicMorphoAnalysis.class.getDeclaredFields()) {
            try {
                MorphoAnalysis.class.getDeclaredField(field.getName());
            } catch (NoSuchFieldException e) {
                builder.add(field.getName());
            }
        }
        ARABIC_FIELDS = builder.build();
    }

    private final boolean cached;
    private final JsonDeserializer<Object> maDeserializer;
    private final JsonDeserializer<Object> hanMaDeserializer;
    private final JsonDeserializer<Object> arMaDeserializer;
    private final JsonDeserializer<Object> korMaDeserializer;

    public MorphoAnalysisListDeserializer() {
        cached = false;
        maDeserializer = null;
        hanMaDeserializer = null;
        arMaDeserializer = null;
        korMaDeserializer = null;
    }

    private MorphoAnalysisListDeserializer(DeserializationContext ctxt) throws JsonMappingException {
        JavaType type = ctxt.constructType(MorphoAnalysis.class);
        maDeserializer = ctxt.findRootValueDeserializer(type);
        type = ctxt.constructType(HanMorphoAnalysis.class);
        hanMaDeserializer = ctxt.findRootValueDeserializer(type);
        type = ctxt.constructType(ArabicMorphoAnalysis.class);
        arMaDeserializer = ctxt.findRootValueDeserializer(type);
        ctxt.setAttribute(MorphoAnalysisListDeserializer.class, maDeserializer);
        type = ctxt.constructType(KoreanMorphoAnalysis.class);
        korMaDeserializer = ctxt.findRootValueDeserializer(type);
        ctxt.setAttribute(KoreanMorphoAnalysis.class, korMaDeserializer);
        cached = true;
    }

    private boolean anyArabicFields(Set<String> fields) {
        for (String field : fields) {
            if (ARABIC_FIELDS.contains(field)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private JsonDeserializer<Object> castDeserializer(Object attributeValue) {
        return (JsonDeserializer<Object>)attributeValue;
    }

    @Override
    public List<MorphoAnalysis> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

        if (!cached) {
            throw new JsonMappingException(jp, "attempt to deserialize with un-contextualized MorphoAnalysisListDeserializer");
        }

        /*
         * This will be entered pointing to the array start.
         */
        if (jp.getCurrentToken() != JsonToken.START_ARRAY) {
            throw ctxt.wrongTokenException(jp, JsonToken.START_ARRAY, "Expected array of items");
        }

        JsonDeserializer<Object> currentDeserializer =
                castDeserializer(ctxt.getAttribute(MorphoAnalysisListDeserializer.class));
        if (currentDeserializer == null) {
            currentDeserializer = maDeserializer;
            ctxt.setAttribute(MorphoAnalysisListDeserializer.class, maDeserializer);
        }

        List<MorphoAnalysis> result = Lists.newArrayList();
        while (jp.nextToken() != JsonToken.END_ARRAY) {
            // if we just read it in as the wrong class, any leftovers will end up in extendedAttributes, and we can cope.

            MorphoAnalysis analysis = (MorphoAnalysis) currentDeserializer.deserialize(jp, ctxt);
            if (analysis.getExtendedProperties().size() != 0) {
                // so, we have leftovers. Note that this will not trim han and arabic down. Tough

                if (analysis.getExtendedProperties().containsKey("morphemes")) {
                    KoreanMorphoAnalysis.Builder builder = new KoreanMorphoAnalysis.Builder();
                    copyBasic(analysis, builder);

                    List<String> morphemes = cast(analysis.getExtendedProperties().get("morphemes"));
                    List<String> morphemeTags = cast(analysis.getExtendedProperties().get("morphemeTags"));
                    for (int x = 0; x < morphemes.size(); x++) {
                        builder.addMorpheme(morphemes.get(x), morphemeTags.get(x));
                    }

                    for (Map.Entry<String, Object> me : analysis.getExtendedProperties().entrySet()) {
                        if (!"morphemes".equals(me.getKey()) && !"morphemeTags".equals(me.getKey())) {
                            builder.extendedProperty(me.getKey(), me.getValue());
                        }
                    }

                    analysis = builder.build();
                    ctxt.setAttribute(MorphoAnalysisListDeserializer.class, korMaDeserializer);

                } else if (analysis.getExtendedProperties().containsKey("readings")) {
                    // convert to Han.
                    HanMorphoAnalysis.Builder builder = new HanMorphoAnalysis.Builder();
                    copyBasic(analysis, builder);

                    for (String reading : cast(analysis.getExtendedProperties().get("readings"))) {
                        builder.addReading(reading);
                    }

                    for (Map.Entry<String, Object> me : analysis.getExtendedProperties().entrySet()) {
                        if (!"readings".equals(me.getKey())) {
                            builder.extendedProperty(me.getKey(), me.getValue());
                        }
                    }

                    analysis = builder.build();
                    ctxt.setAttribute(MorphoAnalysisListDeserializer.class, hanMaDeserializer);
                } else if (anyArabicFields(analysis.getExtendedProperties().keySet())) {
                    ArabicMorphoAnalysis.Builder builder = new ArabicMorphoAnalysis.Builder();
                    copyBasic(analysis, builder);

                    Integer prefixLength = (Integer)analysis.getExtendedProperties().get("prefixLength");
                    Integer stemLength = (Integer)analysis.getExtendedProperties().get("stemLength");
                    if (prefixLength != null && stemLength != null) {
                        builder.lengths(prefixLength, stemLength);
                    }
                    String root = (String)analysis.getExtendedProperties().get("root");
                    if (root != null) {
                        builder.root(root);
                    }
                    Boolean definiteArticle = (Boolean)analysis.getExtendedProperties().get("definiteArticle");
                    if (definiteArticle != null) {
                        builder.definiteArticle(definiteArticle);
                    }
                    Boolean strippablePrefix = (Boolean)analysis.getExtendedProperties().get("strippablePrefix");
                    if (strippablePrefix != null) {
                        builder.strippablePrefix(strippablePrefix);
                    }

                    List<String> prefixes = cast(analysis.getExtendedProperties().get("prefixes"));
                    if (prefixes != null) {
                        List<String> prefixTags = cast(analysis.getExtendedProperties().get("prefixTags"));
                        for (int x = 0; x < prefixes.size(); x++) {
                            builder.addPrefix(prefixes.get(x), prefixTags.get(x));
                        }
                    }

                    List<String> stems = cast(analysis.getExtendedProperties().get("stems"));
                    if (stems != null) {
                        List<String> stemTags = cast(analysis.getExtendedProperties().get("stemTags"));
                        for (int x = 0; x < stems.size(); x++) {
                            builder.addStem(stems.get(x), stemTags.get(x));
                        }
                    }

                    List<String> suffixes = cast(analysis.getExtendedProperties().get("suffixes"));
                    if (suffixes != null) {
                        List<String> suffixTags = cast(analysis.getExtendedProperties().get("suffixTags"));
                        for (int x = 0; x < suffixes.size(); x++) {
                            builder.addSuffix(suffixes.get(x), suffixTags.get(x));
                        }
                    }

                    for (Map.Entry<String, Object> me : analysis.getExtendedProperties().entrySet()) {
                        if (!ARABIC_FIELDS.contains(me.getKey())) {
                            builder.extendedProperty(me.getKey(), me.getValue());
                        }
                    }

                    analysis = builder.build();
                    ctxt.setAttribute(MorphoAnalysisListDeserializer.class, arMaDeserializer);
                }
            }
            result.add(analysis);
        }
        return ImmutableList.copyOf(result);
    }

    private void copyBasic(MorphoAnalysis analysis, MorphoAnalysis.Builder builder) {
        if (analysis.getLemma() != null && !"".equals(analysis.getLemma())) {
            builder.lemma(analysis.getLemma());
        }
        if (analysis.getPartOfSpeech() != null && !"".equals(analysis.getPartOfSpeech())) {
            builder.partOfSpeech(analysis.getPartOfSpeech());
        }
        if (analysis.getRaw() != null && !"".equals(analysis.getRaw())) {
            builder.raw(analysis.getRaw());
        }
        if (analysis.getComponents() != null) {
            for (Token token : analysis.getComponents()) {
                builder.addComponent(token);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static List<String> cast(Object object) {
        return (List<String>)object;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {

        if (cached) {
            return this;
        }

        // construct a replacement object with the cached deserializers in place.
        return new MorphoAnalysisListDeserializer(ctxt);
    }
}
