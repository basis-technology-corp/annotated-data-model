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

/**
 * An evidence for a relationship mention component, pointing to the exact span in the raw text that implies
 * the existence of this component
 * The offsets refer to a half-open range of characters (UTF-16 elements)
 * Note that Extents have no properties of their own.
 */
public class Extent extends Attribute {

    protected Extent(int startOffset, int endOffset) {
        super(startOffset, endOffset);
    }

    protected Extent(int startOffset, int endOffset, Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
    }

    /**
     * Builder for Extent attributes.
     */
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
         * Creates an immutable extent from the current state of the builder.
         *
         * @return the new extent
         */
        public Extent build() {
            return new Extent(startOffset, endOffset);
        }
    }
}
