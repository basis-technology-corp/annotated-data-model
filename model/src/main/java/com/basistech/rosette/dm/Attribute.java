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

import com.google.common.base.Objects;

import java.util.Map;

/**
 * Base class for attributes that span a range of text.  These attributes
 * have a start and end offset.  The offsets refer to a half-open range
 * of characters (UTF-16 elements).
 */
public abstract class Attribute extends BaseAttribute {
    protected final int startOffset;
    protected final int endOffset;

    protected Attribute(int startOffset, int endOffset) {
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }

    protected Attribute(int startOffset, int endOffset, Map<String, Object> extendedProperties) {
        super(extendedProperties);
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }

    /**
     * Returns the start character offset.
     *
     * @return the start character offset
     */
    public int getStartOffset() {
        return startOffset;
    }

    /**
     * Return the end character offset.
     *
     * @return the end character offset
     */
    public int getEndOffset() {
        return endOffset;
    }

    @Override
    protected Objects.ToStringHelper toStringHelper() {
        return super.toStringHelper().add("startOffset", startOffset)
                .add("endOffset", endOffset);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        Attribute attribute = (Attribute) o;

        if (endOffset != attribute.endOffset) {
            return false;
        }
        return startOffset == attribute.startOffset;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + startOffset;
        result = 31 * result + endOffset;
        return result;
    }

    /**
     * Base class for builders for attributes that inherit from {@link com.basistech.rosette.dm.Attribute}.
     */
    public abstract static class Builder<T extends Attribute, B extends Builder<T, B>> extends BaseAttribute.Builder<Attribute, Builder<T, B>> {
        protected int startOffset;
        protected int endOffset;

        /**
         * Constructs a builder with offsets.
         *
         * @param startOffset start characters offset
         * @param endOffset end character offset
         */
        public Builder(int startOffset, int endOffset) {
            super();
            this.startOffset = startOffset;
            this.endOffset = endOffset;
        }

        /**
         * Constructs a builder by copying values from an existing object.
         *
         * @param toCopy the object to copy
         * @adm.ignore
         */
        public Builder(Attribute toCopy) {
            super(toCopy);
            this.startOffset = toCopy.startOffset;
            this.endOffset = toCopy.endOffset;
        }

        protected abstract B getThis();

        /**
         * Specifies the start character offset.
         *
         * @param startOffset the start character offset
         * @return this
         */
        public B startOffset(int startOffset) {
            this.startOffset = startOffset;
            return getThis();
        }

        /**
         * Specifies the end character offset.
         *
         * @param endOffset the end offset
         * @return this
         */
        public B endOffset(int endOffset) {
            this.endOffset = endOffset;
            return getThis();
        }

        /**
         * Specifies the end character offset.
         *
         * @param endOffset the end offset
         * @return this
         * @deprecated use {@link #endOffset(int)}
         */
        @Deprecated
        public B setEndOffset(int endOffset) {
            this.endOffset = endOffset;
            return getThis();
        }
    }
}
