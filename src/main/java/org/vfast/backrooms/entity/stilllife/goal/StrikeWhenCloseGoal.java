package org.vfast.backrooms.entity.stilllife.goal;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import org.vfast.backrooms.entity.stilllife.StillLife;

import java.util.EnumSet;

/**
 * Attack for Still Lives that never move: they only strike what comes within reach.
 */
public class StrikeWhenCloseGoal extends Goal {
    private final StillLife stillLife;
    private int cooldown;

    public StrikeWhenCloseGoal(StillLife stillLife) {
        this.stillLife = stillLife;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.stillLife.getTarget();
        return target != null && target.isAlive() && this.stillLife.isWithinMeleeAttackRange(target) && this.stillLife.getSensing().hasLineOfSight(target);
    }

    @Override
    public void start() {
        this.cooldown = this.adjustedTickDelay(5);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = this.stillLife.getTarget();
        if (target == null) return;

        this.stillLife.getLookControl().setLookAt(target, 30.0F, 30.0F);
        if (--this.cooldown <= 0) {
            this.cooldown = this.adjustedTickDelay(20);
            this.stillLife.swingForAttack(InteractionHand.MAIN_HAND);
            this.stillLife.doHurtTarget(getServerLevel(this.stillLife), target);
        }
    }
}
