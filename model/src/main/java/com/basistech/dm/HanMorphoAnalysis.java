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

package com.basistech.dm;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Chinese and Japanese share the addition of readings to the basic MorphoAnalysis data model.
 */
public class HanMorphoAnalysis extends MorphoAnalysis {
    private final List<String> readings;

    public HanMorphoAnalysis(String partOfSpeech, String lemma, List<Token> components, String raw, List<String> readings) {
        super(partOfSpeech, lemma, components, raw);
        this.readings = readings;
    }

    public List<String> getReadings() {
        return readings;
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
