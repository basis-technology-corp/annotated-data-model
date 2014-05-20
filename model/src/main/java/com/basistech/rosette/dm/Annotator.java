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

package com.basistech.rosette.dm;

import com.basistech.rosette.RosetteException;

/**
 * An Annotator accepts a buffer of text and returns an Annotated Data model
 * that describes the scripts, tokens, and morphological analyses of the data.
 * A single annotator operates in one language; while the Annotated Data Model
 * supports multi-language texts, this interface does not (yet) provide support.
 */
public interface Annotator {
    /**
     * Accept text, return annotated data model.
     * @param input data to process.
     * @return annotated data model that annotates the text.
     * @throws RosetteException
     */
    AnnotatedText annotate(CharSequence input) throws RosetteException;

    /**
     * Accept an Annotated Data Model text object, processes it, returns
     * a new Annotated Data Model with information from Base Linguistics.
     * @param input the input data.
     * @return annotated data model that annotates the text.
     * @throws RosetteException
     */
    AnnotatedText annotate(AnnotatedText input) throws RosetteException;
}
