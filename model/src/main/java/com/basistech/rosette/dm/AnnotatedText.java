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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * The root of the data model. An {@code AnnotatedText} is blob of text and its attributes.
 * The attributes are available from {@link #getAttributes()}, as well as from
 * some convenience accessors, such as {@link #getTokens()} or {@link #getEntityMentions()}.
 * <br>
 * Generally, offsets used in the data model are character (UTF-16 elements) offsets into the
 * original text.  Offset ranges are always half-open.  For example:
 * <pre>
 * 012345678901
 * Hello world
 * </pre>
 * The token "Hello" has start offset 0 and end offset 5.
 * <br>
 * A note on serialization: due to the internal structure of this class and the classes
 * that make up the model, we do not recommend that applications serialize this to
 * Json (or XML or other representations) by applying a reflection-based toolkit 'as-is'.
 * For Json, and Java, the 'adm-json' module provides the supported serialization.
 */
@SuppressWarnings("deprecation")
public class AnnotatedText {
    private final CharSequence data;
    /* The attributes for this text, indexed by type.
     * Only one attribute of a type is permitted, thus the concept
     * of a ListAttribute.
     */
    private final Map<String, BaseAttribute> attributes;
    private final Map<String, List<String>> documentMetadata;
    private ListAttribute<EntityMention> compatMentions;
    private ListAttribute<ResolvedEntity> compatResolvedEntities;

    AnnotatedText(CharSequence data,
                  Map<String, BaseAttribute> attributes,
                  Map<String, List<String>> documentMetadata,
                  /*
                   * This version is only here as a workaround for https://github.com/FasterXML/jackson-databind/issues/1118
                   * It would be better if it was only mentioned in the mixins. No data arrives here, it just
                   * allows the mixin to be matched with the existence of a version item in the json.
                   */
                  String version) {
        this.data = data;
        // allow incoming json that simply lacks attributes or documentMetadata.
        this.attributes = absorbAttributes(attributes);
        if (documentMetadata != null) {
            this.documentMetadata = ImmutableMap.copyOf(documentMetadata);
        } else {
            this.documentMetadata = ImmutableMap.of();
        }
    }

    /*
     * This method is called from the package-private constructor that is only called when deserializing
      * json. It builds the attributes from the
     */
    @SuppressWarnings("unchecked")
    private static Map<String, BaseAttribute> absorbAttributes(Map<String, BaseAttribute> attributes) {
        ImmutableMap.Builder<String, BaseAttribute> builder = new ImmutableMap.Builder<>();
        if (attributes == null) {
            return ImmutableMap.of();
        }

        for (Map.Entry<String, BaseAttribute> me : attributes.entrySet()) {
            if (!AttributeKey.RESOLVED_ENTITY.key().equals(me.getKey())
                && !AttributeKey.ENTITY_MENTION.key().equals(me.getKey())) {
                builder.put(me);
            }
        }

        // Begin compatibility with '1.0' version of ADM.
        ListAttribute<EntityMention> oldMentions = (ListAttribute<EntityMention>)attributes.get(AttributeKey.ENTITY_MENTION.key());
        ListAttribute<ResolvedEntity> oldResolved = (ListAttribute<ResolvedEntity>)attributes.get(AttributeKey.RESOLVED_ENTITY.key());
        if (oldResolved != null) {
            ConvertFromPreAdm1.doResolvedConversion(oldMentions, oldResolved, builder);
        } else if (oldMentions != null) {
            ConvertFromPreAdm1.doUnresolvedConversion(oldMentions, builder);
        }

        return builder.build();
    }

    /**
     * Returns the character data for this text.
     *
     * @return the character data for this text
     * @adm.ignore
     */
    public CharSequence getData() {
        return data;
    }

    /**
     * Returns document-level metadata.  Metadata keys are simple strings;
     * values are lists of strings.
     *
     * @return map of metadata associated with the document
     * @adm.ignore
     */
    public Map<String, List<String>> getDocumentMetadata() {
        return documentMetadata;
    }

    /**
     * Returns all of the annotations on this text. For the defined attributes,
     * the keys will be values from {@link AttributeKey#key()}. The values
     * are polymorphic; the subclass of {@link BaseAttribute} depends
     * on the attribute.  Applications should usually prefer to use the
     * convenience accessors (e.g. {@code getTokens}) instead, to avoid the
     * need for a cast.
     *
     * Note that this map will not return {@link EntityMention} or {@link ResolvedEntity} objects,
     * which are deprecated; they are only available from the specific accessors.
     *
     * @return all of the annotations on this text
     *
     * @adm.ignore
     */
    public Map<String, BaseAttribute> getAttributes() {
        return attributes;
    }

    /**
     * Returns the list of tokens.
     *
     * @return the list of tokens
     */
    @SuppressWarnings("unchecked")
    public ListAttribute<Token> getTokens() {
        return (ListAttribute<Token>) attributes.get(AttributeKey.TOKEN.key());
    }

    /**
     * Returns the translated tokens.  This API allows for multiple
     * translations.  For example, element 0 may contain the {@code TranslatedTokens}
     * for Simplified Chinese, and element 1 may contain the {@code TranslatedTokens}
     * for Japanese.  Usually only element 0 will be populated.
     *
     * @return the list of translated tokens
     */
    @SuppressWarnings("unchecked")
    public ListAttribute<TranslatedTokens> getTranslatedTokens() {
        return (ListAttribute<TranslatedTokens>) attributes.get(AttributeKey.TRANSLATED_TOKENS.key());
    }

    /**
     * Returns the translations for the text.  This API allows multiple
     * translations.  For example, element 0 may contain the {@code TranslatedData}
     * for Simplified Chinese, and element 1 may contain the {@code TranslatedData}
     * for Japanese.  Usually only element 0 will be populated.
     *
     * @return the translations for the text
     */
    @SuppressWarnings("unchecked")
    public ListAttribute<TranslatedData> getTranslatedData() {
        return (ListAttribute<TranslatedData>) attributes.get(AttributeKey.TRANSLATED_DATA.key());
    }

    /**
     * Returns the list of language regions.
     *
     * @return the list of language regions
     */
    @SuppressWarnings("unchecked")
    public ListAttribute<LanguageDetection> getLanguageDetectionRegions() {
        return (ListAttribute<LanguageDetection>) attributes.get(AttributeKey.LANGUAGE_DETECTION_REGIONS.key());
    }

    /**
     * Returns the language results for the entire text.
     *
     * @return the language results for the entire text
     */
    public LanguageDetection getWholeTextLanguageDetection() {
        return (LanguageDetection)attributes.get(AttributeKey.LANGUAGE_DETECTION.key());
    }

    /**
     * Returns the list of entity mentions.
     *
     * @return the list of entity mentions
     * @deprecated this constructs a list of the old objects for compatibility, the supported
     * item is {@link Mention}.
     *
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public synchronized ListAttribute<EntityMention> getEntityMentions() {

        if (compatMentions == null) {
            ListAttribute.Builder<EntityMention> cmListBuilder = new ListAttribute.Builder<>(EntityMention.class);
            ListAttribute<Entity> entities = getEntities();
            if (entities.getExtendedProperties() != null) {
                for (Map.Entry<String, Object> me : entities.getExtendedProperties().entrySet()) {
                    String key = me.getKey();
                    if (key.startsWith("mention.")) {
                        cmListBuilder.extendedProperty(key.substring(8), me.getValue());
                    }
                }
            }

            for (Entity entity : entities) {
                for (Mention mention : entity.getMentions()) {
                    EntityMention.Builder emBuilder = new EntityMention.Builder(mention.getStartOffset(),
                            mention.getEndOffset(),
                            mention.getEntityType());
                    if (mention.getConfidence() != null) {
                        emBuilder.confidence(mention.getConfidence());
                    }
                    if (mention.getExtendedProperties() != null && mention.getExtendedProperties().size() > 0) {
                        for (Map.Entry<String, Object> me : mention.getExtendedProperties().entrySet()) {
                            if (me.getKey().equals("oldFlags")) {
                                emBuilder.flags((Integer) me.getValue());
                            } else if (me.getKey().equals("oldCoreferenceChainId")) {
                                emBuilder.coreferenceChainId((Integer)me.getValue());
                            } else {
                                emBuilder.extendedProperty(me.getKey(), me.getValue());
                            }
                        }
                    }
                    if (mention.getNormalized() != null) {
                        emBuilder.normalized(mention.getNormalized());
                    }
                    if (mention.getSource() != null) {
                        emBuilder.source(mention.getSource());
                    }
                    if (mention.getSubsource() != null) {
                        emBuilder.subsource(mention.getSubsource());
                    }

                    cmListBuilder.add(emBuilder.build());
                }
            }
            compatMentions = cmListBuilder.build();
        }
        return compatMentions;
    }

    @SuppressWarnings("unchecked")
    public ListAttribute<Entity> getEntities() {
        return (ListAttribute<Entity>) attributes.get(AttributeKey.ENTITY.key());
    }

    /**
     * Returns the list of relationship mentions.
     *
     * @return the list of relationship mentions
     */
    @SuppressWarnings("unchecked")
    public ListAttribute<RelationshipMention> getRelationshipMentions() {
        return (ListAttribute<RelationshipMention>) attributes.get(AttributeKey.RELATIONSHIP_MENTION.key());
    }

    /**
     * Returns the list of resolved entities.
     *
     * @return the list of resolved entities
     * @deprecated this constructs a list of the old objects for compatibility, the supported item
     * is {@link Entity}.
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public synchronized ListAttribute<ResolvedEntity> getResolvedEntities() {
        if (compatResolvedEntities == null) {
            ListAttribute.Builder<ResolvedEntity> reListBuilder = new ListAttribute.Builder<>(ResolvedEntity.class);

            ListAttribute<Entity> entities = getEntities();
            if (entities.getExtendedProperties() != null) {
                for (Map.Entry<String, Object> me : entities.getExtendedProperties().entrySet()) {
                    String key = me.getKey();
                    if (!key.startsWith("mention.")) {
                        reListBuilder.extendedProperty(key, me.getValue());
                    }
                }
            }

            for (Entity entity : entities) {
                int headStart = 0;
                int headEnd = 0;
                if (entity.getHeadMentionIndex() != null) {
                    Mention head = entity.getMentions().get(entity.getHeadMentionIndex());
                    headStart = head.getStartOffset();
                    headEnd = head.getEndOffset();
                }

                ResolvedEntity.Builder reBuilder = new ResolvedEntity.Builder(headStart, headEnd, entity.getEntityId());
                if (entity.getConfidence() != null) {
                    reBuilder.confidence(entity.getConfidence());
                }
                if (entity.getSentiment() != null) {
                    reBuilder.sentiment(entity.getSentiment());
                }

                if (entity.getExtendedProperties() != null) {
                    for (Map.Entry<String, Object> me : entity.getExtendedProperties().entrySet()) {
                        if (me.getKey().equals("oldCoreferenceChainId")) {
                            reBuilder.coreferenceChainId((Integer)me.getValue());
                        } else {
                            reBuilder.extendedProperty(me.getKey(), me.getValue());
                        }
                    }
                }

                reListBuilder.add(reBuilder.build());
            }
            compatResolvedEntities = reListBuilder.build();

        }
        return compatResolvedEntities;
    }

    /**
     * Returns the list of script regions.
     *
     * @return the list of script regions
     */
    @SuppressWarnings("unchecked")
    public ListAttribute<ScriptRegion> getScriptRegions() {
        return (ListAttribute<ScriptRegion>) attributes.get(AttributeKey.SCRIPT_REGION.key());
    }

    /**
     * Returns the list of sentences.
     *
     * @return the list of sentences
     */
    @SuppressWarnings("unchecked")
    public ListAttribute<Sentence> getSentences() {
        return (ListAttribute<Sentence>) attributes.get(AttributeKey.SENTENCE.key());
    }

    /**
     * Returns the list of base noun phrases.
     *
     * @return the list of base noun phrases
     */
    @SuppressWarnings("unchecked")
    public ListAttribute<BaseNounPhrase> getBaseNounPhrases() {
        return (ListAttribute<BaseNounPhrase>) attributes.get(AttributeKey.BASE_NOUN_PHRASE.key());
    }

    /**
     * Returns the list of categorizer results.
     *
     * @return the list of categorizer results
     */
    @SuppressWarnings("unchecked")
    public ListAttribute<CategorizerResult> getCategorizerResults() {
        return (ListAttribute<CategorizerResult>) attributes.get(AttributeKey.CATEGORIZER_RESULTS.key());
    }

    /**
     * Returns the list of sentiment results.
     *
     * @return the list of sentiment results
     */
    @SuppressWarnings("unchecked")
    public ListAttribute<CategorizerResult> getSentimentResults() {
        return (ListAttribute<CategorizerResult>) attributes.get(AttributeKey.SENTIMENT_RESULTS.key());
    }

    /**
     * toString is a convenience for accessing the textual data, if any, in this annotated text.
     * @return the data for this AnnotatedText as a String.
     * If the data is {@code null}, this returns {@code null}
     * rather than throwing a {@link NullPointerException}.
     */
    @Override
    public String toString() {
        if (data == null) {
            return null;
        } else {
            return data.toString();
        }
    }

    /**
     * Builder class for {@link AnnotatedText} objects.
     */
    public static class Builder {
        private CharSequence data;
        // Keys are strings to allow for extension.  Predefined keys are from
        // AttributeKey.key().
        private final Map<String, BaseAttribute> attributes = Maps.newHashMap();
        private final Map<String, List<String>> documentMetadata = Maps.newHashMap();

        /**
         * Constructs a builder.  The initial data is the empty string.
         */
        public Builder() {
            // leave data null.
        }

        /**
         * Constructs a builder from an existing {@link com.basistech.rosette.dm.AnnotatedText}.
         *
         * @param startingPoint source object to copy
         */
        public Builder(AnnotatedText startingPoint) {
            this.data = startingPoint.data;
            this.attributes.putAll(startingPoint.attributes);
            this.documentMetadata.putAll(startingPoint.documentMetadata);
        }

        /**
         * Constructs a builder over some character data.
         *
         * @param data the data. This replaces and previous setting.
         * @return this
         */
        public Builder data(CharSequence data) {
            this.data = data;
            return this;
        }

        /**
         * Returns the current character data.
         *
         * @return the current character data
         */
        public CharSequence data() {
            return data;
        }

        /**
         * Attaches a list of base noun phrases.
         *
         * @param baseNounPhrases the base noun phrases
         * @return this
         */
        public Builder baseNounPhrases(ListAttribute<BaseNounPhrase> baseNounPhrases) {
            attributes.put(AttributeKey.BASE_NOUN_PHRASE.key(), baseNounPhrases);
            return this;
        }

        /**
         * Attaches a list of entity mentions.
         *
         * @param entityMentions the entity mentions
         * @return this
         * @deprecated Use {@link #entities(ListAttribute)}.
         */
        @Deprecated
        public Builder entityMentions(ListAttribute<EntityMention> entityMentions) {
            attributes.put(AttributeKey.ENTITY_MENTION.key(), entityMentions);
            return this;
        }

        /**
         * Attaches a list of relationship mentions.
         *
         * @param relationshipMentions the relationship mentions.
         * @return this
         */
        public Builder relationshipMentions(ListAttribute<RelationshipMention> relationshipMentions) {
            attributes.put(AttributeKey.RELATIONSHIP_MENTION.key(), relationshipMentions);
            return this;
        }

        /**
         * Attaches a list of entities.
         * @param entities the entities.
         * @return this.
         */
        public Builder entities(ListAttribute<Entity> entities) {
            attributes.put(AttributeKey.ENTITY.key(), entities);
            return this;
        }

        /**
         * Attaches a list of resolved entities.
         *
         * @param resolvedEntities the resolved entities
         * @return this
         * @deprecated use {@link #entities(ListAttribute)}.
         */
        @Deprecated
        public Builder resolvedEntities(ListAttribute<ResolvedEntity> resolvedEntities) {
            attributes.put(AttributeKey.RESOLVED_ENTITY.key(), resolvedEntities);
            return this;
        }

        /**
         * Attaches a list of language detections.
         *
         * @param languageDetectionRegions the language detections
         * @return this
         */
        public Builder languageDetectionRegions(ListAttribute<LanguageDetection> languageDetectionRegions) {
            attributes.put(AttributeKey.LANGUAGE_DETECTION_REGIONS.key(), languageDetectionRegions);
            return this;
        }

        /**
         * Attaches a whole-document language detection.
         *
         * @param languageDetection the language detection
         * @return this
         */
        public Builder wholeDocumentLanguageDetection(LanguageDetection languageDetection) {
            attributes.put(AttributeKey.LANGUAGE_DETECTION.key(), languageDetection);
            return this;
        }

        /**
         * Attaches a list of script regions.
         *
         * @param scriptRegions the script regions
         * @return this
         */
        public Builder scriptRegions(ListAttribute<ScriptRegion> scriptRegions) {
            attributes.put(AttributeKey.SCRIPT_REGION.key(), scriptRegions);
            return this;
        }

        /**
         * Attaches a list of sentences.
         *
         * @param sentences the sentences
         * @return this
         */
        public Builder sentences(ListAttribute<Sentence> sentences) {
            attributes.put(AttributeKey.SENTENCE.key(), sentences);
            return this;
        }

        /**
         * Attaches a list of tokens.
         *
         * @param tokens the tokens
         * @return this
         */
        public Builder tokens(ListAttribute<Token> tokens) {
            attributes.put(AttributeKey.TOKEN.key(), tokens);
            return this;
        }

        /**
         * Attaches a list of TranslatedTokens objects.
         *
         * @param translatedTokens a list of TranslatedTokens objects
         * @return this
         */
        public Builder translatedTokens(ListAttribute<TranslatedTokens> translatedTokens) {
            attributes.put(AttributeKey.TRANSLATED_TOKENS.key(), translatedTokens);
            return this;
        }

        /**
         * Attaches a TranslatedData object.
         *
         * @param translatedData a TranslatedData object
         * @return this
         */
        public Builder translatedData(ListAttribute<TranslatedData> translatedData) {
            attributes.put(AttributeKey.TRANSLATED_DATA.key(), translatedData);
            return this;
        }

        /**
         * Attaches a list of categorizer results.
         *
         * @param categorizerResults the categorizer results
         * @return this
         */
        public Builder categorizerResults(ListAttribute<CategorizerResult> categorizerResults) {
            attributes.put(AttributeKey.CATEGORIZER_RESULTS.key(), categorizerResults);
            return this;
        }

        /**
         * Attaches a list of sentiment results.
         *
         * @param sentimentResults the sentiment results
         * @return this
         */
        public Builder sentimentResults(ListAttribute<CategorizerResult> sentimentResults) {
            attributes.put(AttributeKey.SENTIMENT_RESULTS.key(), sentimentResults);
            return this;
        }

        /**
         * Adds an attribute.
         *
         * @param key       the attribute key. See {@link AttributeKey}.
         * @param attribute the attribute. Replaces any previous value for this key.
         * @return this
         */
        Builder attribute(String key, BaseAttribute attribute) {
            attributes.put(key, attribute);
            return this;
        }

        /**
         * Adds an attribute.
         *
         * @param key       the attribute key.
         * @param attribute the attribute. Replaces any previous value for this key.
         * @return this
         */
        Builder attribute(AttributeKey key, BaseAttribute attribute) {
            attributes.put(key.key(), attribute);
            return this;
        }

        /**
         * Returns the current attributes.
         *
         * @return the current attributes
         */
        public Map<String, BaseAttribute> attributes() {
            return attributes;
        }

        /**
         * Adds an entry to the document metadata. Replaces any previous value for this key.
         *
         * @param key key
         * @param value value
         * @return this
         */
        public Builder documentMetadata(String key, List<String> value) {
            documentMetadata.put(key, ImmutableList.copyOf(value));
            return this;
        }

        /**
         * Add all of the contents of a map of metadata to the document metadata.
         *
         * @param mapOfValues a map from keys to values.
         * @return this
         */
        public Builder documentMetadata(Map<String, List<String>> mapOfValues) {
            for (Map.Entry<String, List<String>> me : mapOfValues.entrySet()) {
                documentMetadata.put(me.getKey(), ImmutableList.copyOf(me.getValue()));
            }
            return this;
        }

        /**
         * Adds an entry to the document metadata. Replaces any previous value for this key.
         *
         * @param key   key
         * @param value A single string value. The result of this call is to store a list containing this value
         *              as the value for this key.
         * @return this
         */
        public Builder documentMetadata(String key, String value) {
            documentMetadata.put(key, Lists.newArrayList(value));
            return this;
        }

        /**
         * Returns the current document metadata.
         *
         * @return the current document metadata
         */
        public Map<String, List<String>> documentMetadata() {
            return documentMetadata;
        }

        /**
         * Constructs a {@link AnnotatedText} object from the settings in this builder.
         *
         * @return the new object
         */
        public AnnotatedText build() {
            return new AnnotatedText(data, attributes, documentMetadata, null);
        }
    }
}
