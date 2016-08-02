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

import com.basistech.rosette.dm.BaseAttribute;
import com.basistech.rosette.dm.BaseNounPhrase;
import com.basistech.rosette.dm.CategorizerResult;
import com.basistech.rosette.dm.Dependency;
import com.basistech.rosette.dm.Entity;
import com.basistech.rosette.dm.LanguageDetection;
import com.basistech.rosette.dm.ListAttribute;
import com.basistech.rosette.dm.RelationshipComponent;
import com.basistech.rosette.dm.RelationshipMention;
import com.basistech.rosette.dm.ScriptRegion;
import com.basistech.rosette.dm.Sentence;
import com.basistech.rosette.dm.Token;
import com.basistech.rosette.dm.TranslatedData;
import com.basistech.rosette.dm.TranslatedTokens;
import com.basistech.rosette.dm.UnknownAttribute;

/**
 * The attributes currently defined in the data model.
 * Applications may extend the model with additional attributes;
 * this provides keys for {@link com.basistech.rosette.dm.AnnotatedText} and
 * {@link com.basistech.rosette.dm.RelationshipMention} for the known attributes.
 */
public enum KnownAttribute {
    BASE_NOUN_PHRASE("baseNounPhrases", BaseNounPhrase.class),
    ENTITY("entities", Entity.class),
    @SuppressWarnings("deprecation")
    ENTITY_MENTION("entityMentions", com.basistech.rosette.dm.EntityMention.class),
    RELATIONSHIP_MENTION("relationshipMentions", RelationshipMention.class),
    @SuppressWarnings("deprecation")
    RESOLVED_ENTITY("resolvedEntities", com.basistech.rosette.dm.ResolvedEntity.class),
    LANGUAGE_DETECTION("languageDetection", LanguageDetection.class),
    SCRIPT_REGION("scriptRegion", ScriptRegion.class),
    SENTENCE("sentence", Sentence.class),
    TOKEN("token", Token.class),
    TRANSLATED_DATA("translatedData", TranslatedData.class),
    TRANSLATED_TOKENS("translatedTokens", TranslatedTokens.class),
    CATEGORIZATION_RESULT("categorizerResults", CategorizerResult.class),
    SENTIMENT_RESULT("sentimentResults", CategorizerResult.class),
    TOPIC_RESULT("topicResults", CategorizerResult.class),
    LIST("list", ListAttribute.class),
    UNKNOWN("unknown", UnknownAttribute.class),
    RELATION_ARGUMENT("RelationshipComponent", RelationshipComponent.class),
    DEPENDENCY("dependency", Dependency.class);


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
