/*
* Copyright 2019 Basis Technology Corp.
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

import com.basistech.rosette.dm.BaseAttribute;
import com.basistech.rosette.dm.BaseNounPhrase;
import com.basistech.rosette.dm.CategorizerResult;
import com.basistech.rosette.dm.Concept;
import com.basistech.rosette.dm.LayoutRegion;
import com.basistech.rosette.dm.Dependency;
import com.basistech.rosette.dm.Embeddings;
import com.basistech.rosette.dm.Entity;
import com.basistech.rosette.dm.Keyphrase;
import com.basistech.rosette.dm.LanguageDetection;
import com.basistech.rosette.dm.ListAttribute;
import com.basistech.rosette.dm.RelationshipMention;
import com.basistech.rosette.dm.ScriptRegion;
import com.basistech.rosette.dm.Sentence;
import com.basistech.rosette.dm.Token;
import com.basistech.rosette.dm.TranslatedData;
import com.basistech.rosette.dm.TranslatedTokens;
import com.basistech.rosette.dm.TransliterationResults;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.List;
import java.util.Map;

/**
 * {@link com.basistech.rosette.dm.AnnotatedText}.
 */

@JsonAppend(prepend = true, props = { @JsonAppend.Prop(value = VersionProperty.class, name = "version")})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuppressWarnings("deprecated")
public abstract class AnnotatedTextMixin {

    @JsonCreator
    AnnotatedTextMixin(@JsonProperty("data") CharSequence data,
                       @JsonProperty("attributes") Map<String, BaseAttribute> attributes,
                       @JsonProperty("documentMetadata") Map<String, List<String>> documentMetadata,
                       /* work around https://github.com/FasterXML/jackson-databind/issues/1118,
                       * and also quickly check for ADMs from 'the future'. */
                       @JsonProperty("version")
                       @JsonDeserialize(using = VersionCheckDeserializer.class)
                       String version) {
        //
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonTypeIdResolver(DmTypeIdResolver.class)
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

    @SuppressWarnings("deprecation")
    @JsonIgnore
    public abstract ListAttribute<com.basistech.rosette.dm.EntityMention> getEntityMentions();

    @SuppressWarnings("deprecation")
    @JsonIgnore
    public abstract ListAttribute<com.basistech.rosette.dm.ResolvedEntity> getResolvedEntities();

    @JsonIgnore
    public abstract ListAttribute<RelationshipMention> getRelationshipMentions();

    @JsonIgnore
    public abstract ListAttribute<ScriptRegion> getScriptRegions();

    @JsonIgnore
    public abstract ListAttribute<Sentence> getSentences();

    @JsonIgnore
    public abstract ListAttribute<LayoutRegion> getLayoutRegions();

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
    public abstract ListAttribute<CategorizerResult> getTopicResults();

    @JsonIgnore
    public abstract ListAttribute<Entity> getEntities();

    @JsonIgnore
    public abstract ListAttribute<Dependency> getDependencies();

    @JsonIgnore
    public abstract Embeddings getEmbeddings();

    @JsonIgnore
    public abstract ListAttribute<Concept> getConcepts();

    @JsonIgnore
    public abstract ListAttribute<Keyphrase> getKeyphrases();

    @JsonIgnore
    public abstract TransliterationResults getTransliteration();
}
