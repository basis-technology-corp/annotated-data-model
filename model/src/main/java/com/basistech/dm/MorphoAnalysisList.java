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

package com.basistech.dm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A list of MorphoAnalysis objects that can be serialized and deserialized with
 * homogeneous polymorphism.
 */
@JsonSerialize(using = MorphoAnalysisListSerializer.class)
@JsonDeserialize(using = MorphoAnalysisListDeserializer.class)
public class MorphoAnalysisList {

    private final List<MorphoAnalysis> items;
    private final Class<? extends MorphoAnalysis> itemClass;

    public MorphoAnalysisList(Class<? extends MorphoAnalysis> itemClass, List<MorphoAnalysis> items) {
        this.itemClass = itemClass;
        this.items = Collections.unmodifiableList(items);
    }

    public List<MorphoAnalysis> getItems() {
        return items;
    }

    // this is only used by the serializer, never let it get processed automatically.
    @JsonIgnore
    Class<? extends MorphoAnalysis> getItemClass() {
        return itemClass;
    }

    /**
     * A builder.
     */
    public static class Builder {
        private Class<? extends MorphoAnalysis> itemClass;
        private List<MorphoAnalysis> items;

        /**
         * @param itemClass the class of the attribute items.
         */
        public Builder(Class<? extends MorphoAnalysis> itemClass) {
            this.itemClass = itemClass;
            items = Lists.newArrayList();
        }

        public void add(MorphoAnalysis item) {
            items.add(item);
        }

        public void setItems(List<MorphoAnalysis> items) {
            this.items.addAll(items);
        }

        public MorphoAnalysisList build() {
            return new MorphoAnalysisList(itemClass, items);
        }
    }
}
