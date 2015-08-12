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

package com.basistech.dm.osgitest;

import com.basistech.osgi.testsupport.OsgiTestUtil;
import com.basistech.osgi.testsupport.paxfixes.LoggingContainerFactory;
import com.basistech.rosette.dm.LanguageDetection;
import com.basistech.util.LanguageCode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import java.io.File;

/**
 * IT to show that the OSGi bundle works somewhat.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
@ExamFactory(LoggingContainerFactory.class)
public class BundleIT {

    @Configuration
    public Option[] config() {
        String projectBuildDirectory = System.getProperty("project.build.directory");
        String projectVersion = System.getProperty("project.version");

        return OsgiTestUtil.standardPaxExamConfig(new File(projectBuildDirectory),
                projectVersion,
                "adm-model-osgi",
                false);
    }

    @Test
    public void modelWorksAtAll() {
        // the code in here will blow up if we can't talk to 'LanguageCode' which comes from another bundle.
        LanguageDetection.DetectionResult.Builder builder = new LanguageDetection.DetectionResult.Builder(LanguageCode.FINNISH);
        builder.build();
    }
}
