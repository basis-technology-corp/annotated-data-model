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

package com.basistech.rosette.dm;

/**
 * The attributes currently defined in the data model.
 * Applications may extend the model with additional attributes;
 * this provides keys for {@link AnnotatedText} for the known attributes.
 */
enum KnownAttribute {
    BASE_NOUN_PHRASE("baseNounPhrases", BaseNounPhrase.class),
    ENTITY_MENTION("entityMentions", EntityMention.class),
    ENTITY("entities", Entity.class),
    LANGUAGE_DETECTION("languageDetection", LanguageDetection.class),
    SCRIPT_REGION("scriptRegion", ScriptRegion.class),
    SENTENCE("sentence", Sentence.class),
    TOKEN("token", Token.class),
    LIST("list", ListAttribute.class),
    UNKNOWN("unknown", UnknownAttribute.class);

    private final String jsonTag;
    private final Class<? extends BaseAttribute> attributeClass;

    KnownAttribute(String jsonTag, Class<? extends BaseAttribute> attributeClass) {
        this.jsonTag = jsonTag;
        this.attributeClass = attributeClass;
    }

    /**
     * @return a value used as a key in {@link AnnotatedText} and in json serialization.
     */
    public String key() {
        return jsonTag;
    }

    Class<? extends BaseAttribute> attributeClass() {
        return attributeClass;
    }

    static KnownAttribute getAttributeForKey(String key) {
        for (KnownAttribute item : values()) {
            if (item.key().equals(key)) {
                return item;
            }
        }
        return null;
    }

    static KnownAttribute getAttributeForClass(Class<?> attributeClass) {
        for (KnownAttribute item : values()) {
            if (item.attributeClass().equals(attributeClass)) {
                return item;
            }
        }
        return null;
    }
}
