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
 * An attribute that carries a value.
 * @param <T> the type of the value.
 *           <br/>
 * TODO: decide is this is really a good idea. It's used for script regions,
 * and it might be cleaner to just make a class for them.
 */
public class ValueAttribute<T> extends Attribute {
    private final T value;
    private final String type;

    public ValueAttribute(String type, int startOffset, int endOffset, T value) {
        super(startOffset, endOffset);
        this.type = type;
        this.value = value;
    }

    public ValueAttribute(int startOffset, int endOffset, T value, String type, Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
        this.value = value;
        this.type = type;
    }

    public T getValue() {
        return value;
    }

    public String getType() {
        return type;
    }
}
