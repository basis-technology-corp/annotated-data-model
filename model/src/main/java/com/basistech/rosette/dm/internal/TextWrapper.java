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

import com.basistech.rosette.RosetteRuntimeException;
import com.basistech.rosette.dm.AnnotatedText;
import com.basistech.rosette.dm.EntityMention;
import com.basistech.rosette.dm.ResolvedEntity;
import com.basistech.rosette.dm.LanguageDetection;
import com.basistech.rosette.dm.ListAttribute;
import com.basistech.rosette.dm.Sentence;
import com.basistech.rosette.dm.Token;
import com.basistech.util.ISO15924;
import com.basistech.util.LanguageCode;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <code>TextWrapper</code> provides fast access to mention information based
 * on token indexes.  The underlying <code>Text</code> object contains only
 * character offsets.
 */
public class TextWrapper {
    private static class TokenIndexPair {
        int startIndex;
        int endIndex;
        TokenIndexPair(int startIndex, int endIndex) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }
    }

    protected final AnnotatedText text;
    protected final Map<Integer, TokenIndexPair> entityMentionIndexToTokenIndexes;
    protected final Map<Integer, Mention> tokenIndexToMention;
    protected final int[] sentenceTokenEnds;
    protected final int maxChainId;
    protected final List<Mention> mentions;
    protected final Map<Integer, List<Integer>> chainIdToEntityMentionIndexes;
    protected final Map<Integer, ResolvedEntity> chainIdToResolvedEntity;

    /**
     * Constructs from <code>AnnotatedText</code>.
     *
     * @param text text object to wrap
     * @throws java.lang.IllegalArgumentException if the text object contains entity
     * mentions but no tokens
     */
    public TextWrapper(AnnotatedText text) {
        this.text = text;
        entityMentionIndexToTokenIndexes = Maps.newHashMap();
        tokenIndexToMention = Maps.newHashMap();
        chainIdToEntityMentionIndexes = Maps.newHashMap();
        chainIdToResolvedEntity = Maps.newHashMap();
        mentions = Mention.getMentions(this);

        Map<Integer, Integer> startCharOffsetToTokenIndex = Maps.newHashMap();
        Map<Integer, Integer> endCharOffsetToTokenIndex = Maps.newHashMap();
        ListAttribute<Token> tokens = text.getTokens();

        if (tokens != null) {
            for (int i = 0; i < tokens.size(); i++) {
                Token token = tokens.get(i);
                startCharOffsetToTokenIndex.put(token.getStartOffset(), i);
                endCharOffsetToTokenIndex.put(token.getEndOffset(), i);
            }
        }

        if (text.getSentences() != null) {
            if (text.getTokens() == null) {
                // I think a whitespace doc could produce a sentence with zero tokens
                // so we only check null, not empty.
                throw new IllegalArgumentException("If text has sentences it must also have tokens.");
            }
            sentenceTokenEnds = new int[text.getSentences().size()];
            int tokenIndex = 0;
            for (int sentIndex = 0; sentIndex < sentenceTokenEnds.length; sentIndex++) {
                Sentence sentence = text.getSentences().get(sentIndex);
                while (tokenIndex < tokens.size() && tokens.get(tokenIndex).getEndOffset() <= sentence.getEndOffset()) {
                    tokenIndex++;
                }
                sentenceTokenEnds[sentIndex] = tokenIndex;
            }
        } else {
            sentenceTokenEnds = new int[0];
        }

        int maxMentionChainId = -1;
        if (text.getEntityMentions() != null) {
            if (!text.getEntityMentions().isEmpty() && (text.getTokens() == null || text.getTokens().isEmpty())) {
                throw new IllegalArgumentException("If text has entityMentions it must also have tokens.");
            }
            for (int i = 0; i < text.getEntityMentions().size(); i++) {
                EntityMention mention = text.getEntityMentions().get(i);
                int startTokenIndex = startCharOffsetToTokenIndex.get(mention.getStartOffset());
                int endTokenIndex = 1 + endCharOffsetToTokenIndex.get(mention.getEndOffset());
                entityMentionIndexToTokenIndexes.put(i, new TokenIndexPair(startTokenIndex, endTokenIndex));
                for (int tokenIndex = startTokenIndex; tokenIndex < endTokenIndex; tokenIndex++) {
                    tokenIndexToMention.put(tokenIndex, getMention(i));
                }
                if (mention.getCoreferenceChainId() > maxMentionChainId) {
                    maxMentionChainId = mention.getCoreferenceChainId();
                }
                int chainId = mention.getCoreferenceChainId();
                List<Integer> entityIndexes = chainIdToEntityMentionIndexes.get(chainId);
                if (entityIndexes == null) {
                    entityIndexes = Lists.newArrayList();
                    chainIdToEntityMentionIndexes.put(chainId, entityIndexes);
                }
                entityIndexes.add(i);
            }
        }

        if (text.getResolvedEntities() != null) {
            for (ResolvedEntity resolvedEntity : text.getResolvedEntities()) {
                chainIdToResolvedEntity.put(resolvedEntity.getCoreferenceChainId(), resolvedEntity);
            }
        }

        this.maxChainId = maxMentionChainId;
    }

    /**
     * Returns a reference to the wrapped text object.
     *
     * @return reference to the wrapped text object
     */
    public AnnotatedText getText() {
        return text;
    }

    /**
     * Returns the start token index of a mention.
     *
     * @param i index of the mention
     * @return start token index of the mention
     */
    public int getMentionStartTokenIndex(int i) {
        return entityMentionIndexToTokenIndexes.get(i).startIndex;
    }

    /**
     * Returns the end token index (exclusive) of a mention.
     *
     * @param i index of the mention
     * @return end token index (exclusive) of the mention
     */
    public int getMentionEndTokenIndex(int i) {
        return entityMentionIndexToTokenIndexes.get(i).endIndex;
    }

    /**
     * Returns the maximum chain id.  Returns -1 if chaining has not been
     * performed.
     *
     * @return maximum chain id, or -1 if chaining has not been performed
     */
    public int getMaxChainId() {
        return maxChainId;
    }

    /**
     * Returns the language of this document.
     *
     * @return the language of this document
     */
    public LanguageCode getDocLang() {
        LanguageDetection attr = text.getWholeTextLanguageDetection();
        if (attr != null) {
            List<LanguageDetection.DetectionResult> results = attr.getDetectionResults();
            if (!results.isEmpty()) {
                // DetectionResult holds a ranked list of possible languages, but
                // we expect exactly one.
                return results.get(0).getLanguage();
            }
        }
        return LanguageCode.UNKNOWN;
    }

    /**
     * Returns the script of this document.
     *
     * @return the script of this document
     */
    public ISO15924 getDocScript() {
        LanguageDetection attr = text.getWholeTextLanguageDetection();
        if (attr != null) {
            List<LanguageDetection.DetectionResult> results = attr.getDetectionResults();
            if (!results.isEmpty()) {
                // DetectionResult holds a ranked list of possible languages, but
                // we expect exactly one.
                return results.get(0).getScript();
            }
        }
        return ISO15924.Zyyy;
    }

    /**
     * Returns a substring of the raw text. If startToken < 0, 0 is used.
     * If endToken > tokenCount, tokenCount is used.
     *
     * @param startToken start token index
     * @param endToken end token index
     * @return substring encompassed by the token range [startToken, endToken)
     */
    public String getRawText(int startToken, int endToken) {
        if (text.getTokens() == null) {
            throw new RosetteRuntimeException("no token annotation found");
        }

        if (startToken < 0) {
            startToken = 0;
        }
        List<Token> tokens = text.getTokens();
        int tokenCount = tokens.size();
        if (endToken > tokenCount) {
            endToken = tokenCount;
        }
        if (tokenCount > 0) {
            int charStart = tokens.get(startToken).getStartOffset();
            int charEnd = tokens.get(endToken - 1).getEndOffset();
            return text.subSequence(charStart, charEnd).toString();
        }
        return text.toString();
    }

    /**
     * Returns the mention at the given index.
     *
     * @param entityIndex entity index
     * @return the mention at the given index
     */
    public final Mention getMention(int entityIndex) {
        return mentions.get(entityIndex);
    }

    /**
     * Returns the {@link com.basistech.rosette.dm.ResolvedEntity} for the given chain ID.
     *
     * @param chainId the coreference chain ID of the entity
     * @return the resolved entity with the given chain ID, or {@code null} if
     *         there's no resolved entity associated with the chain ID
     */
    public final ResolvedEntity getResolvedEntity(int chainId) {
        return chainIdToResolvedEntity.get(chainId);
    }

    /**
     * Returns all head mentions in this document.
     * @return iterable over all head mentions in this document
     */
    public Iterable<Mention> headMentions() {
        return new HeadMentionIterable();
    }

    private class HeadMentionIterable implements Iterable<Mention> {
        @Override
        public Iterator<Mention> iterator() {
            return new LongestMentionIterator();
        }

        private class LongestMentionIterator implements Iterator<Mention> {
            private Set<Integer> chainIds;
            private Iterator<Integer> chainIdIterator;

            public LongestMentionIterator() {
                ImmutableSortedSet.Builder<Integer> builder = ImmutableSortedSet.naturalOrder();
                if (text.getEntityMentions() != null) {
                    for (EntityMention m : text.getEntityMentions()) {
                        int chainId = m.getCoreferenceChainId();
                        if (chainId != -1) {
                            builder.add(chainId);
                        }
                    }
                }
                chainIds = builder.build();
                chainIdIterator = chainIds.iterator();
            }

            @Override
            public boolean hasNext() {
                return chainIdIterator.hasNext();
            }

            @Override
            public Mention next() {
                int chainId = chainIdIterator.next();
                return getMention(chainId);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("mention cannot be removed");
            }
        }
    }

    /**
     * Returns the number of mentions in this document.
     *
     * @return the number of mentions in this document
     */
    public int numMentions() {
        return mentions.size();
    }

    /**
     * Returns a range of mentions. If startEntityIndex < 0, 0 is used.
     * If endEntityIndex > numMentions, numMentions is used.
     *
     * @param startEntityIndex start index
     * @param endEntityIndex end index
     * @return iterable from start to end (exclusive)
     */
    public Iterable<Mention> mentions(int startEntityIndex, int endEntityIndex) {
        return new MentionIterable(startEntityIndex, endEntityIndex);
    }

    /**
     * Returns a range of mentions from the given index to the end of this
     * document.
     *
     * @param startEntityIndex start index
     * @return iterable from the given index to the end of this document
     */
    public Iterable<Mention> mentions(int startEntityIndex) {
        return new MentionIterable(startEntityIndex, numMentions());
    }

    /**
     * Returns mentions in this document, in order.
     *
     * @return iterable over all mentions in the document, in order
     */
    public Iterable<Mention> mentions() {
        return new MentionIterable(0, numMentions());
    }

    private class MentionIterable implements Iterable<Mention> {
        private int startEntityIndex;
        private int endEntityIndex;

        public MentionIterable(int startEntityIndex, int endEntityIndex) {
            this.startEntityIndex = startEntityIndex;
            this.endEntityIndex = endEntityIndex;
            if (this.startEntityIndex < 0) {
                this.startEntityIndex = 0;
            }
            if (this.endEntityIndex > numMentions()) {
                this.endEntityIndex = numMentions();
            }
        }

        @Override
        public Iterator<Mention> iterator() {
            return new MentionIterator(startEntityIndex, endEntityIndex);
        }

        private class MentionIterator implements Iterator<Mention> {
            private int currentEntityIndex;
            private int endEntityIndex;


            public MentionIterator(int startEntityIndex, int endEntityIndex) {
                this.currentEntityIndex = startEntityIndex;
                this.endEntityIndex = endEntityIndex;
            }

            @Override
            public boolean hasNext() {
                return currentEntityIndex < endEntityIndex;
            }

            @Override
            public Mention next() {
                Mention mention = getMention(currentEntityIndex);
                currentEntityIndex++;
                return mention;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("mention cannot be removed");
            }
        }
    }

    /**
     * Returns the mention containing a given token.
     * Returns null if the token is not contained by a mention.
     *
     * @param tokenIndex token index
     * @return mention containing the token or null if no mention contains the token
     */
    public Mention getMentionForToken(int tokenIndex) {
        return tokenIndexToMention.get(tokenIndex);
    }

    /**
     * Returns the number of tokens in this document.
     *
     * @return the number of tokens in this document
     */
    public int numTokens() {
        ListAttribute<Token> attr = text.getTokens();
        return attr != null ? attr.size() : 0;
    }

    /**
     * Returns a list of mentions chained with the given mention.
     *
     * @param mention mention to query for chains
     * @param excludeHead if true, the head mention will be omitted from results
     * @return list of mentions chained with the given mention
     */
    public List<Mention> getChainForMention(Mention mention, boolean excludeHead) {
        List<Mention> result = Lists.newArrayList();
        int chainId = mention.getIndocChainId();
        List<Integer> indexes = chainIdToEntityMentionIndexes.get(chainId);
        if (indexes != null) {
            for (int i : indexes) {
                Mention m = mentions.get(i);
                if (!excludeHead || m.getIndocChainId() != i) {
                    result.add(mentions.get(i));
                }
            }
        }
        return result;
    }

    /**
     * Returns the sentence number containing the token.
     *
     * @param tokenIndex token index
     * @return sentence number of token. This is guaranteed to be >= 0.
     */
    public int getSentenceForToken(int tokenIndex) {
        return sentDetectionUsingBinarySearch(sentenceTokenEnds, tokenIndex);
    }

    /**
     * Provides a fast way to get a sentence number for a given index. For example,
     * if sentenceBounderies = {5, 11, 20} then for the token in index 12 the method returns 2
     * (i.e., the third sentence).
     * @param sentenceBoundaries
     * @param tokenIndex
     * @return The index in sentenceBoundaries that refers to the first token of tokenIndex's next sentence
     * (which we refer to as sentence number). Note that the method returns sentenceBoundaries.length if tokenIndex
     * is greater than the last index.
     */
    static int sentDetectionUsingBinarySearch(int[] sentenceBoundaries, int tokenIndex) {
        int low = 0;
        int high = sentenceBoundaries.length - 1;
        int mid;

        while (low <= high) {
            mid = (low + high) / 2;

            if (sentenceBoundaries[mid] < tokenIndex) {
                low = mid + 1;
            } else if (sentenceBoundaries[mid] > tokenIndex) {
                high = mid - 1;
            } else { //exact match
                return mid + 1; //plus one since each sentence boundary refers to the next sentence
            }
        }

        return high < 0 ? 0 : low;
    }
}
