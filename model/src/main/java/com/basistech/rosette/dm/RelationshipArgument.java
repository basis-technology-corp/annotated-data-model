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

public class RelationshipArgument extends BaseAttribute {

    /**
     * A display string representing the argument
     */
    private final String argumentPhrase;

    /**
     * list of start and end offsets, representing the evidences in the data for a argument
     */
    private final List<Evidence> evidences;

    /**
     * the argument id: placeholder for an identifier from an external knowledge-base the argument resolves to.
     */
    private final String argumentId;


    protected RelationshipArgument(String argumentPhrase, List<Evidence> evidences,
                                   String argumentId, Map<String, Object> extendedProperties) {
        super(extendedProperties);
        this.argumentPhrase = argumentPhrase;
        this.argumentId = argumentId;
        if (evidences != null) {
            this.evidences = evidences;
        } else {
            this.evidences = Lists.newArrayList();
        }
    }

    public String getArgumentPhrase() {
        return argumentPhrase;
    }

    public List<Evidence> getEvidences() {
        return evidences;
    }

    public String getArgumentId() {
        return argumentId;
    }

    @Override
    protected Objects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("argumentPhrase", argumentPhrase)
                .add("evidences", evidences)
                .add("argumentId", argumentId);
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

        RelationshipArgument that = (RelationshipArgument) o;

        if (argumentId != null ? !argumentId.equals(that.argumentId) : that.argumentId != null) {
            return false;
        }

        if (argumentPhrase != null ? !argumentPhrase.equals(that.argumentPhrase) : that.argumentPhrase != null) {
            return false;
        }

        if (evidences != null ? !evidences.equals(that.evidences) : that.evidences != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + argumentPhrase.hashCode();
        result = 31 * result + (argumentId != null ? argumentId.hashCode() : 0);
        result = 31 * result + (evidences != null ? evidences.hashCode() : 0);
        return result;
    }

    public static class Builder extends BaseAttribute.Builder {
        private String argumentPhrase;
        private String argumentId;
        private List<Evidence> evidences = Lists.newArrayList();

        public Builder() {
            super();
        }

        public Builder argumentPhrase(String argumentPhrase) {
            this.argumentPhrase = argumentPhrase;
            return this;
        }

        public Builder argumentId(String argumentId) {
            this.argumentId = argumentId;
            return this;
        }

        public Builder evidences(List<Evidence> evidences) {
            this.evidences = evidences;
            return this;
        }

        public RelationshipArgument build() {
            return new RelationshipArgument(argumentPhrase, evidences, argumentId,
                    buildExtendedProperties());
        }
    }
}
