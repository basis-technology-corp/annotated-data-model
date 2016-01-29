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

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * The token. The definition of a token can vary by language, but
 * generally a token corresponds to a word.
 */
public class Token extends Attribute {
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

        Token token = (Token) o;

        if (analyses != null ? !analyses.equals(token.analyses) : token.analyses  != null) {
            return false;
        }
        if (normalized != null ? !normalized.equals(token.normalized) : token.normalized != null) {
            return false;
        }
        if (source != null ? !source.equals(token.source) : token.source != null) {
            return false;
        }
        return !(text != null ? !text.equals(token.text) : token.text != null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + text.hashCode();
        if (normalized != null) {
            result = 31 * result + normalized.hashCode();
        }
        if (analyses != null) {
            result = 31 * result + analyses.hashCode();
        }
        if (source != null) {
            result = 31 * result + source.hashCode();
        }
        return result;
    }

    @Override
    protected Objects.ToStringHelper toStringHelper() {
        return Objects.toStringHelper(this)
                .add("text", text)
                .add("normalized", normalized)
                .add("analyses", analyses)
                .add("source", source);
    }

    /**
     * Builder for tokens.
     */
    public static class Builder extends Attribute.Builder<Token> {
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
    }
}
