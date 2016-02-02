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
     */
    @Override
    public AnnotatedText annotate(CharSequence input) {
        AnnotatedText at = annotators.get(0).annotate(input);
        for (int x = 1; x < annotators.size(); x++) {
            at = annotators.get(x).annotate(at);
        }
        return at;
    }

    @Override
    public AnnotatedText annotate(AnnotatedText input) {
        AnnotatedText at = input;
        for (Annotator annotator : annotators) {
            at = annotator.annotate(at);
        }
        return at;
    }
}
