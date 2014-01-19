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

package com.basistech.dm;

/**
 * The token.
 */
public class Token extends Attribute {
    private final String[] normalized;
    private final String[] partOfSpeech;
    private final String[] lemma;
    private final String[][] reading;
    // if components, they are represented as tokens, so that they can have offsets, POS, etc.
    private final ListAttribute<Token> components;

    public Token(int startOffset, int endOffset, String[] normalized,
                 String[] partOfSpeech, String[] lemma, String[][] reading,
                 ListAttribute<Token> components) {
        super(Token.class.getName(), startOffset, endOffset);
        this.normalized = normalized;
        this.partOfSpeech = partOfSpeech;
        this.lemma = lemma;
        this.reading = reading;
        this.components = components;
    }

    public String[] getNormalized() {
        return normalized;
    }

    public String[] getPartOfSpeech() {
        return partOfSpeech;
    }

    public String[] getLemma() {
        return lemma;
    }

    public String[][] getReading() {
        return reading;
    }

    public ListAttribute<Token> getComponents() {
        return components;
    }
}
