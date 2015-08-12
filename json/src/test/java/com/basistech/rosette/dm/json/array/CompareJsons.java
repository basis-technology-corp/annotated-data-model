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

package com.basistech.rosette.dm.json.array;

import com.basistech.rosette.dm.AnnotatedText;
import com.basistech.rosette.dm.jackson.AnnotatedDataModelModule;
import com.basistech.rosette.dm.jackson.array.AnnotatedDataModelArrayModule;
import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.google.common.io.ByteStreams;
import org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic;
import org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.xerial.snappy.Snappy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
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

    interface FactoryFactory {
        JsonFactory newFactory();
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
        runWithFormat(texts, new FactoryFactory() {
            @Override
            public JsonFactory newFactory() {
                return new JsonFactory();
            }
        }, "Plain");
        runWithFormat(texts, new FactoryFactory() {
                    @Override
                    public JsonFactory newFactory() {
                        return new SmileFactory();
                    }
                },
                "SMILE");

        runWithFormat(texts, new FactoryFactory() {
                    @Override
                    public JsonFactory newFactory() {
                        return new CBORFactory();
                    }
                },
                "CBOR");
    }

    private static void runWithFormat(AnnotatedText[] texts, FactoryFactory factoryFactory, String format) throws IOException {

        MetricRegistry metrics = new MetricRegistry();
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();

        // 'normal' means 'classic json textual format', as opposed to array.
        ObjectMapper normalMapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper(factoryFactory.newFactory()));
        ObjectMapper arrayMapper = AnnotatedDataModelArrayModule.setupObjectMapper(new ObjectMapper(factoryFactory.newFactory()));

        // times with Metrics
        Timer normalSerialTime = metrics.timer(String.format("%s-normal-serial", format));
        Timer arraySerialTime = metrics.timer(String.format("%s-array-serial", format));
        Timer normalDeserialTime = metrics.timer(String.format("%s-normal-deserial", format));
        Timer arrayDeserialTime = metrics.timer(String.format("%s-array-deserial", format));

        CompressionStats normalSizeStats = new CompressionStats();
        CompressionStats normalGzipStats = new CompressionStats();
        CompressionStats normalSnappyStats = new CompressionStats();

        CompressionStats arraySizeStats = new CompressionStats();
        CompressionStats arrayGzipStats = new CompressionStats();
        CompressionStats arraySnappyStats = new CompressionStats();

        Timer normalGzipCompressTime = metrics.timer(String.format("%s-gzip-compress-normal", format));
        Timer normalGzipDecompressTime = metrics.timer(String.format("%s-gzip-decompress-normal", format));
        Timer normalSnappyCompressTime = metrics.timer(String.format("%s-snappy-compress-normal", format));
        Timer normalSnappyDecompressTime = metrics.timer(String.format("%s-snappy-decompress-normal", format));
        Timer arrayGzipCompressTime = metrics.timer(String.format("%s-gzip-compress-array", format));
        Timer arrayGzipDecompressTime = metrics.timer(String.format("%s-gzip-decompress-array", format));
        Timer arraySnappyCompressTime = metrics.timer(String.format("%s-snappy-compress-array", format));
        Timer arraySnappyDecompressTime = metrics.timer(String.format("%s-snappy-decompress-array", format));

        for (AnnotatedText text : texts) {
            // text and array time and size
            Timer.Context ctxt = normalSerialTime.time();
            byte[] textJson = normalMapper.writeValueAsBytes(text);
            ctxt.stop();
            normalSizeStats.increment(textJson.length);

            // Time reading it back ...
            ctxt = normalDeserialTime.time();
            normalMapper.readValue(textJson, AnnotatedText.class);
            ctxt.stop();

            ctxt = arraySerialTime.time();
            byte[] arrayJson = arrayMapper.writeValueAsBytes(text);
            ctxt.stop();
            arraySizeStats.increment(arrayJson.length);

            // time reading it back
            ctxt = arrayDeserialTime.time();
            arrayMapper.readValue(arrayJson, AnnotatedText.class);
            ctxt.stop();

            // gzip time and space
            ctxt = normalGzipCompressTime.time();
            byte[] compressed = gzipCompress(textJson);
            int compLen = compressed.length;
            ctxt.stop();
            normalGzipStats.increment(compressionRatio(textJson.length, compLen));

            // decompression?
            ctxt = normalGzipDecompressTime.time();
            gzipDecompress(compressed);
            ctxt.stop();

            ctxt = arrayGzipCompressTime.time();
            compressed = gzipCompress(arrayJson);
            compLen = compressed.length;
            ctxt.stop();
            arrayGzipStats.increment(compressionRatio(arrayJson.length, compLen));

            // decompression?
            ctxt = arrayGzipDecompressTime.time();
            gzipDecompress(compressed);
            ctxt.stop();

            // snappy time and space
            ctxt = normalSnappyCompressTime.time();
            compressed = snappyCompress(textJson);
            compLen = compressed.length;
            ctxt.stop();
            normalSnappyStats.increment(compressionRatio(textJson.length, compLen));

            // decompression?
            ctxt = normalSnappyDecompressTime.time();
            snappyDecompress(compressed);
            ctxt.stop();


            ctxt = arraySnappyCompressTime.time();
            compressed = snappyCompress(arrayJson);
            compLen = compressed.length;
            ctxt.stop();
            arraySnappyStats.increment(compressionRatio(arrayJson.length, compLen));

            // decompression?
            ctxt = arraySnappyDecompressTime.time();
            snappyDecompress(compressed);
            ctxt.stop();
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
        return ((double) (uncompressed - compressed)) / (double) uncompressed;
    }

    private static byte[] gzipCompress(byte[] data) {
        ByteArrayOutputStream sink = new ByteArrayOutputStream();
        try {
            GZIPOutputStream compressedStream = new GZIPOutputStream(sink);
            ByteStreams.copy(new ByteArrayInputStream(data), compressedStream);
            compressedStream.close();
            return sink.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void gzipDecompress(byte[] data) {
        ByteArrayOutputStream sink = new ByteArrayOutputStream();
        try {
            GZIPInputStream compressedStream = new GZIPInputStream(new ByteArrayInputStream(data));
            ByteStreams.copy(compressedStream, sink);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static byte[] snappyCompress(byte[] data) {
        try {
            return Snappy.compress(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void snappyDecompress(byte[] data) {
        try {
            Snappy.uncompress(data);
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
