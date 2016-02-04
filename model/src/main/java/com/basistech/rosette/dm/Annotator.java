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
package com.basistech.rosette.dm;

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
     */
    AnnotatedText annotate(CharSequence input);

    /**
     * Annotates an existing text object with additional attributes.
     * For example, an entity annotator may take the output of a base
     * linguistics annotator (which has token annotations) and add additional
     * entity mention attributes.
     *
     * @param input data to process
     * @return annotated data
     */
    AnnotatedText annotate(AnnotatedText input);
}
