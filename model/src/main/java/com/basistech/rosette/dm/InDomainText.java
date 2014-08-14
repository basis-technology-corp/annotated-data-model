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

import com.basistech.util.TextDomain;
import com.google.common.base.Objects;

/**
 * The InTextDomain.  This tells us which type of alternative we are looking at
 * (e.g., the result of a Chinese script conversion), the text that is the result of
 * that alternative, and the domain of that text (the language and script) for that
 * text.
 */
public class InDomainText {
    private String text;
    private String type;    // We might use an enum here instead.
    private TextDomain domain;

    public InDomainText(String text, String type, TextDomain domain) {
        this.text = text;
        this.type = type;
        this.domain = domain;
    }

    /**
     * Returns the text of this InDomainText object.
     */
    public String getText() {
        return text;
    }

    /**
     * Returns the type of this InDomainText object.  
     * That is, what sort of operation created it.
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the domain of the InDomainText object.
     * That tells us the language and the script of the text.
     */
    public TextDomain getDomain() {
        return domain;
    }

    protected Objects.ToStringHelper toStringHelper() {
        return Objects.toStringHelper(this).omitNullValues()
                .add("text", text)
                .add("type", type)
                .add("domain", domain);
    }

}

