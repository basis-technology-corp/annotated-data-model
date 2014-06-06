# Annotated Data Model #

The Annotated Data Model (ADM) is a family of Java classes that represent a text and a set of annotations
on the text. The text, which would typically derive from a document, is represented as an ordinary Java String
of UTF-16. The annotations are a collection of objects. Some annotations relate to the entire text; others
to a span of text identified by a range of character offsets.

The ADM provides a representation for the annotations that are used by Basis Technology's Rosette platform.  
It is not intended as a generalized representation, such as those defined by UIMA or GATE. There are always 
tradeoffs between generality and efficiency. The ADM is faster and easier to work with because it does need 
to allow for arbitrary annotations, either via a compiled schema or via complete runtime construction.

The ADM is defined as a set of Java classes, together with a Jackson module that provides a standard 
Json serialization representation.

## Texts, Annotations, and Attributes ##

The top-level object of the ADM is the `AnnotatedText` object. An `AnnotatedText` object represents a text.
This object contains the text itself, optional document metadata, and the annotations. In the ADM, the 
annotations of the text are grouped into 'attributes'. As above, some attributes are data about the entire
text, and others concern spans.

The attributes are available in a `Map<String, BaseAttribute>`. The `AttributeKey` enum provides constants
for the defined attributes. In addition, there are convenience methods to retrieve the individual well-known
attributes.

The document metadata is a `Map<String, List<String>>`, intended to model typical MIME or 'Dublin Core'
information.

Most of the annotations are lists of items that span ranges of text. For example, the `TOKEN` annotation 
describes the tokens of the text. It is implemented as a `ListAttribute`. List attributes implement 
`java.util.List<Item extends BaseAttribute>`, 
but they also guarantee that their contents are all of the same type.

All of the attributes inherit from `BaseAttribute`. A base attribute consists only of a map that allows for 
compatibility and extensibility. The `Attribute` subclass adds in start and end offsets. Most of the 
defined attributes inherit from it. 

## Immutability, Traversing, and Building ##

ADM classes are defined to be immutable. They include no APIs that modify their contents, and their implementations
use immutable data structures. This allows arbitrary multi-threaded traversal. 

To construct new ADM items, an application must use the provided Builder classes. 
A common pattern is for some module to accept an ADM data structure and return a new ADM data structure with
additional annotations. To facilitate this, the Builders can be constructed over existing instances of 
their corresponding classes. Of course, since the objects are immutable, the Builders are free to reuse
the input as part of the output.

## The Defined Annotations ##

### BASE_NOUN_PHRASE ###

Base Noun Phrases are spans of text, aligned with token boundaries.

### ENTITY_MENTION ###

Entity Mentions are the output of REX. Mentions are spans of characters, not necessarily aligned with tokens.
See the REX documentation for the information delivered with an 
entity mention.

### LANGUAGE_DETECTION_REGIONS ###

This annotation provides for splitting a text into regions by language.
For each region, the data structure provides details on the 
language detection.

### LANGUAGE_DETECTION ###

This annotation provides language-detection results for the the entire text.

### SCRIPT_REGION ###

This annotation provides information on how the text can be divided 
into regions according to script, as defined in ISO15924.

### SENTENCE ###

This annotation describes the sentences of the text. By convention, the sentences cover the entire text.
Whitespace at the end of each sentences is incorporated into that sentence.

### TOKEN ###

The token annotations identify the tokens of the text and annotate them with the results of 
Rosette Base Linguistics. 

## Class-Level Documentation ##

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
Jackson. It required Jackson 2.4.0, and declares dependencies on
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
