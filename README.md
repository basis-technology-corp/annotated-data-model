This project builds a Java class library that represents annotated
textual data. The primary documentation is the [javadoc](http://git.basistech.net/pages/textanalytics/annotated-data-model/adm-model/apidocs/index.html).

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
