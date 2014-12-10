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
 * This is a collection of Jackson customizations to permit efficient and compact
 * serialization and deserialization of the annotated data model. The only
 * public interface is {@link com.basistech.rosette.dm.AnnotatedDataModelArrayModule}.
 * This Jackson module sets up customizations for all of the objects that make up the model.
 * In general, applications should use
 * {@link com.basistech.rosette.dm.AnnotatedDataModelArrayModule#setupObjectMapper(com.fasterxml.jackson.databind.ObjectMapper)}
 * to apply the module to an {@link com.fasterxml.jackson.databind.ObjectMapper}. Note that this module
 * depends on functionality that was introduced into Jackson at version 2.4.0.
 *
 * For example:
 * <pre>
 * ObjectMapper mapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());
 * </pre>
 */
package com.basistech.rosette.dm;