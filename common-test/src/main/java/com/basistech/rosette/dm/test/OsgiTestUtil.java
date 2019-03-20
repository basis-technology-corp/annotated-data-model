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

package com.basistech.rosette.dm.test;

import com.basistech.rosette.RosetteRuntimeException;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import org.ops4j.pax.exam.Option;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.provision;
import static org.ops4j.pax.exam.CoreOptions.systemPackages;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;
import static org.ops4j.pax.exam.CoreOptions.url;
import static org.ops4j.pax.exam.CoreOptions.when;

/**
 * Reusable test code.
 */
public final class OsgiTestUtil {
    private OsgiTestUtil() {
        //
    }
    /**
     * Assume that a test has a dependencies.properties in the classpath, and use it to retrieve the version.
     * @param groupId maven group id
     * @param artifactId maven artifact id
     * @return the version string
     */
    public static String getDependencyVersion(String groupId, String artifactId) {
        URL depPropsUrl = Resources.getResource("META-INF/maven/dependencies.properties");
        Properties depProps = new Properties();
        try {
            depProps.load(Resources.asByteSource(depPropsUrl).openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String ver = (String)depProps.get(String.format("%s/%s/version", groupId, artifactId));
        if (ver == null) {
            throw new RosetteRuntimeException(String.format("No version available for %s:%s", groupId, artifactId));
        }
        ver = ver.replace("-SNAPSHOT", ".SNAPSHOT");
        return ver;
    }

    /**
     * This method builds an array of pax-exam configuration objects that form the common
     * configuration of Rosette OSGi bundle tests. A test's @Configuration method
     * may simply return these, or add others if needed.
     * @param projectBuildDirectory ${project.build.directory} -- this locates the main project bundle
     *                        in here and the dependency bundles in the 'bundles' subdirectory.
     * @param projectVersion ${project.version}
     * @param projectBaseBundleJarName The jar containing this project's bundle file name base, or null if
     *                                 this project is just testing other bundles. This method builds
     *                                 projectBuildDirectory/THIS-VERSION.jar
     * @param useFragmentBundle if true, expect there to be -root-fragment bundle in projectBuildDirectory, and install it.
     * @return
     */
    public static Option[] standardPaxExamConfig(File projectBuildDirectory,
                                                 String projectVersion,
                                                 String projectBaseBundleJarName,
                                                 boolean useFragmentBundle) {

        List<String> bundleUrls = Lists.newArrayList();
        File bundleDir = new File(projectBuildDirectory, "bundles");
        File[] bundleFiles = bundleDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        });
        for (File bundleFile : bundleFiles) {
            try {
                bundleUrls.add(bundleFile.toURI().toURL().toExternalForm());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        if (projectBaseBundleJarName != null) {
            bundleUrls.add(String.format("file:%s/%s-%s.jar", projectBuildDirectory, projectBaseBundleJarName, projectVersion));
        }

        String[] bundles = bundleUrls.toArray(new String[0]);

        String paxLoggingLevel = System.getProperty("bt.osgi.pax.logging.level", "WARN");

        return options(
                provision(bundles),
                when(useFragmentBundle).useOptions(url(String.format("file:%s/%s-%s-fragment-bundle.jar", projectBuildDirectory, projectBaseBundleJarName, projectVersion)).noStart()),
                systemPackages(
                        // These two are needed for guava.
                        "sun.misc",
                        "javax.annotation",
                        String.format("org.slf4j;version=\"%s\"", getDependencyVersion("org.slf4j", "slf4j-api"))

                ),
                junitBundles(),
                systemProperty("pax.exam.osgi.unresolved.fail").value("true"),
                systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value(paxLoggingLevel)
        );
    }
}
