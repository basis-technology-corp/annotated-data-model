/*
* Copyright 2018 Basis Technology Corp.
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

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * A container for a keyed collection of attributes of a type.
 * Like all other attributes, it stores extended properties.
 * It is immutable and throws for attempts to use methods that would modify it.
 *
 * @param <K> The type of the keys in the map.
 * @param <V> The type of the values in the map.
 */
public class MapAttribute<K, V extends BaseAttribute> extends BaseAttribute implements Map<K, V>, Serializable {
    private static final long serialVersionUID = 222L;

    private final Map<K, V> items;
    private final Class<?> keyClass;
    private final Class<? extends BaseAttribute> valueClass;

    protected MapAttribute(Class<?> keyClass, Class<? extends BaseAttribute> valueClass, Map<K, V> items) {
        this.items = items;
        this.keyClass = keyClass;
        this.valueClass = valueClass;
    }

    // no json creator. This has custom serialization/deserialization.
    protected MapAttribute(Class<?> keyClass, Class<? extends BaseAttribute> valueClass, Map<K, V> items, Map<String, Object> extendedProperties) {
        super(extendedProperties);
        this.items = items;
        this.keyClass = keyClass;
        this.valueClass = valueClass;
    }

    /**
     * Internal use API used in Jackson serialization.
     * @return the map itself.
     */
    public Map<K, V> getItems() {
        return items;
    }

    // this is only used by the serializer, never let it get processed automatically.

    /**
     * Internal use method for Jackson/Json serialization.
     * @return the class of the keys in this list.
     */
    public Class<?> getKeyClass() {
        return keyClass;
    }

    /**
     * Internal use method for Jackson/Json serialization.
     * @return the class of the values in this list.
     */
    public Class<? extends BaseAttribute> getValueClass() {
        return valueClass;
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return items.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return items.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return items.get(key);
    }

    @Override
    public V put(K key, V value) {
        throw readOnly();
    }

    @Override
    public V remove(Object key) {
        throw readOnly();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw readOnly();
    }

    @Override
    public void clear() {
        throw readOnly();
    }

    @Override
    public Set<K> keySet() {
        return items.keySet();
    }

    @Override
    public Collection<V> values() {
        return items.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return items.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return items.equals(o);
    }

    @Override
    public int hashCode() {
        return items.hashCode();
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return MoreObjects.toStringHelper(this)
            .add("items", items)
            .add("keyClass", keyClass)
            .add("valueClass", valueClass);
    }

    private static RuntimeException readOnly() {
        return new UnsupportedOperationException("MapAttribute is read-only");
    }

    public static class Builder<K, V extends BaseAttribute> extends BaseAttribute.Builder<MapAttribute<K, V>, MapAttribute.Builder<K, V>> {
        private Class<?> keyClass;
        private Class<? extends BaseAttribute> valueClass;
        private Map<K, V> items;

        /**
         * Constructs an empty builder.
         *
         * @param keyClass the class for the keys to be stored in the map
         * @param valueClass the class for the values to be stored in the map
         */
        public Builder(Class<?> keyClass, Class<? extends BaseAttribute> valueClass) {
            this.keyClass = keyClass;
            this.valueClass = valueClass;
            items = Maps.newHashMap();
        }

        /**
         * Adds one entry to the map.
         *
         * @param key the key to add
         * @param value the value to add
         * @return this
         */
        public Builder<K, V> put(K key, V value) {
            items.put(key, value);
            return this;
        }

        /**
         * Specifies the complete map.
         *
         * @param items the entire map
         * @return this
         */
        public Builder<K, V> setItems(Map<K, V> items) {
            this.items = items;
            return this;
        }

        /**
         * Constructs an immutable map from the current state of the builder.
         *
         * @return the new map
         */
        public MapAttribute<K, V> build() {
            return new MapAttribute<>(keyClass, valueClass, items, buildExtendedProperties());
        }

        @Override
        protected Builder<K, V> getThis() {
            return this;
        }
    }
}
