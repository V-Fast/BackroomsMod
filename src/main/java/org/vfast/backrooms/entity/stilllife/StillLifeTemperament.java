package org.vfast.backrooms.entity.stilllife;

import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;

public enum StillLifeTemperament implements StringRepresentable {
    /** Never deals damage. */
    PASSIVE("passive"),
    /** Hunts players on sight. */
    HOSTILE("hostile"),
    /** Harmless until something hurts it, then hunts the attacker. */
    VENGEFUL("vengeful");

    public static final Codec<StillLifeTemperament> CODEC = StringRepresentable.fromEnum(StillLifeTemperament::values);

    private final String name;

    StillLifeTemperament(String name) {
        this.name = name;
    }

    public boolean canAttack() {
        return this != PASSIVE;
    }

    /**
     * @param troubled whether the copied person was in a bad state, which makes violent copies far more likely
     */
    public static StillLifeTemperament roll(RandomSource random, boolean troubled) {
        float roll = random.nextFloat();
        float hostileChance = troubled ? 0.6F : 0.3F;
        if (roll < hostileChance) return HOSTILE;
        if (roll < hostileChance + 0.2F) return VENGEFUL;
        return PASSIVE;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
