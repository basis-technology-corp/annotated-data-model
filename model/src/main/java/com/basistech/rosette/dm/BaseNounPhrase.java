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

import java.io.Serializable;
import java.util.Map;

/**
 * A base noun phrase.
 */
public class BaseNounPhrase extends Attribute implements Serializable {
    private static final long serialVersionUID = 222L;

    protected BaseNounPhrase(int startOffset, int endOffset, Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
    }

    /**
     * Builder for base noun phrase attributes.
     */
    public static class Builder extends Attribute.Builder<BaseNounPhrase, BaseNounPhrase.Builder> {
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
            return new BaseNounPhrase(startOffset, endOffset, buildExtendedProperties());
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }
}
