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
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utility class with methods for conversion of the data from
 * the pre-1.0.0 data model to the 1.0.0 data model.
 */
@SuppressWarnings("deprecation")
final class ConvertFromPreAdm1 {

    private ConvertFromPreAdm1() {
        //
    }

    static Mention convertMention(EntityMention em) {
        Mention.Builder mentionBuilder = new Mention.Builder(em.getStartOffset(), em.getEndOffset(),
                em.getEntityType());
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

        return mentionBuilder.build();
    }

    /*
         * Called when we have neither resolved entities nor coreference.
         */
    static void doTrivialConversion(ListAttribute<EntityMention> oldMentions, ImmutableMap.Builder<String, BaseAttribute> builder) {
        ListAttribute.Builder<Entity> entityListBuilder = new ListAttribute.Builder<>(Entity.class);
        convertMentionList(oldMentions, entityListBuilder);
        if (oldMentions.getExtendedProperties() != null) {
            for (Map.Entry<String, Object> me : oldMentions.getExtendedProperties().entrySet()) {
                entityListBuilder.extendedProperty("mention." + me.getKey(),
                        me.getValue());
            }
        }
        builder.put(AttributeKey.ENTITY.key(), entityListBuilder.build());
    }

    static void convertMentionList(List<EntityMention> unresolved, ListAttribute.Builder<Entity> entityListBuilder) {
        for (EntityMention em : unresolved) {
            Mention mention = convertMention(em);
            Entity.Builder entityBuilder = new Entity.Builder();
            entityBuilder.headMentionIndex(0);
            entityBuilder.mention(mention);
            entityListBuilder.add(entityBuilder.build());
        }
    }

    static void doResolvedConversion(ListAttribute<EntityMention> oldMentions,
                                     ListAttribute<ResolvedEntity> oldResolved,
                                     ImmutableMap.Builder<String, BaseAttribute> builder) {
        if (oldMentions == null) {
            throw new RosetteRuntimeException("There are no EntityMentions.");
        } else {
            doResolvedConversionWithMentions(oldMentions, oldResolved, builder);
        }
    }

    static void doResolvedConversionWithMentions(ListAttribute<EntityMention> oldMentions,
                                                 ListAttribute<ResolvedEntity> oldResolved,
                                                 ImmutableMap.Builder<String, BaseAttribute> builder) {
        List<EntityMention> unresolved = Lists.newArrayList();
        Map<Integer, List<EntityMention>> entityMentionsByChain = Maps.newHashMap();

        Set<Integer> resolvedCorefChains = Sets.newHashSet();
        for (ResolvedEntity resolvedEntity : oldResolved) {
            resolvedCorefChains.add(resolvedEntity.getCoreferenceChainId());
        }

        for (EntityMention entityMention : oldMentions) {
            if (entityMention.getCoreferenceChainId() == null
                    || !resolvedCorefChains.contains(entityMention.getCoreferenceChainId())) {
                unresolved.add(entityMention);
            }
            if (entityMention.getCoreferenceChainId() != null) {
                List<EntityMention> chainList = entityMentionsByChain.get(entityMention.getCoreferenceChainId());
                if (chainList == null) {
                    chainList = Lists.newArrayList();
                    entityMentionsByChain.put(entityMention.getCoreferenceChainId(), chainList);
                }
                chainList.add(entityMention);
            }
        }

        ListAttribute.Builder<Entity> entityListBuilder = new ListAttribute.Builder<>(Entity.class);

        if (oldMentions.getExtendedProperties() != null) {
            for (Map.Entry<String, Object> me : oldMentions.getExtendedProperties().entrySet()) {
                entityListBuilder.extendedProperty("mention." + me.getKey(),
                        me.getValue());
            }
        }
        if (oldResolved.getExtendedProperties() != null) {
            for (Map.Entry<String, Object> me : oldResolved.getExtendedProperties().entrySet()) {
                entityListBuilder.extendedProperty(me.getKey(), me.getValue());
            }
        }


        for (ResolvedEntity resolvedEntity : oldResolved) {
            Entity.Builder entityBuilder = new Entity.Builder();
            if (resolvedEntity.getConfidence() != null) {
                entityBuilder.confidence(resolvedEntity.getConfidence());
            }
            if (resolvedEntity.getEntityId() != null) {
                entityBuilder.entityId(resolvedEntity.getEntityId());
            }
            if (resolvedEntity.getSentiment() != null) {
                entityBuilder.sentiment(resolvedEntity.getSentiment());
            }
            if (resolvedEntity.getExtendedProperties() != null
                    && resolvedEntity.getExtendedProperties().size() != 0) {
                entityBuilder.extendedProperties(resolvedEntity.getExtendedProperties());
            }

            if (resolvedEntity.getCoreferenceChainId() != null) {
                entityBuilder.extendedProperty("oldCoreferenceChainId", resolvedEntity.getCoreferenceChainId());
            }

            List<EntityMention> entityMentions = entityMentionsByChain.get(resolvedEntity.getCoreferenceChainId());
            int index = 0;
            for (EntityMention entityMention : entityMentions) {
                Mention mention = convertMention(entityMention);
                entityBuilder.mention(mention);
                if (entityMention.getStartOffset() == resolvedEntity.getStartOffset()
                        && entityMention.getEndOffset() == resolvedEntity.getEndOffset()) {
                    entityBuilder.headMentionIndex(index);
                }
                index++;
            }
            entityListBuilder.add(entityBuilder.build());
        }

        convertMentionList(unresolved, entityListBuilder);
        builder.put(AttributeKey.ENTITY.key(), entityListBuilder.build());
    }

    // no resolution, perhaps indoc coref.
    static void doUnresolvedConversion(ListAttribute<EntityMention> oldMentions, ImmutableMap.Builder<String, BaseAttribute> builder) {
        int maxChainId = -1;
        int unchainedCount = 0;
        for (EntityMention em : oldMentions) {
            if (em.getCoreferenceChainId() != null) {
                maxChainId = Math.max(em.getCoreferenceChainId(), maxChainId);
            } else {
                unchainedCount++;
            }
        }
        if (maxChainId == -1) {
            doTrivialConversion(oldMentions, builder);
            return;
        }

        // we want one Entity per coref chain (or entity with no coref chain).

        int entityCount = maxChainId + unchainedCount + 1;
        List<List<EntityMention>> mentionsByEntities = Lists.newArrayListWithExpectedSize(entityCount);
        for (int x = 0; x < entityCount; x++) {
            mentionsByEntities.add(Lists.<EntityMention>newArrayList());
        }

        int unchainedIndex = maxChainId + 1;
        for (EntityMention em : oldMentions) {
            if (em.getCoreferenceChainId() != null) {
                mentionsByEntities.get(em.getCoreferenceChainId()).add(em);
            } else {
                mentionsByEntities.get(unchainedIndex++).add(em);
            }
        }

        ListAttribute.Builder<Entity> elBuilder = new ListAttribute.Builder<>(Entity.class);

        // now we can just walk mentionsByEntities to build the results.
        for (List<EntityMention> entityMentions : mentionsByEntities) {
            Entity.Builder enBuilder = new Entity.Builder();
            for (EntityMention em : entityMentions) {
                Mention mention = convertMention(em);
                enBuilder.mention(mention);
            }
            enBuilder.headMentionIndex(0); // TODO: Is this correct?
            elBuilder.add(enBuilder.build());
        }

        if (oldMentions.getExtendedProperties() != null) {
            for (Map.Entry<String, Object> me : oldMentions.getExtendedProperties().entrySet()) {
                elBuilder.extendedProperty("mention." + me.getKey(),
                        me.getValue());
            }
        }

        builder.put(AttributeKey.ENTITY.key(), elBuilder.build());
    }
}
