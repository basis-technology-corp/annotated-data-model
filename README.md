This project builds a Java class library that represents annotated
textual data. The primary documentation is the [javadoc](http://git.basistech.net/pages/textanalytics/annotated-data-model/apidocs/com/basistech/dm/package-summary.html).

# How to push the Maven site to gh-pages #

````
  mvn site site:stage
  mvn scm-publish:publish-scm
````
