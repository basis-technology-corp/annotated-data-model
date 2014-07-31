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

import java.util.Map;

/**
 * A base noun phrase.
 */
public class BaseNounPhrase extends Attribute {

    BaseNounPhrase(int startOffset, int endOffset) {
        super(startOffset, endOffset);
    }

    BaseNounPhrase(int startOffset, int endOffset, Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
    }

    // Make json happy
    protected BaseNounPhrase() {
    }

    /**
     * Builder for base noun phrase attributes.
     */
    public static class Builder extends Attribute.Builder {
        /**
         * Constructs a builder from the required properties.
         *
         * @param startOffset start offset in characters
         * @param endOffset end offset in characters
         */
        public Builder(int startOffset, int endOffset) {
            super(startOffset, endOffset);
        }

        /**
         * Constructs a builder from the 'traditional Rosette' data structure.
         * That data structure is an int[] in which the even-numbered items are token
         * start offsets, and the odd-numbered items are token end offsets.
         *
         * @param tokenOffsets array of token start/end offsets
         * @param tokenStartIndex index in tokenOffsets for the start of the phrase
         * @param tokenEndIndex index in tokenOffsets for the end of the phrase
         * @adm.ignore
         */
        public Builder(int[] tokenOffsets, int tokenStartIndex, int tokenEndIndex) {
            this(tokenOffsets[2 * tokenStartIndex], tokenOffsets[2 * (tokenEndIndex - 1) + 1]);
        }

        /**
         * Constructs a builder from an existing BaseNounPhrase.
         *
         * @param toCopy the object to copy
         * @adm.ignore
         */
        public Builder(BaseNounPhrase toCopy) {
            super(toCopy);
        }

        /**
         * Constructs the base noun phrase from the current state of the builder.
         *
         * @return the new analysis object
        */
        public BaseNounPhrase build() {
            return new BaseNounPhrase(startOffset, endOffset, extendedProperties);
        }
    }
}
