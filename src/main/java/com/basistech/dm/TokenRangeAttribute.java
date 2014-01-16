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
 * Some attributes are naturally attached to a range of tokens, even if
 * they more precisely attach to a range of characters. They carry token
 * offsets for convenience.
 */
public class TokenRangeAttribute extends Attribute {
    protected final int startTokenOffset;
    protected final int endTokenOffset;

    public TokenRangeAttribute(String type, int startOffset, int endOffset, int startTokenOffset, int endTokenOffset) {
        super(type, startOffset, endOffset);
        this.startTokenOffset = startTokenOffset;
        this.endTokenOffset = endTokenOffset;
    }

    public int getStartTokenOffset() {
        return startTokenOffset;
    }

    public int getEndTokenOffset() {
        return endTokenOffset;
    }
}
