package org.vfast.backrooms.entity.stilllife;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

/**
 * Imperfections of a Still Life's body. Fully derived from a seed so only that seed needs to be saved and synced.
 *
 * @param neckStretch extra gap between the head and the body, in model pixels
 * @param duplicates bitmask of {@link #DUPLICATE_HEAD} and the other DUPLICATE_ flags
 * @param duplicateSide 1 or -1, which side duplicated parts grow on
 */
public record StillLifeAnatomy(
        float headScale,
        float neckStretch,
        float headTilt,
        float torsoTwist,
        float leftArmLength,
        float rightArmLength,
        float leftLegLength,
        float rightLegLength,
        int duplicates,
        float duplicateSide
) {
    public static final int DUPLICATE_HEAD = 1;
    public static final int DUPLICATE_LEFT_ARM = 1 << 1;
    public static final int DUPLICATE_RIGHT_ARM = 1 << 2;
    public static final int DUPLICATE_LEFT_LEG = 1 << 3;
    public static final int DUPLICATE_RIGHT_LEG = 1 << 4;

    public static StillLifeAnatomy fromSeed(int seed) {
        RandomSource random = RandomSource.create(seed);

        float headScale = 1.0F;
        if (random.nextFloat() < 0.3F) {
            headScale = random.nextBoolean() ? Mth.nextFloat(random, 0.65F, 0.85F) : Mth.nextFloat(random, 1.15F, 1.4F);
        }
        float neckStretch = random.nextFloat() < 0.12F ? Mth.nextFloat(random, 1.5F, 4.0F) : 0.0F;
        float headTilt = random.nextFloat() < 0.25F ? signed(random, 0.2F, 0.7F) : 0.0F;
        float torsoTwist = random.nextFloat() < 0.1F ? signed(random, 0.15F, 0.4F) : 0.0F;
        float leftArmLength = random.nextFloat() < 0.25F ? Mth.nextFloat(random, 0.7F, 1.6F) : 1.0F;
        float rightArmLength = random.nextFloat() < 0.25F ? Mth.nextFloat(random, 0.7F, 1.6F) : 1.0F;
        float leftLegLength = random.nextFloat() < 0.15F ? Mth.nextFloat(random, 0.75F, 0.92F) : 1.0F;
        float rightLegLength = random.nextFloat() < 0.15F ? Mth.nextFloat(random, 0.75F, 0.92F) : 1.0F;

        int duplicates = 0;
        if (random.nextFloat() < 0.06F) duplicates |= DUPLICATE_HEAD;
        if (random.nextFloat() < 0.08F) duplicates |= DUPLICATE_LEFT_ARM;
        if (random.nextFloat() < 0.08F) duplicates |= DUPLICATE_RIGHT_ARM;
        if (random.nextFloat() < 0.06F) duplicates |= DUPLICATE_LEFT_LEG;
        if (random.nextFloat() < 0.06F) duplicates |= DUPLICATE_RIGHT_LEG;

        return new StillLifeAnatomy(headScale, neckStretch, headTilt, torsoTwist, leftArmLength, rightArmLength, leftLegLength, rightLegLength, duplicates, random.nextBoolean() ? 1.0F : -1.0F);
    }

    public boolean hasDuplicate(int part) {
        return (this.duplicates & part) != 0;
    }

    private static float signed(RandomSource random, float min, float max) {
        float value = Mth.nextFloat(random, min, max);
        return random.nextBoolean() ? value : -value;
    }
}
