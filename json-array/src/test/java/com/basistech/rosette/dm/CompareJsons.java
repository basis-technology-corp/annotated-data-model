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
import org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic;
import org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.xerial.snappy.Snappy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;

/**
 * Quick command line to see what we've achieved.
 */
public final class CompareJsons {

    static class CompressionStats extends AbstractStorelessUnivariateStatistic {
        private final Mean mean;
        private final StandardDeviation standardDeviation;

        CompressionStats() {
            mean = new Mean();
            standardDeviation = new StandardDeviation();
        }

        @Override
        public StorelessUnivariateStatistic copy() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            mean.clear();
            standardDeviation.clear();

        }

        @Override
        public double getResult() {
            throw new UnsupportedOperationException();
        }

        @Override
        public long getN() {
            return mean.getN();
        }

        @Override
        public void increment(double d) {
            mean.increment(d);
            standardDeviation.increment(d);
        }

        double getMean() {
            return mean.getResult();
        }

        double getStandardDeviation() {
            return standardDeviation.getResult();
        }

    }

    private CompareJsons() {
        //
    }

    public static void main(String[] args) throws Exception {
        File plenty = new File(args[0]);
        System.out.println(String.format("Original file length %d", plenty.length()));
        ObjectMapper inputMapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());
        AnnotatedText[] texts = inputMapper.readValue(plenty, AnnotatedText[].class);
        System.out.println(String.format("%d documents", texts.length));
        runWithFormat(texts, new MappingJsonFactory(), "Plain");
        runWithFormat(texts, new SmileFactory(), "SMILE");
        runWithFormat(texts, new CBORFactory(), "CBOR");
    }

    private static void runWithFormat(AnnotatedText[] texts, JsonFactory factory, String format) throws JsonProcessingException {

        MetricRegistry metrics = new MetricRegistry();
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();

        // 'normal' means 'classic json textual format', as opposed to array.
        ObjectMapper normalMapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper(factory));
        ObjectMapper arrayMapper = AnnotatedDataModelArrayModule.setupObjectMapper(new ObjectMapper(factory));

        // times with Metrics
        Timer normalSerialTime = metrics.timer(String.format("%s-normal-serial", format));
        Timer arraySerialTime = metrics.timer(String.format("%s-array-serial", format));

        CompressionStats normalSizeStats = new CompressionStats();
        CompressionStats normalGzipStats = new CompressionStats();
        CompressionStats normalSnappyStats = new CompressionStats();

        CompressionStats arraySizeStats = new CompressionStats();
        CompressionStats arrayGzipStats = new CompressionStats();
        CompressionStats arraySnappyStats = new CompressionStats();

        Timer normalGzipCompressTime = metrics.timer(String.format("%s-gzip-compress-normal", format));
        Timer normalSnappyCompressTime = metrics.timer(String.format("%s-snappy-compress-normal", format));
        Timer arrayGzipCompressTime = metrics.timer(String.format("%s-gzip-compress-array", format));
        Timer arraySnappyCompressTime = metrics.timer(String.format("%s-snappy-compress-array", format));

        for (AnnotatedText text : texts) {
            // text and array time and size
            Timer.Context ctxt = normalSerialTime.time();
            byte[] textJson = normalMapper.writeValueAsBytes(text);
            ctxt.stop();
            normalSizeStats.increment(textJson.length);

            ctxt = arraySerialTime.time();
            byte[] arrayJson = arrayMapper.writeValueAsBytes(text);
            ctxt.stop();
            arraySizeStats.increment(arrayJson.length);

            // gzip time and space
            ctxt = normalGzipCompressTime.time();
            int compLen = gzipCompressedLength(textJson);
            ctxt.stop();
            normalGzipStats.increment(compressionRatio(textJson.length, compLen));

            ctxt = arrayGzipCompressTime.time();
            compLen = gzipCompressedLength(arrayJson);
            ctxt.stop();
            arrayGzipStats.increment(compressionRatio(arrayJson.length, compLen));

            // snappy time and space
            ctxt = normalSnappyCompressTime.time();
            compLen = snappyCompressedLength(textJson);
            ctxt.stop();
            normalSnappyStats.increment(compressionRatio(textJson.length, compLen));

            ctxt = arraySnappyCompressTime.time();
            compLen = snappyCompressedLength(arrayJson);
            ctxt.stop();
            arraySnappyStats.increment(compressionRatio(arrayJson.length, compLen));
        }

        System.out.println("\nStatistics for " + format);
        System.out.println();
        System.out.format("Normal Size: mean %.2f stddev %.2f\n", normalSizeStats.getMean(), normalSizeStats.getStandardDeviation());
        System.out.format("Normal GZIP Compression ratio: mean %.2f stddev %.2f\n", normalGzipStats.getMean(), normalGzipStats.getStandardDeviation());
        System.out.format("Normal Snappy Compression ratio: mean %.2f stddev %.2f\n", normalSnappyStats.getMean(), normalSnappyStats.getStandardDeviation());

        System.out.format("Array Size: mean %.2f stddev %.2f\n", arraySizeStats.getMean(), arraySizeStats.getStandardDeviation());
        System.out.format("Array GZIP Compression ratio: mean %.2f stddev %.2f\n", arrayGzipStats.getMean(), arrayGzipStats.getStandardDeviation());
        System.out.format("Array Snappy Compression ratio: mean %.2f stddev %.2f\n", arraySnappyStats.getMean(), arraySnappyStats.getStandardDeviation());
        System.out.println();
        reporter.report();
        reporter.stop();

    }

    private static double compressionRatio(int uncompressed, int compressed) {
        return ((double)(uncompressed - compressed)) / (double)uncompressed;
    }

    private static int gzipCompressedLength(byte[] data) {
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
    
    private static int snappyCompressedLength(byte[] data) {
        try {
            return Snappy.compress(data).length;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
