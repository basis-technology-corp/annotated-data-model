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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * The root of the  data model. A AnnotatedText is blob of text and its attributes.
 * AnnotatedText objects implement {@link java.lang.CharSequence}, to give direct access
 * to the text. The attributes are available from {@link #getAttributes()}, as well as from
 * some convenience accessors.
 * <br/>
 * A note on serialization: due to the internal structure of this class and the classes
 * that make up the model, we do not recommend that applications serialize this to
 * Json (or XML or other representations) by applying a reflection-based toolkit 'as-is'.
 * For Json, the 'adm-json' module provides
 * {@linkplain com.basistech.rosette.dm.AnnotatedDataModelModule#setupObjectMapper(com.fasterxml.jackson.databind.ObjectMapper)},
 * to set up customized support.
 */
public class AnnotatedText implements CharSequence {
    private final CharSequence data;
    /* The attributes for this text, indexed by type.
     * Only one attribute of a type is permitted, thus the concept
     * of a ListAttribute.
     */
    private final Map<String, BaseAttribute> attributes;
    private final Map<String, List<String>> documentMetadata;

    AnnotatedText(CharSequence data,
                  Map<String, BaseAttribute> attributes,
                  Map<String, List<String>> documentMetadata) {
        this.data = data;
        this.attributes = ImmutableMap.copyOf(attributes);
        this.documentMetadata = ImmutableMap.copyOf(documentMetadata);
    }

    /**
     * @return the character data for this text.
     */
    public CharSequence getData() {
        return data;
    }

    /**
     * @return the length.
     * @see CharSequence#length()
     */
    public int length() {
        return data.length();
    }

    /**
     * Return one character.
     * @param index the index.
     * @return the character
     * @see CharSequence#charAt(int)
     */
    public char charAt(int index) {
        return data.charAt(index);
    }

    /**
     * Return a sub-sequence of the text.
     * @param start start index.
     * @param end end index.
     * @return the sub-sequence.
     * @see CharSequence#subSequence(int, int)
     */
    public CharSequence subSequence(int start, int end) {
        return data.subSequence(start, end);
    }

    /**
     * @return an immutable map of metadata associated with the text.
     */
    public Map<String, List<String>> getDocumentMetadata() {
        return documentMetadata;
    }

    /**
     * @return all of the annotations on this text. For the defined attributes,
     * the keys will be values from {@link AttributeKey#key()}. The values
     * are polymorphic; the subclass of {@link BaseAttribute} depends
     * on the attribute.
     */
    public Map<String, BaseAttribute> getAttributes() {
        return attributes;
    }

    /**
     * @return the list of tokens.
     */
    @SuppressWarnings("unchecked")
    public ListAttribute<Token> getTokens() {
        return (ListAttribute<Token>) attributes.get(AttributeKey.TOKEN.key());
    }

    /**
     * @return a list of language detections for regions of the text.
     */
    @SuppressWarnings("unchecked")
    public ListAttribute<LanguageDetection> getLanguageDetectionRegions() {
        return (ListAttribute<LanguageDetection>) attributes.get(AttributeKey.LANGUAGE_DETECTION_REGIONS.key());
    }

    /**
     * @return a language detection result for the entire text.
     */
    public LanguageDetection getWholeTextLanguageDetection() {
        return (LanguageDetection)attributes.get(AttributeKey.LANGUAGE_DETECTION.key());
    }

    /**
     * @return the list of entity mentions.
     */
    @SuppressWarnings("unchecked")
    public ListAttribute<EntityMention> getEntityMentions() {
        return (ListAttribute<EntityMention>) attributes.get(AttributeKey.ENTITY_MENTION.key());
    }

    /**
     * @return the list of script regions.
     */
    @SuppressWarnings("unchecked")
    public ListAttribute<ScriptRegion> getScriptRegions() {
        return (ListAttribute<ScriptRegion>) attributes.get(AttributeKey.SCRIPT_REGION.key());
    }

    /**
     * @return the list of sentences.
     */
    @SuppressWarnings("unchecked")
    public ListAttribute<Sentence> getSentences() {
        return (ListAttribute<Sentence>) attributes.get(AttributeKey.SENTENCE.key());
    }

    /**
     * @return the list of base noun phrases.
     */
    @SuppressWarnings("unchecked")
    public ListAttribute<BaseNounPhrase> getBaseNounPhrases() {
        return (ListAttribute<BaseNounPhrase>) attributes.get(AttributeKey.BASE_NOUN_PHRASE.key());
    }

    /**
     * @return the textual data as a string.
     * @see CharSequence#toString()
     */
    @Override
    public String toString() {
        return data.toString();
    }

    /**
     * Builder class for {@link AnnotatedText} objects.
     */
    public static class Builder {
        private CharSequence data;
        private final Map<String, BaseAttribute> attributes = Maps.newHashMap();
        private final Map<String, List<String>> documentMetadata = Maps.newHashMap();

        public Builder() {
            data = ""; // default to an empty string.
        }

        /**
         * Create a builder from an existing {@link com.basistech.rosette.dm.AnnotatedText}.
         *
         * @param startingPoint source object to copy.
         */
        public Builder(AnnotatedText startingPoint) {
            this.data = startingPoint.data;
            this.attributes.putAll(startingPoint.attributes);
            this.documentMetadata.putAll(startingPoint.documentMetadata);
        }


        /**
         * Create a builder over some character data.
         *
         * @param data the data. This replaces and previous setting.
         * @return this
         */
        public Builder data(CharSequence data) {
            this.data = data;
            return this;
        }


        /**
         * @return the current data.
         */
        public CharSequence data() {
            return data;
        }

        /**
         * Attach a list of base noun phrases.
         * @param baseNounPhrases the base noun phrases
         * @return this
         */
        public Builder baseNounPhrases(ListAttribute<BaseNounPhrase> baseNounPhrases) {
            attributes.put(AttributeKey.BASE_NOUN_PHRASE.key(), baseNounPhrases);
            return this;
        }

        /**
         * Attach a list of entity mentions.
         * @param entityMentions the entity mentions
         * @return this
         */
        public Builder entityMentions(ListAttribute<EntityMention> entityMentions) {
            attributes.put(AttributeKey.ENTITY_MENTION.key(), entityMentions);
            return this;
        }

        /**
         * Attach a list of language detections.
         * @param languageDetectionRegions the language detection regions.
         * @return this
         */
        public Builder languageDetectionRegions(ListAttribute<LanguageDetection> languageDetectionRegions) {
            attributes.put(AttributeKey.LANGUAGE_DETECTION_REGIONS.key(), languageDetectionRegions);
            return this;
        }

        /**
         * Attach a whole-document language detection
         * @param languageDetection the language detection
         * @return this
         */
        public Builder wholeDocumentLanguageDetection(LanguageDetection languageDetection) {
            attributes.put(AttributeKey.LANGUAGE_DETECTION.key(), languageDetection);
            return this;
        }

        /**
         * Attach a list of script regions
         * @param scriptRegions the script regions
         * @return this
         */
        public Builder scriptRegions(ListAttribute<ScriptRegion> scriptRegions) {
            attributes.put(AttributeKey.SCRIPT_REGION.key(), scriptRegions);
            return this;
        }

        /**
         * Attach a list of sentences
         * @param sentences the sentences
         * @return this
         */
        public Builder sentences(ListAttribute<Sentence> sentences) {
            attributes.put(AttributeKey.SENTENCE.key(), sentences);
            return this;
        }

        /**
         * Attach a list of tokens
         * @param tokens the tokens
         * @return this
         */
        public Builder tokens(ListAttribute<Token> tokens) {
            attributes.put(AttributeKey.TOKEN.key(), tokens);
            return this;
        }

        /**
         * Add an attribute.
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
         * Add an attribute.
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
         * @return the current set of attributes.
         */
        public Map<String, BaseAttribute> attributes() {
            return attributes;
        }

        /**
         * Add an entry to the document metadata. Replaces any previous value for this key.
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
         * Add an entry to the document metadata. Replaces any previous value for this key.
         *
         * @param key   key
         * @param value A single string value. The result of this call is to store a list containing this value
         *              as the value for this key.
         * @return this.
         */
        public Builder documentMetadata(String key, String value) {
            documentMetadata.put(key, Lists.newArrayList(value));
            return this;
        }

        /**
         * @return the current set of documentMetadata.
         */
        public Map<String, List<String>> documentMetadata() {
            return documentMetadata;
        }

        /**
         * Construct a {@link AnnotatedText} object from the settings in this builder.
         *
         * @return the new object.
         */
        public AnnotatedText build() {
            return new AnnotatedText(data, attributes, documentMetadata);
        }
    }
}
