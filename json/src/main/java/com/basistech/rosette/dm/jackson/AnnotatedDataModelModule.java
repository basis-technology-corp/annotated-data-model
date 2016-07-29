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

import com.basistech.rosette.dm.AnnotatedText;
import com.basistech.rosette.dm.ArabicMorphoAnalysis;
import com.basistech.rosette.dm.Attribute;
import com.basistech.rosette.dm.BaseAttribute;
import com.basistech.rosette.dm.BaseNounPhrase;
import com.basistech.rosette.dm.CategorizerResult;
import com.basistech.rosette.dm.Dependency;
import com.basistech.rosette.dm.Entity;
import com.basistech.rosette.dm.Extent;
import com.basistech.rosette.dm.HanMorphoAnalysis;
import com.basistech.rosette.dm.KoreanMorphoAnalysis;
import com.basistech.rosette.dm.LanguageDetection;
import com.basistech.rosette.dm.ListAttribute;
import com.basistech.rosette.dm.Mention;
import com.basistech.rosette.dm.MorphoAnalysis;
import com.basistech.rosette.dm.Name;
import com.basistech.rosette.dm.RawData;
import com.basistech.rosette.dm.RelationshipComponent;
import com.basistech.rosette.dm.RelationshipMention;
import com.basistech.rosette.dm.ScriptRegion;
import com.basistech.rosette.dm.Sentence;
import com.basistech.rosette.dm.Token;
import com.basistech.rosette.dm.TranslatedData;
import com.basistech.rosette.dm.TranslatedTokens;
import com.basistech.util.jackson.EnumModule;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * Jackson module to configure Json serialization and deserialization for the
 * annotated data model.
 */
public class  AnnotatedDataModelModule extends EnumModule {

    public AnnotatedDataModelModule() {
        super();
    }

    @SuppressWarnings("deprecation")
    public void setupModule(SetupContext context) {
        super.setupModule(context); // pick up any enum support.
        context.setMixInAnnotations(AnnotatedText.class, AnnotatedTextMixin.class);
        context.setMixInAnnotations(ArabicMorphoAnalysis.class, ArabicMorphoAnalysisMixin.class);
        context.setMixInAnnotations(Attribute.class, AttributeMixin.class);
        context.setMixInAnnotations(BaseAttribute.class, BaseAttributeMixin.class);
        context.setMixInAnnotations(BaseNounPhrase.class, BaseNounPhraseMixin.class);
        context.setMixInAnnotations(CategorizerResult.class, CategorizerResultMixin.class);
        context.setMixInAnnotations(Entity.class, EntityMixin.class);
        context.setMixInAnnotations(com.basistech.rosette.dm.EntityMention.class, EntityMentionMixin.class);
        context.setMixInAnnotations(RelationshipComponent.class, RelationshipComponentMixin.class);
        context.setMixInAnnotations(RelationshipMention.class, RelationshipMentionMixin.class);
        context.setMixInAnnotations(Extent.class, ExtentMixin.class);
        context.setMixInAnnotations(HanMorphoAnalysis.class, HanMorphoAnalysisMixin.class);
        context.setMixInAnnotations(KoreanMorphoAnalysis.class, KoreanMorphoAnalysisMixin.class);
        context.setMixInAnnotations(LanguageDetection.class, LanguageDetectionMixin.class);
        context.setMixInAnnotations(LanguageDetection.DetectionResult.class, LanguageDetectionMixin.DetectionResultMixin.class);
        context.setMixInAnnotations(ListAttribute.class, ListAttributeMixin.class);
        context.setMixInAnnotations(Mention.class, MentionMixin.class);
        context.setMixInAnnotations(MorphoAnalysis.class, MorphoAnalysisMixin.class);
        context.setMixInAnnotations(Name.class, NameMixin.class);
        context.setMixInAnnotations(RawData.class, RawDataMixin.class);
        context.setMixInAnnotations(com.basistech.rosette.dm.ResolvedEntity.class, ResolvedEntityMixin.class);
        context.setMixInAnnotations(Sentence.class, SentenceMixin.class);
        context.setMixInAnnotations(ScriptRegion.class, ScriptRegionMixin.class);
        context.setMixInAnnotations(Token.class, TokenMixin.class);
        context.setMixInAnnotations(TranslatedData.class, TranslatedDataMixin.class);
        context.setMixInAnnotations(TranslatedTokens.class, TranslatedTokensMixin.class);
        context.setMixInAnnotations(Dependency.class, DependencyMixin.class);
    }

    /**
     * Register the Annotated Data Model Jackson module on an {@link ObjectMapper}.
     * @param mapper the mapper.
     * @return the same mapper, for convenience.
     */
    public static ObjectMapper setupObjectMapper(ObjectMapper mapper) {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        final AnnotatedDataModelModule module = new AnnotatedDataModelModule();
        mapper.registerModule(module);

        return mapper;
    }
}
