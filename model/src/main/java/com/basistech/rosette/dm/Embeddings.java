/*
* Copyright 2016 Basis Technology Corp.
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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Embeddings for a text.
 * A text can have embedding values for the entire text and/or one of more of the collections of
 * items inside the text, such as the tokens or entities. The collections are identified by strings.
 * Constants are provided for some common cases.
 */
public class Embeddings extends BaseAttribute implements Serializable {
    private static final long serialVersionUID = 222L;
    /**
     * The embedding name.
     */
    public enum Name {
        /**
         * The embedding values for the entire text.
         */
        TEXT,

        /**
         * The embedding values for tokens.
         */
        TOKENS
    }

    private final Map<Name, EmbeddingCollection> collections;

    protected Embeddings(Map<Name, EmbeddingCollection> collections, Map<String, Object> extendedProperties) {
        super(extendedProperties);
        this.collections = mapOrNull(collections);
    }

    /**
     * Retrieve the embeddings, if any, for a particular collection.
     * @param collectionName The collection name.
     * @return the embeddings.
     */
    public EmbeddingCollection get(Name collectionName) {
        return collections.get(collectionName);
    }

    /**
     * Retrieve all of the collections as a map.
     * @return the collections.
     */
    public Map<Name, EmbeddingCollection> getCollections() {
        return collections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Embeddings that = (Embeddings) o;
        return Objects.equals(collections, that.collections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), collections);
    }

    @Override
    protected com.google.common.base.MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("collection", collections);
    }

    public static class Builder extends BaseAttribute.Builder<Embeddings, Embeddings.Builder> {
        private Map<Name, EmbeddingCollection> collections;

        public Builder() {
            super();
            collections = new HashMap<>();
        }

        /**
         * Put a collection of embeddings into the embeddings.
         * @param collectionName the name of the collection.
         * @param collection the collection.
         * @return this.
         */
        public Builder put(Name collectionName, EmbeddingCollection collection) {
            collections.put(collectionName, collection);
            return this;
        }

        /**
         * Construct the embedding object.
         * @return the embeddings.
         */
        public Embeddings build() {
            return new Embeddings(collections, buildExtendedProperties());
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }

}
