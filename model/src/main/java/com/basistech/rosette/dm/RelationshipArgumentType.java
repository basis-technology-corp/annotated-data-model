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

public enum RelationshipArgumentType {

    ARG1("arg1"),
    ARG2("arg2"),
    ARG3("arg3"),
    ADJUNCT("adjuncts"),
    LOCATIVE("locatives"),
    TEMPORAL("temporals");

    private final String key;

    RelationshipArgumentType(String key) {
        this.key = key;
    }

    /**
     * @return a value used as a key in {@link com.basistech.rosette.dm.RelationshipMention} and in json serialization.
     */
    public String key() {
        return key;
    }
}
