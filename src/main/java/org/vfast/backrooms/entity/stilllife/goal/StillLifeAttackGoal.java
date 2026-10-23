package org.vfast.backrooms.entity.stilllife.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.vfast.backrooms.entity.stilllife.StillLife;

/**
 * Chases and hits the target, unless the Still Life is petrified by someone looking at it.
 */
public class StillLifeAttackGoal extends MeleeAttackGoal {
    private final StillLife stillLife;

    public StillLifeAttackGoal(StillLife stillLife, double speedModifier) {
        super(stillLife, speedModifier, true);
        this.stillLife = stillLife;
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity target) {
        if (!this.stillLife.isPetrified()) {
            super.checkAndPerformAttack(target);
        }
    }
}
