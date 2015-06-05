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
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Map;

public class RelationshipMention extends BaseAttribute {

    /**
     * A display string representing the relation
     */
    private final String relPhrase;

    /**
     * The arguments for this relationsip mention, indexed by type.
     */
    private final Map<String, BaseAttribute> arguments;

    /**
     * Specifies whether the relation appears in the sentence (synthetic=false)
     * or whether it is based on a syntactic configuration like apposition or
     * possessives.
     */
    private final boolean synthetic;

    /**
     * placeholder for an identifier from an external knowledge-base the predicate resolves to.
     */
    private final String relId;

    protected RelationshipMention(String relPhrase, Map<String, BaseAttribute> arguments, boolean synthetic, String relId, Map<String, Object> extendedProperties) {
        super(extendedProperties);
        this.relPhrase = relPhrase;
        this.synthetic = synthetic;
        this.relId = relId;
        if (arguments != null) {
            this.arguments = ImmutableMap.copyOf(arguments);
        } else {
            this.arguments = ImmutableMap.of();
        }
    }

    public String getRelPhrase() {
        return relPhrase;
    }

    public RelationshipArgument getArg1() {
        return (RelationshipArgument) arguments.get(RelationshipArgumentType.ARG1.key());
    }

    public RelationshipArgument getArg2() {
        return (RelationshipArgument) arguments.get(RelationshipArgumentType.ARG2.key());
    }

    public RelationshipArgument getArg3() {
        return (RelationshipArgument) arguments.get(RelationshipArgumentType.ARG3.key());
    }

    public ListAttribute<RelationshipArgument> getAdjuncts() {
        return (ListAttribute<RelationshipArgument>) arguments.get(RelationshipArgumentType.ADJUNCT.key());
    }

    public ListAttribute<RelationshipArgument> getLocatives() {
        return (ListAttribute<RelationshipArgument>) arguments.get(RelationshipArgumentType.LOCATIVE.key());
    }

    public ListAttribute<RelationshipArgument> getTemporals() {
        return (ListAttribute<RelationshipArgument>) arguments.get(RelationshipArgumentType.TEMPORAL.key());
    }

    public String getRelId() {
        return relId;
    }

    public boolean getSynthetic() {
        return synthetic;
    }

    /**
     * Returns all of the arguments of this relation mention. For the defined arguments,
     * the keys will be values from {@link RelationshipArgumentType#key()}. The values
     * are polymorphic; the subclass of {@link BaseAttribute} depends
     * on the attribute.  Applications should usually prefer to use the
     * convenience accessors (e.g. {@code getArg1}) instead, to avoid the
     * need for a cast.
     *
     * @return all of the arguments
     *
     * @adm.ignore
     */
    public Map<String, BaseAttribute> getArguments() {
        return arguments;
    }

    @Override
    protected Objects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("relPhrase", relPhrase)
                .add("arguments", arguments)
                .add("synthetic", synthetic)
                .add("relId", relId);

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

        RelationshipMention that = (RelationshipMention) o;

        if (synthetic != that.synthetic) {
            return false;
        }

        if (!arguments.equals(that.arguments)) {
            return false;
        }

        if (relId != null ? !relId.equals(that.relId) : that.relId != null) {
            return false;
        }

        if (!relPhrase.equals(that.relPhrase)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + relPhrase.hashCode();
        result = 31 * result + arguments.hashCode();
        result = 31 * result + (synthetic ? 1 : 0);
        result = 31 * result + (relId != null ? relId.hashCode() : 0);
        return result;
    }

    public static class Builder extends BaseAttribute.Builder {

        private String relPhrase;
        private final Map<String, BaseAttribute> arguments = Maps.newHashMap();
        private boolean synthetic;
        private String relId;


        public Builder(String relPhrase) {
            super();
            this.relPhrase = relPhrase;
            this.synthetic = false;
        }

        /**
         * Constructs a builder by copying values from an existing resolved entity.
         *
         * @param toCopy the object to create
         * @adm.ignore
         */
        public Builder(RelationshipMention toCopy) {
            super(toCopy);
            this.relPhrase = toCopy.relPhrase;
            this.arguments.putAll(arguments);
            this.synthetic = toCopy.synthetic;
            this.relId = toCopy.relId;
        }

        /**
         * Specifies a phrase representing the relation
         *
         * @param relPhrase the relation phrase
         * @return
         */
        public Builder relPhrase(String relPhrase) {
            this.relPhrase = relPhrase;
            return this;
        }

        /**
         * Adds an extended value key-value pair.
         *
         * @param key the key
         * @param value the value
         */
        public Builder extendedProperty(String key, Object value) {
            this.extendedProperties.put(key, value);
            return this;
        }

        /**
         * Attaches arg1
         *
         * @param arg1 the arg1
         * @return this
         */
        public Builder arg1(RelationshipArgument arg1) {
            arguments.put(RelationshipArgumentType.ARG1.key(), arg1);
            return this;
        }

        /**
         * Attaches arg2
         *
         * @param arg2 the arg2
         * @return this
         */
        public Builder arg2(RelationshipArgument arg2) {
            arguments.put(RelationshipArgumentType.ARG2.key(), arg2);
            return this;
        }

        /**
         * Attaches arg3
         *
         * @param arg3 the arg3
         * @return this
         */
        public Builder arg3(RelationshipArgument arg3) {
            arguments.put(RelationshipArgumentType.ARG3.key(), arg3);
            return this;
        }

        /**
         * Attaches a list of adjuncts
         *
         * @param adjuncts the adjuncts
         * @return this
         */
        public Builder adjuncts(ListAttribute<RelationshipArgument> adjuncts) {
            arguments.put(RelationshipArgumentType.ADJUNCT.key(), adjuncts);
            return this;
        }

        /**
         * Attaches a list of locatives
         *
         * @param locatives the locatives
         * @return this
         */
        public Builder locatives(ListAttribute<RelationshipArgument> locatives) {
            arguments.put(RelationshipArgumentType.LOCATIVE.key(), locatives);
            return this;
        }

        /**
         * Attaches a list of temporals
         *
         * @param temporals the temporals
         * @return this
         */
        public Builder temporals(ListAttribute<RelationshipArgument> temporals) {
            arguments.put(RelationshipArgumentType.TEMPORAL.key(), temporals);
            return this;
        }

        /**
         * Adds an argument.
         *
         * @param type       the attribute key.
         * @param arg the argument/s. Replaces any previous value for this key.
         * @return this
         */
        Builder attribute(RelationshipArgumentType type, BaseAttribute arg) {
            arguments.put(type.key(), arg);
            return this;
        }

        /**
         * Returns the current arguments.
         *
         * @return the current arguments
         */
        public Map<String, BaseAttribute> attributes() {
            return arguments;
        }

        /**
         * Specifies whether the relation is synthetic or textual.
         *
         * @param synthetic flag indicating whether the relation is synthetic or textual.
         * @return this
         */
        public Builder synthetic(boolean synthetic) {
            this.synthetic = synthetic;
            return this;
        }

        /**
         * Specifies the relation id.
         *
         * @param relId the relation id.
         * @return this
         */
        public Builder relId(String relId) {
            this.relId = relId;
            return this;
        }

        /**
         * Returns an immutable relation mention from the current state of the builder.
         *
         * @return the new relation mention.
         */
        public RelationshipMention build() {
            return new RelationshipMention(relPhrase, arguments, synthetic, relId, extendedProperties);
        }
    }
}
