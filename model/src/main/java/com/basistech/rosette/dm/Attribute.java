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

import com.google.common.base.Objects;

import java.util.Map;

/**
 * Base class for attributes that span a range of text.
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

    // make Jackson happy.
    protected Attribute() {
        //
        startOffset = 0;
        endOffset = 0;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public int getEndOffset() {
        return endOffset;
    }

    @Override
    protected Objects.ToStringHelper
    toStringHelper() {
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
        if (startOffset != attribute.startOffset) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + startOffset;
        result = 31 * result + endOffset;
        return result;
    }

    public abstract static class Builder extends BaseAttribute.Builder {
        protected int startOffset;
        protected int endOffset;

        public Builder(int startOffset, int endOffset) {
            super();
            this.startOffset = startOffset;
            this.endOffset = endOffset;
        }

        public Builder(Attribute toCopy) {
            super(toCopy);
            this.startOffset = toCopy.startOffset;
            this.endOffset = toCopy.endOffset;
        }

        public void startOffset(int startOffset) {
            this.startOffset = startOffset;
        }

        public void setEndOffset(int endOffset) {
            this.endOffset = endOffset;
        }
    }
}
