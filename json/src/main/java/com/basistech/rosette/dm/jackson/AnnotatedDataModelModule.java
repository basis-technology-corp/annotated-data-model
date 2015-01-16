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
import com.basistech.rosette.dm.RelationArgument;
import com.basistech.rosette.dm.RelationMention;
import com.basistech.rosette.dm.ResolvedEntity;
import com.basistech.rosette.dm.ScriptRegion;
import com.basistech.rosette.dm.Sentence;
import com.basistech.rosette.dm.Token;
import com.basistech.rosette.dm.TranslatedData;
import com.basistech.rosette.dm.TranslatedTokens;
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

    public void setupModule(SetupContext context) {
        super.setupModule(context); // pick up any enum support.
        context.setMixInAnnotations(AnnotatedText.class, AnnotatedTextMixin.class);
        context.setMixInAnnotations(ArabicMorphoAnalysis.class, ArabicMorphoAnalysisMixin.class);
        context.setMixInAnnotations(Attribute.class, AttributeMixin.class);
        context.setMixInAnnotations(BaseAttribute.class, BaseAttributeMixin.class);
        context.setMixInAnnotations(BaseNounPhrase.class, BaseNounPhraseMixin.class);
        context.setMixInAnnotations(CategorizerResult.class, CategorizerResultMixin.class);
        context.setMixInAnnotations(EntityMention.class, EntityMentionMixin.class);
        context.setMixInAnnotations(RelationArgument.class, RelationArgumentMixin.class);
        context.setMixInAnnotations(RelationMention.class, RelationMentionMixin.class);
        context.setMixInAnnotations(HanMorphoAnalysis.class, HanMorphoAnalysisMixin.class);
        context.setMixInAnnotations(KoreanMorphoAnalysis.class, KoreanMorphoAnalysisMixin.class);
        context.setMixInAnnotations(LanguageDetection.class, LanguageDetectionMixin.class);
        context.setMixInAnnotations(LanguageDetection.DetectionResult.class, LanguageDetectionMixin.DetectionResultMixin.class);
        context.setMixInAnnotations(ListAttribute.class, ListAttributeMixin.class);
        context.setMixInAnnotations(MorphoAnalysis.class, MorphoAnalysisMixin.class);
        context.setMixInAnnotations(Name.class, NameMixin.class);
        context.setMixInAnnotations(RawData.class, RawDataMixin.class);
        context.setMixInAnnotations(ResolvedEntity.class, ResolvedEntityMixin.class);
        context.setMixInAnnotations(Sentence.class, SentenceMixin.class);
        context.setMixInAnnotations(ScriptRegion.class, ScriptRegionMixin.class);
        context.setMixInAnnotations(Token.class, TokenMixin.class);
        context.setMixInAnnotations(TranslatedData.class, TranslatedDataMixin.class);
        context.setMixInAnnotations(TranslatedTokens.class, TranslatedTokensMixin.class);
    }

    /**
     * Register the Annotated Data Model Jackson module on an {@link ObjectMapper}.
     * @param mapper the mapper.
     * @return the same mapper, for convenience.
     */
    public static ObjectMapper setupObjectMapper(ObjectMapper mapper) {
        mapper.getSerializationConfig().withSerializationInclusion(JsonInclude.Include.NON_NULL);
        final AnnotatedDataModelModule module = new AnnotatedDataModelModule();
        mapper.registerModule(module);
        return mapper;
    }
}
