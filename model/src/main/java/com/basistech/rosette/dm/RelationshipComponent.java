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
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * A Relationship Component: a building block of a relationship mention, such as an argument, predicate or adjunct.
 *
 */
public class RelationshipComponent extends BaseAttribute implements Serializable {
    private static final long serialVersionUID = 222L;

    private final String phrase;
    private final List<Extent> extents;
    private final String identifier;


    protected RelationshipComponent(String phrase, List<Extent> extents,
                                    String identifier, Map<String, Object> extendedProperties) {
        super(extendedProperties);
        this.phrase = phrase;
        this.identifier = identifier;
        this.extents = listOrNull(extents);
    }

    /**
     * Returns a display string representing the component.
     *
     * @return a display string representing the component
     */
    public String getPhrase() {
        return phrase;
    }

    /**
     * Returns a list of start and end offsets, which serve as extents in the data for a component.
     *
     * @return a list of start and end offsets
     */
    public List<Extent> getExtents() {
        return extents;
    }

    /**
     * Returns a textual identifier from an external database of "real world" entity, property or value this component
     * refers (or "resolves") to.
     *
     * @return an textual identifier of a "real world" entity
     */
    public String getIdentifier() {
        return identifier;
    }

    @Override
    protected Objects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("phrase", phrase)
                .add("extents", extents)
                .add("identifier", identifier);
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

        RelationshipComponent that = (RelationshipComponent) o;

        if (identifier != null ? !identifier.equals(that.identifier) : that.identifier != null) {
            return false;
        }

        if (phrase != null ? !phrase.equals(that.phrase) : that.phrase != null) {
            return false;
        }

        return !(extents != null ? !extents.equals(that.extents) : that.extents != null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + phrase.hashCode();
        result = 31 * result + (identifier != null ? identifier.hashCode() : 0);
        result = 31 * result + (extents != null ? extents.hashCode() : 0);
        return result;
    }

    public static class Builder extends BaseAttribute.Builder<RelationshipComponent, RelationshipComponent.Builder> {
        private String phrase;
        private String identifier;
        private List<Extent> extents = Lists.newArrayList();

        public Builder() {
            super();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder phrase(String phrase) {
            this.phrase = phrase;
            return this;
        }

        public Builder identifier(String identifier) {
            this.identifier = identifier;
            return this;
        }

        public Builder extents(List<Extent> extents) {
            this.extents = extents;
            return this;
        }

        public RelationshipComponent build() {
            return new RelationshipComponent(phrase, extents, identifier,
                    buildExtendedProperties());
        }
    }
}
