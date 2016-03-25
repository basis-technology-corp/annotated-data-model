/*
* Copyright 2016 Basis Technology Corp.
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
package com.basistech.rosette.dm;

import com.basistech.rosette.RosetteRuntimeException;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Utility class with methods for conversion of the data from
 * the pre-1.1.0 data model to the 1.1.0 data model.
 */
@SuppressWarnings("deprecation")
final class ConvertFromPreAdm11 {

    private ConvertFromPreAdm11() {
        //
    }

    static Mention convertMention(EntityMention em) {
        Mention.Builder mentionBuilder = new Mention.Builder(em.getStartOffset(), em.getEndOffset());
        if (em.getConfidence() != null) {
            mentionBuilder.confidence(em.getConfidence());
        }
        if (em.getFlags() != null && em.getFlags() != 0) {
            mentionBuilder.extendedProperty("oldFlags", em.getFlags());
        }
        if (em.getCoreferenceChainId() != null) {
            mentionBuilder.extendedProperty("oldCoreferenceChainId", em.getCoreferenceChainId());
        }
        if (em.getNormalized() != null) {
            mentionBuilder.normalized(em.getNormalized());
        }
        if (em.getSource() != null) {
            mentionBuilder.source(em.getSource());
        }
        if (em.getSubsource() != null) {
            mentionBuilder.subsource(em.getSubsource());
        }

        if (em.getExtendedProperties() != null && !em.getExtendedProperties().isEmpty()) {
            for (Map.Entry<String, Object> me : em.getExtendedProperties().entrySet()) {
                mentionBuilder.extendedProperty(me.getKey(), me.getValue());
            }
        }

        mentionBuilder.extendedProperty("old-entity-type", em.getEntityType());

        return mentionBuilder.build();
    }

    /*
     * Each mention can in one of two states:
     * 1: attached to a 'ResolvedEntity'
     * 2: part of a coref chain; perhaps a singleton.
     *
     * In the second case, we need the same processing as we have
     * below in unresolved conversion; how to factor?
     */


    // there are at least indoc chains
    static void doResolvedConversion(ListAttribute<Entity> newResolved,
                                     ListAttribute<EntityMention> oldMentions,
                                     ListAttribute<ResolvedEntity> oldResolved,
                                     ImmutableMap.Builder<String, BaseAttribute> builder) {

        if (oldMentions == null) {
            doNoMentionConversion(oldResolved, builder);
            return;
        }

        int maxChainId = -1;
        for (EntityMention oldMention : oldMentions) {
            if (oldMention.getCoreferenceChainId() != null) {
                maxChainId = Math.max(maxChainId, oldMention.getCoreferenceChainId());
            }
        }
        maxChainId = Math.max(maxChainId, oldMentions.size());

        ResolvedEntity[] resolvedByChainId = new ResolvedEntity[maxChainId + 1];
        if (oldResolved != null) {
            for (ResolvedEntity resolvedEntity : oldResolved) {
                if (resolvedEntity.getCoreferenceChainId() != null) {
                    resolvedByChainId[resolvedEntity.getCoreferenceChainId()] = resolvedEntity;
                } else {
                    throw new RosetteRuntimeException("Resolved entity with no coref chain id.");
                }
            }
        }

        // Note that indoc chain ids can be sparse, or altogether absent.
        // Absent is important, as it means that no indoc happened.
        // If any indoc happened, all the mentions have indoc chains.
        boolean indocPresent = oldMentions.get(0).getCoreferenceChainId() != null;


        int[] newIndices = new int[maxChainId + 1];
        //chain ids cannot be larger than the count of mentions.
        int[] chainToIndex = new int[maxChainId + 1];
        Arrays.fill(chainToIndex, -1);
        int newEntityCount = 0;

        for (int oldIndex = 0; oldIndex < oldMentions.size(); oldIndex++) {
            EntityMention em = oldMentions.get(oldIndex);
            if (em.getCoreferenceChainId() != null) {
                if (chainToIndex[em.getCoreferenceChainId()] == -1) {
                    chainToIndex[em.getCoreferenceChainId()] = newEntityCount++;
                }
                newIndices[oldIndex] = chainToIndex[em.getCoreferenceChainId()];
            } else {
                newIndices[oldIndex] = newEntityCount++;
            }
        }

        List<List<EntityMention>> mentionsByEntities = Lists.newArrayListWithExpectedSize(newEntityCount);
        for (int x = 0; x < newEntityCount; x++) {
            mentionsByEntities.add(Lists.<EntityMention>newArrayList());
        }

        /* For each coref chain, the head is the entity whose index in the old mentions
         * is equal to the chain id.
         */
        int[] heads = new int[newEntityCount];

        for (int oldIndex = 0; oldIndex < oldMentions.size(); oldIndex++) {
            EntityMention em = oldMentions.get(oldIndex);
            int newIndex = newIndices[oldIndex];
            mentionsByEntities.get(newIndex).add(em);
            if (em.getCoreferenceChainId() != null && em.getCoreferenceChainId() == oldIndex) {
                heads[newIndex] = mentionsByEntities.get(newIndex).size() - 1;
            }
        }


        ListAttribute.Builder<Entity> elBuilder = buildEntities(newResolved, oldMentions, resolvedByChainId, indocPresent, mentionsByEntities, heads);

        builder.put(AttributeKey.ENTITY.key(), elBuilder.build());
    }

    // We can have old resolved entities without mentions.
    private static void doNoMentionConversion(ListAttribute<ResolvedEntity> oldResolved, ImmutableMap.Builder<String, BaseAttribute> builder) {
        ListAttribute.Builder<Entity> elBuilder = new ListAttribute.Builder<>(Entity.class);
        for (ResolvedEntity resolvedEntity : oldResolved) {
            Entity.Builder enBuilder = new Entity.Builder();
            convertOneEntity(enBuilder, resolvedEntity);
            elBuilder.add(enBuilder.build());
        }
        builder.put(AttributeKey.ENTITY.key(), elBuilder.build());
    }

    private static ListAttribute.Builder<Entity> buildEntities(ListAttribute<Entity> newResolved, ListAttribute<EntityMention> oldMentions,
                                                               ResolvedEntity[] resolvedByChainId,
                                                               boolean indocPresent,
                                                               List<List<EntityMention>> mentionsByEntities,
                                                               int[] heads) {
        // Since we need to sort, put them in an ordinary list for a start.
        List<Entity> entities = Lists.newArrayList();
        if (newResolved != null) {
            entities.addAll(newResolved);
        }

        // now we can just walk mentionsByEntities to build the results.
        int newIndex = 0;
        for (List<EntityMention> entityMentions : mentionsByEntities) {
            Entity.Builder enBuilder = new Entity.Builder();
            String type = null;
            for (int x = 0; x < entityMentions.size(); x++) {
                EntityMention em = entityMentions.get(x);
                if (x == heads[newIndex]) {
                    type = em.getEntityType();
                }
                Mention mention = convertMention(em);
                enBuilder.mention(mention);
            }
            enBuilder.type(type);

            EntityMention entityMention = entityMentions.get(0);
            ResolvedEntity resolvedEntity = null;
            if (entityMention.getCoreferenceChainId() != null) {
                resolvedEntity = resolvedByChainId[entityMention.getCoreferenceChainId()];
            }

            if (resolvedEntity != null) {
                convertOneEntity(enBuilder, resolvedEntity);
            }

            if (indocPresent) {
                enBuilder.headMentionIndex(heads[newIndex]);
            }

            entities.add(enBuilder.build());
            newIndex++;
        }

        if (indocPresent) {
            Collections.sort(entities, new Comparator<Entity>() {
                @Override
                public int compare(Entity o1, Entity o2) {
                    return o1.getMentions().get(o1.getHeadMentionIndex()).getStartOffset()
                            - o2.getMentions().get(o2.getHeadMentionIndex()).getStartOffset();
                }
            });
        }

        ListAttribute.Builder<Entity> elBuilder = new ListAttribute.Builder<>(Entity.class);
        for (Entity entity : entities) {
            elBuilder.add(entity);
        }


        if (oldMentions.getExtendedProperties() != null) {
            for (Map.Entry<String, Object> me : oldMentions.getExtendedProperties().entrySet()) {
                elBuilder.extendedProperty("mention." + me.getKey(),
                        me.getValue());
            }
        }

        if (newResolved != null && newResolved.getExtendedProperties().size() != 0) {
            for (Map.Entry<String, Object> me : newResolved.getExtendedProperties().entrySet()) {
                elBuilder.extendedProperty(me.getKey(),  me.getValue());
            }
        }

        return elBuilder;
    }

    private static void convertOneEntity(Entity.Builder enBuilder, ResolvedEntity resolvedEntity) {
        enBuilder.entityId(resolvedEntity.getEntityId());
        enBuilder.confidence(resolvedEntity.getConfidence());
        if (resolvedEntity.getSentiment() != null) {
            // It's a list in the new code, a single item in the old code.
            enBuilder.sentiment(resolvedEntity.getSentiment());
        }
        if (resolvedEntity.getExtendedProperties() != null) {
            for (Map.Entry<String, Object> me : resolvedEntity.getExtendedProperties().entrySet()) {
                enBuilder.extendedProperty(me.getKey(), me.getValue());
            }
        }
        if (resolvedEntity.getCoreferenceChainId() != null) {
            enBuilder.extendedProperty("oldCoreferenceChainId", resolvedEntity.getCoreferenceChainId());
        }
    }
}
