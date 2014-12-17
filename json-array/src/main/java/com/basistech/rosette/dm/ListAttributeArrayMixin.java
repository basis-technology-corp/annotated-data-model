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

package com.basistech.rosette.dm;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * {@link com.basistech.rosette.dm.ListAttribute}.
 */
@JsonSerialize(using = ListAttributeArraySerializer.class)
@JsonDeserialize(using = ListAttributeArrayDeserializer.class)
@JsonPropertyOrder(alphabetic = true)
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public abstract class ListAttributeArrayMixin {

    // this is only used by the serializer, never let it get processed automatically.
    @JsonIgnore
    abstract String getItemJsonKey();

    @JsonIgnore
    public abstract boolean isEmpty();

}
