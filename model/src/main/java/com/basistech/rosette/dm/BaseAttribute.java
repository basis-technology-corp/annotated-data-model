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

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Base class for attributes that annotate text.  Each attribute contains
 * an extended properties map of String to Object to hold optional
 * "user-defined" elements.
 *
 * @adm.ignore
 */
public abstract class BaseAttribute {
    protected Map<String, Object> extendedProperties;

    protected BaseAttribute() {
        this.extendedProperties = ImmutableMap.of();
    }

    protected BaseAttribute(Map<String, Object> extendedProperties) {
        if (extendedProperties != null) {
            this.extendedProperties = ImmutableMap.copyOf(extendedProperties);
        } else {
            this.extendedProperties = ImmutableMap.of();
        }
    }

    /**
     * Returns the extended properties.  All attributes store a map of extended
     * properties. This mechanism is available to add additional data to
     * attributes.
     *
     * @return the map of extended properties
     */
    public Map<String, Object> getExtendedProperties() {
        return extendedProperties;
    }

    /**
     * Called only from Jackson to implement deserialization via JsonAnySetter.
     * @param name property name
     * @param value property value
     */
    protected void setExtendedProperty(String name, Object value) {
        /* This is only called in deserialization. So we do something
        * to work around the read-only collection. */
        if (extendedProperties.size() > 0) {
            extendedProperties = ImmutableMap.<String, Object>builder().putAll(extendedProperties).put(name, value).build();
        } else {
            extendedProperties = ImmutableMap.of(name, value);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BaseAttribute that = (BaseAttribute) o;

        if (!extendedProperties.equals(that.extendedProperties)) {
            return false;
        }

        return true;
    }

    protected Objects.ToStringHelper toStringHelper() {
        return Objects.toStringHelper(this).omitNullValues()
                .add("extendedProperties", extendedProperties);
    }

    @Override
    public String toString() {
        return toStringHelper().toString();
    }

    @Override
    public int hashCode() {
        return extendedProperties.hashCode();
    }

    /**
     * Base class for builders for the subclasses of {@link com.basistech.rosette.dm.BaseAttribute}.
     */
    public abstract static class Builder {
        protected final Map<String, Object> extendedProperties;

        /**
         * Constructs a builder with no data.
         */
        protected Builder() {
            this.extendedProperties = Maps.newHashMap();
        }

        /**
         * Constructs a builder with values derived from an existing object.
         *
         * @param toCopy the object to copy
         */
        protected Builder(BaseAttribute toCopy) {
            this.extendedProperties = Maps.newHashMap();
            this.extendedProperties.putAll(toCopy.extendedProperties);
        }

        /**
         * Adds an extended value key-value pair.
         *
         * @param key the key
         * @param value the value
         */
        public Builder extendedProperty(String key, Object value) {
            this.extendedProperties.put(key, value);
            return this;
        }

        /**
         * Utility method for the 'no empty lists' convention. Takes a list,
         * if empty returns null, else returns the list.
         * @param listToBuild the list
         * @param <T> the type of the list
         * @return a list, or null.
         */
        protected static <T> List<T> listOrNull(List<T> listToBuild) {
            // note: Guava's nullable business is an option here, but it's a lot to drag in at the moment.
            if (listToBuild == null || listToBuild.size() == 0) {
                return null;
            } else {
                return listToBuild;
            }
        }

        /**
         * Add all the entries of a list to another list, but don't NPE if the 'to be added' list is null.
         * @param list list to add to.
         * @param toAdd list to add.
         * @param <T> type of elements.
         */
        protected static <T> void addAllToList(List<T> list, List<T> toAdd) {
            if (toAdd != null) {
                list.addAll(toAdd);
            }
        }
    }
}
