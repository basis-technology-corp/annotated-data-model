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
 * A base noun phrase.
 */
public class BaseNounPhrase extends Attribute {

    public BaseNounPhrase(int startOffset, int endOffset) {
        super(startOffset, endOffset);
    }

    public static class Builder extends Attribute.Builder {
        public Builder(int startOffset, int endOffset) {
            super(startOffset, endOffset);
        }

        public Builder(BaseNounPhrase toCopy) {
            super(toCopy);
        }

        public BaseNounPhrase build() {
            return new BaseNounPhrase(startOffset, endOffset);
        }
    }
}
