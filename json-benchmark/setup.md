I downloaded the contents of:

com.basistech.raas:perf-test-mongo-data:3:tar.bz2, and unpacked it.

I ran mongodump on it with --directoryperdb.

That got me a giant file of json.

I ran com.basistech.adm.tools.ExtractAdmsFromMongoDump to turn it into a giant file of smile, just containing the ADMs. (767 MB of data).

The idea of the benchmark is to time reading that file and writing that file, using Jackson streaming to process one adm at a time, with and without afterburner.

