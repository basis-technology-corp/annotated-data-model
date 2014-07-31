/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2014 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/

/**
 * Rosette Data Model.
 * This package contains a set of classes that define a data model that represents annotations over text.
 * The data model is a Java (and Json) representation of some text and some annotations on the text.
 * <h2>AnnotatedText</h2>
 * <p>
  * The root of the model is the {@link com.basistech.rosette.dm.AnnotatedText} class.
 *  {@adm.java}this implements {@link java.lang.CharSequence}
 *  and stores annotations that refer to subsequences.{@adm.java}
 * </p>
 * <h2>KnownAttribute</h2>
 * <p>
 *     The annotations are represented as objects that inherit from {@link com.basistech.rosette.dm.BaseAttribute}.
 *     The base attribute is the simplest attribute; all this class provides is a map of <strong>extended properties</strong>
 *     that are used, as described below, as an extensibility mechanism.
 * </p>
 * <p>
 *     Most attribute classes inherit from {@link com.basistech.rosette.dm.Attribute}. This class adds a start offset and an end offset.
 *     Thus, attributes that refer to the {@code AnnotatedText} as a whole inherit from {@code BaseAttribute}, while attributes
 *     that refer to subsequences of text inherit from {@code Attribute}.
 * </p>
 * {@adm.java}
 * <h2>RawData</h2>
 *     <p>In some cases, applications of this data model may also need to represent initial raw data.
 *     The {@link com.basistech.rosette.dm.RawData}
 *     class supports that usage. {@code RawData} stores a {@link java.nio.ByteBuffer} and a {@code Map<String, <List<String>>} of
 *     metadata. There is no connection in the code between {@code AnnotatedText} and {@code RawData}.
 *     </p>
 *     {@adm.java}
 * <h2>Immutability</h2>
 * <p>
 *     All of the classes in this package are immutable. If a program needs to modify, it needs to construct new classes.
 *     This 'functional' approach avoids any possibility of concurrent access problems. Creating a new {@code AnnotatedText}
 *     over all the attributes of an old {@code AnnotatedText} plus a new set is not particularly costly compared to whatever
 *     actual NLP task is producing the annotations.
 * </p>
 * <h2>Builders</h2>
 * <p>
 *     Because these classes are immutable, they have many arguments to their constructors. Each class has a
 *     nested {@code Builder} class to avoid this inconvenience; the constructors are thus not public.
 * </p>
 * {@adm.java}
 * <h2>Extensibility Model</h2>
 * <p>
 *     We could have designed this data model to defer all the binding until runtime -- essentially, a giant
 *     collection of maps and arrays. This would have allowed any program at any time to define a new annotation, and
 *     would have made it very difficult to encounter a version skew amongst libraries compiled to different versions
 *     of the model. Programming to that sort of data model is painful, so we chose to write specific classes for
 *     specific annotations.
 * </p>
 * <p>
 *     To mitigate the possible unpleasant consequences resulting from version skew, this model includes an extensibility
 *     mechanism. {@link com.basistech.rosette.dm.BaseAttribute} contains a {@code Map<String, Object>}. This allows programs
 *     that have differing sets of annotations to communicate via Json. The {@link com.fasterxml.jackson.annotation.JsonAnySetter}
 *     and {@link com.fasterxml.jackson.annotation.JsonAnyGetter} annotations cause any items in the Json object to be mapped to
 *     entries in the map. Entries in the map are serialized as keys in the object. Thus, a program can read in a
 *     serialized {@code AnnotatedText} that contains attributes with fields that it does not know about.
 * </p>
 *
 * <h2>Serialization</h2>
 * <p>
 *     All of the classes in here support json serialization and deserialization via Jackson 2.4.x. However, they require
 *     some customization to get a correct and efficient representation.
 *     This customization is provided in a separate module: adm-json.
 * </p>
 * {@adm.java}
 */
package com.basistech.rosette.dm;

/*
The following is not true (yet) but is preserved here for future work.
<p>
 *     This scheme also handles 'future-proofing' for new attributes. If the deserialization process encounters an attribute
 *     it does not know, it creates an instance of a non-public class that extends {@link com.basistech.rosette.dm.BaseAttribute}.
 *     All of the information for the attribute is delivered to the extended properties of this object.
 * </p>
 */
