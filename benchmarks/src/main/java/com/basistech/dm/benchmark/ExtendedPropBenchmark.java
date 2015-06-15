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

/**
 *
 */
public final class ExtendedPropBenchmark {
    private ExtendedPropBenchmark() {
        //
    }

    public static void main(String[] args) {
        for (int x = 0; x < 10; x++) {
            extpropsCopy(true);
        }
        extprops();
        extPropsCopyReal();
        noExtprops();
    }

    private static void extPropsCopyReal() {
        extpropsCopy(false);
    }

    private static void noExtprops() {
        long start;
        ListAttribute.Builder<Token> tlBuilder;
        long end;

        start = System.nanoTime();
        tlBuilder = new ListAttribute.Builder<Token>(Token.class);

        for (int x = 0; x < 1000000; x++) {
            Token.Builder builder = new Token.Builder(x, x + 10, Integer.toString(x));
            tlBuilder.add(builder.build());
        }
        end = System.nanoTime();
        System.out.format("Time without extprops      : %f milliseconds%n", (double)(end - start) / 1000);
    }

    private static void extpropsCopy(boolean quiet) {
        long start;
        ListAttribute.Builder<Token> tlBuilder;
        long end;

        start = System.nanoTime();
        tlBuilder = new ListAttribute.Builder<Token>(Token.class);

        for (int x = 0; x < 1000000; x++) {
            Token.Builder builder = new Token.Builder(x, x + 10, Integer.toString(x));
            builder.extendedProperty("extProp", "blah");
            builder.extendedProperty("extProp2", "bleep");

            Token.Builder b2 = new Token.Builder(builder.build());
            tlBuilder.add(b2.build());
        }
        end = System.nanoTime();
        if (!quiet) {
            System.out.format("Time with extprops and copy: %f milliseconds%n", (double) (end - start) / 1000);
        }
    }

    private static void extprops() {
        long start = System.nanoTime();
        ListAttribute.Builder<Token> tlBuilder = new ListAttribute.Builder<Token>(Token.class);

        for (int x = 0; x < 1000000; x++) {
            Token.Builder builder = new Token.Builder(x, x + 10, Integer.toString(x));
            builder.extendedProperty("extProp", "blah");
            builder.extendedProperty("extProp2", "bleep");
            tlBuilder.add(builder.build());
        }
        long end = System.nanoTime();
        System.out.format("Time with extprops         : %f milliseconds%n", (double)(end - start) / 1000);
    }
}
