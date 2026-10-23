package org.vfast.backrooms.entity.stilllife;

import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Pose;

/**
 * The pose a Still Life is "copied" in. It is rolled once and never changes, except while riding.
 */
public enum StillLifeStance implements StringRepresentable {
    UPRIGHT("upright", 30),
    CROUCHED("crouched", 8),
    SLOUCHED("slouched", 10),
    ARMS_FORWARD("arms_forward", 8),
    ARMS_RAISED("arms_raised", 5),
    T_POSE("t_pose", 5),
    HEAD_DOWN("head_down", 8),
    HEAD_BACK("head_back", 5),
    BROKEN_NECK("broken_neck", 6),
    STIFF("stiff", 10),
    ARMS_BEHIND("arms_behind", 6),
    DISLOCATED("dislocated", 5);

    public static final Codec<StillLifeStance> CODEC = StringRepresentable.fromEnum(StillLifeStance::values);
    private static final int TOTAL_WEIGHT;

    private final String name;
    private final int weight;

    StillLifeStance(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    public Pose pose() {
        return this == CROUCHED ? Pose.CROUCHING : Pose.STANDING;
    }

    public static StillLifeStance byId(int id) {
        StillLifeStance[] values = values();
        return id >= 0 && id < values.length ? values[id] : UPRIGHT;
    }

    public static StillLifeStance roll(RandomSource random) {
        int pick = random.nextInt(TOTAL_WEIGHT);
        for (StillLifeStance stance : values()) {
            pick -= stance.weight;
            if (pick < 0) return stance;
        }
        return UPRIGHT;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    static {
        int total = 0;
        for (StillLifeStance stance : values()) total += stance.weight;
        TOTAL_WEIGHT = total;
    }
}
