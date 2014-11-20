# Cumulative Release Notes for the Annotated Data Model #

# 1.10.1 #

## Dispatching Annotator ([COMN-147](http://jira.basistech.net/browse/COMN-147)) ##

`com.basistech.rosette.dm.util.WholeDocumentLanguageDispatchAnnotatorBuilder` creates `Annotator` objects
that delegate based on language. This permits an application to build a family of annotators, one per language,
and then aggregate them into a single annotator. It is a DRY feature; it's not complex, it's just that
we don't want to end up with multiple copies of this in multiple
places.

## New Parent/New Common ([COMN-151](http://jira.basistech.net/browse/COMN-151)) ##
   
Pick up new rosette-common-java, with new jar structure, via
textanalytics 56.3.1.

   
