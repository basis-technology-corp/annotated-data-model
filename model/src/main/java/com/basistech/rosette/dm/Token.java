/*
* Copyright 2018 Basis Technology Corp.
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

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * The token. The definition of a token can vary by language, but
 * generally a token corresponds to a word.
 */
@EqualsAndHashCode(callSuper = true)
public class Token extends Attribute implements Serializable {
    private static final long serialVersionUID = 250L;
    // we don't want to have to go look at the parent {@link AnnotatedText}.
    private final String text;
    private final List<String> normalized;
    private final List<MorphoAnalysis> analyses;
    private final String source;

    protected Token(int startOffset,
                    int endOffset,
                    String text,
                    List<String> normalized,
                    String source,
                    List<MorphoAnalysis> analyses,
                    Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
        this.text = text;
        this.normalized = listOrNull(normalized);
        this.source = source;
        this.analyses = listOrNull(analyses);
    }

    /**
     * Returns the text of the token.
     * Note that, in some languages, the text may <strong>not</strong> be a substring of
     * the character data stored in the {@link com.basistech.rosette.dm.AnnotatedText}.
     * For example, a Chinese token could start at the end of a line and continue
     * to the next line.  The raw text would include the newline character, but
     * the token would not.
     *
     * @return the text of the token
     */
    public String getText() {
        return text;
    }

    /**
     * Returns the normalized form of the token.
     *
     * @return the normalized form of the token
     */
    public List<String> getNormalized() {
        return normalized;
    }

    /**
     * Returns the list of analyses. Note: the items of this list are of the smallest type needed. So, even if the text
     * is Arabic or Chinese, some of the items in this list may be {@link com.basistech.rosette.dm.MorphoAnalysis}, <strong>not</strong>
     * the corresponding subclass. Callers must use instanceof to check if a particular item is of the subclass.
     *
     * @return the list of analyses
     */
    public List<MorphoAnalysis> getAnalyses() {
        return analyses;
    }

    /**
     * Returns the source of this token.
     * This identifies the component that performed the tokenization.
     *
     * @return the source of this token
     */
    public String getSource() {
        return source;
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return MoreObjects.toStringHelper(this)
                .add("text", text)
                .add("normalized", normalized)
                .add("analyses", analyses)
                .add("source", source);
    }

    /**
     * Builder for tokens.
     */
    public static class Builder extends Attribute.Builder<Token, Token.Builder> {
        private String text;
        private List<String> normalized;
        private List<MorphoAnalysis> analyses;

        private String source;

        /**
         * Constructs a builder from the required properties.
         *
         * @param startOffset the start offset in characters
         * @param endOffset the end offset in characters
         * @param text the text of the token
         */
        public Builder(int startOffset, int endOffset, String text) {
            super(startOffset, endOffset);
            this.text = text;
            this.analyses = Lists.newArrayList();
            this.source = null;
            normalized = Lists.newArrayList();
        }

        /**
         * Constructs a builder from the values of an existing token.
         *
         * @param toCopy existing token to copy
         * @adm.ignore
         */
        public Builder(Token toCopy) {
            super(toCopy);
            text = toCopy.text;
            normalized = Lists.newArrayList();
            analyses = Lists.newArrayList();
            addAllToList(normalized, toCopy.normalized);
            addAllToList(analyses, toCopy.getAnalyses());
        }

        /**
         * Specifies the text.
         *
         * @param text the text
         * @return this
         */
        public Builder text(String text) {
            this.text = text;
            return this;
        }

        /**
         * Adds a normalized form.
         *
         * @param normalized the normalized form
         * @return this
         */
        public Builder addNormalized(String normalized) {
            this.normalized.add(normalized);
            return this;
        }

        /**
         * Sets the list of normalized forms.
         * @param normalized the normalized token forms.
         * @return this.
         */
        public Builder normalized(List<String> normalized) {
            this.normalized = nullOrList(normalized);
            return this;
        }

        /**
         * Specifies the source of this token.
         *
         * @param source the source
         * @return this
         */
        public Builder source(String source) {
            this.source = source;
            return this;
        }

        /**
         * Adds an analysis.
         *
         * @param analysis the analysis
         * @return this
         */
        public Builder addAnalysis(MorphoAnalysis analysis) {
            this.analyses.add(analysis);
            return this;
        }

        /**
         * Sets the list of morphological analyses.
         * @param analyses the analyses.
         * @return this.
         */
        public Builder analyses(List<MorphoAnalysis> analyses) {
            this.analyses = nullOrList(analyses);
            return this;
        }

        /**
         * Creates a new immutable Token object from the current state of the builder.
         *
         * @return the new token
         */
        public Token build() {
            return new Token(startOffset, endOffset, text, normalized, source, analyses, buildExtendedProperties());
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }
}
