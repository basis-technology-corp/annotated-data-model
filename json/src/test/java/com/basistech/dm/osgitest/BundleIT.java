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
import com.basistech.rosette.dm.Name;
import com.basistech.rosette.dm.jackson.AnnotatedDataModelModule;
import com.basistech.util.ISO15924;
import com.basistech.util.LanguageCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import java.io.File;
import java.util.List;

/**
 * IT to show that the OSGi bundle works somewhat.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class BundleIT {

    private ObjectMapper objectMapper() {
        return AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());
    }

    @Configuration
    public Option[] config() {
        String projectBuildDirectory = System.getProperty("project.build.directory");
        String projectVersion = System.getProperty("project.version");

        return OsgiTestUtil.standardPaxExamConfig(new File(projectBuildDirectory),
                projectVersion,
                "adm-json",
                false);
    }

    @Test
    public void modelWorksAtAll() {
        // the code in here will blow up if we can't talk to 'LanguageCode' which comes from another bundle.
        LanguageDetection.DetectionResult.Builder builder = new LanguageDetection.DetectionResult.Builder(LanguageCode.FINNISH);
        builder.build();
    }

    @Test
    public void jsonWorksABit() throws Exception {
        List<Name> names = Lists.newArrayList();
        Name.Builder builder = new Name.Builder("Fred");
        names.add(builder.build());
        builder = new Name.Builder("George");
        builder.languageOfOrigin(LanguageCode.ENGLISH).script(ISO15924.Latn).languageOfUse(LanguageCode.FRENCH);
        names.add(builder.build());
        ObjectMapper mapper = objectMapper();
        String json = mapper.writeValueAsString(names);
        // one way to inspect the works is to read it back in _without_ our customized mapper.
        ObjectMapper plainMapper = new ObjectMapper();
        JsonNode tree = plainMapper.readTree(json);
        Assert.assertTrue(tree.isArray());
        Assert.assertEquals(2, tree.size());
        JsonNode node = tree.get(0);
        Assert.assertTrue(node.has("text"));
        Assert.assertEquals("Fred", node.get("text").asText());
        Assert.assertFalse(node.has("script"));
        Assert.assertFalse(node.has("languageOfOrigin"));
        Assert.assertFalse(node.has("languageOfUse"));

        List<Name> readBack = mapper.readValue(json, new TypeReference<List<Name>>() { });
        Assert.assertEquals(names, readBack);
    }
}
