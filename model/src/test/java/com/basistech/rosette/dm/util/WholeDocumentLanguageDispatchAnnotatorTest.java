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
import com.basistech.rosette.dm.AbstractAnnotator;
import com.basistech.rosette.dm.AnnotatedText;
import com.basistech.rosette.dm.Annotator;
import com.basistech.rosette.dm.LanguageDetection;
import com.basistech.util.LanguageCode;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test the dispatcher.
 */
class WholeDocumentLanguageDispatchAnnotatorTest {
    private static class ConstantAnnotator extends AbstractAnnotator {
        private final AnnotatedText output;

        private ConstantAnnotator(final AnnotatedText output) {
            this.output = output;
        }

        @Override
        public AnnotatedText annotate(final AnnotatedText input) {
            return output;
        }
    }

    @Test
    void dispatch() {

        AnnotatedText.Builder textBuilder = new AnnotatedText.Builder();
        LanguageDetection.DetectionResult.Builder drBuilder = new LanguageDetection.DetectionResult.Builder(LanguageCode.FRENCH);
        LanguageDetection.Builder ldBuilder = new LanguageDetection.Builder(0, 0, Lists.newArrayList(drBuilder.build()));
        textBuilder.wholeDocumentLanguageDetection(ldBuilder.build());
        final AnnotatedText fraText = textBuilder.build();

        textBuilder = new AnnotatedText.Builder();
        drBuilder = new LanguageDetection.DetectionResult.Builder(LanguageCode.SPANISH);
        ldBuilder = new LanguageDetection.Builder(0, 0, Lists.newArrayList(drBuilder.build()));
        textBuilder.wholeDocumentLanguageDetection(ldBuilder.build());
        final AnnotatedText spaText = textBuilder.build();

        // object identity is all we need to see that we got the right answer.
        final AnnotatedText fraResult = new AnnotatedText.Builder().build();
        final AnnotatedText spaResult = new AnnotatedText.Builder().build();

        final Annotator fraAnnotator = new ConstantAnnotator(fraResult);
        final Annotator spaAnnotator = new ConstantAnnotator(spaResult);

        WholeDocumentLanguageDispatchAnnotatorBuilder builder = new WholeDocumentLanguageDispatchAnnotatorBuilder();
        builder.delegate(LanguageCode.FRENCH, fraAnnotator);
        builder.delegate(LanguageCode.SPANISH, spaAnnotator);
        Annotator delegator = builder.build();
        assertSame(fraResult, delegator.annotate(fraText));
        assertSame(spaResult, delegator.annotate(spaText));
    }

    @Test
    void noHandler(){
        AnnotatedText.Builder textBuilder = new AnnotatedText.Builder();
        LanguageDetection.DetectionResult.Builder drBuilder = new LanguageDetection.DetectionResult.Builder(LanguageCode.FRENCH);
        LanguageDetection.Builder ldBuilder = new LanguageDetection.Builder(0, 0, Lists.newArrayList(drBuilder.build()));
        textBuilder.wholeDocumentLanguageDetection(ldBuilder.build());
        final AnnotatedText fraText = textBuilder.build();
        WholeDocumentLanguageDispatchAnnotatorBuilder builder = new WholeDocumentLanguageDispatchAnnotatorBuilder();
        Annotator delegator = builder.build();
        assertThrows(RosetteUnsupportedLanguageException.class, () -> delegator.annotate(fraText));
    }
}
