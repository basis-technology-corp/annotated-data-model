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

import com.basistech.util.ISO15924;
import com.google.common.base.Objects;

/**
 * A script region.
 */
public class ScriptRegion extends Attribute {
    private final ISO15924 value;

    ScriptRegion(int startOffset, int endOffset, ISO15924 value) {
        super(startOffset, endOffset);
        this.value = value;
    }

    ScriptRegion(int startOffset, int endOffset, ISO15924 value, Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
        this.value = value;
    }

    // make Jackson happy.
    protected ScriptRegion() {
        value = null;
    }

    public ISO15924 getValue() {
        return value;
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

        if (value != that.value) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    protected Objects.ToStringHelper toStringHelper() {
        return Objects.toStringHelper(this)
                .add("value", value);
    }

    /**
     * Builder for ScriptRegion.
     */
    public static class Builder extends Attribute.Builder {
        private ISO15924 script;

        public Builder(int startOffset, int endOffset, ISO15924 script) {
            super(startOffset, endOffset);
            this.script = script;
        }

        public ScriptRegion build() {
            return new ScriptRegion(startOffset, endOffset, script, extendedProperties);
        }
    }
}
