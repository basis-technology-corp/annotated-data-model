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

import com.fasterxml.jackson.core.Version;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;

/**
 * Common class to set up Jackson version from property.
 */
final class ModuleVersion {
    static final Version VERSION;
    static {
        URL vurl = Resources.getResource(AnnotatedDataModelModule.class, "version.properties");
        Version version;
        try {
            String verString = Resources.toString(vurl, Charsets.UTF_8);
            verString = verString.trim();
            verString = verString.substring("version=".length()); // for now, just one property.

            String snapshot = "";
            if (verString.endsWith("-SNAPSHOT")) {
                snapshot = "-SNAPSHOT";
                verString = verString.substring(0, verString.length() - "-SNAPSHOT".length());
            }
            String[] bits = verString.split("\\.");
            version = new Version(Integer.parseInt(bits[0]), Integer.parseInt(bits[1]), Integer.parseInt(bits[2]), snapshot,
                    "com.basistech", "adm-json");
        } catch (IOException e) {
            // alternative: runtime exception.
            version = new Version(0, 0, 0, "", "", "");
        }
        VERSION = version;
    }

    private ModuleVersion() {
        //
    }
}
