package com.lumination.backrooms.levels;

/**
 * A level's security level. Depending on if the player could live in a level or not
 */
public enum SafetyLevels {
    /**
     * When you can freely live in the specified level
     */
    SAFE,

    /**
     * You can live in there but there are a few entities
     */
    NEUTRAL,

    /**
     * There are entities that try to kill the player
     */
    MEDIUM,

    /**
     * You cannot live in there
     */
    HIGH,

    /**
     * Instant death unless?
     */
    IMPOSSIBLE,

    /**
     * The specified level is different to some people
     */
    VARIABLE,

    /**
     * No one knows
     */
    UNKNOWN,
}
