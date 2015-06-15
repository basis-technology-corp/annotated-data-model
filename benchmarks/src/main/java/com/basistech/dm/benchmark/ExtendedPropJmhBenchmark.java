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

package com.basistech.dm.benchmark;

import com.basistech.rosette.dm.ListAttribute;
import com.basistech.rosette.dm.Token;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 *
 */
//CHECKSTYLE:OFF
public class ExtendedPropJmhBenchmark {

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ExtendedPropJmhBenchmark.class.getSimpleName())
                .warmupIterations(5)
                .measurementIterations(5)
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public ListAttribute<Token> noExtprops() {
        ListAttribute.Builder<Token> tlBuilder;

        tlBuilder = new ListAttribute.Builder<Token>(Token.class);

        for (int x = 0; x < 1000; x++) {
            Token.Builder builder = new Token.Builder(x, x + 10, "Mumble");
            tlBuilder.add(builder.build());
        }
        return tlBuilder.build();
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public ListAttribute<Token> noExtpropsCopy() {
        ListAttribute.Builder<Token> tlBuilder;

        tlBuilder = new ListAttribute.Builder<Token>(Token.class);

        for (int x = 0; x < 1000; x++) {
            Token.Builder builder = new Token.Builder(x, x + 10, "Mumble");
            Token.Builder b2 = new Token.Builder(builder.build());
            tlBuilder.add(b2.build());
        }
        return tlBuilder.build();
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public ListAttribute<Token> extpropsCopy() {
        ListAttribute.Builder<Token> tlBuilder;
        tlBuilder = new ListAttribute.Builder<Token>(Token.class);

        for (int x = 0; x < 1000; x++) {
            Token.Builder builder = new Token.Builder(x, x + 10, "Mumble");
            builder.extendedProperty("extProp", "blah");
            builder.extendedProperty("extProp2", "bleep");

            Token.Builder b2 = new Token.Builder(builder.build());
            tlBuilder.add(b2.build());
        }
        return tlBuilder.build();
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public ListAttribute<Token> extprops() {
        ListAttribute.Builder<Token> tlBuilder = new ListAttribute.Builder<Token>(Token.class);

        for (int x = 0; x < 1000; x++) {
            Token.Builder builder = new Token.Builder(x, x + 10, "Mumble");
            builder.extendedProperty("extProp", "blah");
            builder.extendedProperty("extProp2", "bleep");
            tlBuilder.add(builder.build());
        }
        return tlBuilder.build();
    }
}
