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

import com.basistech.rosette.dm.BaseAttribute;
import com.basistech.rosette.dm.BaseNounPhrase;
import com.basistech.rosette.dm.CategorizerResult;
import com.basistech.rosette.dm.EntityMention;
import com.basistech.rosette.dm.LanguageDetection;
import com.basistech.rosette.dm.ListAttribute;
import com.basistech.rosette.dm.RelationshipComponent;
import com.basistech.rosette.dm.RelationshipMention;
import com.basistech.rosette.dm.ResolvedEntity;
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
    ENTITY_MENTION("entityMentions", EntityMention.class),
    RELATIONSHIP_MENTION("relationshipMentions", RelationshipMention.class),
    RESOLVED_ENTITY("resolvedEntities", ResolvedEntity.class),
    LANGUAGE_DETECTION("languageDetection", LanguageDetection.class),
    SCRIPT_REGION("scriptRegion", ScriptRegion.class),
    SENTENCE("sentence", Sentence.class),
    TOKEN("token", Token.class),
    TRANSLATED_DATA("translatedData", TranslatedData.class),
    TRANSLATED_TOKENS("translatedTokens", TranslatedTokens.class),
    CATEGORIZATION_RESULT("categorizerResults", CategorizerResult.class),
    SENTIMENT_RESULT("sentimentResults", CategorizerResult.class),
    LIST("list", ListAttribute.class),
    UNKNOWN("unknown", UnknownAttribute.class),
    RELATION_ARGUMENT("RelationshipComponent", RelationshipComponent.class);


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
