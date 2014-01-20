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
 * An attribute that carries a value. Do we want to use this, or just keep making
 * specific classes? Script regions are a potential use of this.
 */
public class ValueAttribute<T> extends Attribute {
    private final T value;
    private final String type;

    public ValueAttribute(String type, int startOffset, int endOffset, T value) {
        super(startOffset, endOffset);
        this.type = type;
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public String getType() {
        return type;
    }
}
