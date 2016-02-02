/*
* Copyright 2014 Basis Technology Corp.
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

package com.basistech.rosette.dm.jackson.array;

import com.basistech.rosette.dm.ArabicMorphoAnalysis;
import com.basistech.rosette.dm.HanMorphoAnalysis;
import com.basistech.rosette.dm.KoreanMorphoAnalysis;
import com.basistech.rosette.dm.MorphoAnalysis;
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

    static final Map<Class<? extends MorphoAnalysis>, MorphoAnalysisTypes> BY_CLASS =
        new ImmutableMap.Builder<Class<? extends MorphoAnalysis>, MorphoAnalysisTypes>()
            .put(MorphoAnalysis.class, PLAIN)
            .put(HanMorphoAnalysis.class, HAN)
            .put(KoreanMorphoAnalysis.class, KOREAN)
            .put(ArabicMorphoAnalysis.class, ARABIC).build();

    private Class<? extends MorphoAnalysis> clazz;

    MorphoAnalysisTypes(Class<? extends MorphoAnalysis> clazz) {
        this.clazz = clazz;
    }

    static MorphoAnalysisTypes byOrdinal(int ord) {
        return MorphoAnalysisTypes.values()[ord];
    }

    static MorphoAnalysisTypes byClass(Class<? extends MorphoAnalysis> clazz) {
        return BY_CLASS.get(clazz);
    }

    Class<? extends MorphoAnalysis> getMorphoAnalysisClass() {
        return clazz;
    }

}
