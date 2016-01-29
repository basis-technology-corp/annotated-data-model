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

package com.basistech.rosette.dm.util;

import com.basistech.rosette.RosetteUnsupportedLanguageException;
import com.basistech.rosette.dm.AnnotatedText;
import com.basistech.rosette.dm.Annotator;
import com.basistech.rosette.dm.LanguageDetection;
import com.basistech.util.LanguageCode;

import java.util.Map;

/**
 * An annotator that delegates to one of a collection of per-language annotators.
 * This picks the annotator based on the first detected language, and has no provision
 * to dispatch 'other' languages as if they are unknown. If the best detected
 * language is not in the map, this throws {@link RosetteUnsupportedLanguageException}.
 */
public class WholeDocumentLanguageDispatchAnnotator implements Annotator {
    private final Map<LanguageCode, Annotator> delegates;

    WholeDocumentLanguageDispatchAnnotator(Map<LanguageCode, Annotator> delegates) {
        this.delegates = delegates;
    }

    @Override
    public AnnotatedText annotate(CharSequence input) {
        throw new UnsupportedOperationException("No AnnotatedText provided.");
    }

    @Override
    public AnnotatedText annotate(AnnotatedText input) {
        LanguageDetection languageDetection = input.getWholeTextLanguageDetection();
        if (languageDetection == null || languageDetection.getDetectionResults().size() == 0) {
            throw new IllegalArgumentException("No whole document language detection in the input.");
        }
        Annotator delegate = delegates.get(languageDetection.getDetectionResults().get(0).getLanguage());
        if (delegate == null) {
            throw new RosetteUnsupportedLanguageException(languageDetection.getDetectionResults().get(0).getLanguage());
        }
        return delegate.annotate(input);
    }
}
