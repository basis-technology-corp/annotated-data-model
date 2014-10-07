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
 * this provides keys for {@link com.basistech.rosette.dm.AnnotatedText} for the known attributes.
 */
enum AttributeKey {
    BASE_NOUN_PHRASE("baseNounPhrases"),
    ENTITY_MENTION("entityMentions"),
    RESOLVED_ENTITY("resolvedEntities"),
    /**
     * The list of language detections by region of the document.
     */
    LANGUAGE_DETECTION_REGIONS("languageDetectionRegions"),
    /**
     * The language detection for the entire document.
     */
    LANGUAGE_DETECTION("languageDetection"),
    SCRIPT_REGION("scriptRegion"),
    SENTENCE("sentence"),
    TOKEN("token"),
    TRANSLATED_DATA("translatedData"),
    TRANSLATED_TOKENS("translatedTokens"),

    CATEGORIZER_RESULTS("categorizerResults");  // TODO: split into cat, review sentiment, short string sentiment

    private final String key;

    AttributeKey(String key) {
        this.key = key;
    }

    /**
     * @return a value used as a key in {@link com.basistech.rosette.dm.AnnotatedText} and in json serialization.
     */
    public String key() {
        return key;
    }
}
