package org.vfast.backrooms.entity.stilllife.goal;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.vfast.backrooms.entity.stilllife.StillLife;

import java.util.EnumSet;

/**
 * Follows a nearby player for a short distance or time, then loses interest for a while.
 */
public class FollowPlayerBrieflyGoal extends Goal {
    private static final double NOTICE_DISTANCE = 16.0;
    private static final double KEEP_DISTANCE = 3.0;

    private final StillLife stillLife;
    private final double speedModifier;
    /** On average, how many checks it takes before it starts following someone. */
    private final int rarity;
    private @Nullable Player player;
    private Vec3 origin = Vec3.ZERO;
    private double maxTravel;
    private int ticksLeft;
    private int repathTicks;
    private long nextAllowedTime;

    public FollowPlayerBrieflyGoal(StillLife stillLife, double speedModifier, int rarity) {
        this.stillLife = stillLife;
        this.speedModifier = speedModifier;
        this.rarity = rarity;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.stillLife.level().getGameTime() < this.nextAllowedTime || this.stillLife.getRandom().nextInt(this.rarity) != 0) {
            return false;
        }
        Player nearest = this.stillLife.level().getNearestPlayer(this.stillLife, NOTICE_DISTANCE);
        if (nearest == null || nearest.isSpectator() || !this.stillLife.getSensing().hasLineOfSight(nearest)) return false;

        this.player = nearest;
        return true;
    }

    @Override
    public void start() {
        this.origin = this.stillLife.position();
        this.maxTravel = 8.0 + this.stillLife.getRandom().nextInt(12);
        this.ticksLeft = this.adjustedTickDelay(80 + this.stillLife.getRandom().nextInt(160));
        this.repathTicks = 0;
    }

    @Override
    public boolean canContinueToUse() {
        return this.player != null && this.player.isAlive() && !this.player.isSpectator()
                && this.ticksLeft > 0
                && this.stillLife.position().distanceToSqr(this.origin) < this.maxTravel * this.maxTravel
                && this.stillLife.distanceToSqr(this.player) < NOTICE_DISTANCE * NOTICE_DISTANCE * 2.0;
    }

    @Override
    public void tick() {
        if (this.player == null) return;
        this.ticksLeft--;
        this.stillLife.getLookControl().setLookAt(this.player, 10.0F, this.stillLife.getMaxHeadXRot());

        if (--this.repathTicks <= 0) {
            this.repathTicks = this.adjustedTickDelay(10);
            if (this.stillLife.distanceToSqr(this.player) > KEEP_DISTANCE * KEEP_DISTANCE) {
                this.stillLife.getNavigation().moveTo(this.player, this.speedModifier);
            } else {
                this.stillLife.getNavigation().stop();
            }
        }
    }

    @Override
    public void stop() {
        this.player = null;
        this.stillLife.getNavigation().stop();
        this.nextAllowedTime = this.stillLife.level().getGameTime() + 400 + this.stillLife.getRandom().nextInt(800);
    }
}
