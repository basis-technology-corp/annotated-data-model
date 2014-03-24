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


package com.basistech.dm;

import com.basistech.rlp.AbstractResultAccess;
import com.basistech.rlp.ResultAccessDeserializer;
import com.basistech.rlp.ResultAccessSerializedFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.collect.Maps;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Convert an {@link com.basistech.rlp.AbstractResultAccess} to a {@link Text}.
 */
public final class AraDmConverter {

    private static final int ARBL_FEATURE_DEFINITE_ARTICLE = 1 << 1;
    private static final int ARBL_FEATURE_STRIPPABLE_PREFIX = 1 << 2;

    private AraDmConverter() {
        //
    }

    public static Text convert(AbstractResultAccess ara) {
        Text text = new Text(ara.getRawText(), 0, ara.getRawText().length, Maps.<String, String[]>newHashMap());
        // no provisions for original binary data, so no getRawBytes.
        /* an ARA by itself doesn't have language regions or detections;
         * The code for that is just in the RWS, so we'll ignore it at this prototype stage.
         */

        buildTokens(ara, text);
        buildEntities(ara, text);
        buildSentences(ara, text);

        return text;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: AraDmConverter ara.json dm.json");
            return;
        }

        ResultAccessDeserializer rad = new ResultAccessDeserializer();
        rad.setFormat(ResultAccessSerializedFormat.JSON);
        InputStream input = null;
        AbstractResultAccess ara;
        try {
            input = new FileInputStream(args[0]);
            ara = rad.deserializeAbstractResultAccess(input);
        } finally {
            IOUtils.closeQuietly(input);
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        writer.writeValue(new File(args[1]), convert(ara));
    }

    private static void buildSentences(AbstractResultAccess ara, Text text) {
        if (ara.getSentenceBoundaries() != null || ara.getTextBoundaries() != null) {
            SentenceBoundaries.Builder builder = new SentenceBoundaries.Builder();
            if (ara.getSentenceBoundaries() != null) {
                builder.tokenBoundaries(ara.getSentenceBoundaries());
            }
            if (ara.getTextBoundaries() != null) {
                builder.charBoundaries(ara.getTextBoundaries());
            }
            text.getAttributes().put(SentenceBoundaries.class.getName(), builder.build());
        }
    }

    private static void buildEntities(AbstractResultAccess ara, Text text) {
        int[] entities = ara.getNamedEntity();
        if (entities != null) {
            ListAttribute.Builder<EntityMention> entityListBuilder = new ListAttribute.Builder<EntityMention>(EntityMention.class);
            int namedEntityCount = entities.length / 3;
            for (int x = 0; x < namedEntityCount; x++) {
                entityListBuilder.add(buildOneEntity(ara, x));
            }
            text.getAttributes().put(EntityMention.class.getName(), entityListBuilder.build());
        }
    }

    private static EntityMention buildOneEntity(AbstractResultAccess ara, int x) {
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
            for (int tx = start; tx < end; tx++) {
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
            builder.source(ara.getNamedEntitySourceString()[x]);
        }
        return builder.build();
    }

    private static void buildTokens(AbstractResultAccess ara, Text text) {
        if (ara.getTokens() != null) {
            ListAttribute.Builder<Token> tokenListBuilder = new ListAttribute.Builder<Token>(Token.class);
            int tokenCount = ara.getTokens().length;

            for (int x = 0; x < tokenCount; x++) {
                tokenListBuilder.add(buildOneToken(ara, x));
            }
            text.getAttributes().put(Token.class.getName(), tokenListBuilder.build());
        }
    }

    private static void buildAnalysisList(AbstractResultAccess ara, int x, Token.Builder builder) {
        builder.addAnalysis(buildBaseAnalysis(ara, x));
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
        anBuilder.root(ara.getAlternativeRoots().getStringsForTokenIndex(x)[ax]);
        // looks like we forgot to do alternatives for prefix-stem-suffix and for flags. So we're done.
        builder.addAnalysis(anBuilder.build());
    }

    private static void setupCommonAltAnalysis(AbstractResultAccess ara, int x, int ax, MorphoAnalysis.Builder anBuilder) {
        anBuilder.partOfSpeech(ara.getAlternativePartsOfSpeech().getStringsForTokenIndex(x)[ax]);
        anBuilder.lemma(ara.getAlternativeLemmas().getStringsForTokenIndex(x)[ax]);
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
        switch (ara.getDetectedLanguage()) {
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
        setupCommonBaseAnalysis(ara, x, builder);
        return builder.build();
    }

    private static MorphoAnalysis buildBaseHanAnalysis(AbstractResultAccess ara, int x) {
        HanMorphoAnalysis.Builder builder = new HanMorphoAnalysis.Builder();
        setupCommonBaseAnalysis(ara, x, builder);
        String[] readings = ara.getReading().getStringsForTokenIndex(x);
        if (readings != null) {
            for (String reading : readings) {
                builder.addReading(reading);
            }
        }
        return builder.build();
    }

    private static void setupCommonBaseAnalysis(AbstractResultAccess ara, int x, MorphoAnalysis.Builder builder) {
        builder.lemma(ara.getLemma()[x]);
        builder.partOfSpeech(ara.getPartOfSpeech()[x]);
        if (ara.getCompound() != null) {
            String[] comps = ara.getCompound().getStringsForTokenIndex(x);
            if (comps != null) {
                for (String comp : comps) {
                    Token.Builder tokenBuilder = new Token.Builder(0, 0, comp);
                    builder.addComponent(tokenBuilder.build());
                }
            }
        }
    }

    private static MorphoAnalysis buildBaseArabicAnalysis(AbstractResultAccess ara, int x) {
        ArabicMorphoAnalysis.Builder builder = new ArabicMorphoAnalysis.Builder();
        setupCommonBaseAnalysis(ara, x, builder);
        builder.lengths(ara.getTokenPrefixStemLength()[x * 2], ara.getTokenPrefixStemLength()[(x * 2) + 1]);
        builder.root(ara.getRoots()[x]);
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
                for (String variation : variations) {
                    builder.addVariation(variation);
                }
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
