package org.vfast.backrooms.interfaces;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public interface DarknessDamage {
    int TICK_PREVENT = 30;
    int TICK_ATTACK = 700; // 35 seconds
    int ATTACK_TICK_RATE = 25;

    default int getBlockLight(LivingEntity entity) {
        Level level = entity.level();
        return level.getLightEngine().getRawBrightness(entity.blockPosition(), 999); // remove skylight
    }

    default void attackEntity() {
        if (this.shouldPreventAndAttack()) {
            this.prevent();
        }
        this.performAttack();
    }

    default boolean shouldPreventAndAttack() {
        return true;
    }

    void tickDarkness();
    void prevent();
    void performAttack();
}
