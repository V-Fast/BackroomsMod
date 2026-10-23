package org.vfast.backrooms.entity.stilllife.goal;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;
import org.vfast.backrooms.entity.stilllife.StillLife;

import java.util.EnumSet;

/**
 * Short bursts of movement at random speeds towards random safe spots, cut off before arriving, mixed with sudden
 * stops, head snaps and jumps.
 */
public class ErraticMovementGoal extends Goal {
    private final StillLife stillLife;
    private boolean moving;
    private int phaseTicks;

    public ErraticMovementGoal(StillLife stillLife) {
        this.stillLife = stillLife;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        return this.stillLife.getRandom().nextInt(10) == 0;
    }

    @Override
    public boolean canContinueToUse() {
        return this.stillLife.getRandom().nextInt(600) != 0;
    }

    @Override
    public void start() {
        this.moving = false;
        this.nextPhase();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        RandomSource random = this.stillLife.getRandom();
        if (--this.phaseTicks <= 0 || (this.moving && this.stillLife.getNavigation().isDone())) {
            this.nextPhase();
        }

        if (!this.moving && random.nextInt(6) == 0) {
            this.stillLife.getLookControl().setLookAt(
                    this.stillLife.getX() + random.nextGaussian() * 6.0,
                    this.stillLife.getEyeY() + random.nextGaussian() * 2.0,
                    this.stillLife.getZ() + random.nextGaussian() * 6.0,
                    90.0F, 90.0F);
        } else if (this.moving && this.stillLife.onGround() && random.nextInt(40) == 0) {
            this.stillLife.getJumpControl().jump();
        }
    }

    private void nextPhase() {
        RandomSource random = this.stillLife.getRandom();
        if (!this.moving || random.nextFloat() < 0.65F) {
            // LandRandomPos only returns walkable spots away from water and other dangers
            Vec3 pos = LandRandomPos.getPos(this.stillLife, 4 + random.nextInt(8), 3);
            if (pos != null && this.stillLife.getNavigation().moveTo(pos.x, pos.y, pos.z, 0.6 + random.nextDouble() * 1.3)) {
                this.moving = true;
                this.phaseTicks = this.adjustedTickDelay(8 + random.nextInt(32));
                return;
            }
        }
        this.stillLife.getNavigation().stop();
        this.moving = false;
        this.phaseTicks = this.adjustedTickDelay(4 + random.nextInt(40));
    }

    @Override
    public void stop() {
        this.stillLife.getNavigation().stop();
    }
}
