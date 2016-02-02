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
