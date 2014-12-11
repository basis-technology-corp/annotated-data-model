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

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.google.common.io.ByteSource;
import com.google.common.io.ByteStreams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;

/**
 * Quick command line to see what we've achieved.
 */
public final class CompareJsons2 {
    static final MetricRegistry METRICS = new MetricRegistry();
    static ConsoleReporter reporter;

    private CompareJsons2() {
        //
    }

    public static void main(String[] args) throws Exception {
        startReport();
        File plenty = new File(args[0]);
        String mapping = args[1];
        SimpleModule module;
        if ("classic".equals(mapping)) {
            module = new AnnotatedDataModelModule();
        } else if ("array".equals(mapping)) {
            module = new AnnotatedDataModelArrayModule();
        } else {
            System.err.println("Invalid mapping " + mapping);
            System.exit(1);
            return;
        }


        System.out.println(String.format("Original file length %d", plenty.length()));
        ObjectMapper inputMapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());
        AnnotatedText[] texts = inputMapper.readValue(plenty, AnnotatedText[].class);
        System.out.println(String.format("%d documents", texts.length));
        runWithFormat(texts, module, new MappingJsonFactory(), "Plain");
        runWithFormat(texts, module, new SmileFactory(), "SMILE");
        runWithFormat(texts, module, new CBORFactory(), "CBOR");
        reporter.report();
        reporter.stop();
    }

    private static void runWithFormat(AnnotatedText[] texts, SimpleModule module, JsonFactory factory, String format) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(factory);
        mapper.registerModule(module);

        int totalLength = 0;
        int totalCompressedLength = 0;

        Timer time = METRICS.timer(String.format("%s", format));

        Timer compressTime = METRICS.timer(String.format("%s-compression", format));

        for (AnnotatedText text : texts) {
            Timer.Context ctxt = time.time();
            byte[] results = mapper.writeValueAsBytes(text);
            ctxt.stop();

            System.out.println(mapper.writeValueAsString(text));

            totalLength += results.length;

            ctxt = compressTime.time();
            totalCompressedLength += compressedLength(results);
            ctxt.stop();
        }

        System.out.println(format + ":");
        System.out.println(String.format("Length %d", totalLength));
        System.out.println("Compressed " + format + ":");
        System.out.println(String.format("Length %d", totalCompressedLength));
    }

    private static int compressedLength(byte[] data) {
        ByteSource dataSource = ByteSource.wrap(data);
        ByteArrayOutputStream sink = new ByteArrayOutputStream();
        try {
            GZIPOutputStream compressedStream = new GZIPOutputStream(sink);
            ByteStreams.copy(dataSource.openBufferedStream(), compressedStream);
            return sink.toByteArray().length;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void startReport() {
        reporter = ConsoleReporter.forRegistry(METRICS)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
    }
}
