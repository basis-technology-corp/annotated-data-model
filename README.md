services-data-model
===================

In spite of the content of [my google document](https://docs.google.com/a/basistech.com/document/d/1a3SiHdRjjB1jUWW_orpcnmEQD3KvCqUa9oOQT0u18uA/edit),
the comments of James and others have convinced me that an annotation model is the wrong place to start. Coding to a pure annotation model is pure
hell. If we need to layer an abstract data model over the annotation model to get work done, we might as well start with the abstract model. It
would enable implementation of the web service. Thus, this repo contains a sketch in actual code.

# Data Model Overview #

The top-level item in the model is a `Text`. A Text contains a blob, not surprisingly, of text. A Text has a collection
of attributes. An attribute has a name and a value; the general restriction is that the value must be serializable
with Jackson. Attributes can, as a result have attibutes.

## Top-level Attributes ##

* Sentence Boundaries: just as in RLP, a sentence boundary is just a marker in the text blob. There can be other boundaries.
* RLI attributes: such as overall languages detected and language region boundaries.
* Tokens: Much of the complexity of all this adheres to the tokens.
* Named Entities
* Base Noun Porases

## Mutability ##

Currently, all the attribute classes are immutable. The idea is that mutation requires making a new one.
If we decide that this bit of functional programming thinking is in fact evidence of confused thinking, we can add
setters.

## Token Data Model ##

We need to be able to represent the current output of RLP. This is a
bit messy.  In RLP, a token can have a set of 'alternative lemmas',
and a set of 'alternative POS tags', and the two _are not connected_,
because the compound components are included in the lemmas. This is
more complex than the model of RBL-JE, which is that a token has one
or more analyses, each consisting of a collection of attributes such
as lemma and POS. Underneath, C++ has the same data as Java, more or
less, and we could add an option, even for '7.10.2', to make it
possible to retrieve that model.

Let's fill in details, language by language. 

### Xerox/FST ###

The output of the FSTs is a set of analyses, each consisting of:

* POS
* Lemma
* compound components

### Arabic ###

Arabic starts by producing a 'normalized token' which consists
of the results of cleaning up orthographic variations. There can be
several alternative normalizations.

Then, it does 'Buckwalter analysis' on each of the alternative
normalizations, and that produces several normalizations.

The output of Buckwalter is a set of analyses, each consisting of:

* the normalized form that started this analysis.
* division of the normalized token into prefix/stem/suffix.
* POS
* Lemma
* Semitic Root

The P/S/S can have POS codes of their own, which we have not exposed
via RLP.

On top of all of this, the code can generate a collection of
alternative forms, for the benefit of applications that prefer to
query for variations instead of indexing reduced forms.

### Korean ###

Korean is a bit in flux, but one variation looks like a set of analyses:

* Lemma
* POS, but sort of by convention
* Compound components, each with a POS

### 'Lemmatizer' / 'Dictionary Only' languages

The results are a set of alternative lemmas.

### Chinese ###

In Chinese a token might have several possible POS tags and several possible readings. The two might or might not be connected.

### Japanese ###

In Japanese a token might have several possible POS tags and several possible readings. The two might or might not be connected.

### The 'Term Expander' ### 

In addition to all this, there is a 'term expander'. This attaches a
set of alternative forms to a token. To me, this feels like it should
be a Lucene TokenFilter that is not handled in this data model at all.

One solution here is to give a token a set of attributes, each of which can be multi-valued: a list of POS, a list of lemmas,
etc. Then we need to be able to indicate the interconnections when they are present. A solution here would be a `Text`-level
boolean attribute that indicates that the token attributes correlate into 'analyses' (or not).

A related issue is the matter of disambiguated output. In RLP, we have distinct attributes for the 'right answer': Part Of Speech
is a different attribute from 'alternative parts of speech'. In RBL-JE, we have an attribute that identifies the 'selected analysis'.

David Murgatroyd has pointed out that the concept of 'correct' or 'selected' is a stand-in for an ordering. So, one path to take is that
disambiguation reorders the multi-valued attributes so that the right answer is in the \[0] slot. If we ever do a full
ranking component, it could deliver a full order. I think that this is preferable to the RLP-like solution of a additional attributes
to contain the selected solution from amongst the multiple alternatives.

## Entity Data Model ##

Entities have offsets, type, confidence, and chainId. Keeping an integer chain ID is a compromise; we could add a new top-level
attribute called 'CoreferenceChain' that stored a list of EntityMentions. That would require care in serialization and deserialization,
but I skipped that work for the moment.



