/*
* Copyright 2018 Basis Technology Corp.
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

import com.basistech.util.ISO15924;
import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Map;

/**
 * A script region. Script regions describe text as spans defined by
 * ISO-15924.
 */
public class ScriptRegion extends Attribute implements Serializable {
    private static final long serialVersionUID = 222L;
    private final ISO15924 script;

    protected ScriptRegion(int startOffset, int endOffset, ISO15924 script, Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
        this.script = script;
    }

    /**
     * Returns the script for this region.
     *
     * @return the script for this region
     */
    public ISO15924 getScript() {
        return script;
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

        ScriptRegion that = (ScriptRegion) o;

        return script == that.script;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + script.hashCode();
        return result;
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("script", script);
    }

    /**
     * Builder for script regions.
     */
    public static class Builder extends Attribute.Builder<ScriptRegion, ScriptRegion.Builder> {
        private ISO15924 script;

        /**
         * Constructs a script region builder from the required values.
         *
         * @param startOffset the start offset in characters
         * @param endOffset the end offset in characters
         * @param script the script
         */
        public Builder(int startOffset, int endOffset, ISO15924 script) {
            super(startOffset, endOffset);
            this.script = script;
        }

        /**
         * Constructs a builder from an existing script region.
         *
         * @param toCopy the object to copy
         * @adm.ignore
         */
        public Builder(ScriptRegion toCopy) {
            super(toCopy);
            this.script = toCopy.script;
        }

        /**
         * Builds an immutable script region from the current state of this builder.
         *
         * @return the new region
         */
        public ScriptRegion build() {
            return new ScriptRegion(startOffset, endOffset, script, buildExtendedProperties());
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }
}
