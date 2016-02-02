/*
* Copyright 2014 Basis Technology Corp.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
/**
 * This is a collection of Jackson customizations to permit efficient and compact
 * serialization and deserialization of the annotated data model. The only
 * public interface is {@link com.basistech.rosette.dm.jackson.AnnotatedDataModelModule}.
 * This Jackson module sets up customizations for all of the objects that make up the model.
 * In general, applications should use
 * {@link com.basistech.rosette.dm.jackson.AnnotatedDataModelModule#setupObjectMapper(com.fasterxml.jackson.databind.ObjectMapper)}
 * to apply the module to an {@link com.fasterxml.jackson.databind.ObjectMapper}. Note that this module
 * depends on functionality that was introduced into Jackson at version 2.4.0.
 *
 * For example:
 * <pre>
 * ObjectMapper mapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());
 * </pre>
 */
package com.basistech.rosette.dm.jackson;
