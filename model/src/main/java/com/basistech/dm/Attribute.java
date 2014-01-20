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

package com.basistech.dm;

/**
 * Base class for attributes. Note that this class works 'as is' for attributes
 * that simply mark a span of text as having a boolean feature.
  */
public class Attribute extends BaseAttribute {
    protected final int startOffset;
    protected final int endOffset;

    public Attribute(int startOffset, int endOffset) {
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

    public static class Builder extends BaseAttribute.Builder {
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

        public Attribute build() {
            Attribute attribute = new Attribute(startOffset, endOffset);
            attribute.extendedProperties.putAll(this.extendedProperties);
            return attribute;
        }
    }
}
