#! /bin/sh
#
# Run a java class with a main method by using classpath.txt files
# generate from the maven build.  For example:
#
# ./run-java.sh com.basistech.rosette.dm.tools.AraDmConverter
#
# This is often more convenient than running with maven exec:java or
# appassembler.

script_dir=$(dirname $0)

CP=${script_dir}:$(cat ${script_dir}/target/classpath.txt)

# The -Dfile.encoding=UTF-8 is for mac, where the default encoding is
# MacRoman *even* if your locale is UTF-8.  The mac/jvm folks, for
# compatibility, think that they should not listen to the system
# locale for the encoding.  Another way around this is to set
# JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8 in the environment, but then
# all your java tools will echo an annoying line saying "Picked up
# JAVA_TOOL_OPTIONS: .."
java ${JVM_ARGS} -Dfile.encoding=UTF-8 -cp "${CP}" "$@"
