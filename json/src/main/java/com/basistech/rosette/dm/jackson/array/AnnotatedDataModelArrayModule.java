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

package com.basistech.rosette.dm.jackson.array;

import com.basistech.rosette.dm.AnnotatedText;
import com.basistech.rosette.dm.ArabicMorphoAnalysis;
import com.basistech.rosette.dm.Attribute;
import com.basistech.rosette.dm.BaseAttribute;
import com.basistech.rosette.dm.BaseNounPhrase;
import com.basistech.rosette.dm.CategorizerResult;
import com.basistech.rosette.dm.EntityMention;
import com.basistech.rosette.dm.HanMorphoAnalysis;
import com.basistech.rosette.dm.KoreanMorphoAnalysis;
import com.basistech.rosette.dm.LanguageDetection;
import com.basistech.rosette.dm.ListAttribute;
import com.basistech.rosette.dm.MorphoAnalysis;
import com.basistech.rosette.dm.Name;
import com.basistech.rosette.dm.RawData;
import com.basistech.rosette.dm.RelationshipComponent;
import com.basistech.rosette.dm.RelationshipMention;
import com.basistech.rosette.dm.ResolvedEntity;
import com.basistech.rosette.dm.ScriptRegion;
import com.basistech.rosette.dm.Sentence;
import com.basistech.rosette.dm.Token;
import com.basistech.rosette.dm.TranslatedData;
import com.basistech.rosette.dm.TranslatedTokens;
import com.basistech.rosette.dm.jackson.ArabicMorphoAnalysisMixin;
import com.basistech.rosette.dm.jackson.AttributeMixin;
import com.basistech.rosette.dm.jackson.BaseNounPhraseMixin;
import com.basistech.rosette.dm.jackson.CategorizerResultMixin;
import com.basistech.rosette.dm.jackson.EntityMentionMixin;
import com.basistech.rosette.dm.jackson.HanMorphoAnalysisMixin;
import com.basistech.rosette.dm.jackson.KoreanMorphoAnalysisMixin;
import com.basistech.rosette.dm.jackson.MorphoAnalysisMixin;
import com.basistech.rosette.dm.jackson.NameMixin;
import com.basistech.rosette.dm.jackson.RawDataMixin;
import com.basistech.rosette.dm.jackson.RelationshipComponentMixin;
import com.basistech.rosette.dm.jackson.RelationshipMentionMixin;
import com.basistech.rosette.dm.jackson.ResolvedEntityMixin;
import com.basistech.rosette.dm.jackson.ScriptRegionMixin;
import com.basistech.rosette.dm.jackson.SentenceMixin;
import com.basistech.rosette.dm.jackson.TranslatedDataMixin;
import com.basistech.rosette.dm.jackson.TranslatedTokensMixin;
import com.basistech.util.jackson.EnumModule;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * Jackson module to configure Json serialization and deserialization for the
 * annotated data model, using an array syntax for performance.
 *
 * By default, Jackson represents Java objects as Json objects. This results in writing out
 * all the field names as keys and looking them up, reflectively, when reading in. Jackson
 * supports an alternative that writes out Java objects as arrays, using a fixed order for the fields
 * This module installs customizations that read and write ADM using arrays.
 */
public class AnnotatedDataModelArrayModule extends EnumModule {

    public AnnotatedDataModelArrayModule() {
        super();
    }

    public void setupModule(SetupContext context) {
        super.setupModule(context); // pick up any enum support.
        context.setMixInAnnotations(AnnotatedText.class, AnnotatedTextArrayMixin.class);
        context.setMixInAnnotations(ArabicMorphoAnalysis.class, ArabicMorphoAnalysisMixin.class);
        context.setMixInAnnotations(Attribute.class, AttributeMixin.class);
        context.setMixInAnnotations(BaseAttribute.class, BaseAttributeArrayMixin.class);
        context.setMixInAnnotations(BaseNounPhrase.class, BaseNounPhraseMixin.class);
        context.setMixInAnnotations(CategorizerResult.class, CategorizerResultMixin.class);
        context.setMixInAnnotations(EntityMention.class, EntityMentionMixin.class);
        context.setMixInAnnotations(RelationshipComponent.class, RelationshipComponentMixin.class);
        context.setMixInAnnotations(RelationshipMention.class, RelationshipMentionMixin.class);
        context.setMixInAnnotations(HanMorphoAnalysis.class, HanMorphoAnalysisMixin.class);
        context.setMixInAnnotations(KoreanMorphoAnalysis.class, KoreanMorphoAnalysisMixin.class);
        context.setMixInAnnotations(LanguageDetection.class, LanguageDetectionArrayMixin.class);
        context.setMixInAnnotations(LanguageDetection.DetectionResult.class, LanguageDetectionArrayMixin.DetectionResultMixin.class);
        context.setMixInAnnotations(ListAttribute.class, ListAttributeArrayMixin.class);
        context.setMixInAnnotations(MorphoAnalysis.class, MorphoAnalysisMixin.class);
        context.setMixInAnnotations(Name.class, NameMixin.class);
        context.setMixInAnnotations(RawData.class, RawDataMixin.class);
        context.setMixInAnnotations(ResolvedEntity.class, ResolvedEntityMixin.class);
        context.setMixInAnnotations(Sentence.class, SentenceMixin.class);
        context.setMixInAnnotations(ScriptRegion.class, ScriptRegionMixin.class);
        context.setMixInAnnotations(Token.class, TokenArrayMixin.class);
        context.setMixInAnnotations(TranslatedData.class, TranslatedDataMixin.class);
        context.setMixInAnnotations(TranslatedTokens.class, TranslatedTokensMixin.class);
    }

    /**
     * Register the Annotated Data Model Jackson module on an {@link ObjectMapper}.
     * @param mapper the mapper.
     * @return the same mapper, for convenience.
     */
    public static ObjectMapper setupObjectMapper(ObjectMapper mapper) {
        final AnnotatedDataModelArrayModule module = new AnnotatedDataModelArrayModule();
        mapper.registerModule(module);
        return mapper;
    }
}
