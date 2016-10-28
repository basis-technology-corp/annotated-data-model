
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

### ENTITY ###

Entities are the output of REX. Entities are a result of document-level grouping of mentions that refer
to the same thing. Mentions are spans of characters, not necessarily aligned with tokens.
See the REX documentation for the information delivered with an entity mention.

### RELATION_MENTIONS ###

Relationship Mentions are the output of RELAX which identifies how entities and other noun phrases are connected
to each other in the text. THIS IS AN ALPHA FEATURE OF THE ANNOTATED DATA MODEL.

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
 
### TRANSLATED_DATA ###
 
This annotation provides a translation for the entire text into a language and script specified by the domain.

### TRANSLATED_TOKENS ###

The translated tokens annotations provides a itemList of translations for each of the tokens in the text.  Each annotation
contains a domain identifying the language and script of the translations and a itemList of translations corresponding to
the tokens of the text.

# Serialization #

The adm-json artifact provides support for reading and writing Json via
the Jackson library. Custom serialization and deserialization provides
type-safe lists and accomplishes polymorphism with a minumum of
additional space.

The artifact provides two Jackson modules. One writes the ADM as a
graph of Json objects. The second avoids all the key names by using
the 'array shape' feature of Jackson. In most cases, you can get
space/time performance from the normal object model by using the
Jackson 'SMILE' format or some other compression mechanism, but the
array variation reduces overhead.

The library has a concept of the version of the serialized form. When
serializing, it adds a `version` property, and when deserializing it
checks this version for compatibility. As of now, the format is at
version 1.1.0. The deserialization code accepts either '1.x.y' or
nothing for a version. Future versions may include the ability to read
and convert old formats.

## How to Incorporate ##

This project builds several Maven artifacts.

### adm-model ###

The data model proper is in adm-model. This provides the classes of
the model, and has dependencies, notably on Guava.

````
<dependency>
    <groupId>com.basistech</groupId>
    <artifactId>adm-model</artifactId>
    <version>2.0.0</version>
</dependency>

````

### adm-json ###

adm-json provides classes for reading and writing the ADM with
Jackson. It required Jackson 2.6.2, and declares dependencies on
Jackson.

```
<dependency>
    <groupId>com.basistech</groupId>
    <artifactId>adm-json</artifactId>
    <version>2.0.0</version>
</dependency>

```


### How to push the Maven site to gh-pages ###

```
  mvn site site:stage
  mvn scm-publish:publish-scm
```

Typically, this would be after a release:

```
  mvn release:perform
  cd target/checkout 
  mvn site site:stage
  mvn scm-publish:publish-scm
```
