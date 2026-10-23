package org.vfast.backrooms.entity.stilllife.goal;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.vfast.backrooms.entity.stilllife.StillLife;

import java.util.EnumSet;

/**
 * Runs away from any other Still Life it notices, until it is {@code safeDistance} blocks away from it.
 */
public class FleeStillLivesGoal extends Goal {
    private static final double NOTICE_DISTANCE = 14.0;
    private static final int ESCAPE_ATTEMPTS = 12;

    private final StillLife stillLife;
    private final double safeDistance;
    private final double speedModifier;
    private @Nullable StillLife threat;
    private @Nullable Path path;
    private long nextCheck;

    public FleeStillLivesGoal(StillLife stillLife, double safeDistance, double speedModifier) {
        this.stillLife = stillLife;
        this.safeDistance = safeDistance;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        long time = this.stillLife.level().getGameTime();
        if (time < this.nextCheck) return false;
        this.nextCheck = time + 10;

        this.threat = this.findThreat();
        if (this.threat == null) return false;

        this.path = this.findEscapePath(this.threat.position());
        if (this.path == null) {
            this.nextCheck = time + 40;
            return false;
        }
        return true;
    }

    private @Nullable StillLife findThreat() {
        StillLife closest = null;
        double closestDistance = NOTICE_DISTANCE * NOTICE_DISTANCE;
        for (StillLife other : this.stillLife.level().getEntitiesOfClass(StillLife.class, this.stillLife.getBoundingBox().inflate(NOTICE_DISTANCE, 4.0, NOTICE_DISTANCE))) {
            if (other == this.stillLife || !other.isAlive()) continue;
            double distance = this.stillLife.distanceToSqr(other);
            if (distance < closestDistance && this.stillLife.getSensing().hasLineOfSight(other)) {
                closest = other;
                closestDistance = distance;
            }
        }
        return closest;
    }

    /**
     * Looks for a reachable spot at least {@link #safeDistance} away from the threat. If none is found, falls back to
     * the furthest reachable spot so it still puts some distance between them.
     */
    private @Nullable Path findEscapePath(Vec3 threatPos) {
        Path fallback = null;
        double fallbackDistance = this.stillLife.position().distanceTo(threatPos);
        for (int i = 0; i < ESCAPE_ATTEMPTS; i++) {
            Vec3 pos = LandRandomPos.getPosAway(this.stillLife, this.safeDistance * 0.6, this.safeDistance + 4.0, 7, threatPos);
            if (pos == null) continue;

            double distance = pos.distanceTo(threatPos);
            if (distance <= fallbackDistance) continue;

            Path candidate = this.stillLife.getNavigation().createPath(pos.x, pos.y, pos.z, 0);
            if (candidate == null || !candidate.canReach()) continue;
            if (distance >= this.safeDistance) return candidate;

            fallback = candidate;
            fallbackDistance = distance;
        }
        return fallback;
    }

    @Override
    public void start() {
        this.stillLife.getNavigation().moveTo(this.path, this.speedModifier);
    }

    @Override
    public boolean canContinueToUse() {
        return this.threat != null && this.threat.isAlive()
                && !this.stillLife.getNavigation().isDone()
                && this.stillLife.distanceToSqr(this.threat) < this.safeDistance * this.safeDistance;
    }

    @Override
    public void tick() {
        // panic sprint when the other one is really close
        if (this.threat != null && this.stillLife.distanceToSqr(this.threat) < 49.0) {
            this.stillLife.getNavigation().setSpeedModifier(this.speedModifier * 1.2);
        } else {
            this.stillLife.getNavigation().setSpeedModifier(this.speedModifier);
        }
    }

    @Override
    public void stop() {
        this.threat = null;
        this.path = null;
    }
}
