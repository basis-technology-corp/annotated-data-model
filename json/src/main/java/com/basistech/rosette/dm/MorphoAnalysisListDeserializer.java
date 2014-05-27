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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Jackson deserialization that handles polymorphism of MorphoAnalysis without writing
 * out the type in each one.
 */
public class MorphoAnalysisListDeserializer extends JsonDeserializer<List<MorphoAnalysis>> {
    private static final Set<String> ARABIC_FIELDS = ImmutableSet.of("prefixLength", "stemLength", "root", "prefixes", "stems", "suffixes");

    private boolean anyArabicFields(TreeNode treeNode) {
        Iterator<String> fieldNameIt = treeNode.fieldNames();
        while (fieldNameIt.hasNext()) {
            if (ARABIC_FIELDS.contains(fieldNameIt.next())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<MorphoAnalysis> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        /*
         * This will be entered pointing to the array start.
         */
        if (jp.getCurrentToken() != JsonToken.START_ARRAY) {
            throw ctxt.wrongTokenException(jp, JsonToken.START_ARRAY, "Expected array of items");
        }

        List<MorphoAnalysis> result = Lists.newArrayList();
        while (jp.nextToken() != JsonToken.END_ARRAY) {
            // pick up the item as a tree.
            MorphoAnalysis analysis;
            TreeNode tempTree = jp.readValueAsTree();
            // and now something that will need cleaning up.
            if (tempTree.get("readings") != null) {
                analysis = jp.getCodec().treeToValue(tempTree, HanMorphoAnalysis.class);
            } else if (anyArabicFields(tempTree)) {
                analysis = jp.getCodec().treeToValue(tempTree, ArabicMorphoAnalysis.class);
            } else {
                analysis = jp.getCodec().treeToValue(tempTree, MorphoAnalysis.class);
            }
            result.add(analysis);
        }
        return ImmutableList.copyOf(result);
    }
}
