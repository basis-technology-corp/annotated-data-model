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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * The simplest possible attribute.
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class BaseAttribute {
    protected final Map<String, Object> extendedProperties;

    public BaseAttribute() {
        extendedProperties = Maps.newHashMap();
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

    public static class Builder {
        protected final Map<String, Object> extendedProperties;

        public Builder() {
            this.extendedProperties = Maps.newHashMap();
        }

        public Builder(BaseAttribute toCopy) {
            this.extendedProperties = Maps.newHashMap();
            this.extendedProperties.putAll(toCopy.extendedProperties);
        }

        public void extendedProperty(String key, Object value) {
            this.extendedProperties.put(key, value);
        }

        public BaseAttribute build() {
            BaseAttribute newAttribute =  new BaseAttribute();
            newAttribute.extendedProperties.putAll(this.extendedProperties);
            return newAttribute;
        }

    }
}
