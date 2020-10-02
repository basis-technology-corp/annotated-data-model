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

import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Map;

/**
 * An evidence for a relationship mention component, pointing to the exact span in the raw text that implies
 * the existence of this component
 * The offsets refer to a half-open range of characters (UTF-16 elements)
 * Note that Extents have no properties of their own.
 */
@EqualsAndHashCode(callSuper = true)
public class Extent extends Attribute implements Serializable {
    private static final long serialVersionUID = 250L;

    protected Extent(int startOffset, int endOffset) {
        super(startOffset, endOffset);
    }

    protected Extent(int startOffset, int endOffset, Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
    }

    /**
     * Builder for Extent attributes.
     */
    public static class Builder extends Attribute.Builder<Extent, Extent.Builder>  {

        /**
         * Constructs a builder with offsets.
         *
         * @param startOffset start characters offset
         * @param endOffset end character offset
         */
        public Builder(int startOffset, int endOffset) {
            super(startOffset, endOffset);
        }

        /**
         * Constructs a builder by copying values from an existing object.
         *
         * @param toCopy the object to copy
         */
        public Builder(Attribute toCopy) {
            super(toCopy);
            this.startOffset = toCopy.startOffset;
            this.endOffset = toCopy.endOffset;
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        /**
         * Creates an immutable extent from the current state of the builder.
         *
         * @return the new extent
         */
        public Extent build() {
            return new Extent(startOffset, endOffset);
        }
    }
}
