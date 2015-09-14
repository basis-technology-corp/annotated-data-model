/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2009 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/

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
