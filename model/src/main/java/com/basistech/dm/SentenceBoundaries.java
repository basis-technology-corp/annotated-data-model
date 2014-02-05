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

import java.util.Map;

/**
 * Sentence Boundaries are an attribute of the overall text, and contain the token
 * and character offsets of the boundaries.
 */
public class SentenceBoundaries extends BaseAttribute {
    private final int[] charBoundaries;
    private final int[] tokenBoundaries;

    public SentenceBoundaries(int [] charBoundaries, int[] tokenBoundaries) {
        this.charBoundaries = charBoundaries;
        this.tokenBoundaries = tokenBoundaries;
    }

    public SentenceBoundaries(int[] charBoundaries, int[] tokenBoundaries, Map<String, Object> extendedProperties) {
        super(extendedProperties);
        this.charBoundaries = charBoundaries;
        this.tokenBoundaries = tokenBoundaries;
    }

    protected SentenceBoundaries() {
        charBoundaries = null;
        tokenBoundaries = null;
    }

    public int[] getCharBoundaries() {
        return charBoundaries;
    }

    public int[] getTokenBoundaries() {
        return tokenBoundaries;
    }

    /**
     * Builder for sentence boundaries.
     */
    public static class Builder extends BaseAttribute.Builder {
        private int[] charBoundaries;
        private int[] tokenBoundaries;

        public Builder() {
            //
        }

        public Builder(SentenceBoundaries toCopy) {
            super(toCopy);
            this.charBoundaries = toCopy.charBoundaries;
            this.tokenBoundaries = toCopy.tokenBoundaries;
        }

        public void charBoundaries(int[] charBoundaries) {
            this.charBoundaries = charBoundaries;
        }

        public void tokenBoundaries(int[] tokenBoundaries) {
            this.tokenBoundaries = tokenBoundaries;
        }

        public SentenceBoundaries build() {
            return new SentenceBoundaries(this.charBoundaries, this.tokenBoundaries, extendedProperties);
        }
    }
}
