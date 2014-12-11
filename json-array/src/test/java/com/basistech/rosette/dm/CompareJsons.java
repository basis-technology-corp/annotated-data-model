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
public final class CompareJsons {
    static final MetricRegistry METRICS = new MetricRegistry();
    static ConsoleReporter reporter;

    private CompareJsons() {
        //
    }

    public static void main(String[] args) throws Exception {
        startReport();
        File plenty = new File(args[0]);
        System.out.println(String.format("Original file length %d", plenty.length()));
        ObjectMapper inputMapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());
        AnnotatedText[] texts = inputMapper.readValue(plenty, AnnotatedText[].class);
        System.out.println(String.format("%d documents", texts.length));
        runWithFormat(texts, new MappingJsonFactory(), "Plain");
        runWithFormat(texts, new SmileFactory(), "SMILE");
        runWithFormat(texts, new CBORFactory(), "CBOR");
        reporter.report();
        reporter.stop();
    }

    private static void runWithFormat(AnnotatedText[] texts, JsonFactory factory, String format) throws JsonProcessingException {
        ObjectMapper classicMapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper(factory));
        ObjectMapper arrayMapper = AnnotatedDataModelArrayModule.setupObjectMapper(new ObjectMapper(factory));

        int totalClassicLength = 0;
        int totalClassicCompressedLength = 0;
        int totalArrayLength = 0;
        int totalArrayCompressedLength = 0;

        Timer classicTime = METRICS.timer(String.format("%s-classic", format));
        Timer arrayTime = METRICS.timer(String.format("%s-array", format));

        Timer classicCompressTime = METRICS.timer(String.format("%s-compress-classic", format));
        Timer arrayCompressTime = METRICS.timer(String.format("%s-compress-array", format));

        for (AnnotatedText text : texts) {
            Timer.Context ctxt = classicTime.time();
            byte[] classic = classicMapper.writeValueAsBytes(text);
            ctxt.stop();

            ctxt = arrayTime.time();
            byte[] array = arrayMapper.writeValueAsBytes(text);
            ctxt.stop();

            System.out.println(arrayMapper.writeValueAsString(text));

            totalClassicLength += classic.length;

            ctxt = classicCompressTime.time();
            totalClassicCompressedLength += compressedLength(classic);
            ctxt.stop();

            totalArrayLength += array.length;

            ctxt = arrayCompressTime.time();
            totalArrayCompressedLength += compressedLength(array);
            ctxt.stop();
        }

        System.out.println(format + ":");
        System.out.println(String.format("Classic %d Array %d: %.02f", totalClassicLength, totalArrayLength,
                (float)(totalClassicLength - totalArrayLength) / totalClassicLength));
        System.out.println("Compressed " + format + ":");
        System.out.println(String.format("Classic %d Array %d: %.02f", totalClassicCompressedLength, totalArrayCompressedLength,
                (float)(totalClassicCompressedLength - totalArrayCompressedLength) / totalClassicCompressedLength));
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
