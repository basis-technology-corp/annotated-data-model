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

public class Evidence extends Attribute {

    protected Evidence(int startOffset, int endOffset) {
        super(startOffset, endOffset);
    }

    protected Evidence(int startOffset, int endOffset, Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
    }

    public static class Builder extends Attribute.Builder {

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
         * @adm.ignore
         */
        public Builder(Attribute toCopy) {
            super(toCopy);
            this.startOffset = toCopy.startOffset;
            this.endOffset = toCopy.endOffset;
        }

        /**
         * Specifies the start character offset.
         *
         * @param startOffset the start character offset
         * @return this
         */
        public Builder startOffset(int startOffset) {
            this.startOffset = startOffset;
            return this;
        }

        /**
         * Specifies the end character offset.
         *
         * @param endOffset the end offset
         * @return this
         */
        public Builder setEndOffset(int endOffset) {
            this.endOffset = endOffset;
            return this;
        }

        public Evidence build() {
            return new Evidence(startOffset, endOffset);
        }
    }
}
