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

import com.basistech.rosette.dm.AnnotatedDataModelModule;
import com.basistech.rosette.dm.AnnotatedText;
import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import org.apache.commons.io.output.NullOutputStream;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import static com.codahale.metrics.MetricRegistry.name;

/**
 * Created by benson on 9/26/14.
 */
public class AdmJsonBenchmark {
    static final MetricRegistry METRICS = new MetricRegistry();
    private final Timer read = METRICS.timer(name(AdmJsonBenchmark.class, "read"));
    private final Timer write = METRICS.timer(name(AdmJsonBenchmark.class, "write"));
    private ConsoleReporter reporter;

    public static void main(String[] args) throws IOException {
        boolean afterburner = args.length == 2 && "-afterburner".equals(args[1]);
        new AdmJsonBenchmark().go(new File(args[0]), afterburner);
    }

    private void go(File input, boolean afterburner) throws IOException {
        startReport();
        SmileFactory smileFactory = new SmileFactory();
        ObjectMapper mapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper(smileFactory));
        if (afterburner) {
            mapper.registerModule(new AfterburnerModule());
        }
        JsonParser jp = smileFactory.createParser(input);
        jp.setCodec(mapper);

        JsonToken current;
        current = jp.nextToken();
        if (current != JsonToken.START_ARRAY) {
            System.err.println("Error: root should be array: quiting.");
            return;
        }

        while (jp.nextToken() != JsonToken.END_ARRAY) {
            // pointing at an ADM
            AnnotatedText text;

            final Timer.Context readContext = read.time();
            try {
                text = jp.readValueAs(AnnotatedText.class);
            } finally {
                readContext.stop();
            }

            OutputStream dump = new NullOutputStream();

            // write out one big file?
            ObjectMapper outMapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper(smileFactory));

            final Timer.Context writeContext = write.time();
            try {
                outMapper.writeValue(dump, text);
            } finally {
                writeContext.stop();
            }

        }
        reporter.report();
        reporter.close();
    }

    void startReport() {
        reporter = ConsoleReporter.forRegistry(METRICS)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(1, TimeUnit.SECONDS);
    }
}
