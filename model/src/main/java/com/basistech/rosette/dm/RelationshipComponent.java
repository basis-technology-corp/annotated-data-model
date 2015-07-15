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
    private final List<Evidence> evidences;
    private final String resolutionId;


    protected RelationshipComponent(String phrase, List<Evidence> evidences,
                                    String resolutionId, Map<String, Object> extendedProperties) {
        super(extendedProperties);
        this.phrase = phrase;
        this.resolutionId = resolutionId;
        this.evidences = listOrNull(evidences);
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
     * Returns a list of start and end offsets, which serve as evidences in the data for a component.
     *
     * @return a list of start and end offsets
     */
    public List<Evidence> getEvidences() {
        return evidences;
    }

    /**
     * Returns a textual identifier from an external database of "real world" entity, property or value this component
     * refers (or "resolves") to.
     *
     * @return an textual identifier of a "real world" entity
     */
    public String getResolutionId() {
        return resolutionId;
    }

    @Override
    protected Objects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("phrase", phrase)
                .add("evidences", evidences)
                .add("resolutionId", resolutionId);
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

        if (resolutionId != null ? !resolutionId.equals(that.resolutionId) : that.resolutionId != null) {
            return false;
        }

        if (phrase != null ? !phrase.equals(that.phrase) : that.phrase != null) {
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
        result = 31 * result + phrase.hashCode();
        result = 31 * result + (resolutionId != null ? resolutionId.hashCode() : 0);
        result = 31 * result + (evidences != null ? evidences.hashCode() : 0);
        return result;
    }

    public static class Builder extends BaseAttribute.Builder {
        private String phrase;
        private String resolutionId;
        private List<Evidence> evidences = Lists.newArrayList();

        public Builder() {
            super();
        }

        public Builder phrase(String phrase) {
            this.phrase = phrase;
            return this;
        }

        public Builder resolutionId(String resolutionId) {
            this.resolutionId = resolutionId;
            return this;
        }

        public Builder evidences(List<Evidence> evidences) {
            this.evidences = evidences;
            return this;
        }

        public RelationshipComponent build() {
            return new RelationshipComponent(phrase, evidences, resolutionId,
                    buildExtendedProperties());
        }
    }
}
