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
 * A Sentence. By convention (influenced by the sentence boundary rules from
 * Unicode TR#29), a sentence should include trailing whitespace after an
 * end-of-sentence marker.  For example, for the string "Hello.  World "
 * with two spaces before "World" and one space after:
 * <pre>
 * 012345678901234
 * Hello.  World
 * </pre>
 * the first sentence is at offsets [0, 8), and the second at [8, 14).
 * <br>
 * Note that sentences have no properties of their own.
 */
public class Sentence extends Attribute {

    protected Sentence(int startOffset, int endOffset, Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
    }

    /**
     * Builder for Sentence attributes.
     */
    public static class Builder extends Attribute.Builder<Sentence, Sentence.Builder> {
        /**
         * Constructs a builder from the required properties.
         *
         * @param startOffset start character offset
         * @param endOffset end character offset
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
         * Constructs a builder from the contents of an existing sentence.
         *
         * @param toCopy the object to copy
         * @adm.ignore
         */
        public Builder(Sentence toCopy) {
            super(toCopy);
        }

        /**
         * Creates an immutable sentence from the current state of the builder.
         *
         * @return the new sentence
         */
        public Sentence build() {
            return new Sentence(startOffset, endOffset, buildExtendedProperties());
        }

        @Override
        protected Builder getThis() {
            return this;
        }

    }
}
