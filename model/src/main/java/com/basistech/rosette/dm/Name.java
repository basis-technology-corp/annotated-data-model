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

import com.basistech.util.ISO15924;
import com.basistech.util.LanguageCode;
import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.Map;

/*
 * This class is designed to be enough to support an RNT web service, and no more.
 * So it does not do anything with the concept of fields or contain any of the other
 * complexities of RNI storage or query production.
 */

/**
 * A name of something in the world.
 * Both Names and {@link com.basistech.rosette.dm.EntityMention} objects can contains the names of things.
 * {@linkplain com.basistech.rosette.dm.Mention} is used for reference inside of documents, while
 * {@linkplain Name} is used for names unrelated to documents.
 */
public class Name extends BaseAttribute implements Serializable {
    private static final long serialVersionUID = 222L;
    private final String text;
    private final String type;
    private final ISO15924 script;
    private final LanguageCode languageOfOrigin;
    private final LanguageCode languageOfUse;

    // work around a Jackson bug.
    Name() {
        text = "";
        type = null;
        script = ISO15924.Zyyy;
        languageOfUse = LanguageCode.UNKNOWN;
        languageOfOrigin = LanguageCode.UNKNOWN;
    }

    protected Name(String text, String type, ISO15924 script, LanguageCode languageOfOrigin, LanguageCode languageOfUse, Map<String, Object> extendedProperties) {
        super(extendedProperties);

        if (text == null) {
            this.text = "";
        } else {
            this.text = text;
        }

        this.type = type;

        if (script == null) {
            this.script = ISO15924.Zyyy;
        } else {
            this.script = script;
        }

        if (languageOfOrigin == null) {
            this.languageOfOrigin = LanguageCode.UNKNOWN;
        } else {
            this.languageOfOrigin = languageOfOrigin;
        }

        if (languageOfUse == null) {
            this.languageOfUse = LanguageCode.UNKNOWN;
        } else {
            this.languageOfUse = languageOfUse;
        }
    }

    /**
     * @return the text of this name.
     */
    public String getText() {
        return text;
    }

    /**
     * @return the type of the name, or null. Types are types of things in the world, such as
     * 'PERSON' or 'LOCATION'.
     */
    public String getType() {
        return type;
    }

    /**
     * @return the script code for this name. If the script is not specified, this will return
     * {@link com.basistech.util.ISO15924#Zyyy }.
     */
    public ISO15924 getScript() {
        return script;
    }

    /**
     * @return the language of origin for this name. If the language of origin is not specified,
     * this will return {@link LanguageCode#UNKNOWN}.
     */
    public LanguageCode getLanguageOfOrigin() {
        return languageOfOrigin;
    }

    /**
     * @return the language of use for this name. If the language of use is not specified,
     * this will return {@link LanguageCode#UNKNOWN}.
     */
    public LanguageCode getLanguageOfUse() {
        return languageOfUse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        Name name = (Name) o;

        if (languageOfOrigin != name.languageOfOrigin) {
            return false;
        }
        if (languageOfUse != name.languageOfUse) {
            return false;
        }
        if (script != name.script) {
            return false;
        }
        if (!text.equals(name.text)) {
            return false;
        }
        return !(type != null ? !type.equals(name.type) : name.type != null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + text.hashCode();
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + script.hashCode();
        result = 31 * result + languageOfOrigin.hashCode();
        result = 31 * result + languageOfUse.hashCode();
        return result;
    }

    @Override
    protected Objects.ToStringHelper toStringHelper() {
        Objects.ToStringHelper builder = super.toStringHelper().add("text", text);
        if (type != null) {
            builder.add("type", type);
        }
        if (script != null && script != ISO15924.Zyyy) {
            builder.add("script", script.code4());
        }
        if (languageOfUse != null && languageOfUse != LanguageCode.UNKNOWN) {
            builder.add("languageOfUse", languageOfUse.ISO639_3());
        }
        if (languageOfOrigin != null && languageOfOrigin != LanguageCode.UNKNOWN) {
            builder.add("languageOfOrigin", languageOfOrigin.ISO639_3());
        }
        return builder;
    }

    /**
     * Builder for {@link com.basistech.rosette.dm.Name}.
     */
    public static class Builder extends BaseAttribute.Builder<Name, Name.Builder> {
        private String text;
        private String type;
        private ISO15924 script;
        private LanguageCode languageOfUse;
        private LanguageCode languageOfOrigin;

        /**
         * Construct a builder from a name text.
         * @param text the text.
         */
        public Builder(String text) {
            this.text = text;
            this.script = ISO15924.Zyyy;
            this.languageOfOrigin = LanguageCode.UNKNOWN;
            this.languageOfUse = LanguageCode.UNKNOWN;
        }

        /**
         * Alter the text for the name.
         * @param text new text.
         * @return this.
         */
        public Builder text(String text) {
            this.text = text;
            return this;
        }

        /**
         * Specify the type for this name, such as 'PERSON' or 'LOCATION'.
         * @param type the type. Null is interpreted as an unspecified type.
         * @return this.
         */
        public Builder type(String type) {
            this.type = type;
            return this;
        }

        /**
         * Specify the script of the text of a name.
         * @param script script identifier. Null is interpreted as {@link com.basistech.util.ISO15924#Zyyy}.
         * @return this
         */
        public Builder script(ISO15924 script) {
            this.script = script;
            return this;
        }

        /**
         * Specify the language of use for the name.
         * @param languageOfUse language. Null is interpreted as {@link com.basistech.util.LanguageCode#UNKNOWN}.
         * @return this.
         */
        public Builder languageOfUse(LanguageCode languageOfUse) {
            this.languageOfUse = languageOfUse;
            return this;
        }

        /**
         * Specify the language of origin for the name.
         * @param languageOfOrigin language. Null is interpreted as {@link com.basistech.util.LanguageCode#UNKNOWN}.
         * @return this.
         */
        public Builder languageOfOrigin(LanguageCode languageOfOrigin) {
            this.languageOfOrigin = languageOfOrigin;
            return this;
        }

        /**
         * Construct a new, immutable, {@link Name} object from the contents of the builder.
         * @return the new {@linkplain Name}.
         */
        public Name build() {
            return new Name(text, type, script, languageOfOrigin, languageOfUse, buildExtendedProperties());
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }
}
