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
package com.basistech.rosette.dm;

/**
 * The attributes currently defined in the data model.
 * Applications may extend the model with additional attributes;
 * this provides keys for {@link com.basistech.rosette.dm.AnnotatedText} for the known attributes.
 * The keys are defined by strings, rather than the enum itself, to allow for
 * this extension.
 */
enum AttributeKey {
    BASE_NOUN_PHRASE("baseNounPhrases"),
    ENTITY("entities"),
    MENTION("mentions"),
    @Deprecated
    ENTITY_MENTION("entityMentions"),
    RELATIONSHIP_MENTION("relationshipMentions"),
    @Deprecated
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

    CATEGORIZER_RESULTS("categorizerResults"),
    SENTIMENT_RESULTS("sentimentResults");

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
