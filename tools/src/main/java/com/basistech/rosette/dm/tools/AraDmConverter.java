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


package com.basistech.rosette.dm.tools;

import com.basistech.rlp.AbstractResultAccess;
import com.basistech.rosette.dm.AnnotatedText;
import com.basistech.rosette.dm.ArabicMorphoAnalysis;
import com.basistech.rosette.dm.BaseNounPhrase;
import com.basistech.rosette.dm.EntityMention;
import com.basistech.rosette.dm.HanMorphoAnalysis;
import com.basistech.rosette.dm.LanguageDetection;
import com.basistech.rosette.dm.ListAttribute;
import com.basistech.rosette.dm.MorphoAnalysis;
import com.basistech.rosette.dm.ScriptRegion;
import com.basistech.rosette.dm.Sentence;
import com.basistech.rosette.dm.Token;
import com.basistech.util.ISO15924;
import com.basistech.util.LanguageCode;
import com.google.common.collect.Lists;
import java.util.List;

/**
 * Convert an {@link com.basistech.rlp.AbstractResultAccess} to a {@link com.basistech.rosette.dm.AnnotatedText}.
 */
public final class AraDmConverter {

    private static final int ARBL_FEATURE_DEFINITE_ARTICLE = 1 << 1;
    private static final int ARBL_FEATURE_STRIPPABLE_PREFIX = 1 << 2;

    private AraDmConverter() {
        //
    }

    public static AnnotatedText convert(AbstractResultAccess ara) {
        AnnotatedText.Builder builder = new AnnotatedText.Builder().data(new String(ara.getRawText(), 0, ara.getRawText().length));

        // no provisions for original binary data, so no getRawBytes.
        /* an ARA by itself doesn't have language regions or detections;
         * The code for that is just in the RWS, so we'll ignore it at this prototype stage.
         */

        buildLanguageDetections(ara, builder);
        buildTokens(ara, builder);
        buildEntityMentions(ara, builder);
        buildSentences(ara, builder);
        buildScriptRegions(ara, builder);
        buildBaseNounPhrase(ara, builder);

        return builder.build();
    }

    // An ARA can have results from RLP/RLI and RLBL.  If we have an RLI result
    // for the whole doc, it is always the first region.
    private static void buildLanguageDetections(AbstractResultAccess ara, AnnotatedText.Builder builder) {
        addRLIResults(ara, builder);
        ListAttribute.Builder<LanguageDetection> attrBuilder = new ListAttribute.Builder<LanguageDetection>(LanguageDetection.class);
        addRLBLResults(ara, builder, attrBuilder);
        builder.languageDetectionRegions(attrBuilder.build());
    }

    private static void addRLIResults(AbstractResultAccess ara, AnnotatedText.Builder builder) {
        LanguageCode langCode = ara.getDetectedLanguage();
        String encoding = ara.getDetectedEncoding();
        ISO15924 scriptCode = ara.getDetectedScript();
        if (langCode != null || encoding != null || scriptCode != null) {
            int startOffset = 0;
            int endOffset = builder.data().length();
            LanguageDetection.DetectionResult result = new LanguageDetection.DetectionResult.Builder(langCode).encoding(encoding).script(scriptCode).build();
            List<LanguageDetection.DetectionResult> results = Lists.newArrayList(result);
            LanguageDetection.Builder ldBuilder = new LanguageDetection.Builder(startOffset, endOffset, results);
            builder.wholeDocumentLanguageDetection(ldBuilder.build());
        }
    }

    private static void addRLBLResults(AbstractResultAccess ara, AnnotatedText.Builder builder,
                                       ListAttribute.Builder<LanguageDetection> attrBuilder) {
        // Each region contain 6 integers:
        // 0: start
        // 1: end
        // 2: level     // reserved
        // 3: type      // reserved
        // 4: script    // reserved
        // 5: language
        int[] regions = ara.getLanguageRegion();
        if (regions != null) {
            for (int i = 0; i < regions.length / 6; i++) {
                int startOffset = regions[6 * i];
                int endOffset = regions[6 * i + 1];
                ISO15924 scriptCode = ISO15924.Zyyy;
                LanguageCode langCode = LanguageCode.lookupByLanguageID(regions[6 * i + 5]);
                LanguageDetection.DetectionResult result = new LanguageDetection.DetectionResult
                        .Builder(langCode).encoding("UTF-16").script(scriptCode).build();
                List<LanguageDetection.DetectionResult> results = Lists.newArrayList(result);
                LanguageDetection.Builder ldBuilder = new LanguageDetection.Builder(startOffset, endOffset, results);
                attrBuilder.add(ldBuilder.build());
            }
        }
    }

    private static void buildBaseNounPhrase(AbstractResultAccess ara, AnnotatedText.Builder builder) {
        if (ara.getBaseNounPhrase() != null) {
            ListAttribute.Builder<BaseNounPhrase> attrBuilder = new ListAttribute.Builder<BaseNounPhrase>(BaseNounPhrase.class);
            int[] baseNounPhrase = ara.getBaseNounPhrase();
            int[] tokenOffsets = ara.getTokenOffset();
            for (int i = 0; i < baseNounPhrase.length; i += 2) {
                BaseNounPhrase.Builder bnpBuilder = new BaseNounPhrase.Builder(tokenOffsets, baseNounPhrase[i], baseNounPhrase[i + 1]);
                attrBuilder.add(bnpBuilder.build());
            }
            builder.baseNounPhrases(attrBuilder.build());
        }
    }

    private static void buildScriptRegions(AbstractResultAccess ara, AnnotatedText.Builder builder) {
        if (ara.getScriptRegion() != null) {
            ListAttribute.Builder<ScriptRegion> listBuilder = new ListAttribute.Builder<ScriptRegion>(ScriptRegion.class);
            int[] scriptRegion = ara.getScriptRegion();
            for (int i = 0; i < scriptRegion.length; i += 3) {
                ScriptRegion.Builder srBuilder = new ScriptRegion.Builder(scriptRegion[i], scriptRegion[i + 1], ISO15924.lookupByNumeric(scriptRegion[i + 2]));
                listBuilder.add(srBuilder.build());
            }
            builder.scriptRegions(listBuilder.build());
        }
    }

    private static void buildSentences(AbstractResultAccess ara, AnnotatedText.Builder builder) {
        // If token-level sentences exist, we use them by mapping back to offsets.
        // Otherwise we fallback to text boundaries.  If we have both, there is no
        // attempt to make sure they are aligned.
        if (ara.getSentenceBoundaries() != null) {
            int[] tokenOffsets = ara.getTokenOffset();
            ListAttribute.Builder<Sentence> sentListBuilder = new ListAttribute.Builder<Sentence>(Sentence.class);
            int start = 0;
            for (int endToken : ara.getSentenceBoundaries()) {
                // This is the start character of the token that begins the next sentence.
                // That's correct since sentence boundaries are supposed to include trailing
                // whitespace, per Unicode TR#29.
                int end = (endToken < tokenOffsets.length / 2)
                    ? tokenOffsets[2 * endToken] : ara.getRawText().length;
                Sentence.Builder sentBuilder = new Sentence.Builder(start, end);
                sentListBuilder.add(sentBuilder.build());
                start = end;
            }
            builder.sentences(sentListBuilder.build());
        } else if (ara.getTextBoundaries() != null) {
            ListAttribute.Builder<Sentence> sentListBuilder = new ListAttribute.Builder<Sentence>(Sentence.class);
            int start = 0;
            for (int end : ara.getTextBoundaries()) {
                Sentence.Builder sentBuilder = new Sentence.Builder(start, end);
                sentListBuilder.add(sentBuilder.build());
                start = end;
            }
            builder.sentences(sentListBuilder.build());
        }
    }

    private static void buildEntityMentions(AbstractResultAccess ara, AnnotatedText.Builder builder) {
        int[] entities = ara.getNamedEntity();
        if (entities != null) {
            ListAttribute.Builder<EntityMention> emListBuilder = new ListAttribute.Builder<EntityMention>(EntityMention.class);
            int namedEntityCount = entities.length / 3;
            for (int x = 0; x < namedEntityCount; x++) {
                emListBuilder.add(buildOneEntityMention(ara, x));
            }
            builder.entityMentions(emListBuilder.build());
        }
    }

    private static EntityMention buildOneEntityMention(AbstractResultAccess ara, int x) {
        int startToken = ara.getNamedEntity()[x * 3];
        int endToken = ara.getNamedEntity()[(x * 3) + 1];
        // The type string is added by the redactor, so it might be missing depending
        // on the user's rlp context.
        String[] typeStrings = ara.getNamedEntityTypeString();
        String entityType = typeStrings != null ? typeStrings[x] : "NONE";

        int start = ara.getTokenOffset()[startToken * 2];
        int end = ara.getTokenOffset()[(endToken - 1) * 2 + 1];

        EntityMention.Builder builder = new EntityMention.Builder(start, end, entityType);
        if (ara.getNamedEntityTokenConfidence() != null) {
            double confidence = Double.POSITIVE_INFINITY;
            for (int tx = startToken; tx < endToken; tx++) {
                confidence = Math.min(confidence, ara.getNamedEntityTokenConfidence()[tx]);
            }
            builder.confidence(confidence);
        }
        if (ara.getNamedEntityChainId() != null) {
            builder.coreferenceChainId(ara.getNamedEntityChainId()[x]);
        }
        if (ara.getNormalizedNamedEntity() != null) {
            builder.normalized(ara.getNormalizedNamedEntity()[x]);
        }
        if (ara.getNamedEntitySourceString() != null) {
            String[] split = ara.getNamedEntitySourceString()[x].split(":", 2);
            builder.source(split[0]);
            if (split.length > 1) {
                builder.subsource(split[1]);
            }
        }
        return builder.build();
    }

    private static void buildTokens(AbstractResultAccess ara, AnnotatedText.Builder builder) {
        if (ara.getTokens() != null) {
            ListAttribute.Builder<Token> tokenListBuilder = new ListAttribute.Builder<Token>(Token.class);
            int tokenCount = ara.getTokens().length;

            for (int x = 0; x < tokenCount; x++) {
                tokenListBuilder.add(buildOneToken(ara, x));
            }
            builder.tokens(tokenListBuilder.build());
        }
    }

    private static void buildAnalysisList(AbstractResultAccess ara, int x, Token.Builder builder) {
        final MorphoAnalysis analysis = buildBaseAnalysis(ara, x);
        if (analysis != null) {
            builder.addAnalysis(analysis);
        }
        if (ara.getAlternativePartsOfSpeech() != null && ara.getAlternativePartsOfSpeech().getStringsForTokenIndex(x) != null) {
            buildAltAnalyses(ara, x, builder);
        }
    }

    private static void buildAltAnalyses(AbstractResultAccess ara, int x, Token.Builder builder) {
        for (int ax = 0; ax < ara.getAlternativePartsOfSpeech().getStringsForTokenIndex(x).length; ax++) {
            switch (ara.getDetectedLanguage()) {
            case ARABIC:
                buildAltArabicAnalysis(ara, x, ax, builder);
                break;
            case JAPANESE:
            case CHINESE:
            case SIMPLIFIED_CHINESE:
            case TRADITIONAL_CHINESE:
                buildAltHanAnalysis(ara, x, ax, builder);
                break;
            default:
                buildAltCommonAnalysis(ara, x, ax, builder);
                break;
            }
        }
    }

    private static void buildAltCommonAnalysis(AbstractResultAccess ara, int x, int ax, Token.Builder builder) {
        MorphoAnalysis.Builder anBuilder = new MorphoAnalysis.Builder();
        setupCommonAltAnalysis(ara, x, ax, anBuilder);
        builder.addAnalysis(anBuilder.build());
    }

    private static void buildAltHanAnalysis(AbstractResultAccess ara, int x, int ax, Token.Builder builder) {
        HanMorphoAnalysis.Builder anBuilder = new HanMorphoAnalysis.Builder();
        setupCommonAltAnalysis(ara, x, ax, anBuilder);
        // no such thing as alt readings, so we're done.
        builder.addAnalysis(anBuilder.build());
    }

    private static void buildAltArabicAnalysis(AbstractResultAccess ara, int x, int ax, Token.Builder builder) {
        ArabicMorphoAnalysis.Builder anBuilder = new ArabicMorphoAnalysis.Builder();
        setupCommonAltAnalysis(ara, x, ax, anBuilder);
        if (ara.getAlternativeRoots() != null && ara.getAlternativeRoots().getStringsForTokenIndex(x) != null) {
            anBuilder.root(ara.getAlternativeRoots().getStringsForTokenIndex(x)[ax]);
        }
        // looks like we forgot to do alternatives for prefix-stem-suffix and for flags. So we're done.
        builder.addAnalysis(anBuilder.build());
    }

    private static void setupCommonAltAnalysis(AbstractResultAccess ara, int x, int ax, MorphoAnalysis.Builder anBuilder) {
        if (ara.getAlternativePartsOfSpeech() != null && ara.getAlternativePartsOfSpeech().getStringsForTokenIndex(x) != null) {
            anBuilder.partOfSpeech(ara.getAlternativePartsOfSpeech().getStringsForTokenIndex(x)[ax]);
        }
        if (ara.getAlternativeLemmas() != null && ara.getAlternativeLemmas().getStringsForTokenIndex(x) != null) {
            anBuilder.lemma(ara.getAlternativeLemmas().getStringsForTokenIndex(x)[ax]);
        }
        if (ara.getAlternativeCompounds() != null) {
            String[] comps = ara.getAlternativeCompounds().getStringsForTokenIndex(x);
            // these are delimited. And we don't know the delimiter.
            String delim = System.getProperty("bt.alt.comp.delim", "\uF8FF");
            if (comps != null) {
                String[] compsComps = comps[ax].split(delim);
                for (String compComp : compsComps) {
                    Token.Builder tokenBuilder = new Token.Builder(0, 0, compComp);
                    anBuilder.addComponent(tokenBuilder.build());
                }
            }
        }
    }

    private static MorphoAnalysis buildBaseAnalysis(AbstractResultAccess ara, int x) {
        MorphoAnalysis analysis;
        LanguageCode langCode = ara.getDetectedLanguage();
        if (langCode == null) {
            langCode = LanguageCode.UNKNOWN;
        }
        switch (langCode) {
        case ARABIC:
            analysis = buildBaseArabicAnalysis(ara, x);
            break;
        case JAPANESE:
        case CHINESE:
        case SIMPLIFIED_CHINESE:
        case TRADITIONAL_CHINESE:
            analysis = buildBaseHanAnalysis(ara, x);
            break;
        default:
            analysis = buildCommonAnalysis(ara, x);
            break;
        }
        return analysis;
    }

    private static MorphoAnalysis buildCommonAnalysis(AbstractResultAccess ara, int x) {
        MorphoAnalysis.Builder builder = new MorphoAnalysis.Builder();
        if (setupCommonBaseAnalysis(ara, x, builder)) {
            return builder.build();
        } else {
            return null;
        }
    }

    private static MorphoAnalysis buildBaseHanAnalysis(AbstractResultAccess ara, int x) {
        HanMorphoAnalysis.Builder builder = new HanMorphoAnalysis.Builder();
        setupCommonBaseAnalysis(ara, x, builder);
        if (ara.getReading() != null) {
            String[] readings = ara.getReading().getStringsForTokenIndex(x);
            if (readings != null) {
                for (String reading : readings) {
                    builder.addReading(reading);
                }
            }
        }
        return builder.build();
    }

    private static boolean setupCommonBaseAnalysis(AbstractResultAccess ara, int x, MorphoAnalysis.Builder builder) {
        boolean anythingHere = false;
        if (ara.getLemma() != null) {
            anythingHere = true;
            builder.lemma(ara.getLemma()[x]);
        }
        if (ara.getPartOfSpeech() != null) {
            anythingHere = true;
            builder.partOfSpeech(ara.getPartOfSpeech()[x]);
        }
        if (ara.getCompound() != null) {
            String[] comps = ara.getCompound().getStringsForTokenIndex(x);
            if (comps != null) {
                anythingHere = true;
                for (String comp : comps) {
                    Token.Builder tokenBuilder = new Token.Builder(0, 0, comp);
                    builder.addComponent(tokenBuilder.build());
                }
            }
        }
        return anythingHere;
    }

    private static MorphoAnalysis buildBaseArabicAnalysis(AbstractResultAccess ara, int x) {
        ArabicMorphoAnalysis.Builder builder = new ArabicMorphoAnalysis.Builder();
        setupCommonBaseAnalysis(ara, x, builder);
        builder.lengths(ara.getTokenPrefixStemLength()[x * 2], ara.getTokenPrefixStemLength()[(x * 2) + 1]);
        if (ara.getRoots() != null) {
            builder.root(ara.getRoots()[x]);
        }
        if (ara.getFlags() != null) {
            int flags = ara.getFlags()[x];
            builder.strippablePrefix((flags & ARBL_FEATURE_STRIPPABLE_PREFIX) != 0);
            builder.definiteArticle((flags & ARBL_FEATURE_DEFINITE_ARTICLE) != 0);
        }

        return builder.build();
    }

    private static Token buildOneToken(AbstractResultAccess ara, int x) {
        int start = ara.getTokenOffset()[x * 2];
        int end = ara.getTokenOffset()[(x * 2) + 1];
        Token.Builder builder = new Token.Builder(start, end, ara.getTokens()[x]);
        // todo, the arabic PSL stuff, or strings, more likely, for the bits.
        if (ara.getTokenVariations() != null) {
            String[] variations = ara.getTokenVariations().getStringsForTokenIndex(x);
            if (variations != null) {
                // no support yet to relocate variations into raw.
                builder.extendedProperty("TOKEN_VARIATIONS", variations);
            }
        }

        if (ara.getNormalizedToken() != null) {
            builder.addNormalized(ara.getNormalizedToken()[x]);
        }

        if (ara.getAlternativeNormalizedToken() != null) {
            String[] norms = ara.getAlternativeNormalizedToken().getStringsForTokenIndex(x);
            if (norms != null) {
                for (String norm : norms) {
                    builder.addNormalized(norm);
                }
            }
        }

        buildAnalysisList(ara, x, builder);

        if (ara.getTokenSourceName() != null) {
            builder.source(ara.getTokenSourceName()[x]);
        }

        return builder.build();
    }
}
