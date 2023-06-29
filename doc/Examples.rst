==================
Rosette Data Model
==================

.. contents::

Introduction
============

The Rosette Data Model is a collection of classes that store plain
text and attributes of that text, such as tokens, part-of-speech tags,
entities, etc.  Attributes are also sometimes called
"annotations" in this context.

This document describes the most interesting features of the data
model and its attributes, providing code snippets to demonstrate how
attributes are retrieved.  Please see the Javadoc documentation for
more details.

``AnnotatedText`` is the top-level class of the data model.

Rosette components provide an ``Annotator`` which accepts an input
text object and returns a new text object with some added attributes.
Typical use of a Rosette component looks like this:

::

    Annotator annotator = ...;  // from a Rosette component factory
    AnnotatedText input = new AnnotatedText.Builder().data("some text").build();
    AnnotatedText output = annotator.annotate(input);
    // now use attributes on the output object

The ``Annotator`` typically comes from a factory in a Rosette
component.

The input text object comes from the output of some other Rosette
component, or if this is the first component in a pipeline, from an
``AnnotatedText.Builder``.

Start/end offsets in attributes are always half-open ranges, and they
refer to character-level offsets in the original text.  ("Character"
here really means a UTF-16 element.)

``AnnotatedText`` objects are immutable.


Document Metadata
=================

The document metadata stores information about the document as a
whole, such as a document id or date.  The metadata is a map from
string to list-of-string.  For example, RES (Rosette Entity Resolver)
requires a document id:

::

    AnnotatedText fromREX = ...;
    AnnotatedText.Builder builder = new AnnotatedText.Builder(fromREX);
    builder.documentMetadata(EntityResolver.DOC_ID_KEY, "my_doc_id");
    AnnotatedText inputToRes = builder.build();


Generic Attributes
==================

All attributes of the data model extend ``BaseAttribute``.  Attributes
with with character offsets (e.g. a ``Token``) extend ``Attribute``.
All attributes are stored in a map of string to ``BaseAttribute``,
accessed via ``AnnotatedText.getAttributes()``.  The keys are defined
by the ``AttributeKey`` enumeration, however most applications will
choose to use named attribute accessors instead.  This avoids the need
for the application to cast the returned ``BaseAttibute``.

``ListAttribute`` is a container for other attributes.  For example,
entities are stored as ``ListAttribute<Entity>``.


Named Attribute Accessors
=========================

Each defined attribute comes with a convenience accessor method which
avoids casting in the application.  Consider:

::

    // via generic attributes
    AnnotatedText text = ...;
    ListAttribute<Entity> entities = (ListAttribute<Entity>) text.getAttributes().get(AttributeKey.ENTITY.key());

::

    // via named attribute accessor
    AnnotatedText text = ...;
    ListAttribute<Entity> entities = text.getEntities();


Nulls in the Data Model
=======================

One common pitfall of using the data model is not handling null
results correctly.  Null is used to preserve a distinction between a
component that has not yet run and a component that has run but
produced zero results.  For example:

::

    ListAttribute<Entity> entities = text.getEntity();
    if (entities != null) {
        if (entities.isEmpty()) {
            // The REX component was run, but the document contained
            // no entities, e.g. a very small document.
        }
    } else {
        // The REX component was not run at all; entities.isEmpty()
        // would throw a NullPointerException
    }

The examples below assume the results are not null for brevity.


The Attributes
==============


Text
----

The simplest attribute is the text itself, stored as a UTF-16 string.
``AnnotatedText`` implements ``CharSequence`` to provide direct access
to the text.

::

    AnnotatedText text = new AnnotatedText.Builder().data("some text").build();
    System.out.println(text.toString());
    System.out.println(text.length());

    // some text
    // 9


Language Detection
------------------

Language detection produces a ranked list of possible languages for a
document.  Each detection result contains a language, script,
confidence, and encoding.  Most applications are concerned only with
the language of the best result.

::

    Annotator annotator = builder.buildSingleLanguageAnnotator();
    AnnotatedText output = annotator.annotate("This is just a quick test.");
    LanguageDetection languageDetection = output.getWholeTextLanguageDetection();
    System.out.println("best language: "
        + languageDetection.getDetectionResults().get(0).getLanguage());
    for (LanguageDetection.DetectionResult r : languageDetection.getDetectionResults()) {
        System.out.printf("%s, %s, %s, %s%n",
            r.getLanguage(), r.getScript(), r.getConfidence(), r.getEncoding());
    }

    // best language: ENGLISH
    // ENGLISH, Latn, 0.01789626033853431, UTF-16BE
    // ROMANIAN, Latn, 0.0036553000535808344, UTF-16BE
    // SPANISH, Latn, 0.0036448829569105667, UTF-16BE
    // PORTUGUESE, Latn, 0.003620677306329756, UTF-16BE
    // ESTONIAN, Latn, 0.002865207423279781, UTF-16BE


Language Regions
----------------

Language region detection divides a document into regions and assigns
a language to each region.  The API allows for multiple language
guesses for each region, however the current implementation supplies
only a single language.

::

    Annotator annotator = builder.buildLanguageRegionAnnotator();
    AnnotatedText output = annotator.annotate(
        "This is just a quick test.  Это просто быстрый тест.");
    for (LanguageDetection languageDetection : output.getLanguageDetectionRegions()) {
        LanguageDetection.DetectionResult r = languageDetection.getDetectionResults().get(0);
        System.out.printf("[%d, %d), %s, %s, %s, %s%n",
            languageDetection.getStartOffset(), languageDetection.getEndOffset(),
            r.getLanguage(), r.getScript(), r.getConfidence(), r.getEncoding());
    }

    // [0, 28), ENGLISH, Latn, 0.018970035958846387, UTF-16
    // [28, 52), RUSSIAN, Cyrl, 0.020585351774082494, UTF-16


Script Regions
--------------

Script regions can be produced separately or as part of language
region detection.  A single script region may contain multiple
langauge regions.  For example, a Latn region may contain English and
French.  A Cryl region may contain Russian and Serbian.

::

    Annotator annotator = builder.buildLanguageRegionAnnotator();
    AnnotatedText output = annotator.annotate(
        "This is just a quick test.  Это просто быстрый тест.");
    for (ScriptRegion scriptRegion : output.getScriptRegions()) {
        System.out.printf("[%d, %d), %s%n", scriptRegion.getStartOffset(),
            scriptRegion.getEndOffset(), scriptRegion.getScript());
    }

    // [0, 28), Latn
    // [28, 52), Cyrl


Sentences
---------

Sentences are represented as start/end offsets into the text.  A
sentence usually includes any trailing whitespace.

::

    AnnotatedText output = annotator.annotate("Hello world!  How are you?");
    for (Sentence sentence : output.getSentences()) {
        System.out.printf("[%d, %d)%n", sentence.getStartOffset(),
            sentence.getEndOffset());
    }

    // [0, 14)
    // [14, 26)


Layout Regions
--------------

Layout regions are represented as start/end offsets into the text, along with what
type of layout the region has, structured or unstructured. A layout region usually
includes any trailing whitespace.

::

    AnnotatedText output = annotator.annotate("Consider the following table. City\tState\nBoston\tMassachusetts\nConcord\tNew Hampshire\n");
    for (LayoutRegion layoutRegion : output.getLayoutRegions()) {
        System.out.printf("[%d, %d), %s%n", layoutRegion.getStartOffset(),
            layoutRegion.getEndOffset(), layoutRegion.getLayout());
    }

    // [0, 30), UNSTRUCTURED
    // [30, 84), STRUCTURED

Base Noun Phrases
-----------------

Base noun phrases are represented as start/end offsets into the text.

::

    AnnotatedText input = ...;  // "The book is on the table."
    AnnotatedText output = annotator.annotate(input);
    for (BaseNounPhrase bnp : output.getBaseNounPhrases()) {
        System.out.printf("[%d, %d)%n", bnp.getStartOffset(),
            bnp.getEndOffset());
    }

    // [0, 8)
    // [15, 24)


Tokens
------

A token represents a word with optional morphological analyses and
normalizations.  Start/end offsets of a token always refer back to the
original text, however the text of a token may be different from the
characters at those offsets.  For example, some Unicode normalizations
may have been applied which can change the characters themselves as
well as the length of the token (e.g. NFKC).  In Chinese, a token may
span a newline or may contain "artistic whitespace".  In some
configurations of Rosette, even an English tokenizer can include a
token that includes whitespace or newlines.

In the example below, notice the single token "in front of" spans the
offsets [12, 25) with length 13, but the text of the token is of
length 11.  The text contains a newline and an extra space.  This is a
result of using a non-default tokenizer.  The default will treat "in
front of" as three tokens.

::

    AnnotatedText input = ...;  // "The book is in front\n  of the table.";
    AnnotatedText output = annotator.annotate(input);
    for (com.basistech.rosette.dm.Token token : output.getTokens()) {
        System.out.printf("[%d, %d), %s%n", token.getStartOffset(),
            token.getEndOffset(), token.getText());
    }

    // [0, 3), The
    // [4, 8), book
    // [9, 11), is
    // [12, 25), in front of
    // [26, 29), the
    // [30, 35), table
    // [35, 36), .


Morphological Analyses
----------------------

Each token has a list of possible morphological analyses.  A
disambiguation phase is responsible for selecting the best of these
possibilities.  Disambiguation places the best analysis as the first
element of the list.

There is a class hierarchy for language-specific analyses.  Code that
needs to handle any possible language needs to cast the returned
``MorphologicalAnalysis`` to the proper language-specific class.  For
example:

::

    MorphologicalAnalysis analysis = ...;
    if (analysis instanceof HanMorphoAnalysis) {
        HanMorphoAnalysis hanAnalysis = (HanMorphoAnalysis) analysis;
        // process Chinese/Japanese specifics
    } else if (analysis instanceof KoreanMorphoAnalysis) {
        KoreanMorphoAnalysis koreanAnalysis = (KoreanMorphoAnalysis) analysis;
        // process Korean specifics
    } else if (analysis instanceof ArabicMorphoAnalysis) {
        ArabicMorphoAnalysis arabicAnalysis = (arabicMorphoAnalysis) analysis;
        // process Arabic/Farsi/Urdu specifics
    } else {
        // process base MorphologicalAnalysis (e.g. English, French, etc.)
    }

MorphologicalAnalysis
~~~~~~~~~~~~~~~~~~~~~

Analyses can have different attributes for different languages.
``MorphologicalAnalysis`` is the base class for analyses.  It supports
lemmas, part-of-speech tags, and compound components, though not all
languages will produce compound components.

English example:

::

    AnnotatedText input = ...;  // "The book is on the table.";
    AnnotatedText output = annotator.annotate(input);
    for (com.basistech.rosette.dm.Token token : output.getTokens()) {
        System.out.println(token.getText());
        for (MorphoAnalysis analysis : token.getAnalyses()) {
            System.out.printf("  %s, %s%n",
                analysis.getLemma(), analysis.getPartOfSpeech());
        }
    }

    // The
    //   the, DET
    //   The, PROP
    // book
    //   book, NOUN
    //   book, VI
    //   book, VPRES
    // is
    //   be, VBPRES
    // on
    //   on, PREP
    //   on, ADJ
    //   on, ADV
    //   on, int_adv
    // the
    //   the, DET
    // table
    //   table, NOUN
    //   table, VI
    //   table, VPRES
    // .
    //   ., SENT

In German, words can have compound components.  Each compound
component is represented as a ``Token``, recursively, so the component
itself may have analyses, but in this case only the surface form of
the component is used.

In the example below, the word has three possible analyses, all with
the same lemma and part of speech (NOUN).  But they differ in how the
token gets broken into compound components.  The first analysis is the
disambiguated result, so the preferred components are "Bund" +
"Innenminister".

German example:

::

    AnnotatedText input = ...;  // "Bundesinnenminister"
    AnnotatedText output = annotator.annotate(input);
    for (com.basistech.rosette.dm.Token token : output.getTokens()) {
        System.out.println(token.getText());
        for (MorphoAnalysis analysis : token.getAnalyses()) {
            List<String> components = Lists.newArrayList();
            for (com.basistech.rosette.dm.Token component : analysis.getComponents()) {
                components.add(component.getText());
            }
            System.out.printf("  %s, %s, %s%n",
                analysis.getLemma(), analysis.getPartOfSpeech(), components);
        }
    }

    // Bundesinnenminister
    //   Bundesinnenminister, NOUN, [Bund, Innenminister]
    //   Bundesinnenminister, NOUN, [Bund, innen, Minister]
    //   Bundesinnenminister, NOUN, [Bund, innen, mini, Ster]

HanMorphoAnalysis
~~~~~~~~~~~~~~~~~

In Chinese and Japanese, an analysis can contain readings.  Notice the
need for the user to cast ``MorphoAnalysis`` to ``HanMorphoAnalysis``
to access the readings.

Japanese example:

::

    String s = "電子計算機";
    AnnotatedText.Builder builder = new AnnotatedText.Builder().data(s);
    AnnotatedText input = builder.build();
    AnnotatedText output = annotator.annotate(input);
    for (com.basistech.rosette.dm.Token token : output.getTokens()) {
        System.out.println(token.getText());
        for (MorphoAnalysis baseAnalysis : token.getAnalyses()) {
            HanMorphoAnalysis analysis = (HanMorphoAnalysis) baseAnalysis;
            List<String> readings = Lists.newArrayList();
            for (String reading : analysis.getReadings()) {
                readings.add(reading);
            }
            System.out.printf("  %s, %s, %s%n",
                analysis.getLemma(), analysis.getPartOfSpeech(), readings);
        }
    }

    // 電子
    //   電子, NC, [デンシ]
    // 計算
    //   計算, VN, [ケイサン]
    // 機
    //   機, NC, [キ]
    //   機, NU, [キ]
    //   機, NC, [ハタ]
    //   機, WS, [キ]

KoreanMorphoAnalysis
~~~~~~~~~~~~~~~~~~~~

Korean analyses are broken down into morphemes, and each morpheme has
a morpheme tag.  Notice the need to cast ``MorphoAnalysis`` to
``KoreanMorphoAnalysis`` to access these attributes.

Korean example:

::

    String s = "한국온라인신문협회";
    AnnotatedText.Builder builder = new AnnotatedText.Builder().data(s);
    AnnotatedText input = builder.build();
    AnnotatedText output = annotator.annotate(input);
    for (com.basistech.rosette.dm.Token token : output.getTokens()) {
        System.out.println(token.getText());
        for (MorphoAnalysis baseAnalysis : token.getAnalyses()) {
            KoreanMorphoAnalysis analysis = (KoreanMorphoAnalysis) baseAnalysis;
            System.out.printf("  %s, %s%n",
                analysis.getLemma(), analysis.getPartOfSpeech());
            int morphemeCount = analysis.getMorphemes().size();
            for (int i = 0; i < morphemeCount; i++) {
                System.out.printf("    %s[%s]%n",
                    analysis.getMorphemes().get(i),
                    analysis.getMorphemeTags().get(i));
            }
        }
    }

    // 한국온라인신문협회
    //   한국온라인신문협회, NPR
    //     한국[NPR]
    //     온라인[NNC]
    //     신문[NNC]
    //     협회[NNC]

ArabicMorphoAnalysis
~~~~~~~~~~~~~~~~~~~~

Arabic analyses are the most complex.  Each analysis is broken down
into prefix, stem, and suffix components, where some components could
be empty.  The components may be broken down further into
sub-components, e.g. a prefix could have multiple parts ("and the").
Each component and sub-component has an associated tag.  Analyses also
have distinct slots for stem, lemma, and root, which are all different
concepts in Arabic.  Notice the need for the user to cast
``MorphoAnalysis`` to ``ArabicMorphoAnalysis`` to access this
information.

Here's an example that shows how a single word ("and the books") is
divided into prefix, stem, and suffix.  The disambiguated analysis
(the first one) shows a prefix (for "and the"), a stem ("books"), and
no suffix.  The prefix itself is divided into two parts ("and" and
"the"), and each of those has a tag ("CONJ", and "DET").

::

    String s = "والكتب";  // "and the books"
    AnnotatedText.Builder builder = new AnnotatedText.Builder().data(s);
    AnnotatedText input = builder.build();
    AnnotatedText output = annotator.annotate(input);
    for (com.basistech.rosette.dm.Token token : output.getTokens()) {
        for (MorphoAnalysis baseAnalysis : token.getAnalyses()) {
            ArabicMorphoAnalysis analysis = (ArabicMorphoAnalysis) baseAnalysis;
            String tokenText = token.getText();
            String prefix = tokenText.substring(0, analysis.getPrefixLength());
            String stem = tokenText.substring(analysis.getPrefixLength(),
                analysis.getPrefixLength() + analysis.getStemLength());
            String suffix = tokenText.substring(analysis.getPrefixLength()
                + analysis.getStemLength());
            System.out.printf("prefix: %s, stem: %s, suffix: %s, POS: %s%n", prefix, stem, suffix, analysis.getPartOfSpeech());
            System.out.printf("  prefix info: %s, %s%n", analysis.getPrefixes(), analysis.getPrefixTags());
            System.out.printf("  stem info:   %s, %s%n", analysis.getStems(), analysis.getStemTags());
            System.out.printf("  suffix info: %s, %s%n", analysis.getSuffixes(), analysis.getSuffixTags());
        }
    }

    // prefix: وال, stem: كتب, suffix: , POS: NOUN
    //   prefix info: [و, ال], [CONJ, DET]
    //   stem info:   [كتب], [NOUN]
    //   suffix info: [], [NO_FUNC]
    // prefix: , stem: والكتب, suffix: , POS: NOUN_PROP
    //   prefix info: [], [NO_FUNC]
    //   stem info:   [والكتب], [NOUN_PROP]
    //   suffix info: [], [NO_FUNC]
    // prefix: , stem: والكتب, suffix: , POS: NOUN_PROP
    //   prefix info: [و, ال], [CONJ, DET]
    //   stem info:   [كتب], [NOUN_PROP]
    //   suffix info: [], [NO_FUNC]
    // prefix: , stem: والكتب, suffix: , POS: NOUN_PROP
    //   prefix info: [و], [CONJ]
    //   stem info:   [الكتب], [NOUN_PROP]
    //   suffix info: [], [NO_FUNC]



Entities
---------------

Entities are a result of document-level grouping of mentions that refer
to the same thing. A mention is a span of text that mentions an entity, 
while an entity describes the entity itself. It has an entity type (e.g. 
PERSON, LOCATION), entity id and head mention index (in the mentions list). 

Mentions are strings in the text that refer to entities.
A mention is identified by its character start/end offsets. It has 
a source and subsource which refer to the way it was extracted.
It may have a normalized form (e.g. normalized whitespace, affixes 
removed) and confidence score.

Below, mentions ["George Bush", "Bush"] form an entity of "PERSON" type and
entity id "T0".  "Washington" entiy has a single mention as well as "Texas".

::

    String s = "George Bush lived in Washington.  Bush lives in Texas now.";
    AnnotatedText.Builder builder = new AnnotatedText.Builder().data(s);
    AnnotatedText input = builder.build();
    AnnotatedText output = annotator.annotate(input);
    int i = 0;
    for (Entity entity : output.getEntities())
        for (Mention mention : entity.getMentions()) {
            System.out.printf("%d: [%d, %d], %s, %s, %s%n",
                    i, mention.getStartOffset(), mention.getEndOffset(),
                    entity.getEntityId(), entity.getType(),
                    mention.getNormalized());
            i++;
        }
    
    }

    // 0: [0, 11], T0, PERSON, George Bush
    // 1: [34, 38], T0, PERSON, Bush
    // 2: [21, 31], T1, LOCATION, Washington
    // 3: [48, 53], T3, LOCATION, Texas



Relationship Mentions
---------------------

Relationship mentions are facts expressed in plain text through
connections between entities or other noun phrases. A relationship
mention in a sentence has a number of components that describe
the relationship: The predicate is the sentence's main verb or action.
The first argument is the subject or agent of the relationship. The second
argument is the object of the action in the relationship. The third argument
is any additional object that may appear in the relationship. Optional
components include locatives and temporals, which express the location(s)
and time(s) at which the relationship took place, respectively, and adjuncts,
which are all other optional parts of a relationship.

All components are represented by the ``RelationshipComponent`` class. 
There are three ways in which the component could be presented to a user or to the calling  application:

* The ``phrase`` is intended to be used for display purposes. It normally contains a lemmatized, indoc-resolved or otherwise normalized version of the raw text. 

* The raw text underlying the component is provided through ``extent`` s. These are character offsets pointing at the ``data`` portion of the ``AnnotatedText``.

* The ``identifier`` field is holds a link to an external, 'canonical' representation of the component (e.g. a QID). This is currently only a placeholder and is not populated by the version of RELAX at the time of this writing. 

The following example yields one relationship mention in which "announce" is
the predicate, expressing the relationship between the first argument,
"The former Google CEO," and the second argument, "that he is now CEO of
Alphabet." The relationship also includes examples of optional components:
the temporal "Monday" and the adjunct "in a blog post." Locatives are accessed
the same way as temporals and adjuncts, as shown here.

::

    String s = "The former Google CEO announced in a blog post Monday that he is now CEO of Alphabet.";
    AnnotatedText output = annotator.annotate(s);

    for (RelationshipMention mention : output.getRelationshipMentions()) {
        String sentence = output.getData().subSequence(mention.getStartOffset(), mention.getEndOffset()).toString();
        String arg1 = mention.getArg1().getPhrase();
        String predicate = mention.getPredicate().getPhrase();
        String arg2 = mention.getArg2().getPhrase();
        String temporal = "";
        for (RelationshipComponent temp : mention.getTemporals())
            temporal += "[" + temp.getPhrase() + "] ";

        String adjunct = "";
        for (RelationshipComponent adj : mention.getAdjuncts())
            adjunct += "[" + adj.getPhrase() + "] ";

        System.out.printf("%s%n[%s] [%s] [%s]%nTemporals: %s%nAdjuncts: %s",
                sentence, arg1, predicate, arg2, temporal, adjunct);
    }

    // The former Google CEO announced in a blog post Monday that he is now CEO of Alphabet.
    // [The former Google CEO] [announce] [that he is now CEO of Alphabet]
    // Temporals: [Monday]
    // Adjuncts: [in a blog post]



Translated Data
---------------

Translated data allows for one or more translations of the full text
document.  The "domain" of the text includes its script, language, and
transliteration scheme.

::

    AnnotatedText input = ...; // "One.  Two."
    AnnotatedText output = annotator.annotate(input);
    // Usually there will be only one translation.
    TranslatedData t = output.getTranslatedData().get(0);
    System.out.printf("%s: %s%n", t.getDomain(), t.getTranslation());

    // [Latn/deu/native]: Ein.  Zwei.


Translated Tokens
-----------------

Translated tokens hold token-level translations.  This is used when
converting Chinese text between Traditional and Simplified scripts.

::

    String s = "正體字";  // Traditional Chinese
    AnnotatedText.Builder builder = new AnnotatedText.Builder().data(s);
    AnnotatedText input = builder.build();
    AnnotatedText output = annotator.annotate(input);
    // Usually there will be only one set of translated tokens.
    TranslatedTokens tt = output.getTranslatedTokens().get(0);
    System.out.println(tt.getDomain());
    System.out.println(tt.getTranslations());

    // [Hans/zhs/native]
    // [正, 体字]


Categorizer Result
------------------

Categorizer results hold the results of document categorization.  The
results contain a ranked list of possible categories, sorted by
confidence (descending).

::

    AnnotatedText input = ...;  // "The Red Sox won last night at Fenway."
    AnnotatedText output = annotator.annotate(input);
    for (CategorizerResult r : output.getCategorizerResults()) {
        System.out.printf("%s, %s%n", r.getLabel(), r.getConfidence());
    }

    // SPORTS, 0.12
    // STYLE_AND_FASHION
    // HEALTH_AND_FITNESS
    // ...


Sentiment Results
-----------------

Sentiment results have the same shape as categorizer results, except
the categories are just "pos" and "neg".

::

    AnnotatedText input = ...;  // "The Red Sox won last night at Fenway."
    AnnotatedText output = annotator.annotate(input);
    for (CategorizerResult r : output.getCategorizerResults()) {
        System.out.printf("%s, %s%n", r.getLabel(), r.getConfidence());
    }

    // pos, 0.55
    // neg, 0.45


Name
----

A ``Name`` is a entity mention not related to a document, in contrast
to ``Entity`` and its ``Mention``s, which refers to offsets within a document.
``Name`` is provided to facilitate ``RNT`` functionality.  A name can
have a language-of-origin and a language-of-use.  For example, an
English name "George" can be used in a French document.

::

    Name name = ...;  // obtained from elsewhere
    System.out.printf("%s, %s, %s, %s, %s%n", name.getText(), name.getLanguageOfOrigin(),
        name.getLanguageOfUse(), name.getScript(), name.getType());

    // George, ENGLISH, FRENCH, Latn, PERSON
