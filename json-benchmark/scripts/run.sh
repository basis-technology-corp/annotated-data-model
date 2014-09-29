#!/bin/sh

java -cp target/classes:$(cat target/classpath.txt) com.basistech.adm.tools.AdmJsonBenchmark $1
java -cp target/classes:$(cat target/classpath.txt) com.basistech.adm.tools.AdmJsonBenchmark $1 -afterburner
java -cp target/classes:$(cat target/classpath.txt) com.basistech.adm.tools.AdmJsonBenchmark $1 -smile
java -cp target/classes:$(cat target/classpath.txt) com.basistech.adm.tools.AdmJsonBenchmark $1 -smile -afterburner

