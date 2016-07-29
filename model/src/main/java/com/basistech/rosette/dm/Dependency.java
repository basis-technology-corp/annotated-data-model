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

import java.util.Map;
import java.util.Objects;

/**
 * An inter-token dependency from a parser.
 * Note that a token index of {@code -1} means 'ROOT'.
 */
public class Dependency extends BaseAttribute {
    private final String relationship;
    private final int governorTokenIndex;
    private final int dependencyTokenIndex;

    protected Dependency(String relationship, int governorTokenIndex, int dependencyTokenIndex, Map<String, Object> extendedProperties) {
        super(extendedProperties);
        this.relationship = relationship;
        this.governorTokenIndex = governorTokenIndex;
        this.dependencyTokenIndex = dependencyTokenIndex;
    }

    /**
     * @return the relationship label.
     */
    public String getRelationship() {
        return relationship;
    }

    /**
     * @return the index of the governing token.
     */
    public int getGovernorTokenIndex() {
        return governorTokenIndex;
    }

    /**
     * @return the index of the dependency token.
     */
    public int getDependencyTokenIndex() {
        return dependencyTokenIndex;
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
        Dependency that = (Dependency) o;
        return governorTokenIndex == that.governorTokenIndex
                && dependencyTokenIndex == that.dependencyTokenIndex
                && Objects.equals(relationship, that.relationship);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), relationship, governorTokenIndex, dependencyTokenIndex);
    }

    /**
     * Builder for Dependency.
     */
    public static class Builder extends BaseAttribute.Builder<Dependency, Dependency.Builder> {
        private String relationship;
        private int governorTokenIndex;
        private int dependencyTokenIndex;
        /**
         * Constructs a builder from the required properties.
         *
         * @param relationship the relationship label on this dependency.
         * @param governorTokenIndex the token index of the governing token.
         * @param dependencyTokenIndex the token index of the dependency token.
         */
        public Builder(String relationship, int governorTokenIndex, int dependencyTokenIndex) {
            this.relationship = relationship;
            this.governorTokenIndex = governorTokenIndex;
            this.dependencyTokenIndex = dependencyTokenIndex;
        }


        /**
         * Constructs a builder from an existing Dependency.
         *
         * @param toCopy the object to copy
         * @adm.ignore
         */
        public Builder(Dependency toCopy) {
            super(toCopy);
            this.relationship = toCopy.relationship;
            this.governorTokenIndex = toCopy.governorTokenIndex;
            this.dependencyTokenIndex = toCopy.dependencyTokenIndex;
        }

        /**
         * Constructs the Dependency from the current state of the builder.
         *
         * @return the new dependency.
         */
        public Dependency build() {
            return new Dependency(relationship, governorTokenIndex, dependencyTokenIndex, buildExtendedProperties());
        }

        @Override
        protected Dependency.Builder getThis() {
            return this;
        }
    }
}
