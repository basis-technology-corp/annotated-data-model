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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

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
public class ListAttribute<Item extends BaseAttribute> extends BaseAttribute implements List<Item> {

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
    protected Objects.ToStringHelper toStringHelper() {
        return Objects.toStringHelper(this)
                .add("items", items)
                .add("itemClass", itemClass);
    }

    /**
     * A builder for lists.
     *
     * @param <Item> the type of attribute in the list
     */
    public static class Builder<Item extends BaseAttribute> extends BaseAttribute.Builder {
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
        public Builder add(Item item) {
            items.add(item);
            return this;
        }

        /**
         * Specifies the complete list of items.
         *
         * @param items all the items
         * @return this
         */
        public Builder setItems(List<Item> items) {
            this.items.addAll(items);
            return this;
        }

        /**
         * Constructs an immutable list from the current state of the builder.
         *
         * @return the new list
         */
        public ListAttribute<Item> build() {
            return new ListAttribute<Item>(itemClass, items, buildExtendedProperties());
        }
    }
}
