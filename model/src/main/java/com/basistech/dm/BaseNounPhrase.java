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

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A base noun phrase.
 */
public class BaseNounPhrase extends Attribute {

    @JsonCreator
    public BaseNounPhrase(@JsonProperty("startOffset") int startOffset, @JsonProperty("endOffset") int endOffset) {
        super(startOffset, endOffset);
    }

    public BaseNounPhrase(int startOffset, int endOffset, Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
    }

    // Make json happy
    protected BaseNounPhrase() {
    }

    /**
     * Builder for base noun phrase attributes.
     */
    public static class Builder extends Attribute.Builder {
        public Builder(int startOffset, int endOffset) {
            super(startOffset, endOffset);
        }

        public Builder(int[] tokenOffsets, int tokenStartIndex, int tokenEndIndex) {
            this(tokenOffsets[2 * tokenStartIndex], tokenOffsets[2 * (tokenEndIndex - 1) + 1]);
        }

        public Builder(BaseNounPhrase toCopy) {
            super(toCopy);
        }

        public BaseNounPhrase build() {
            return new BaseNounPhrase(startOffset, endOffset);
        }
    }
}
