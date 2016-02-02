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
