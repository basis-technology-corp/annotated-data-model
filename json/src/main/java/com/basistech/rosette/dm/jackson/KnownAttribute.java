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
import com.basistech.rosette.dm.Dependency;
import com.basistech.rosette.dm.Embeddings;
import com.basistech.rosette.dm.Entity;
import com.basistech.rosette.dm.Event;
import com.basistech.rosette.dm.Keyphrase;
import com.basistech.rosette.dm.LanguageDetection;
import com.basistech.rosette.dm.LayoutRegion;
import com.basistech.rosette.dm.ListAttribute;
import com.basistech.rosette.dm.MapAttribute;
import com.basistech.rosette.dm.RelationshipComponent;
import com.basistech.rosette.dm.RelationshipMention;
import com.basistech.rosette.dm.ScriptRegion;
import com.basistech.rosette.dm.Sentence;
import com.basistech.rosette.dm.SimilarTerm;
import com.basistech.rosette.dm.Token;
import com.basistech.rosette.dm.TranslatedData;
import com.basistech.rosette.dm.TranslatedTokens;
import com.basistech.rosette.dm.TransliterationResults;
import com.basistech.rosette.dm.UnknownAttribute;

/**
 * The attributes currently defined in the data model.
 * Applications may extend the model with additional attributes;
 * this provides keys for {@link com.basistech.rosette.dm.AnnotatedText} and
 * {@link com.basistech.rosette.dm.RelationshipMention} for the known attributes.
 */
public enum KnownAttribute {
    BASE_NOUN_PHRASE("baseNounPhrases", BaseNounPhrase.class),
    CATEGORIZATION_RESULT("categorizerResults", CategorizerResult.class),
    CONCEPT("concept", Concept.class),
    DEPENDENCY("dependency", Dependency.class),
    EMBEDDING("embedding", Embeddings.class),
    ENTITY("entities", Entity.class),
    @SuppressWarnings("deprecation")
    ENTITY_MENTION("entityMentions", com.basistech.rosette.dm.EntityMention.class),
    EVENT("event", Event.class),
    KEYPHRASE("keyphrase", Keyphrase.class),
    LANGUAGE_DETECTION("languageDetection", LanguageDetection.class),
    LAYOUT_REGION("layoutRegion", LayoutRegion.class),
    LIST("list", ListAttribute.class),
    MAP("map", MapAttribute.class),
    RELATION_ARGUMENT("RelationshipComponent", RelationshipComponent.class),
    RELATIONSHIP_MENTION("relationshipMentions", RelationshipMention.class),
    @SuppressWarnings("deprecation")
    RESOLVED_ENTITY("resolvedEntities", com.basistech.rosette.dm.ResolvedEntity.class),
    SCRIPT_REGION("scriptRegion", ScriptRegion.class),
    SENTENCE("sentence", Sentence.class),
    SENTIMENT_RESULT("sentimentResults", CategorizerResult.class),
    SIMILAR_TERM("similarTerm", SimilarTerm.class),
    TOKEN("token", Token.class),
    TOPIC_RESULT("topicResults", CategorizerResult.class),
    TRANSLATED_DATA("translatedData", TranslatedData.class),
    TRANSLATED_TOKENS("translatedTokens", TranslatedTokens.class),
    TRANSLITERATION("transliteration", TransliterationResults.class),
    UNKNOWN("unknown", UnknownAttribute.class);

    private final String jsonTag;
    private final Class<? extends BaseAttribute> attributeClass;

    KnownAttribute(String jsonTag, Class<? extends BaseAttribute> attributeClass) {
        this.jsonTag = jsonTag;
        this.attributeClass = attributeClass;
    }

    /**
     * @return a value used as a key in {@link com.basistech.rosette.dm.AnnotatedText} or
     * {@link com.basistech.rosette.dm.RelationshipMention} and in json serialization.
     */
    public String key() {
        return jsonTag;
    }

    public Class<? extends BaseAttribute> attributeClass() {
        return attributeClass;
    }

    public static KnownAttribute getAttributeForKey(String key) {
        for (KnownAttribute item : values()) {
            if (item.key().equals(key)) {
                return item;
            }
        }
        return null;
    }

    public static KnownAttribute getAttributeForClass(Class<?> attributeClass) {
        for (KnownAttribute item : values()) {
            if (item.attributeClass().equals(attributeClass)) {
                return item;
            }
        }
        return null;
    }
}
