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

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Base class for attributes. Note that this class works 'as is' for attributes
 * that simply mark a span of text as having a boolean feature.
  */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
public class Attribute {
    protected final String type;
    protected final int startOffset;
    protected final int endOffset;
    protected final Map<String, Object> extendedProperties;

    public Attribute(String type, int startOffset, int endOffset) {
        this.type = type;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        extendedProperties = Maps.newHashMap();
    }

    public String getType() {
        return type;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public int getEndOffset() {
        return endOffset;
    }

    /**
     * All attributes allow for properties that aren't in the data model yet.
     * @return home for wayward properties.
     */
    @JsonAnyGetter
    @JsonAnySetter
    public Map<String, Object> getExtendedProperties() {
        return extendedProperties;
    }
}
