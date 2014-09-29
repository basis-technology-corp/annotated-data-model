#!/bin/sh

time java -cp target/classes:$(cat target/classpath.txt) com.basistech.adm.tools.AdmJsonBenchmark $1
time java -cp target/classes:$(cat target/classpath.txt) com.basistech.adm.tools.AdmJsonBenchmark $1 -afterburner
time java -cp target/classes:$(cat target/classpath.txt) com.basistech.adm.tools.AdmJsonBenchmark $1 -smile
time java -cp target/classes:$(cat target/classpath.txt) com.basistech.adm.tools.AdmJsonBenchmark $1 -smile -afterburner

