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
import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import static com.codahale.metrics.MetricRegistry.name;

/**
 * A simple benchmark to compare json alternatives.
 */
public final class AdmJsonBenchmark {
    private AdmJsonBenchmark() {
        //
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Usage: AdmJsonBenchmark DATA_FILE [-smile] [-afterburner]");
            return;
        }
        File input = new File(args[0]);
        boolean smile = false;
        boolean afterburner = false;
        for (int argx = 1; argx < args.length; argx++) {
            if ("-smile".equals(args[argx])) {
                smile = true;
            } else if ("-afterburner".equals(args[argx])) {
                afterburner = true;
            } else {
                System.err.println("Invalid argument " + args[argx]);
                return;
            }
        }

        new AdmJsonBenchmark().go(input, afterburner, smile);
    }

    private void go(File input, boolean afterburner, boolean smile) throws IOException {
        // new metrics for each case.
        MetricRegistry metrics = new MetricRegistry();
        final Timer read = metrics.timer(name(AdmJsonBenchmark.class, "read"));
        final Timer write = metrics.timer(name(AdmJsonBenchmark.class, "write"));
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(60, TimeUnit.SECONDS);
        System.out.printf("%s %s %s\n", input.getAbsolutePath(), afterburner ? " afterburner" : "", smile ? " smile" : " json");

        // Use a different mapper for reading from the file and measuring.

        JsonFactory measureFactory;
        if (smile) {
            measureFactory = new SmileFactory();
        } else {
            measureFactory = new JsonFactory();
        }

        ObjectMapper measureMapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper(measureFactory));
        if (afterburner) {
            measureMapper.registerModule(new AfterburnerModule());
        }

        // now set up a mapper just for reading the reference file.
        SmileFactory smileFactory = new SmileFactory();
        ObjectMapper mapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper(smileFactory));
        mapper.registerModule(new AfterburnerModule()); // why not?
        JsonParser jp = smileFactory.createParser(input);
        jp.setCodec(mapper);

        JsonToken current;
        current = jp.nextToken();
        if (current != JsonToken.START_ARRAY) {
            System.err.println("Error: root should be array: quiting.");
            return;
        }

        while (jp.nextToken() != JsonToken.END_ARRAY) {
            // pointing at an ADM; just read it in.
            AnnotatedText text = jp.readValueAs(AnnotatedText.class);

            ByteArrayOutputStream dump = new ByteArrayOutputStream();

            final Timer.Context writeContext = write.time();
            try {
                // time the write
                measureMapper.writeValue(dump, text);
            } finally {
                writeContext.stop();
            }

            // now time a read from json.
            InputStream inputStream = new ByteArrayInputStream(dump.toByteArray());
            final Timer.Context readContext = read.time();
            try {
                // read back to measure.
                measureMapper.readValue(inputStream, AnnotatedText.class);
            } finally {
                readContext.stop();
            }
        }
        reporter.report();
        reporter.close();
    }
}
