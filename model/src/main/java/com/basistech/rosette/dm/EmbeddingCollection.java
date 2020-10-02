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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A vector of embeddings for some vector of items in an {@link AnnotatedText}.
 * Embeddings can exist for the entire text and for some or all the sub-items,
 * such as tokens or entities. The collection of embeddings is represented as a map
 * from indices to the value vector. In the case of the embedding for the entire text,
 * the index value is 0.
 */
public class EmbeddingCollection implements Serializable {
    private static final long serialVersionUID = 250L;
    private final Map<Integer, float[]> embeddings;

    protected EmbeddingCollection(Map<Integer, float[]> embeddings) {
        this.embeddings = BaseAttribute.mapOrNull(embeddings);
    }

    /**
     * Return the embeddings.
     * @return a map from item indices to values.
     */
    public Map<Integer, float[]> getEmbeddings() {
        return embeddings;
    }

    // Because of the float[] fields, we need to write our own implementation of equals() here
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EmbeddingCollection that = (EmbeddingCollection) o;
        if (!embeddings.keySet().equals(that.embeddings.keySet())) {
            return false;
        }
        for (Map.Entry<Integer, float[]> me : embeddings.entrySet()) {
            // equals used for round-trip testing, floating point == is ok.
            if (!Arrays.equals(me.getValue(), that.embeddings.get(me.getKey()))) {
                return false;
            }

        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(embeddings);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EmbeddingCollection{" + "embeddings={");
        for (Map.Entry<Integer, float[]> me : embeddings.entrySet()) {
            sb.append(String.format("%d : %s,", me.getKey(), Arrays.toString(me.getValue())));
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * Builder class for EmbeddingsCollection.
     */
    public static class Builder {
        private Map<Integer, float[]> embeddings;

        public Builder() {
            embeddings = new HashMap<>();
        }

        public Builder(EmbeddingCollection toCopy) {
            embeddings = new HashMap<>();
            for (Map.Entry<Integer, float[]> me : toCopy.getEmbeddings().entrySet()) {
                embeddings.put(me.getKey(), me.getValue().clone());
            }
        }

        /**
         * Add an embedding.
         * @param index the item index.
         * @param values the values.
         * @return this
         */
        public Builder put(int index, float[] values) {
            embeddings.put(index, values);
            return this;
        }

        /**
         * Retrieve the current embeddings
         * @return the embeddings.
         */
        public Map<Integer, float[]> embeddings() {
            return embeddings;
        }

        /**
         * Build the collection.
         * @return the collection.
         */
        public EmbeddingCollection build() {
            return new EmbeddingCollection(embeddings);
        }


    }
}
