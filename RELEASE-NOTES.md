# Cumulative Release Notes for the Annotated Data Model

## 2.1.3

### [ROS-230](https://basistech.atlassian.net/browse/ROS-230) Fix
  bug: head mention indices not copied by copy constructor.

## 2.1.2

### [RELAX-360](https://basistech.atlassian.net/browse/RELAX-360) Fix
    mistake in mention sorting.

## 2.1.1

### [ROS-227](https://basistech.atlassian.net/browse/ROS-227) Document order for upgraded Entity/Mention

Contrary to the explanation below (in ROS-43), when the code constructs Entity/Mention structures from old
EntityMention objects, it respects _document_ order, never head mention order. Entities are ordered
by their first mention's document position, and mentions in an entity, of course, by their document order.

### [ROS-226](https://basistech.atlassian.net/browse/ROS-226) NPE in Entity.Builder

Prevent NullPointerException when adding sentiment to an
Entity.Builder created via the copy constructor.

### [ROS-228](https://basistech.atlassian.net/browse/ROS-228) Coreference chain ID compatibility

The EntityMention objects created in response to AnnotatedText.getEntityMentions()
contain correct coreferenceChainId fields derived from the head mentions in the 
Entity objects.

## 2.1.0

### [ROS-218](https://basistech.atlassian.net/browse/ROS-218) Entity-level sentiment is a list

Entity-level sentiment used to return a single `CategorizerResult`.
After this change it returns a `List<CategorizerResult>` in confidence
ranked order.  Currently there will be three results, one for each of
"neg", "neu", "pos".  `ResolvedEntity#getSentiment` is already
deprecated because of ROS-43.  It still returns the top result only.
`Entity#getSentiment()` is the new method that returns the list.

### [ROS-43](https://basistech.atlassian.net/browse/ROS-43) Combine EntityMentions and ResolvedEntity

Old behavior:

`EntityMention` and `ResolvedEntity` are produced in separate lists,
releated by the chainId field, where the chainId of a `ResolvedEntity`
is the index of the head mention in the list of `EntityMention`.  All
`EntityMention`s with the same chainId are mentions of the same
entity.  It is awkward to interate though the entities, which seems to
be the most popular use-case.

New behavior:

`EntityMention` and `ResolvedEntity` are deprecated, replaced by
`Mention` and `Entity`.  An `Entity` *contains* a list of one or more
`Mention`s.  The `Entity` list is ordered by the document order of the
head mentions.  (But see change in ROS-227 above.)  The `Mention` list
in each `Entity` is in document order, but note that mentions across
entities cannot be ordered in this way.  Each `Entity` contains a
`headMentionIndex` which points to its head mention.

The entity type field now lives at the `Entity` level, not the
`Mention` level.

If no chaining is done, each `Entity` will have a single `Mention`
without a `headMentionIndex` (it will be null).

If chaining is done, `headMentionIndex` will always be non-null.  Note
that it is possible to do chaining but still end up with all singleton
entities.  In this case, `headMentionIndex` for each `Entity` will be
0.

If entity resolution is also done, the `Entity` will have an
`entityId` in addition to the `headMentionIndex`.

It's now easy to iterate through the entities, and the mentions within
an entity.  However, it is awkward to get a list of mentions in
document order.  A helper method may be added for this in the future.

Older json containing `EntityMention` and `ResolvedEntity` will be
deserialized into the new data structure, compatibly, so you can code
to the new api, or still use the deprecated classes and methods.  It
is recommended to switch to the new api, and to re-serialize any files
with the old json format, since the compatibility layer does add some
overhead.


### [ROS-50](https://basistech.atlassian.net/browse/ROS-50) Add version attribute

This change adds a new version attribute to the serialized json form
of `AnnotatedText`.  It will currently use version 1.1.0.  An older
json without a version attribute will be treated as having version
1.0.0, and it will be converted compatibly.  The changes between
versions 1.0.0 and 1.1.0 are detailed above, in ROS-43.  If an
incompatible version is found at runtime, an exception will be thrown.


## 2.0.0

### [ROS-194](https://basistech.atlassian.net/browse/ROS-194) ADM model and json on github.com

This is the first release of the the annotated data model from source
code at github.com. It depends on new major versions of common-api and
common-lib, so even though its own API is not significantly different
from the prior version, we moved up to 2.0.0.

### [ROS-205](https://basistech.atlassian.net/browse/ROS-205): make builders of polymorphic classes use generics to return the right type

Before this change, the following would not compile:

```
new HanMorphoAnalysis.Builder().lemma("foo").addReading("bar");
                                            ^^^^^^^^^^^^^^^^^^
```

because lemma() returns a base class builder but addReading() is only
on the subclass builder.  The reverse would work:

```
new HanMorphoAnalysis.Builder().addReading("bar").lemma("foo");
```

or a cast:

```
HanMorphoAnalysis.Builder hmaBuilder = new HanMorphoAnalysis.Builder();
((HanMorphoAnalysis.Builder)hmaBuilder.lemma("foo")).addReading("bar");
```

After the change, both orders work.


## 1.18.1

repair to the change for ROS-201 to make the builder part work.


## 1.18.0

### [ROS-201](https://basistech.atlassian.net/browse/ROS-201) AnnotatedText#getData() should be null by default

`AnnotatedData#getData` now returns null instead of the empty string
by default.


## 1.17.0

###  [ROS-204](https://basistech.atlassian.net/browse/ROS-204) new common libs

###  [ROS-205](https://basistech.atlassian.net/browse/ROS-205): make builders of polymorphic classes use generics to return the right type

The intent here was to allow something like:

```
   new HanMorphoAnalysis.Builder().lemma("x").addReading("y");
```

But it does not work, since the return value of `lemma` is still not
`HanMorhoAnalysis`. Research continues to decide if there is a further
tweak that does the desired thing, or whether the commits in question
should be `git revert`-ed.

## 1.16.0

### [ROS-198](https://basistech.atlassian.net/browse/ROS-198) entity-level sentiment refactored

ResolvedEntity sentimentCategory and sentimentConfidence have been
moved into a CategorizerResult, which also has an explanationSet.
Also pull in new textanalytics parent version 57.2.14.

## 1.15.1

### [ROS-186](https://basistech.atlassian.net/browse/ROS-186) enum module moved out

The `EnumModule` is now part of common-api; also pick up the fact that
common-api has an independent version.  Code changes are required
since the package has changed from:

  com.basistech.rosette.dm.jackson.EnumModule

to

  com.basistech.util.jackson.EnumModule

## 1.15.0 because we added to the api

### [ROS-183](https://basistech.atlassian.net/browse/ROS-183) new parent and some checkstyle repairs
 
### [ROS-181](https://basistech.atlassian.net/browse/ROS-181) add entity-level sentiment

ResolvedEntity now has slots for sentimentCategory (e.g. "positive",
"negative", "neutral") and a sentimentConfidence.

## 1.14.2

* [ROS-98](https://basistech.atlassian.net/browse/ROS-98)
  Apply workarounds to bugs in Jackson 2.6.2.
* [ROS-166]  (https://basistech.atlassian.net/browse/ROS-98)
  Use new parent that uses maven-bundle-plugin 3.0.0.

## 1.14.1

### [RELAX-119](https://basistech.atlassian.net/browse/RELAX-119)
    hasSyntheticPredicate should check for null extents.

## 1.14.0

*Note:* This release changes the inventory of components that are
 built here, and makes 'interesting' API changes. There are no more
 '-osgi' jar files; the necessary OSGi metadata is in the jars that
 need it. When moving to this version, please be sure to take
 advantage of AbstractAnnotator and note the switch to Jackson 2.6.2.

### [ROS-39](https://basistech.atlassian.net/browse/ROS-39) 
  Throw an appropriate Jackson exception (InvalidFormatException) when we encounter a bad
  ISO-639 code.

### no jira: add AbstractAnnotator to avoid repeated code to map from
    String to AnnotatedText.

### [ROS-161](https://basistech.atlassian.net/browse/ROS-161) 
  Annotator.annotate no longer declares `throws RosetteException`.

### [ROS-78](https://basistech.atlassian.net/browse/ROS-78) 
  AnnotateText no longer `implements CharSequence`. Callers will need
  to use `getData()` to get to the textual data.

### [ROS-159](https://basistech.atlassian.net/browse/ROS-159) stop serializing null values.

### no jira: updated to parent 57.1.2, thus using Jackson 2.6.2.

### [ROS-97](https://basistech.atlassian.net/browse/ROS-97) More OSGi/shade improvements
     
We got rid of the adm-model-osgi vs. adm-model distinction. There's
just adm-model with OSGi metadata. The MANIFEST is maintained by
configuration in the pom.xml.

The shading removed in ROS-87 is back here; the one adm-model jar does
shade Guava.


### [ROS-87](https://basistech.atlassian.net/browse/ROS-87) OSGi/shade improvements

adm-json-array was integrated into adm-json.  adm-json was made into
an OSGi bundle.  It still functions outside of OSGi but you must
provide the guava dependency.

adm-json-osgi was removed.  The package
com.basistech.rosette.dm.jackson was removed and replaced with
com.basistech.rosette.dm.json.plain and
com.basistech.rosette.dm.json.array.

adm-model-osgi no longer contains a shaded copy of guava, it just
imports it via OSGi metadata.

## 1.13.2

### [RELAX-143] (https://basistech.atlassian.net/browse/RELAX-143) Add relationship fields.


### [RELAX-139] (https://basistech.atlassian.net/browse/RELAX-139) Fixing  a crash in the copy builder of RelationshipMention
  Missing initializations
  

### [ROS-88](https://basistech.atlassian.net/browse/ROS-88) Parent  57.1.1
    
## 1.13.1    

### [ROS-76](https://basistech.atlassian.net/browse/ROS-76) Fix serialization for CharSequence

If an AnnotatedText was constructed with a special CharSequence
(rather than just a string), the serialized form could contain
extraneous fields from the non-String object, which couldn't be
deserialized.  This change forces the serialziation to use just the
toString representation of the CharSequence.

## 1.13.0

## [RELAX-112](https://basistech.atlassian.net/browse/RELAX-112)
   Rework of the relationship classes. *not* compatible, but nothing
   touches them outside of relax, yet.

## 1.12.1

Rerelease with 57.0.1 as parent, so that it looks for common 35.0.0
instead of 34.x.x. Let this be a lesson to us in picking version numbers.

## 1.12.0

### [COMN-199](https://basistech.atlassian.net/browse/COMN-199) Remove adm-shaded

adm-shaded was a mistake.

### [COMN-198](https://basistech.atlassian.net/browse/COMN-198) Add  Relationship Support
    
This also includes a move to basis parent 57.0.0, and so a requirement
for Java 1.7.

### [COMN-189](https://basistech.atlassian.net/browse/COMN-189) Add ComposingAnnotator
    
`com.basistech.rosette.dm.util.ComposingAnnotator` provides a function
needed in RBL-JVM, which is to group a sequence of annotators into a
single annotator.

### [COMN-186](https://basistech.atlassian.net/browse/COMN-186) Optimize copying attributes

There were too many copies of the map behind extendedProperties, which
exist on every attribute, even if empty.  This showed up in an ADM
application that did a lot of Token copies.

### [COMN-183](http://jira.basistech.net/browse/COMN-183) Rename setEndOffset

`Attribute.Builder.setEndOffset()` was deprecated.  Use the new method
`Attribute.Builder.endOffset()` instead.

### [COMN-190](http://jira.basistech.net/browse/COMN-190) ADM builders can reset lists

Attribute builders that hold lists now have a setter for the entire
list, not just the ability to add new elements.  For example, in
`Token.Builder`:

```
  public Builder addAnalysis(MorphoAnalysis analysis);
  public Builder analyses(List<MorphoAnalysis> analyses);  // new
```

Calling the list setter with an empty list or null results in an empty
list in the Builder.

## 1.11.2

### [COMN-180](http://jira.basistech.net/browse/COMN-180) Remove guava dependency

adm-model-osgi no longer externally depend on guava; adm-shaded no
longer has extra copies of Guava classes in it.

## 1.11.1

In support of [INDOC-11](http://jira.basistech.net/browse/INDOC-11),
added com.basistech.dm.internal to the exported package list so that
indoc can get to TextWrapper. Not wonderfully pretty, and not the end
of the world.

## 1.11.0

### [COMN-167](http://jira.basistech.net/browse/COMN-167) Restructure OSGi more

The Jackson customizations move out of the com.basistech.dm package,
into com.basistech.dm.jackson and com.basistech.dm.jackson.array.  The
com.basistech:adm-osgi artifact was split into
com.basistech:adm-model-osgi and com.basistech:adm-json-osgi.

Moving the Jackson classes to to their own OSGi bundle allows an OSGi
application to use the Jackson code _inside_ the OSGi environment and
still use the main data model to communicate between the inside and
the outside.

Note that the package movement means that this has to fan out to
projects that are using the json support today.

## 1.10.2

### [COMN-163](http://jira.basistech.net/browse/COMN-163) Restructure OSGi
    
All the OSGi metadata moves to a new artifact, adm-osgi. This avoids
'split package' issues with multiple OSGi components with code in the
same package.

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
