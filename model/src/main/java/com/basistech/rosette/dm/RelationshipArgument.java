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

import java.util.Map;

public class RelationshipArgument extends Attribute {

    /**
     * the argument argumentPhrase: subject, object, locative, temporal, etc. At the moment
     *                    this is free text.
     */
    private final String argumentPhrase;

    /**
     * the argument id: placeholder for an identifier from an external knowledge-base the argument resolves to.
     */
    private final String argumentId;


    protected RelationshipArgument(int startOffset, int endOffset, String argumentPhrase, String argumentId, Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
        this.argumentPhrase = argumentPhrase;
        this.argumentId = argumentId;
    }

    public String getArgumentPhrase() {
        return argumentPhrase;
    }

    public String getArgumentId() {
        return argumentId;
    }

    @Override
    protected Objects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("argumentPhrase", argumentPhrase)
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

        if (!argumentPhrase.equals(that.argumentPhrase)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + argumentPhrase.hashCode();
        result = 31 * result + (argumentId != null ? argumentId.hashCode() : 0);
        return result;
    }

    public static class Builder extends Attribute.Builder {
        private String argumentPhrase;
        private String argumentId;

        public Builder(int startOffset, int endOffset) {
            super(startOffset, endOffset);
        }

        public Builder argumentPhrase(String argumentPhrase) {
            this.argumentPhrase = argumentPhrase;
            return this;
        }

        public Builder argumentId(String argumentId) {
            this.argumentId = argumentId;
            return this;
        }

        public RelationshipArgument build() {
            return new RelationshipArgument(startOffset, endOffset, argumentPhrase, argumentId, extendedProperties);
        }
    }
}
