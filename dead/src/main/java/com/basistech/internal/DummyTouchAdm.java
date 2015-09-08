/******************************************************************************
 * * This data and information is proprietary to, and a valuable trade secret
 * * of, Basis Technology Corp.  It is given in confidence by Basis Technology
 * * and may only be used as permitted under the license agreement under which
 * * it has been distributed, and in no other way.
 * *
 * * Copyright (c) 2015 Basis Technology Corporation All rights reserved.
 * *
 * * The technical data and information provided herein are provided with
 * * `limited rights', and the computer software provided herein is provided
 * * with `restricted rights' as those terms are defined in DAR and ASPR
 * * 7-104.9(a).
 ******************************************************************************/

package com.basistech.internal;

import com.basistech.rosette.dm.AnnotatedText;
import com.basistech.rosette.dm.ArabicMorphoAnalysis;
import com.basistech.rosette.dm.HanMorphoAnalysis;
import com.basistech.rosette.dm.KoreanMorphoAnalysis;
import com.basistech.rosette.dm.internal.TextWrapper;
import com.basistech.rosette.dm.util.ComposingAnnotator;
import com.basistech.rosette.dm.util.WholeDocumentLanguageDispatchAnnotator;
import com.basistech.rosette.dm.util.WholeDocumentLanguageDispatchAnnotatorBuilder;

/**
 * The maven shade plugin, in 'minimize' mode, is only useful if some class
 * in the primary project artifact has a reference tree that reaches what's needed.
 * This is that class.
 */
public class DummyTouchAdm {
    //CHECKSTYLE:OFF
    AnnotatedText annotatedText;
    ArabicMorphoAnalysis arabicMorphoAnalysis;
    HanMorphoAnalysis hanMorphoAnalysis;
    KoreanMorphoAnalysis koreanMorphoAnalysis;
    TextWrapper textWrapper;
    ComposingAnnotator composingAnnotator;
    WholeDocumentLanguageDispatchAnnotator wholeDocumentLanguageDispatchAnnotator;
    WholeDocumentLanguageDispatchAnnotatorBuilder wholeDocumentLanguageDispatchAnnotatorBuilder;
    //CHECKSTYLE:ON
}
