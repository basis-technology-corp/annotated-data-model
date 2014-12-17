# Cumulative Release Notes for the Annotated Data Model

## 1.10.2

### [COMN-158](http://jira.basistech.net/browse/COMN-158) Support array "shape" json

The new adm-json-array artifact supports serialization of ADM as
array-shaped json.  There is some space savings and some possible
runtime savings, however components should never store this format.
It's strictly for use "over the wire" - intended for RaaS.

To use normal ADM "object" shape serialization, do this:

    ObjectMapper mapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());

To use the new "array" shape serialization, do this:

    ObjectMapper mapper = AnnotatedDataModelArrayModule.setupObjectMapper(new ObjectMapper());

## 1.10.1

### [COMN-142](http://jira.basistech.net/browse/COMN-142) Boxed Integer/Double consistency

ResolvedEntity coreferenceChainId was changed from int to Integer.
ResolvedEntity confidence was changed from double to Double.

Clients may have to adjust code on ADM boundaries if they are dealing
with -1 chainIds, and they may need to add checks for null.

### [COMN-140](http://jira.basistech.net/browse/COMN-140) AraDmConverter - separate api/cli

AraDmConverter#main has been moved to a separate class [AraDmConverterCommand](https://git.basistech.net/textanalytics/annotated-data-model/blob/6e469a572bdf3991280c11fee1c5ace204aa6d0b/tools/src/main/java/com/basistech/rosette/dm/tools/AraDmConverterCommand.java).  This avoids the need for users of the AraDmConverter api
to depend on args4j.

### [COMN-137](http://jira.basistech.net/browse/COMN-137) Name translation api

Added the
[Name](https://git.basistech.net/textanalytics/annotated-data-model/blob/07802b864ac9ba79419695e1a3bd1b45734eb0b1/model/src/main/java/com/basistech/rosette/dm/Name.java)
class, which is a stripped down version of the RNI Name.  The intent
is to allow RNT functionality via the ADM.

### [COMN-61](http://jira.basistech.net/browse/COMN-61) Add map api for metadata

Added
[documentMetadata](https://git.basistech.net/textanalytics/annotated-data-model/blob/fa0d46f034aec71296f2fc455b412e06b09ceb92/model/src/main/java/com/basistech/rosette/dm/AnnotatedText.java#L502)
convenience method to build metadata from a map.

### [COMN-73](http://jira.basistech.net/browse/COMN-73) Lang code serialization
### [COMN-76](http://jira.basistech.net/browse/COMN-76) Enum map key serialzaiton

These allow serialization from ISO codes rather than the enum string,
e.g. "eng" instead of "ENGLISH".

### [COMN-147](http://jira.basistech.net/browse/COMN-147) Dispatching Annotator 

`com.basistech.rosette.dm.util.WholeDocumentLanguageDispatchAnnotatorBuilder`
creates `Annotator` objects that delegate based on language. This
permits an application to build a family of annotators, one per
language, and then aggregate them into a single annotator. It is a DRY
feature; it's not complex, it's just that we don't want to end up with
multiple copies of this in multiple places.

### [COMN-151](http://jira.basistech.net/browse/COMN-151) New Parent/New Common 
   
Pick up rosette-common-java 34.0.0, with new jar structure, via
textanalytics 56.3.1.

### [COMN-133](http://jira.basistech.net/browse/COMN-133) Support unordered json attributes

json attributes are unordered per the spec, so tools are allowed to
shuffle map keys.  This change allows deserialization from shuffled
keys.

### [COMN-149](http://jira.basistech.net/browse/COMN-149) Remove Guava dependency

Guava is now shaded inside adm-model.

### [COMN-152](http://jira.basistech.net/browse/COMN-152) OSGi metadata

The adm-model jar file now has OSGi metadata.  We intend to use this
for a classpath isolation solution for RaaS.

### [COMN-92](http://jira.basistech.net/browse/COMN-92) Empty analysis fix

Fixes serialization of an empty analysis.
