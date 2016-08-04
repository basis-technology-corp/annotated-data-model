/*
* Copyright 2014 Basis Technology Corp.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.basistech.rosette.dm;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
            if (extendedProperties instanceof ImmutableMap) {
                this.extendedProperties = extendedProperties;
            } else {
                this.extendedProperties = ImmutableMap.copyOf(extendedProperties);
            }
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

        return extendedProperties.equals(that.extendedProperties);

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
     * Utility method for the 'no empty lists' convention. Takes a list,
     * if empty returns null, else returns a copy of the list.  Use this
     * in constructors of the the immutable objects, not in their builders.
     * @param listToBuild the list
     * @param <T> the type of the list
     * @return a list, or null.
     */
    protected static <T> List<T> listOrNull(List<T> listToBuild) {
        // note: Guava's nullable business is an option here, but it's a lot to drag in at the moment.
        if (listToBuild == null || listToBuild.size() == 0) {
            return null;
        } else {
            return ImmutableList.copyOf(listToBuild);
        }
    }

    /**
     * Utility method for the 'no empty sets' convention. Takes a set,
     * if empty returns null, else returns a copy of the set.  Use this
     * in constructors of the the immutable objects, not in their builders.
     * @param setToBuild the list
     * @param <T> the type of the list
     * @return a list, or null.
     */
    static <T> Set<T> setOrNull(Set<T> setToBuild) {
        // note: Guava's nullable business is an option here, but it's a lot to drag in at the moment.
        if (setToBuild == null || setToBuild.size() == 0) {
            return null;
        } else {
            return ImmutableSet.copyOf(setToBuild);
        }
    }

    static<K, V> Map<K, V> mapOrNull(Map<K, V> mapToCopy) {
        if (mapToCopy == null || mapToCopy.size() == 0) {
            return null;
        } else {
            return ImmutableMap.copyOf(mapToCopy);
        }
    }

    /**
     * Base class for builders for the subclasses of {@link com.basistech.rosette.dm.BaseAttribute}.
     */
    public abstract static class Builder<T extends BaseAttribute, B extends Builder<T, B>> {
        private ImmutableMap.Builder<String, Object> extendedPropertiesBuilder;
        private ImmutableMap<String, Object> extendedPropertiesToCopy;

        /**
         * Constructs a builder with no data.
         */
        protected Builder() {
            /* Usually we don't have any. So start with an empty map, which the constructor above will 'copy'
             * cheaply due to optimization inside ImmutableMap.
             */
            this.extendedPropertiesToCopy = ImmutableMap.of();
        }

        /**
         * Constructs a builder with values derived from an existing object.
         *
         * @param toCopy the object to copy
         */
        protected Builder(BaseAttribute toCopy) {
            /* Just treat the copy as an immutable item until the caller asks us to change it. */
            this.extendedPropertiesToCopy = (ImmutableMap<String, Object>) toCopy.extendedProperties;
        }

        protected abstract B getThis();

        /**
         * Cook up a map to pass to the constructor.
         * If we have an unmodified map to 'copy', we just use it. Otherwise, we build from the builder.
         * @return the map.
         */
        protected Map<String, Object> buildExtendedProperties() {
            if (extendedPropertiesToCopy != null) {
                return extendedPropertiesToCopy;
            } else if (extendedPropertiesBuilder != null) {
                return extendedPropertiesBuilder.build();
            } else {
                return null;
            }
        }

        /**
         * Adds an extended value key-value pair.
         *
         * @param key the key
         * @param value the value
         * @return this
         */
        public B extendedProperty(String key, Object value) {
            if (extendedPropertiesBuilder == null) {
                /* if we don't have a builder yet, forget 'toCopy' in favor of a builder. */
                extendedPropertiesBuilder = ImmutableMap.builder();
                if (extendedPropertiesToCopy != null) {
                    extendedPropertiesBuilder.putAll(extendedPropertiesToCopy);
                    extendedPropertiesToCopy = null;
                }
            }
            this.extendedPropertiesBuilder.put(key, value);
            return getThis();
        }

        /**
         * Replace the entire extended property map.
         * @param properties a map of extended properties.
         * @return this.
         */
        public B extendedProperties(Map<String, Object> properties) {
            /* Turn this into the version we copy. */
            if (properties instanceof ImmutableMap) {
                this.extendedPropertiesToCopy = (ImmutableMap<String, Object>) properties;
            } else {
                this.extendedPropertiesToCopy = ImmutableMap.copyOf(properties);
            }
            /* No builder. */
            this.extendedPropertiesBuilder = null;
            return getThis();
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

        /**
         * Add all the entries of a set to another set, but don't NPE if the 'to be added' collection is null.
         * @param set set to add to.
         * @param toAdd set to add.
         * @param <T> type of elements.
         */
        protected static <T> void addAllToSet(Set<T> set, Set<T> toAdd) {
            if (toAdd != null) {
                set.addAll(toAdd);
            }
        }

        protected static <T> List<T> nullOrList(List<T> newListValue) {
            if (newListValue == null) {
                return Lists.newArrayList();
            } else {
                return newListValue;
            }
        }
    }
}
