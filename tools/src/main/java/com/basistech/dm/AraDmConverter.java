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
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Convert an {@link com.basistech.rlp.AbstractResultAccess} to a {@link Text}.
 */
public final class AraDmConverter {
    private AraDmConverter() {
        //
    }

    public static Text convert(AbstractResultAccess ara) {
        Text text = new Text(ara.getRawText(), 0, ara.getRawText().length);
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
        SentenceBoundaries.Builder builder = new SentenceBoundaries.Builder();
        if (ara.getSentenceBoundaries() != null) {
            builder.tokenBoundaries(ara.getSentenceBoundaries());
        }
        if (ara.getTextBoundaries() != null) {
            builder.charBoundaries(ara.getTextBoundaries());
        }
        text.getAttributes().put(SentenceBoundaries.class.getName(), builder.build());
    }

    private static void buildEntities(AbstractResultAccess ara, Text text) {
        ListAttribute.Builder<EntityMention> entityListBuilder = new ListAttribute.Builder<EntityMention>(EntityMention.class);
        int namedEntityCount = ara.getNamedEntitySourceString().length;

        for (int x = 0; x < namedEntityCount; x++) {
            entityListBuilder.add(buildOneEntity(ara, x));
        }
        text.getAttributes().put(EntityMention.class.getName(), entityListBuilder.build());
    }

    private static EntityMention buildOneEntity(AbstractResultAccess ara, int x) {
        int startToken = ara.getNamedEntity()[x * 3];
        int endToken = ara.getNamedEntity()[(x * 3) + 1];
        String entityType = ara.getNamedEntityTypeString()[x];

        int start = ara.getTokenOffset()[startToken * 2];
        int end = ara.getTokenOffset()[(endToken * 2) + 1];

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
        ListAttribute.Builder<Token> tokenListBuilder = new ListAttribute.Builder<Token>(Token.class);
        int tokenCount = ara.getTokens().length;

        for (int x = 0; x < tokenCount; x++) {
            tokenListBuilder.add(buildOneToken(ara, x));
        }
        text.getAttributes().put(Token.class.getName(), tokenListBuilder.build());
    }

    private static Token buildOneToken(AbstractResultAccess ara, int x) {
        int start = ara.getTokenOffset()[x * 2];
        int end = ara.getTokenOffset()[(x * 2) + 1];
        Token.Builder builder = new Token.Builder(start, end, ara.getTokens()[x]);
        // todo, the arabic PSL stuff, or strings, more likely, for the bits.
        if (ara.getTokenVariations() != null) {
            String[] variations = ara.getTokenVariations().getStringsForTokenIndex(x);
            if (variations != null) {
                for (String variation : variations) {
                    builder.addVariation(variation);
                }
            }
        }
        if (ara.getPartOfSpeech() != null) {
            builder.addPartOfSpeech(ara.getPartOfSpeech()[x]);
        }

        if (ara.getAlternativePartsOfSpeech() != null) {
            String[] parts = ara.getAlternativePartsOfSpeech().getStringsForTokenIndex(x);
            if (parts != null) {
                for (String part : parts) {
                    builder.addPartOfSpeech(part);
                }
            }
        }

        if (ara.getLemma() != null) {
            builder.addLemma(ara.getLemma()[x]);
        }
        if (ara.getAlternativeLemmas() != null) {
            String[] lemmas = ara.getAlternativeLemmas().getStringsForTokenIndex(x);
            if (lemmas != null) {
                for (String lemma : lemmas) {
                    builder.addLemma(lemma);
                }
            }
        }

        if (ara.getStem() != null) {
            builder.addStem(ara.getStem()[x]);
        }
        if (ara.getAlternativeStems() != null) {
            String[] stems = ara.getAlternativeStems().getStringsForTokenIndex(x);
            if (stems != null) {
                for (String stem : stems) {
                    builder.addLemma(stem);
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
        // TODO: Many-to-One, which may contain redundant data?
        // TODO: 'roots', not yet allowed for in token?
        if (ara.getReading() != null) {
            String[] readings = ara.getReading().getStringsForTokenIndex(x);
            if (readings != null) {
                // the ellipsis in newArrayList should accept the String[].
                List<String> readingsInList = Lists.newArrayList(readings);
                builder.addReadings(readingsInList);
            }
        }

        if (ara.getCompound() != null) {
            String[] comps = ara.getCompound().getStringsForTokenIndex(x);
            if (comps != null) {
                builder.addComponents(buildTokensForComps(comps, start, end));
            }
        }

        if (ara.getAlternativeCompounds() != null) {
            String[] encComps = ara.getAlternativeCompounds().getStringsForTokenIndex(x);
            if (encComps != null) {
                List<List<Token>> alts = buildTokensForAltComps(encComps, start, end);
                for (List<Token> alt : alts) {
                    builder.addComponents(alt);
                }
            }
        }

        if (ara.getTokenSourceName() != null) {
            builder.source(ara.getTokenSourceName()[x]);
        }

        return builder.build();
    }

    private static List<List<Token>> buildTokensForAltComps(String[] encComps, int start, int end) {
        List<List<Token>> tokenSets = Lists.newArrayList();
        for (String compSet : encComps) {
            List<Token> tokens = Lists.newArrayList();
            // hmm, we can't get the delim easily, and this is all for show at this point.
            String[] comps = compSet.split("\uf8ff");
            for (String comp : comps) {
                Token.Builder builder = new Token.Builder(start, end, comp);
                tokens.add(builder.build());
            }
            tokenSets.add(tokens);
        }
        return tokenSets;
    }

    private static List<Token> buildTokensForComps(String[] comps, int start, int end) {
        List<Token> tokens = Lists.newArrayList();
        for (String comp : comps) {
            Token.Builder builder = new Token.Builder(start, end, comp);
            tokens.add(builder.build());
        }
        return tokens;
    }

}
