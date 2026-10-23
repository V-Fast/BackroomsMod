package org.vfast.backrooms.entity.stilllife.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.vfast.backrooms.entity.stilllife.StillLife;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

/**
 * When a player looks at it, the Still Life runs to the closest spot (by path length) that player cannot see, then
 * stays hidden there for a while. Hostile ones stop hiding once their target is close enough to strike.
 */
public class HideFromPlayersGoal extends Goal {
    private static final double SEEK_RANGE = 32.0;
    private static final double STRIKE_DISTANCE = 6.0;
    private static final int CANDIDATES = 24;
    private static final int PATHS_TO_COMPARE = 4;

    private final StillLife stillLife;
    private final double speedModifier;
    private @Nullable Player seeker;
    private @Nullable Path path;
    private int hiddenTicks;
    private int hideDuration;
    private long nextCheck;

    public HideFromPlayersGoal(StillLife stillLife, double speedModifier) {
        this.stillLife = stillLife;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        long time = this.stillLife.level().getGameTime();
        if (time < this.nextCheck || this.stillLife.isPetrified()) return false;
        this.nextCheck = time + 5;

        this.seeker = this.stillLife.findObserver(SEEK_RANGE);
        if (this.seeker == null || this.wantsToStrike(this.seeker)) return false;

        this.path = this.findHidingPath(this.seeker);
        if (this.path == null) {
            // nowhere to hide around here, don't search again right away
            this.nextCheck = time + 40;
            return false;
        }
        return true;
    }

    private boolean wantsToStrike(Player player) {
        return this.stillLife.getTarget() == player && this.stillLife.distanceToSqr(player) < STRIKE_DISTANCE * STRIKE_DISTANCE;
    }

    private @Nullable Path findHidingPath(Player player) {
        List<Vec3> spots = new ArrayList<>();
        for (int i = 0; i < CANDIDATES; i++) {
            Vec3 spot = i % 2 == 0
                    ? LandRandomPos.getPos(this.stillLife, 10, 4)
                    : LandRandomPos.getPosAway(this.stillLife, 14, 4, player.position());
            if (spot != null && spot.distanceToSqr(player.position()) > 16.0 && this.stillLife.isHiddenFrom(spot, player)) {
                spots.add(spot);
            }
        }
        spots.sort(Comparator.comparingDouble(spot -> spot.distanceToSqr(this.stillLife.position())));

        // straight-line distance is only a hint, the real cost is the length of the path around the walls
        Path best = null;
        for (int i = 0; i < Math.min(PATHS_TO_COMPARE, spots.size()); i++) {
            Path candidate = this.stillLife.getNavigation().createPath(BlockPos.containing(spots.get(i)), 0);
            if (candidate != null && candidate.canReach() && (best == null || candidate.getNodeCount() < best.getNodeCount())) {
                best = candidate;
            }
        }
        return best;
    }

    @Override
    public void start() {
        this.stillLife.getNavigation().moveTo(this.path, this.speedModifier);
        this.hiddenTicks = 0;
        this.hideDuration = this.adjustedTickDelay(60 + this.stillLife.getRandom().nextInt(160));
    }

    @Override
    public boolean canContinueToUse() {
        if (this.seeker == null || !this.seeker.isAlive() || this.seeker.isSpectator() || this.wantsToStrike(this.seeker)) return false;
        return !this.stillLife.getNavigation().isDone() || this.hiddenTicks < this.hideDuration;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.seeker == null || !this.stillLife.getNavigation().isDone()) return;

        this.hiddenTicks++;
        if (this.hiddenTicks % 10 == 0 && this.stillLife.isObservedBy(this.seeker)) {
            // found again, look for another spot
            Path newPath = this.findHidingPath(this.seeker);
            if (newPath != null) {
                this.stillLife.getNavigation().moveTo(newPath, this.speedModifier);
                this.hiddenTicks = 0;
            }
        } else if (this.stillLife.getRandom().nextInt(40) == 0) {
            // peek towards where the player is
            this.stillLife.getLookControl().setLookAt(this.seeker);
        }
    }

    @Override
    public void stop() {
        this.seeker = null;
        this.path = null;
        this.stillLife.getNavigation().stop();
    }
}
