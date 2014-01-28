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

import com.basistech.util.ISO15924;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * The root of the data model. A blob of text and its attributes.
 */
public class Text implements CharSequence {
    private static final String SCRIPT_REGION_ATTRIBUTE = "com.basistech.dm.ScriptRegion";

    private final char[] data;
    private final int startOffset;
    private final int endOffset;
    /* The attributes for this text, indexed by type.
     * Only one attribute of a type is permitted, thus the concept
     * of a ListAttribute.
     */
    private final Map<String, BaseAttribute> attributes;

    @JsonCreator
    public Text(@JsonProperty("data") char[] data, @JsonProperty("startOffset") int startOffset, @JsonProperty("endOffset") int endOffset) {
        this.data = data;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        attributes = Maps.newHashMap();
    }

    public int length() {
        return endOffset - startOffset;
    }

    public char charAt(int index) {
        return data[index + startOffset];
    }

    public CharSequence subSequence(int start, int end) {
        return new String(data, start + startOffset, end - start);
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
    public ListAttribute<ValueAttribute<ISO15924>> getScriptRegions() {
        return (ListAttribute<ValueAttribute<ISO15924>>) attributes.get(SCRIPT_REGION_ATTRIBUTE);
    }
}
