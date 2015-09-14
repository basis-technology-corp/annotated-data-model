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
