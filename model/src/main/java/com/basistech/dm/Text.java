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
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * The root of the data model. A blob of text and its atttibutes.
 */
public class Text implements CharSequence {
    private static final String SENTENCE_ATTRIBUTE = "com.basistech.dm.Sentence";
    private static final String SCRIPT_REGION_ATTRIBUTE = "com.basistech.dm.ScriptRegion";

    private final char[] data;
    private final int startOffset;
    private final int endOffset;
    /* The attributes for this text, indexed by type.
     * Only one attribute of a type is permitted, thus the concept
     * of a SetAttribute.
     */
    private final Map<String, BaseAttribute> attributes;

    public Text(char[] data, int startOffset, int endOffset) {
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

    public Map<String, BaseAttribute> getAttributes() {
        return attributes;
    }

    @SuppressWarnings("unchecked")
    public ListAttribute<Token> getTokens() {
        return (ListAttribute<Token>) attributes.get(Token.class.getName());
    }

    @SuppressWarnings("unchecked")
    public ListAttribute<LanguageDetection> getLanguageDetections() {
        return (ListAttribute<LanguageDetection>)attributes.get(LanguageDetection.class.getName());
    }

    @SuppressWarnings("unchecked")
    public ListAttribute<BaseAttribute> getSentences() {
        return (ListAttribute<BaseAttribute>)attributes.get(SENTENCE_ATTRIBUTE);
    }

    @SuppressWarnings("unchecked")
    public ListAttribute<EntityMention> getEntityMentions() {
        return (ListAttribute<EntityMention>)attributes.get(EntityMention.class.getName());
    }

    @SuppressWarnings("unchecked")
    public ListAttribute<ValueAttribute<ISO15924>> getScriptRegions() {
        return (ListAttribute<ValueAttribute<ISO15924>>) attributes.get(SCRIPT_REGION_ATTRIBUTE);
    }
}
