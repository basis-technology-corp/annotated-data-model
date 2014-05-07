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

package com.basistech.dm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * The root of the data model. A blob of text and its attributes.
 * Text objects implement {@link java.lang.CharSequence}, and to give direct access
 * to the text. The attributes are available from {@link #getAttributes()}, as well as from
 * some convenience accessors. Text does not (yet) have a builder, and so is not quite immutable;
 * applications may add to the attributes.
 * TODO: make immutable and add a builder.
 */
public class Text implements CharSequence {
    private final CharSequence data;
    /* The attributes for this text, indexed by type.
     * Only one attribute of a type is permitted, thus the concept
     * of a ListAttribute.
     */
    private final Map<String, BaseAttribute> attributes;
    private final Map<String, List<String>> documentMetadata;

    @JsonCreator
    public Text(@JsonProperty("data") CharSequence data,
                @JsonProperty("documentMetadata") Map<String, List<String>> documentMetadata) {
        this.data = data;
        attributes = Maps.newHashMap();
        this.documentMetadata = documentMetadata;
    }

    public Text(Text copyFrom) {
        this.data = copyFrom.data;
        // the attributes themselves are immutable so we can take them.
        this.attributes = Maps.newHashMap(copyFrom.attributes);
        // same for the metadata.
        this.documentMetadata = Maps.newHashMap(copyFrom.documentMetadata);
    }

    public CharSequence getData() {
        return data;
    }

    public int length() {
        return data.length();
    }

    public char charAt(int index) {
        return data.charAt(index);
    }

    public CharSequence subSequence(int start, int end) {
        return data.subSequence(start, end);
    }

    public Map<String, List<String>> getDocumentMetadata() {
        return documentMetadata;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    public Map<String, BaseAttribute> getAttributes() {
        return attributes;
    }

    @JsonIgnore
    @SuppressWarnings("unchecked")
    public ListAttribute<Token> getTokens() {
        return (ListAttribute<Token>) attributes.get(Token.class.getName());
    }

    @JsonIgnore
    @SuppressWarnings("unchecked")
    public ListAttribute<LanguageDetection> getLanguageDetections() {
        return (ListAttribute<LanguageDetection>)attributes.get(LanguageDetection.class.getName());
    }

    @JsonIgnore
    @SuppressWarnings("unchecked")
    public ListAttribute<EntityMention> getEntityMentions() {
        return (ListAttribute<EntityMention>)attributes.get(EntityMention.class.getName());
    }

    @JsonIgnore
    @SuppressWarnings("unchecked")
    public ListAttribute<ScriptRegion> getScriptRegions() {
        return (ListAttribute<ScriptRegion>)attributes.get(ScriptRegion.class.getName());
    }

    @JsonIgnore
    @SuppressWarnings("unchecked")
    public SentenceBoundaries getSentenceBoundaries() {
        return (SentenceBoundaries)attributes.get(SentenceBoundaries.class.getName());
    }

    @JsonIgnore
    @SuppressWarnings("unchecked")
    public ListAttribute<BaseNounPhrase> getBaseNounPhrases() {
        return (ListAttribute<BaseNounPhrase>)attributes.get(BaseNounPhrase.class.getName());
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
