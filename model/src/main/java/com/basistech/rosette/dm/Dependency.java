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

import java.util.Map;
import java.util.Objects;

/**
 * An inter-token dependency from a parser.
 * Note that a token index of {@code -1} means 'ROOT'.
 * <br>
 * Consider a ConLL file with dependency information:
 * <pre>
 * 1    My      my      PRP$  _   2   poss    _   O
 * 2    dog     dog     NN    _   3   nsubj   _   O
 * 3    has     have    VBZ   _   0   root    _   O
 * 4    fleas   flea    NNS   _   3   dobj    _   O
 * 5    .       .       .     _   3   punct   _   O
 * </pre>
 * Each token has two columns related to dependencies: the 'governor' and the 'label'. In the ADM,
 * this would turn into a list of {@code Dependency} objects as follows:
 * <table summary="An example list of Dependencies">
 *     <thead>
 *         <tr>
 *             <td>relationship</td>
 *             <td>governorTokenIndex</td>
 *             <td>dependencyTokenIndex</td>
 *         </tr>
 *     </thead>
 *     <tbody>
 *         <tr>
 *             <td>poss</td>
 *             <td>1</td>
 *             <td>0</td>
 *         </tr>
 *         <tr>
 *             <td>nsubj</td>
 *             <td>2</td>
 *             <td>1</td>
 *         </tr>
 *         <tr>
 *             <td>root</td>
 *             <td>-1</td>
 *             <td>2</td>
 *         </tr>
 *         <tr>
 *             <td>dobj</td>
 *             <td>2</td>
 *             <td>3</td>
  *         </tr>
 *         <tr>
 *             <td>punct</td>
 *             <td>2</td>
 *             <td>4</td>
 *         </tr>
 *     </tbody>
 * </table>
 * Viewed as a list, the {@code dependencyTokenIndex} is redundant, but it is useful to pass a single dependency
 * around in isolation as a complete representation of a dependency arc.
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

    @Override
    protected com.google.common.base.Objects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("relationship", relationship)
                .add("dependencyTokenIndex", dependencyTokenIndex)
                .add("governorTokenIndex", governorTokenIndex);
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
