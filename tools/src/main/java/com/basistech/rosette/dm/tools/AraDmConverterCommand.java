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

package com.basistech.rosette.dm.tools;

import com.basistech.rlp.AbstractResultAccess;
import com.basistech.rlp.ResultAccessDeserializer;
import com.basistech.rlp.ResultAccessSerializedFormat;
import com.basistech.rosette.dm.AnnotatedDataModelModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.io.Closeables;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * CLI for converting ARA to ADM.
 */
public final class AraDmConverterCommand {
    @Option(name = "-outputDirectory", metaVar = "OUTPUT_DIR", usage = "output directory")
    File outputDirectory;

    // note that in the simple case
    @Argument(required = true, usage = "input1 ... inputN (or) INPUT OUTPUT")
    List<File> inputs;

    AraDmConverterCommand() {
        //
    }

    public static void main(String[] args) throws Exception {

        AraDmConverterCommand that = new AraDmConverterCommand();
        CmdLineParser parser = new CmdLineParser(that);
        try {
            if (args.length == 0 || args.length == 1) {
                System.err.println("ara-dm-converter INPUT OUTPUT");
                System.err.println(" - or -");
                System.err.println("ara-dm-converter -outputDirectory OUTPUT_DIR Input1 ... InputN");
                parser.printUsage(System.err);
                return;
            }
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            // handling of wrong arguments
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            System.exit(1);
        }

        that.process();

    }

    void process() throws IOException {

        File twoArgOutput = null; // non-null implies two-arg case.


        if (outputDirectory == null) {
            if (inputs.size() == 2) {
                // support simple usage model.
                twoArgOutput = inputs.get(1);
                outputDirectory = null;
                inputs.remove(1); // get rid of the output
            } else {
                System.err.println("More than two inputs but no -outputDirectory.");
                System.exit(1);
                return;
            }
        }

        ResultAccessDeserializer rad = new ResultAccessDeserializer();
        rad.setFormat(ResultAccessSerializedFormat.JSON);
        ObjectMapper mapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();

        for (File input : inputs) {
            System.out.println("Processing " + input.getAbsolutePath());
            File output;
            if (twoArgOutput != null) {
                output = twoArgOutput;
            } else {
                output = new File(outputDirectory, input.getName());
            }
            InputStream inputStream = null;
            AbstractResultAccess ara;
            try {
                inputStream = new FileInputStream(input);
                ara = rad.deserializeAbstractResultAccess(inputStream);
                writer.writeValue(output, AraDmConverter.convert(ara));
            } finally {
                Closeables.close(inputStream, true);
            }
        }
    }
}
