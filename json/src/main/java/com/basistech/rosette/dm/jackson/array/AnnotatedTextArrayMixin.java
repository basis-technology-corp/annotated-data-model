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

import com.basistech.rosette.dm.BaseAttribute;
import com.basistech.rosette.dm.BaseNounPhrase;
import com.basistech.rosette.dm.CategorizerResult;
import com.basistech.rosette.dm.EntityMention;
import com.basistech.rosette.dm.LanguageDetection;
import com.basistech.rosette.dm.ListAttribute;
import com.basistech.rosette.dm.RelationshipMention;
import com.basistech.rosette.dm.ResolvedEntity;
import com.basistech.rosette.dm.ScriptRegion;
import com.basistech.rosette.dm.Sentence;
import com.basistech.rosette.dm.Token;
import com.basistech.rosette.dm.TranslatedData;
import com.basistech.rosette.dm.TranslatedTokens;
import com.basistech.rosette.dm.jackson.DmTypeIdResolver;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.List;
import java.util.Map;

/**
 * {@link com.basistech.rosette.dm.AnnotatedText}.
 */
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonAppend(attrs={ @JsonAppend.Attr("version")})
@JsonPropertyOrder(alphabetic = true, value = {"version"})
public abstract class AnnotatedTextArrayMixin {

    @JsonCreator
    AnnotatedTextArrayMixin(@JsonProperty("data") CharSequence data,
                            @JsonProperty("attributes") Map<String, BaseAttribute> attributes,
                            @JsonProperty("documentMetadata") Map<String, List<String>> documentMetadata) {
        //
    }

    // The first two work right for deserialization but not for serialization, so we have the third.
    @JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.WRAPPER_ARRAY)
    @JsonTypeIdResolver(DmTypeIdResolver.class)
    @JsonSerialize(using = TempAttributeMapSerializer.class)
    public abstract Map<String, BaseAttribute> getAttributes();

    /* prevent Jackson from serializing a complex object here. */
    @JsonSerialize(using = ToStringSerializer.class)
    public abstract CharSequence getData();

    @JsonIgnore
    public abstract ListAttribute<Token> getTokens();

    @JsonIgnore
    public abstract ListAttribute<TranslatedData> getTranslatedData();

    @JsonIgnore
    public abstract ListAttribute<TranslatedTokens> getTranslatedTokens();

    @JsonIgnore
    public abstract ListAttribute<EntityMention> getEntityMentions();

    @JsonIgnore
    public abstract ListAttribute<ResolvedEntity> getResolvedEntities();

    @JsonIgnore
    public abstract ListAttribute<RelationshipMention> getRelationshipMentions();

    @JsonIgnore
    public abstract ListAttribute<ScriptRegion> getScriptRegions();

    @JsonIgnore
    public abstract ListAttribute<Sentence> getSentences();

    @JsonIgnore
    public abstract ListAttribute<BaseNounPhrase> getBaseNounPhrases();

    @JsonIgnore
    public abstract ListAttribute<LanguageDetection> getLanguageDetectionRegions();

    @JsonIgnore
    public abstract LanguageDetection getWholeTextLanguageDetection();

    @JsonIgnore
    public abstract ListAttribute<CategorizerResult> getCategorizerResults();

    @JsonIgnore
    public abstract ListAttribute<CategorizerResult> getSentimentResults();

    @JsonIgnore
    public abstract void setVersion(String version);
}
