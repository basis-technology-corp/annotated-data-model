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

package com.basistech.rosette.dm;

/**
 * Created by hillel on 1/7/15.
 */
public class RelationArgument extends Attribute {

    /**
     * the argument type: subject, object, locative, temporal, etc. At the moment
     *                    this is free text.
     */
    private final String type;

    /**
     * the argument id: placeholder for a freebase id.
     */
    private final String argumentId;

    protected RelationArgument(int startOffset, int endOffset, String type, String argumentId) {
        super(startOffset, endOffset);
        this.type = type;
        this.argumentId = argumentId;
    }

    public String getType() {
        return type;
    }

    public String getArgumentId() {
        return argumentId;
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

        RelationArgument that = (RelationArgument) o;

        if (argumentId != null ? !argumentId.equals(that.argumentId) : that.argumentId != null) {
            return false;
        }

        if (!type.equals(that.type)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + (argumentId != null ? argumentId.hashCode() : 0);
        return result;
    }

    public static class Builder extends Attribute.Builder {
        private String type;
        private String argumentId;

        public Builder(int startOffset, int endOffset) {
            super(startOffset, endOffset);
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder argumentId(String argumentId) {
            this.argumentId = argumentId;
            return this;
        }

        public RelationArgument build() {
            return new RelationArgument(startOffset, endOffset, type, argumentId);
        }
    }
}
