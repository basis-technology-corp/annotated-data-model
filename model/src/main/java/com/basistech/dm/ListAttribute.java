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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

/**
 * When attributes nest, the outer object has a single attribute
 * that carries the set. The obvious use of this is for an entire
 * {@link Text} to have a set of tokens or named entities or language
 * regions or whatever.
 */
@JsonSerialize(using = ListAttributeSerializer.class)
public class ListAttribute<Item extends BaseAttribute> extends BaseAttribute {

    private final List<Item> items;
    private final Class<Item> itemClass;

    public ListAttribute(Class<Item> itemClass, List<Item> items) {
        this.itemClass = itemClass;
        this.items = Collections.unmodifiableList(items);
    }

    public List<Item> getItems() {
        return items;
    }

    // this is only used by the serializer, never let it get processed automatically.
    @JsonIgnore
    Class<Item> getItemClass() {
        return itemClass;
    }

    public static class Builder<Item extends BaseAttribute> extends BaseAttribute.Builder {
        private Class<Item> itemClass;
        private List<Item> items;

        public Builder(Class<Item> itemClass) {
            this.itemClass = itemClass;
            items = Lists.newArrayList();
        }

        public void add(Item item) {
            items.add(item);
        }

        public ListAttribute<Item> build() {
            return new ListAttribute<Item>(itemClass, items);
        }
    }
}
