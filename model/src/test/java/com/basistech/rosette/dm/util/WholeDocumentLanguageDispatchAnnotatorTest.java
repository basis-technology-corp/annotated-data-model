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
import com.google.common.collect.Lists;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test the dispatcher.
 */
@RunWith(JMockit.class)
public class WholeDocumentLanguageDispatchAnnotatorTest {

    @Mocked
    Annotator fraAnnotator;

    @Mocked
    Annotator spaAnnotator;

    @Test
    public void dispatch() throws Exception {

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


        new Expectations() {
            {
                fraAnnotator.annotate(fraText); returns(fraResult);
                spaAnnotator.annotate(spaText); returns(spaResult);
            }

        };

        WholeDocumentLanguageDispatchAnnotatorBuilder builder = new WholeDocumentLanguageDispatchAnnotatorBuilder();
        builder.delegate(LanguageCode.FRENCH, fraAnnotator);
        builder.delegate(LanguageCode.SPANISH, spaAnnotator);
        Annotator delegator = builder.build();
        delegator.annotate(fraText);
        delegator.annotate(spaText);
    }

    @Test(expected = RosetteUnsupportedLanguageException.class)
    public void noHandler() throws Exception {
        AnnotatedText.Builder textBuilder = new AnnotatedText.Builder();
        LanguageDetection.DetectionResult.Builder drBuilder = new LanguageDetection.DetectionResult.Builder(LanguageCode.FRENCH);
        LanguageDetection.Builder ldBuilder = new LanguageDetection.Builder(0, 0, Lists.newArrayList(drBuilder.build()));
        textBuilder.wholeDocumentLanguageDetection(ldBuilder.build());
        final AnnotatedText fraText = textBuilder.build();
        WholeDocumentLanguageDispatchAnnotatorBuilder builder = new WholeDocumentLanguageDispatchAnnotatorBuilder();
        Annotator delegator = builder.build();
        delegator.annotate(fraText);
    }
}
