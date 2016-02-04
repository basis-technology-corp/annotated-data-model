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

import com.basistech.rosette.dm.Annotator;
import com.basistech.util.LanguageCode;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Fluent builder for annotators that delegate to other annotators based
 * on the language from {@link com.basistech.rosette.dm.AnnotatedText#getWholeTextLanguageDetection()}.
 * You supply this builder with pairs of {@link com.basistech.util.LanguageCode}, {@link com.basistech.rosette.dm.Annotator},
 * and then call {@link #build()} to build an {@link com.basistech.rosette.dm.Annotator}. The resulting annotator
 * handles only {@link com.basistech.rosette.dm.Annotator#annotate(com.basistech.rosette.dm.AnnotatedText)}.
 * It looks at the first {@link com.basistech.rosette.dm.LanguageDetection.DetectionResult} from
 * {@link com.basistech.rosette.dm.AnnotatedText#getWholeTextLanguageDetection()},
 * and selects the annotator corresponding to the language in that
 * detection result, delegating the call to that annotator.
 */
public class WholeDocumentLanguageDispatchAnnotatorBuilder {
    private final Map<LanguageCode, Annotator> delegates;

    /**
     * Create a builder.
     */
    public WholeDocumentLanguageDispatchAnnotatorBuilder() {
        delegates = Maps.newEnumMap(LanguageCode.class);
    }

    /**
     * Add a delegate
     * @param language the language to process.
     * @param delegate the annotator to process it.
     * @return this.
     */
    public WholeDocumentLanguageDispatchAnnotatorBuilder delegate(LanguageCode language, Annotator delegate) {
        if (delegate == null) {
            delegates.remove(language);
        } else {
            delegates.put(language, delegate);
        }
        return this;
    }

    /**
     * Create the annotator.
     * @return the annotator.
     */
    public Annotator build() {
        return new WholeDocumentLanguageDispatchAnnotator(delegates);
    }
}
