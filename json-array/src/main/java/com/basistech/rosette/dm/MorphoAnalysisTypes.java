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

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Enum used for MA polymorphism.
 */
enum MorphoAnalysisTypes {
    // don't change order, ordinals in use!
    PLAIN(MorphoAnalysis.class),
    HAN(HanMorphoAnalysis.class),
    KOREAN(KoreanMorphoAnalysis.class),
    ARABIC(ArabicMorphoAnalysis.class);

    private Class<? extends MorphoAnalysis> clazz;
    static final Map<Class<? extends MorphoAnalysis>, MorphoAnalysisTypes> BY_CLASS =
        new ImmutableMap.Builder<Class<? extends MorphoAnalysis>, MorphoAnalysisTypes>()
            .put(MorphoAnalysis.class, PLAIN)
            .put(HanMorphoAnalysis.class, HAN)
            .put(KoreanMorphoAnalysis.class, KOREAN)
            .put(ArabicMorphoAnalysis.class, ARABIC).build();

    MorphoAnalysisTypes(Class<? extends MorphoAnalysis> clazz) {
        this.clazz = clazz;
    }

    Class<? extends MorphoAnalysis> getMorphoAnalysisClass() {
        return clazz;
    }

    static MorphoAnalysisTypes byOrdinal(int ord) {
        return MorphoAnalysisTypes.values()[ord];
    }

    static MorphoAnalysisTypes byClass(Class<? extends MorphoAnalysis> clazz) {
        return BY_CLASS.get(clazz);
    }
}
