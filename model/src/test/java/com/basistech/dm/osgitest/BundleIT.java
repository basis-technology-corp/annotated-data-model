/*
* Copyright 2014 Basis Technology Corp.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.basistech.dm.osgitest;

import com.basistech.rosette.dm.LanguageDetection;
import com.basistech.util.LanguageCode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
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
public class BundleIT {

    @Configuration
    public Option[] config() {
        String projectBuildDirectory = System.getProperty("project.build.directory");
        String projectVersion = System.getProperty("project.version");

        return OsgiTestUtil.standardPaxExamConfig(new File(projectBuildDirectory),
                projectVersion,
                "adm-model",
                false);
    }

    @Test
    public void modelWorksAtAll() {
        // the code in here will blow up if we can't talk to 'LanguageCode' which comes from another bundle.
        LanguageDetection.DetectionResult.Builder builder = new LanguageDetection.DetectionResult.Builder(LanguageCode.FINNISH);
        builder.build();
    }
}
