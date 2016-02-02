/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2015 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/

package com.basistech.rosette.dm;


import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * A Relationship Component: a building block of a relationship mention, such as an argument, predicate or adjunct.
 *
 */
public class RelationshipComponent extends BaseAttribute {

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
