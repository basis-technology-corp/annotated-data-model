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

import com.basistech.util.ISO15924;
import com.google.common.base.Objects;

import java.util.Map;

/**
 * A script region. Script regions describe text as spans defined by
 * ISO-15924.
 */
public class ScriptRegion extends Attribute {
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
    protected Objects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("script", script);
    }

    /**
     * Builder for script regions.
     */
    public static class Builder extends Attribute.Builder {
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
    }
}
