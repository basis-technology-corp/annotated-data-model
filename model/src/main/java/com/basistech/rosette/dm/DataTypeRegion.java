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
 * Data type defines text as spans defined by structured or unstructured regions.
 */
public class DataTypeRegion extends Attribute implements Serializable {

    public enum Type {
        UNSTRUCTURED,
        STRUCTURED,
    }

    private static final long serialVersionUID = 250L;
    private final Type type;

    protected DataTypeRegion(int startOffset, int endOffset, Type type, Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
        this.type = type;
    }

    /**
     * Gets the type.
     * @return the type
     */
    public Type getType() {
        return type;
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

        DataTypeRegion that = (DataTypeRegion) o;

        return type == that.type;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("type", type);
    }

    /**
     * Builder for data type regions.
     */
    public static class Builder extends Attribute.Builder<DataTypeRegion, DataTypeRegion.Builder> {
        private Type type;

        /**
         * Constructs a data type region builder from the required values.
         *
         * @param startOffset the start offset in characters
         * @param endOffset the end offset in characters
         * @param type the type
         */
        public Builder(int startOffset, int endOffset, Type type) {
            super(startOffset, endOffset);
            this.type = type;
        }

        /**
         * Constructs a builder from an existing data type region.
         *
         * @param toCopy the object to copy
         * @adm.ignore
         */
        public Builder(DataTypeRegion toCopy) {
            super(toCopy);
            this.type = toCopy.type;
        }

        /**
         * Builds an immutable data type region from the current state of this builder.
         *
         * @return the new region
         */
        public DataTypeRegion build() {
            return new DataTypeRegion(startOffset, endOffset, type, buildExtendedProperties());
        }

        @Override
        protected DataTypeRegion.Builder getThis() {
            return this;
        }
    }
}
