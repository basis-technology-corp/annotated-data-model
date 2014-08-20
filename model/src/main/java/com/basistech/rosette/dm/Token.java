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
import com.google.common.collect.ImmutableList;
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

    Token(int startOffset,
          int endOffset,
          String text,
          List<String> normalized,
          String source,
          List<MorphoAnalysis> analyses,
          Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
        this.text = text;

        if (normalized != null) {
            this.normalized = ImmutableList.copyOf(normalized);
        } else {
            this.normalized = null;
        }

        this.source = source;

        if (analyses != null) {
            this.analyses = ImmutableList.copyOf(analyses);
        } else {
            this.analyses = null;
        }

    }

    protected Token() {
        text = "";
        normalized = ImmutableList.of();
        analyses = ImmutableList.of();
        source = "";
    }

    Token(int startOffset,
          int endOffset,
          String text,
          List<String> normalized,
          String source,
          List<MorphoAnalysis> analyses) {
        super(startOffset, endOffset);
        this.text = text;

        if (normalized != null) {
            this.normalized = ImmutableList.copyOf(normalized);
        } else {
            this.normalized = null;
        }

        this.source = source;

        if (analyses != null) {
            this.analyses = ImmutableList.copyOf(analyses);
        } else {
            this.analyses = null;
        }
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

        if (!analyses.equals(token.analyses)) {
            return false;
        }
        if (!normalized.equals(token.normalized)) {
            return false;
        }
        if (!source.equals(token.source)) {
            return false;
        }
        if (!text.equals(token.text)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + text.hashCode();
        result = 31 * result + normalized.hashCode();
        result = 31 * result + analyses.hashCode();
        result = 31 * result + source.hashCode();
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
    public static class Builder extends Attribute.Builder {
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
            normalized = toCopy.normalized;
            analyses = toCopy.getAnalyses();
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
         * Specifies the source of this token.
         *
         * @param source the source
         */
        public Builder source(String source) {
            this.source = source;
            return this;
        }

        /**
         * Adds an extended property.
         *
         * @param key the key of the property
         * @param value the value of the property
         */
        public Builder addExtendedProperty(String key, Object value) {
            extendedProperties.put(key, value);
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
         * Creates a new immutable Token object from the current state of the builder.
         *
         * @return the new token
         */
        public Token build() {
            // we do not want to build the token with empty lists.
            List<String> actualNormalized = null;
            if (normalized.size() > 0) {
                actualNormalized = normalized;
            }

            List<MorphoAnalysis> actualAnalyses = null;
            if (analyses.size() != 0) {
                actualAnalyses = analyses;
            }

            return new Token(startOffset, endOffset, text, actualNormalized, source, actualAnalyses, extendedProperties);
        }
    }
}
