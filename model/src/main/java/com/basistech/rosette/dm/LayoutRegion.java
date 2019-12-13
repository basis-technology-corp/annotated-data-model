/*
 * Copyright 2019 Basis Technology Corp.
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


import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Map;

/**
 * Layout defines text as spans defined by structured or unstructured regions.
 */
public class LayoutRegion extends Attribute implements Serializable {

    /**
     * Layout types
     */
    public enum Layout {
        /**
         * For unstructured text.
         */
        UNSTRUCTURED,
        /**
         * For structured text, such as lists or tables.
         */
        STRUCTURED,
    }

    private static final long serialVersionUID = 270L;
    private final Layout layout;

    protected LayoutRegion(int startOffset, int endOffset, Layout layout, Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
        this.layout = layout;
    }

    /**
     * Gets the layout.
     * @return the layout
     */
    public Layout getLayout() {
        return layout;
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

        LayoutRegion that = (LayoutRegion) o;

        return layout == that.layout;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + layout.hashCode();
        return result;
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("layout", layout);
    }

    /**
     * Builder for layout regions.
     */
    public static class Builder extends Attribute.Builder<LayoutRegion, LayoutRegion.Builder> {
        private Layout layout;

        /**
         * Constructs a layout region builder from the required values.
         *
         * @param startOffset the start offset in characters
         * @param endOffset the end offset in characters
         * @param layout the layout type
         */
        public Builder(int startOffset, int endOffset, Layout layout) {
            super(startOffset, endOffset);
            this.layout = layout;
        }

        /**
         * Constructs a builder from an existing layout region.
         *
         * @param toCopy the object to copy
         * @adm.ignore
         */
        public Builder(LayoutRegion toCopy) {
            super(toCopy);
            this.layout = toCopy.layout;
        }

        /**
         * Builds an immutable layout region from the current state of this builder.
         *
         * @return the new region
         */
        public LayoutRegion build() {
            return new LayoutRegion(startOffset, endOffset, layout, buildExtendedProperties());
        }

        @Override
        protected LayoutRegion.Builder getThis() {
            return this;
        }
    }
}
