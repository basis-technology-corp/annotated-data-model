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

package com.basistech.dm.taglets;

import com.sun.javadoc.Tag;
import com.sun.tools.doclets.Taglet;

import java.util.Map;

/**
 * Taglet to mark java-only doc.
 */
public class JavaTaglet implements Taglet {
    private static final String NAME = "adm.java";

    @Override
    public boolean inField() {
        return true;
    }

    @Override
    public boolean inConstructor() {
        return true;
    }

    @Override
    public boolean inMethod() {
        return true;
    }

    @Override
    public boolean inOverview() {
        return true;
    }

    @Override
    public boolean inPackage() {
        return true;
    }

    @Override
    public boolean inType() {
        return true;
    }

    @Override
    public boolean isInlineTag() {
        return true;
    }

    @Override
    public String getName() {
        return NAME;
    }

    /**
     * Register this Taglet.
     *
     * @param tagletMap  the map to register this tag to
     */
    public static void register(Map<String, Taglet> tagletMap) {
        JavaTaglet tag = new JavaTaglet();
        tagletMap.remove(tag.getName());
        tagletMap.put(tag.getName(), tag);
    }

    @Override
    public String toString(Tag tag) {
        // this should not have embedded text, but I see no harm.
        return tag.text();
    }

    @Override
    public String toString(Tag[] tags) {
        return null;
    }
}
