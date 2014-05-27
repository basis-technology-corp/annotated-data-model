This project builds a Java class library that represents annotated
textual data. 

## Documentatation ##

The primary documentation is the
[javadoc](http://git.basistech.net/pages/textanalytics/annotated-data-model/adm-model/apidocs/index.html).

## How to Incorporate ##

This project builds several Maven artifacts.

### adm-model ###

The data model proper is in adm-model. This provides the classes of
the model, and has dependencies, notably on Guava.

````

<dependency>
    <groupId>com.basistech</groupId>
    <artifactId>adm-model</artifactId>
    <version>1.9.100</version>
</dependency>

````

### adm-json ###

adm-json provides classes for reading and writing the ADM with
Jackson. It required 2.4.0-rc3, and declares dependencies on
Jackson. This is appropriate for tools internal code or code that will
use shade for itself.

````

<dependency>
    <groupId>com.basistech</groupId>
    <artifactId>adm-json</artifactId>
    <version>1.9.100</version>
</dependency>

````

### adm-shaded ###

This combines adm-model and adm-json into a package with no declared
dependencies, by using the Maven shade plugin on adm-model and
adm-json. As a result, it offers only a minimal json API from the
class com.basistech.rosette.dm.DmJsonUtils. This is suitable for code
that is delivered to SDK customers (REX-JE springs to mind).

````

<dependency>
    <groupId>com.basistech</groupId>
    <artifactId>adm-shaded</artifactId>
    <version>1.9.100</version>
</dependency>

````

### adm-tools ###

This project includes the classes to convert AbstractResultAccess
objects to ADM. It uses Jackson, and inherits the 2.4.0-rc3 version
requirement from the adm-json project. 

````

<dependency>
    <groupId>com.basistech</groupId>
    <artifactId>adm-model</artifactId>
    <version>1.9.100</version>
</dependency>

````



### How to push the Maven site to gh-pages ###

````
  mvn site site:stage
  mvn scm-publish:publish-scm
````

Typically, this would be after a release:

````
  mvn release:perform
  cd target/checkout 
  mvn site site:stage
  mvn scm-publish:publish-scm
````
