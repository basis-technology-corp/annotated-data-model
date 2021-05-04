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
    CATEGORIZER_RESULTS("categorizerResults"),
    CONCEPT("concepts"),
    DEPENDENCY("dependency"),
    EMBEDDING("embeddings"),
    ENTITY("entities"),
    EVENT("events"),
    KEYPHRASE("keyphrases"),
    @Deprecated
    ENTITY_MENTION("entityMentions"),
    MENTION("mentions"),
    /**
     * The language detection for the entire document.
     */
    LANGUAGE_DETECTION("languageDetection"),
    /**
     * The list of language detections by region of the document.
     */
    LANGUAGE_DETECTION_REGIONS("languageDetectionRegions"),
    LAYOUT_REGION("layoutRegion"),
    RELATIONSHIP_MENTION("relationshipMentions"),
    @Deprecated
    RESOLVED_ENTITY("resolvedEntities"),
    SCRIPT_REGION("scriptRegion"),
    SENTENCE("sentence"),
    SENTIMENT_RESULTS("sentimentResults"),
    SIMILAR_TERMS("similarTerms"),
    TOKEN("token"),
    TOPIC_RESULTS("topicResults"),
    TRANSLATED_DATA("translatedData"),
    TRANSLATED_TOKENS("translatedTokens"),
    TRANSLITERATION("transliteration");

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
