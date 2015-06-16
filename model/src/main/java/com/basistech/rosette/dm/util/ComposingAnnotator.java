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

package com.basistech.rosette.dm.util;

import com.basistech.rosette.RosetteException;
import com.basistech.rosette.dm.AnnotatedText;
import com.basistech.rosette.dm.Annotator;

import java.util.List;

/**
 * An annotator that applies a series of annotators.
 */
public class ComposingAnnotator implements Annotator {
    private final List<Annotator> annotators;

    /**
     * Construct a composing annotator from a list of annotators. Input is passed to the first annotator.
     * The results of the first annotator are passed to the second annotator, etc, and the last annotator's
     * results are returned.
     * @param annotators the annotators.
     */
    public ComposingAnnotator(List<Annotator> annotators) {
        this.annotators = annotators;
    }


    /**
     * Apply the first annotator to the plain character sequence, and then chain the results of that
     * annotator through the remaining annotators.
     * @param input data to process
     * @return the result of the last annotator.
     * @throws RosetteException
     */
    @Override
    public AnnotatedText annotate(CharSequence input) throws RosetteException {
        AnnotatedText at = annotators.get(0).annotate(input);
        for (int x = 1; x < annotators.size(); x++) {
            at = annotators.get(x).annotate(at);
        }
        return at;
    }

    @Override
    public AnnotatedText annotate(AnnotatedText input) throws RosetteException {
        AnnotatedText at = input;
        for (int x = 0; x < annotators.size(); x++) {
            at = annotators.get(x).annotate(at);
        }
        return at;
    }
}
