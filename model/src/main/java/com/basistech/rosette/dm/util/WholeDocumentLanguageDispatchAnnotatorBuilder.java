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
