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
 * An {@code Annotator} annotates text with attributes.  Typical annotations
 * are tokens, script/language regions, morphological analyses, sentences,
 * base noun phrases, and entity mentions.  A single annotator operates in one
 * language; while the data model supports multi-language texts, this
 * interface does not (yet) provide support.
 * @adm.ignore
 */
public interface Annotator {
    /**
     * Annotates raw text with attributes.  For example, a base linguistics
     * annotator may accept raw text and annotate it with token attributes.
     *
     * @param input data to process
     * @return annotated data
     * @throws RosetteException
     */
    AnnotatedText annotate(CharSequence input) throws RosetteException;

    /**
     * Annotates an existing text object with additional attributes.
     * For example, an entity annotator may take the output of a base
     * linguistics annotator (which has token annotations) and add additional
     * entity mention attributes.
     *
     * @param input data to process
     * @return annotated data
     * @throws RosetteException
     */
    AnnotatedText annotate(AnnotatedText input) throws RosetteException;
}
