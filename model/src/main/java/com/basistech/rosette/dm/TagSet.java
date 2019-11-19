/*
 * Copyright 2019 Basis Technology Corp.
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

/**
 * Enumeration for part of speech tag sets used in Basis products.
 */
public enum TagSet {

    /**
     * UPT-16 version 1 tag set
     */
    UPT16_V1,

    /**
     * Basis Technology's language neutral tag set, used for emojis, emoticons, hashtags, etc.
     */
    BT_LANGUAGE_NEUTRAL,

    /**
     * Basis Technology's tag set for Arabic
     */
    BT_ARABIC,

    /**
     * Basis Technology's tag set for Chinese which was added in RBL-JE 7.16.0.c58.2.
     * This is the preferred tag set for Chinese
     */
    BT_CHINESE,

    /**
     * Basis Technology's tag set for Chinese which was added in RBL-JE 2.3.0
     */
    BT_CHINESE_RBLJE_2,

    /**
     * Basis Technology's tag set for Czech
     */
    BT_CZECH,

    /**
     * Basis Technology's tag set for Dutch
     */
    BT_DUTCH,

    /**
     * Basis Technology's tag set for English
     */
    BT_ENGLISH,

    /**
     * Basis Technology's tag set for French
     */
    BT_FRENCH,

    /**
     * Basis Technology's tag set for German
     */
    BT_GERMAN,

    /**
     * Basis Technology's tag set for Greek
     */
    BT_GREEK,

    /**
     * The MILA tag set for Hebrew
     */
    MILA_HEBREW,

    /**
     * Basis Technology's tag set for Hungarian
     */
    BT_HUNGARIAN,

    /**
     * Basis Technology's tag set for Italian
     */
    BT_ITALIAN,

    /**
     * Basis Technology's tag set for Japanese which was added in RBL-JE 7.13.0.c56.6.
     * This is the preferred tag set for Japanese
     */
    BT_JAPANESE,

    /**
     * Basis Technology's tag set for Japanese which was added in RBL-JE 2.0.0
     */
    BT_JAPANESE_RBLJE_2,

    /**
     * Basis Technology's tag set for Korean
     */
    BT_KOREAN,

    /**
     * Basis Technology's tag set for Persian
     */
    BT_PERSIAN,

    /**
     * Basis Technology's tag set for Polish
     */
    BT_POLISH,

    /**
     * Basis Technology's tag set for Portuguese
     */
    BT_PORTUGUESE,

    /**
     * Basis Technology's tag set for Russian
     */
    BT_RUSSIAN,

    /**
     * Basis Technology's tag set for Spanish
     */
    BT_SPANISH,

    /**
     * Basis Technology's tag set for Urdu
     */
    BT_URDU,

    /**
     * Penn Treebank tag set for English
     */
    PTB_ENGLISH,
}
