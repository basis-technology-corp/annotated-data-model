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

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * A container for an ordered collection of attributes of a type.
 * Like all other attributes, it stores extended properties.
 * It is immutable and throws for attempts to use methods that would modify it.
 *
 * @param <Item> The type of the attributes in the list.
 */
public class ListAttribute<Item extends BaseAttribute> extends BaseAttribute implements List<Item>, Serializable {
    private static final long serialVersionUID = 222L;

    private final List<Item> items;
    private final Class<? extends BaseAttribute> itemClass;

    protected ListAttribute(Class<? extends BaseAttribute> itemClass, List<Item> items) {
        this.itemClass = itemClass;
        this.items = ImmutableList.copyOf(items);
    }

    // no json creator. This has custom serialization/deserialization.
    protected ListAttribute(Class<? extends BaseAttribute> itemClass, List<Item> items, Map<String, Object> extendedProperties) {
        super(extendedProperties);
        this.items = items;
        this.itemClass = itemClass;
    }

    // needed to avoid recursion in the Json serialization!

    /**
     * Internal use API used in Jackson serialization.
     * @return the list itself.
     */
    public List<Item> getItems() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        return items.equals(o);
    }

    @Override
    public int hashCode() {
        return items.hashCode();
    }

    // this is only used by the serializer, never let it get processed automatically.

    /**
     * Internal use method for Jackson/Json serialization.
     * @return the class of the items in this list.
     */
    public Class<? extends BaseAttribute> getItemClass() {
        return itemClass;
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
    public boolean contains(Object o) {
        return items.contains(o);
    }

    @Override
    public Iterator<Item> iterator() {
        return items.iterator();
    }

    @Override
    public Object[] toArray() {
        return items.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return items.toArray(a);
    }

    public boolean add(Item item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return items.containsAll(c);
    }

    public boolean addAll(Collection<? extends Item> c) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(int index, Collection<? extends Item> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Item get(int index) {
        return items.get(index);
    }

    public Item set(int index, Item element) {
        throw new UnsupportedOperationException();
    }

    public void add(int index, Item element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Item remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        return items.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return items.lastIndexOf(o);
    }

    @Override
    public ListIterator<Item> listIterator() {
        return items.listIterator();
    }

    @Override
    public ListIterator<Item> listIterator(int index) {
        return items.listIterator(index);
    }

    @Override
    public List<Item> subList(int fromIndex, int toIndex) {
        return items.subList(fromIndex, toIndex);
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return MoreObjects.toStringHelper(this)
                .add("items", items)
                .add("itemClass", itemClass);
    }

    /**
     * A builder for lists.
     *
     * @param <Item> the type of attribute in the list
     */
    public static class Builder<Item extends BaseAttribute> extends BaseAttribute.Builder<ListAttribute<Item>, ListAttribute.Builder<Item>> {
        private Class<? extends BaseAttribute> itemClass;
        private List<Item> items;

        /**
         * Constructs an empty builder.
         *
         * @param itemClass the class for the items to be stored in the list
         */
        public Builder(Class<? extends BaseAttribute> itemClass) {
            this.itemClass = itemClass;
            items = Lists.newArrayList();
        }

        /**
         * Adds one item to the list.
         *
         * @param item the item to add
         * @return this
         */
        public Builder<Item> add(Item item) {
            items.add(item);
            return this;
        }

        /**
         * Specifies the complete list of items.
         *
         * @param items all the items
         * @return this
         */
        public Builder<Item> setItems(List<Item> items) {
            this.items.addAll(items);
            return this;
        }

        /**
         * Constructs an immutable list from the current state of the builder.
         *
         * @return the new list
         */
        public ListAttribute<Item> build() {
            return new ListAttribute<>(itemClass, items, buildExtendedProperties());
        }

        @Override
        protected Builder<Item> getThis() {
            return this;
        }
    }
}
