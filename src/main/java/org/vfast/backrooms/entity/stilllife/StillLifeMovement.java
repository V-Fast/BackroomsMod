package org.vfast.backrooms.entity.stilllife;

import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;

public enum StillLifeMovement implements StringRepresentable {
    /** Never moves, never turns. Hostile ones still strike anything in reach. */
    STILL("still", 20),
    /** Strolls around and sometimes follows a player for a little while. */
    WANDERER("wanderer", 30),
    /** Often follows players for a short distance before losing interest. */
    FOLLOWER("follower", 15),
    /** Bursts of movement in random directions, sudden stops and head snaps. */
    ERRATIC("erratic", 20),
    /** Behaves like a wanderer, but freezes completely while a player looks at it. */
    STATUE("statue", 15);

    public static final Codec<StillLifeMovement> CODEC = StringRepresentable.fromEnum(StillLifeMovement::values);
    private static final int TOTAL_WEIGHT;

    private final String name;
    private final int weight;

    StillLifeMovement(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    public static StillLifeMovement roll(RandomSource random) {
        int pick = random.nextInt(TOTAL_WEIGHT);
        for (StillLifeMovement movement : values()) {
            pick -= movement.weight;
            if (pick < 0) return movement;
        }
        return WANDERER;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    static {
        int total = 0;
        for (StillLifeMovement movement : values()) total += movement.weight;
        TOTAL_WEIGHT = total;
    }
}
