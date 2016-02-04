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

package com.basistech.rosette.dm.internal;

import com.basistech.rosette.dm.EntityMention;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * <code>Mention</code> class for internal use only.  Not to be confused
 * with the public <code>EntityMention</code> class.
 */
public class Mention {
    private final TextWrapper textWrapper;
    private final int entityIndex;
    private final EntityMention entityMention;

    public Mention(TextWrapper textWrapper, int entityIndex) {
        this.textWrapper = textWrapper;
        this.entityIndex = entityIndex;
        this.entityMention = textWrapper.getText().getEntityMentions().get(entityIndex);
    }

    public static List<Mention> getMentions(TextWrapper tw) {
        List<Mention> mentions = Lists.newArrayList();
        if (tw.getText().getEntityMentions() != null) {
            for (int i = 0; i < tw.getText().getEntityMentions().size(); i++) {
                mentions.add(new Mention(tw, i));
            }
        }
        return mentions;
    }

    public int getStartTokenIndex() {
        return textWrapper.getMentionStartTokenIndex(entityIndex);
    }

    public int getEndTokenIndex() {
        return textWrapper.getMentionEndTokenIndex(entityIndex);
    }
    
    public int getStartCharacterOffset() {
        return entityMention.getStartOffset();
    }
    
    public int getEndCharacterOffset() {
        return entityMention.getEndOffset();
    }
            
    public String getEntityType() {
        return entityMention.getEntityType();
    }

    public String getNormalizedText() {
        return entityMention.getNormalized();
    }

    /**
     * Returns the in-document coreference chain identifier. If no in-document coreference chain information
     * is present then returns null.
     * @return chain identifier or null.
     */
    public Integer getIndocChainId() {
        return entityMention.getCoreferenceChainId();
    }

    public int getEntityIndex() {
        return entityIndex;
    }

    public boolean isHeadMention() {
        return getEntityIndex() == getIndocChainId();
    }

    public Mention getHeadMentionOfChain() {
        if (isHeadMention()) {
            return this;
        } else {
            return new Mention(textWrapper, getIndocChainId());
        }
    }

    public boolean isChainedTo(Mention other) {
        Integer indocChainId = getIndocChainId();
        return indocChainId != null && indocChainId.equals(other.getIndocChainId());
    }

    public String toString() {
        return String.format("%d: %s, [%d, %d), %s", getEntityIndex(), getEntityType(),
                getStartTokenIndex(), getEndTokenIndex(), getNormalizedText());
    }
}
