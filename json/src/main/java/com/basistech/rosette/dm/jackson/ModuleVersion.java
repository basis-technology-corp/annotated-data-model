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
package com.basistech.rosette.dm.jackson;

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
