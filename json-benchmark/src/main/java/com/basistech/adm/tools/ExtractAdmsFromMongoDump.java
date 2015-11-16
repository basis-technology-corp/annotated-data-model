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

package com.basistech.adm.tools;

import com.basistech.rosette.dm.jackson.AnnotatedDataModelModule;
import com.basistech.rosette.dm.AnnotatedText;
import com.basistech.rosette.dm.BaseAttribute;
import com.basistech.rosette.dm.ListAttribute;
import com.basistech.rosette.dm.MorphoAnalysis;
import com.basistech.rosette.dm.Token;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.ByteSink;
import com.google.common.io.CharSource;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * The RaaS team made a mongo db with ~7000 adms. We want to pull the
 * adms out from the rest of the mongo docs for use in benchmarks.
 * This runs with -Xmx10g. It might run with less.
 *
 */
public final class ExtractAdmsFromMongoDump {
    private ExtractAdmsFromMongoDump() {
        //
    }
    static final class MongoDoc {
        @JsonProperty
        AnnotatedText resAnnotatedText;

        @JsonAnySetter
        public void handleUnknown(String key, Object value) {
            // ignore
        }
    }

    public static void main(String[] args) throws Exception {
        File input = new File(args[0]);
        CharSource charSource = Files.asCharSource(input, Charsets.UTF_8);
        // see if it all just fits into memory...
        final List<AnnotatedText> texts = Lists.newArrayList();
        final ObjectMapper mapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());

        charSource.readLines(new VoidLineProcessor(mapper, texts));

        SmileFactory smileFactory = new SmileFactory();
        // write out one big file.
        ObjectMapper outMapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper(smileFactory));
        ByteSink sink = Files.asByteSink(new File(args[1]));
        OutputStream outputStream = null;
        try {
            outputStream = sink.openBufferedStream();
            outMapper.writeValue(outputStream, texts);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

    private static class VoidLineProcessor implements LineProcessor<Void> {
        private final ObjectMapper mapper;
        private final List<AnnotatedText> texts;

        VoidLineProcessor(ObjectMapper mapper, List<AnnotatedText> texts) {
            this.mapper = mapper;
            this.texts = texts;
        }

        @Override
        public boolean processLine(String line) throws IOException {
            final AnnotatedText text = mapper.readValue(line, MongoDoc.class).resAnnotatedText;
            // we need to eliminate 'variations' from all the tokens in here, or else we spend a lot of time on stupid overhead.
            AnnotatedText.Builder noVariationBuilder = new AnnotatedText.Builder();
            noVariationBuilder.data(text.getData());
            noVariationBuilder.documentMetadata().putAll(text.getDocumentMetadata());
            for (Map.Entry<String, BaseAttribute> me : text.getAttributes().entrySet()) {
                if ("token".equals(me.getKey())) {
                    noVariationBuilder.tokens(stripTokens(me.getValue()));
                } else {
                    noVariationBuilder.attributes().put(me.getKey(), me.getValue());
                }
            }


            texts.add(noVariationBuilder.build());
            return true;
        }

        @SuppressWarnings("unchecked")
        private ListAttribute<Token> stripTokens(BaseAttribute value) {
            List<Token> tokens = (List<Token>)value;
            ListAttribute.Builder<Token> listBuilder = new ListAttribute.Builder<>(Token.class);
            for (Token token : tokens) {
                Token.Builder tokenBuilder = new Token.Builder(token.getStartOffset(), token.getEndOffset(), token.getText());
                if (token.getAnalyses() != null) {
                    for (MorphoAnalysis ma : token.getAnalyses()) {
                        tokenBuilder.addAnalysis(ma);
                    }
                }
                if (token.getNormalized() != null) {
                    for (String normalized : token.getNormalized()) {
                        tokenBuilder.addNormalized(normalized);
                    }
                }
                if (token.getSource() != null) {
                    tokenBuilder.source(token.getSource());
                }
                // ignore extended properties!
                listBuilder.add(tokenBuilder.build());
            }
            return listBuilder.build();
        }

        @Override
        public Void getResult() {
            return null;
        }
    }
}
