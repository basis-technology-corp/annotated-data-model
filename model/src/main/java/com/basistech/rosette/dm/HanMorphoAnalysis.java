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

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Chinese and Japanese share the addition of readings to the basic MorphoAnalysis data model.
 */
public class HanMorphoAnalysis extends MorphoAnalysis {
    private final List<String> readings;

    HanMorphoAnalysis(String partOfSpeech,
                      String lemma,
                      List<Token> components,
                      String raw,
                      List<String> readings) {
        super(partOfSpeech, lemma, components, raw);
        this.readings = ImmutableList.copyOf(readings);
    }

    protected HanMorphoAnalysis() {
        readings = ImmutableList.of();
    }

    public List<String> getReadings() {
        return readings;
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

        HanMorphoAnalysis that = (HanMorphoAnalysis) o;

        if (!readings.equals(that.readings)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + readings.hashCode();
        return result;
    }

    @Override
    protected Objects.ToStringHelper toStringHelper() {
        return Objects.toStringHelper(this)
                .add("readings", readings);
    }

    public static class Builder extends MorphoAnalysis.Builder {
        private List<String> readings;

        public Builder() {
            super();
            readings = Lists.newArrayList();
        }

        public void addReading(String reading) {
            readings.add(reading);
        }

        public HanMorphoAnalysis build() {
            return new HanMorphoAnalysis(partOfSpeech, lemma, components, raw, readings);
        }
    }
}
